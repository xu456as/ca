package ca.instruction;

import ca.enums.ALUM2Reg;
import ca.enums.RegOut;
import ca.module.ALU;
import ca.module.ControlUnit;
import ca.module.DataMemory;
import ca.module.RegisterFile;

public class MoveInstruction extends Instruction {
    public MoveInstruction(byte[] rawInstruction) {
        super(rawInstruction);
    }

    int rs = 0;
    int rd = 0;

    @Override
    public void ID(ControlUnit controlUnit) {
        byte[] ins = this.rawInstruction;
        String insStr = String.format("%x%x%x%x", ins[0], ins[1], ins[2], ins[3]);
        if(insStr.substring(0, 6).equals("100000")) {
            rs = Integer.parseInt(insStr.substring(6, 11));
            rd = Integer.parseInt(insStr.substring(16, 21));
//            controlUnit.alum2Reg = ALUM2Reg.STATE_0;
            controlUnit.regOut = RegOut.STATE_1;
        }
    }

    @Override
    public void EXE(RegisterFile registerFile, ALU alu, ControlUnit controlUnit) {

    }

    @Override
    public void MEM(RegisterFile registerFile, DataMemory dataMemory, ALU alu, ControlUnit controlUnit) {

    }

    @Override
    public void WB(RegisterFile registerFile, ALU alu, ControlUnit controlUnit) {
        if(controlUnit.regOut == RegOut.STATE_1) {
            registerFile.signalRead1(rs);
            byte[] result = registerFile.fetchRead1();
            registerFile.signalWrite(rd, result);
        }
    }
}
