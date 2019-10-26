package ca.module;

import ca.Utils;

public class PC {
    public ALU adder = new ALU();
    int lastAddress = 0;
    int currentAddress = 0;
    public void plus4() {
        lastAddress= currentAddress;
        adder.add(Utils.Int32ToRawMemory(currentAddress), Utils.Int32ToRawMemory(4));
        currentAddress = Utils.rawMemoryToInt32(adder.aluResult());
        System.out.println(String.format("PC.plus4 currentInstructionAddress: %08d", currentAddress));
    }

    public int fetch() {
        return lastAddress;
    }

    public void write(int immediate) {
        currentAddress += immediate;
        System.out.println(String.format("PC.plus4 write: %08d", currentAddress));
    }

}
