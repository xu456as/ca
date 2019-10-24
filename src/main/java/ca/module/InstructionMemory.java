package ca.module;

public class InstructionMemory {
    byte[][] rawInstruction = new byte[1024][4];
    int address;

    public InstructionMemory(){
        for(int i = 0; i < 1024; ++i) {
            rawInstruction[i] = new byte[4];
        }
    }

    public byte[] getInstruction(){
        return rawInstruction[address % 4];
    }

    public void signalGet(int address) {
        this.address = address;
    }
}
