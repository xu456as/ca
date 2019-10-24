package ca.instruction;

import ca.Utils;
import ca.enums.PCSrc;
import ca.enums.RegOut;
import ca.module.*;

public class BranchInstruction extends Instruction {
    public BranchInstruction(byte[] rawInstruction) {
        super(rawInstruction);
    }

    int rs = 0;
    int rt = 0;
    int immediate = 0;

    @Override
    public void ID(RegisterFile registerFile, ControlUnit controlUnit) {
        byte[] ins = this.rawInstruction;
        String insStr = Utils.byte32ToString(ins);
        if(insStr.substring(0, 6).equals("110000")) {
            rs = Integer.parseInt(insStr.substring(6, 11), 2);
            rt = Integer.parseInt(insStr.substring(11, 16), 2);
            immediate = Integer.parseInt(insStr.substring(16, 32), 2);
            registerFile.signalRead1(rs);
            registerFile.signalRead2(rt);
        }
    }

    @Override
    public void EXE(RegisterFile registerFile, ALU alu, ControlUnit controlUnit) {
        byte[] bytes1 = registerFile.fetchRead1();
        byte[] bytes2 = registerFile.fetchRead2();
        alu.minus(bytes1, bytes2);
        boolean b = alu.generateZero();
        if(b) {
            controlUnit.pcSrc = PCSrc.STATE_1;
        } else {
            controlUnit.pcSrc = PCSrc.STATE_0;
        }
    }

    @Override
    public void MEM(RegisterFile registerFile, DataMemory dataMemory, ALU alu, ControlUnit controlUnit, PC pc) {
        if(controlUnit.pcSrc == PCSrc.STATE_1) {
            pc.write(immediate << 2);
        }
    }

    @Override
    public void WB(RegisterFile registerFile, ALU alu, ControlUnit controlUnit, DataMemory dataMemory) {

    }
}
