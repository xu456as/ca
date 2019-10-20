package ca.instruction;

import ca.Utils;
import ca.enums.*;
import ca.module.ALU;
import ca.module.ControlUnit;
import ca.module.DataMemory;
import ca.module.RegisterFile;

import javax.rmi.CORBA.Util;

public class LogicInstruction extends Instruction {

    int rs = 0;
    int rt = 0;
    int rd = 0;

    public LogicInstruction(byte[] rawInstruction) {
        super(rawInstruction);
    }

    @Override
    public void ID(RegisterFile registerFile, ControlUnit controlUnit) {
        String rawString = new String(this.rawInstruction);

        String functionString = rawString.substring(0, 5);
        String operationString = rawString.substring(25, 31);

        controlUnit.RegWre = RegWre.STATE_1;
        controlUnit.ALUSrcB = ALUSrcB.STATE_0;
        controlUnit.regOut = RegOut.STATE_1;
        controlUnit.alum2Reg = ALUM2Reg.STATE_0;

        if (operationString.equals("000010")) {
            controlUnit.aluOp = ALUOp.AND;
        } else if (functionString.equals("100101")) {
            controlUnit.aluOp = ALUOp.OR;
        } else if (functionString.equals("100110")) {
            controlUnit.aluOp = ALUOp.XOR;
        }

        rs = Integer.parseInt(rawString.substring(21, 25));
        rt = Integer.parseInt(rawString.substring(16, 20));
        rd = Integer.parseInt(rawString.substring(11, 15));

        registerFile.signalRead1(rs);
        registerFile.signalRead2(rt);
    }

    @Override
    public void EXE(RegisterFile registerFile, ALU alu, ControlUnit controlUnit) {
        String rawString = new String(this.rawInstruction);

        byte[] data1 = registerFile.fetchRead1();
        byte[] data2 = registerFile.fetchRead2();

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
    public void MEM(RegisterFile registerFile, DataMemory dataMemory, ALU alu, ControlUnit controlUnit) {

    }

    @Override
    public void WB(RegisterFile registerFile, ALU alu, ControlUnit controlUnit) {
        byte[] result = alu.aluResult();
        registerFile.signalWrite(rd, result);
        registerFile.doWrite();
    }
}
