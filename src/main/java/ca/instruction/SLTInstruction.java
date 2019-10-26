package ca.instruction;

import ca.Utils;
import ca.enums.*;
import ca.module.*;

public class SLTInstruction extends Instruction {

    public SLTInstruction(byte[] rawInstruction) {
        super(rawInstruction);
    }
    int rs;
    int rt;
    int rd;

    @Override
    public void ID(RegisterFile registerFile, ControlUnit controlUnit) {
        String rawString = Utils.byte32ToString(this.rawInstruction);

        rs = Integer.parseInt(rawString.substring(6, 11), 2);
        rt = Integer.parseInt(rawString.substring(11, 16), 2);
        rd = Integer.parseInt(rawString.substring(16, 21), 2);

        registerFile.signalRead1(rs);
        registerFile.signalRead2(rt);
    }

    @Override
    public void EXE(RegisterFile registerFile, ALU alu, ControlUnit controlUnit) {
        byte[] rsValue = registerFile.fetchRead1();
        byte[] rtValue = registerFile.fetchRead2();

        alu.minus(rsValue, rtValue);

    }

    @Override
    public void MEM(RegisterFile registerFile, DataMemory dataMemory, ALU alu, ControlUnit controlUnit, PC pc) {

    }

    @Override
    public void WB(RegisterFile registerFile, ALU alu, ControlUnit controlUnit, DataMemory dataMemory) {
        byte[] aluResult = alu.aluResult();
        String result = Utils.byte32ToString(aluResult);
        System.out.println(String.format("SLTInstruction.WB generate %s (result < 0) which means true-return", result));
        if(result.charAt(0) == '1') {
            registerFile.signalWrite(rd, Utils.Int32ToRawMemory(1));
        }else {
            registerFile.signalWrite(rd, Utils.Int32ToRawMemory(0));
        }
        registerFile.doWrite();
    }
}
