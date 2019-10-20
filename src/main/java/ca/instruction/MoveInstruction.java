package ca.instruction;

import ca.Utils;
import ca.enums.RegOut;
import ca.module.*;

public class MoveInstruction extends Instruction {
    public MoveInstruction(byte[] rawInstruction) {
        super(rawInstruction);
    }

    int rs = 0;
    int rd = 0;

    @Override
    public void ID(ControlUnit controlUnit, RegisterFile registerFile) {
        byte[] ins = this.rawInstruction;
        //String insStr = String.format("%x%x%x%x", ins[0], ins[1], ins[2], ins[3]);
        String insStr = Utils.byte32ToString(ins);
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
    public void MEM(RegisterFile registerFile, DataMemory dataMemory, ALU alu, ControlUnit controlUnit, PC pc) {

    }

    @Override
    public void WB(RegisterFile registerFile, ALU alu, ControlUnit controlUnit) {
        if(controlUnit.regOut == RegOut.STATE_1) {
            registerFile.signalRead1(rs);
            byte[] result = registerFile.fetchRead1();
            registerFile.signalWrite(rd, result);
        }
    }

    public static void main(String[] args) {
        String str= "10000000001000000001000000000000";
        byte[] bytes = new byte[]{
                Utils.stringToByte(str.substring(0, 8)),
                Utils.stringToByte(str.substring(8, 16)),
                Utils.stringToByte(str.substring(16, 24)),
                Utils.stringToByte(str.substring(24, 32))
        };
        ControlUnit c = new ControlUnit();

        MoveInstruction mv = new MoveInstruction(bytes);
        mv.ID(c, new RegisterFile());
        mv.WB(new RegisterFile(), new ALU(), c);
    }
}
