package ca.instruction;

import ca.Utils;
import ca.enums.*;
import ca.module.*;

public class ArithmeticInstruction extends Instruction {

    int rs = 0;
    int rd = 0;
    int rt = 0;

    public ArithmeticInstruction(byte[] rawInstruction) {
        super(rawInstruction);
    }

    @Override
    public void ID(RegisterFile registerFile, ControlUnit controlUnit) {
        String rawString = Utils.byte32ToString(this.rawInstruction);

        String operationString = rawString.substring(25, 31);

        controlUnit.RegWre = RegWre.STATE_1;
        controlUnit.ALUSrcB = ALUSrcB.STATE_0;
        controlUnit.regOut = RegOut.STATE_1;
        controlUnit.alum2Reg = ALUM2Reg.STATE_0;

        if (operationString.equals("000000")) {
            controlUnit.aluOp = ALUOp.ADD;
        } else  if (operationString.equals("0000001")) {
            controlUnit.aluOp = ALUOp.SUB;
        }

        rs = Integer.parseInt(rawString.substring(6, 11), 2);
        rt = Integer.parseInt(rawString.substring(11, 16), 2);
        rd = Integer.parseInt(rawString.substring(16, 21), 2);

        registerFile.signalRead1(rs);
        registerFile.signalRead2(rt);
    }

    @Override
    public void EXE(RegisterFile registerFile, ALU alu, ControlUnit controlUnit) {

        byte[] data1 = registerFile.fetchRead1();
        byte[] data2 = registerFile.fetchRead2();

        System.out.println(String.format("ArithmeticInstruction.EXE rsValue: %d, rtValue: %d", Utils.rawMemoryToInt32(data1), Utils.rawMemoryToInt32(data2)));


        switch (controlUnit.aluOp) {
            case ADD:
            {
                alu.add(data1, data2);
                break;
            }
            case SUB:
            {
                alu.minus(data1, data2);
                break;
            }
        }
    }

    @Override
    public void MEM(RegisterFile registerFile, DataMemory dataMemory, ALU alu, ControlUnit controlUnit, PC pc) {

    }

    @Override
    public void WB(RegisterFile registerFile, ALU alu, ControlUnit controlUnit, DataMemory dataMemory) {
        byte[] result = alu.aluResult();
        registerFile.signalWrite(rd, result);
        registerFile.doWrite();
        System.out.println(String.format("LogicInstruction.WB result: %d", Utils.rawMemoryToInt32(result)));
    }
}
