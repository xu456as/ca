package ca.instruction;

import ca.Utils;
import ca.enums.*;
import ca.module.*;

import javax.naming.ldap.Control;

public class ArithmeticInstruction extends Instruction {

    int rs = 0;
    int rd = 0;
    int rt = 0;
    byte[] immediate;

    public ArithmeticInstruction(byte[] rawInstruction) {
        super(rawInstruction);
    }

    @Override
    public void ID(RegisterFile registerFile, ControlUnit controlUnit) {
        String rawString = Utils.byte32ToString(this.rawInstruction);
        String operationString = rawString.substring(25, 31);
        boolean isAddi = false;

        if (operationString.equals("000000")) {
            controlUnit.aluOp = ALUOp.ADD;
        } else if (operationString.equals("0000010")) {         //Addi
            controlUnit.aluOp = ALUOp.SUB;
            isAddi = true;
            immediate = SignZeroExtend.extend(rawString.substring(0, 15).getBytes(), 0);
        } else if (operationString.equals("000001")) {
            controlUnit.aluOp = ALUOp.ADD;
        }

        rs = Integer.parseInt(rawString.substring(21, 25), 2);
        rt = Integer.parseInt(rawString.substring(16, 20), 2);
        rd = Integer.parseInt(rawString.substring(11, 15), 2);

        controlUnit.ALUSrcB = isAddi ? ALUSrcB.STATE_1 : ALUSrcB.STATE_0;
        controlUnit.RegWre = RegWre.STATE_1;
        controlUnit.regOut = RegOut.STATE_1;
        controlUnit.alum2Reg = ALUM2Reg.STATE_0;

        registerFile.signalRead1(rs);
        registerFile.signalRead2(rt);
    }

    @Override
    public void EXE(RegisterFile registerFile, ALU alu, ControlUnit controlUnit) {

        byte[] data1 = registerFile.fetchRead1();
        byte[] data2 = controlUnit.ALUSrcB == ALUSrcB.STATE_0 ? registerFile.fetchRead2() : immediate;

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
