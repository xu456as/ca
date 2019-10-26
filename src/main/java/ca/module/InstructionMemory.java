package ca.module;

import ca.Utils;

public class InstructionMemory {
    byte[][] rawInstruction = new byte[1024][4];
    int address;
    int instructionCount = 0;

    public InstructionMemory(){
        for(int i = 0; i < 1024; ++i) {
            rawInstruction[i] = new byte[4];
        }
    }

    public byte[] getInstruction(){
        System.out.println(String.format("InstructionMemory.getInstruction address: %08d, rawInstruction: %s", address, Utils.byte32ToString(rawInstruction[address % 4])));
        return rawInstruction[address % 4];
    }

    public void signalGet(int address) {
        this.address = address;
    }
}
