package ca.instruction;

import ca.Utils;
import ca.enums.*;
import ca.module.*;

public class LogicInstruction extends Instruction {

    int rs = 0;
    int rt = 0;
    int rd = 0;
    byte[] immediate;

    public LogicInstruction(byte[] rawInstruction) {
        super(rawInstruction);
    }

    @Override
    public void ID(RegisterFile registerFile, ControlUnit controlUnit) {
        String rawString = Utils.byte32ToString(this.rawInstruction);
        String functionString = rawString.substring(0, 5);
        String operationString = rawString.substring(25, 31);
        boolean isOri = false;

        if (operationString.equals("010001")) {
            controlUnit.aluOp = ALUOp.AND;
        } else if (functionString.equals("010010")) {
            controlUnit.aluOp = ALUOp.OR;
        } else if (functionString.equals("100110")) {
            controlUnit.aluOp = ALUOp.XOR;
        } else if (functionString.equals("010000")) {       // ori
            controlUnit.aluOp = ALUOp.OR;
            isOri = true;
            immediate = ("0000000000000000" + rawString.substring(0, 15)).getBytes();
        }

        controlUnit.RegWre = RegWre.STATE_1;
        controlUnit.ALUSrcB = isOri ? ALUSrcB.STATE_1 : ALUSrcB.STATE_0;
        controlUnit.regOut = RegOut.STATE_1;
        controlUnit.alum2Reg = ALUM2Reg.STATE_0;

        rs = Integer.parseInt(rawString.substring(21, 25), 2);
        rt = Integer.parseInt(rawString.substring(16, 20), 2);
        rd = Integer.parseInt(rawString.substring(11, 15), 2);

        registerFile.signalRead1(rs);
        registerFile.signalRead2(rt);
    }

    @Override
    public void EXE(RegisterFile registerFile, ALU alu, ControlUnit controlUnit) {

        byte[] data1 = registerFile.fetchRead1();
        byte[] data2 = controlUnit.ALUSrcB == ALUSrcB.STATE_1 ? immediate : registerFile.fetchRead2();

        switch (controlUnit.aluOp) {
            case AND:
            {
                alu.and(data1, data2);
                break;
            }
            case OR:
            {
                alu.or(data1, data2);
                break;
            }
            case XOR:
            {
                alu.xor(data1, data2);
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
    }
}
