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

    byte[] rsValue;
    byte[] rtValue;

    ControlUnit controlUnit;
    volatile byte[] aluResult;

    public ArithmeticInstruction(byte[] rawInstruction) {
        super(rawInstruction);
    }

    @Override
    public void ID(RegisterFile registerFile, ControlUnit controlUnit) {
        String rawString = Utils.byte32ToString(this.rawInstruction);
        String operationString = rawString.substring(0, 6);
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

        this.controlUnit = controlUnit;
        rs = Integer.parseInt(rawString.substring(6, 11), 2);
        rt = Integer.parseInt(rawString.substring(11, 16), 2);
        rd = Integer.parseInt(rawString.substring(16, 21), 2);

        controlUnit.ALUSrcB = isAddi ? ALUSrcB.STATE_1 : ALUSrcB.STATE_0;
        controlUnit.RegWre = RegWre.STATE_1;
        controlUnit.regOut = RegOut.STATE_1;
        controlUnit.alum2Reg = ALUM2Reg.STATE_0;

    }

    @Override
    public void EXE(RegisterFile registerFile, ALU alu, ControlUnit controlUnit) {
        rsValue = registerFile.signalRead1(rs);
        rtValue = registerFile.signalRead2(rt);
        byte[] data1 = rsValue;
        byte[] data2 = this.controlUnit.ALUSrcB == ALUSrcB.STATE_0 ? rtValue : immediate;

        System.out.println(String.format("ArithmeticInstruction.EXE rsValue: %d, rtValue: %d", Utils.rawMemoryToInt32(data1), Utils.rawMemoryToInt32(data2)));


        switch (controlUnit.aluOp) {
            case ADD:
            {
                aluResult = alu.add(data1, data2);
                break;
            }
            case SUB:
            {
                aluResult = alu.minus(data1, data2);
                break;
            }
        }
    }

    @Override
    public void MEM(RegisterFile registerFile, DataMemory dataMemory, ALU alu, ControlUnit controlUnit, PC pc) {

    }

    @Override
    public void WB(RegisterFile registerFile, ALU alu, ControlUnit controlUnit, DataMemory dataMemory) {
        byte[] result = aluResult;
        registerFile.signalWrite(rd, result);
        System.out.println(String.format("ArithmeticInstruction.WB result: %d", Utils.rawMemoryToInt32(result)));
    }
}
