package ca.module;

import ca.Utils;

import java.util.Arrays;

public class RegisterFile {
    byte[][] rawRegister = new byte[32][4];

    volatile int read1Index;
    volatile int read2Index;
    volatile int writeIndex;
    volatile byte[] writeBuffer;

    public RegisterFile() {
        for(int i = 0; i < 32;++i) {
            rawRegister[i] = new byte[4];
        }

        rawRegister[16] = Utils.Int32ToRawMemory(0);
        rawRegister[17] = Utils.Int32ToRawMemory(4);
        rawRegister[30] = Utils.Int32ToRawMemory(8);
        rawRegister[31] = Utils.Int32ToRawMemory(12);
    }

    public byte[] signalRead1(int index){
        this.read1Index = index;
        System.out.println(String.format("RegisterFile.fetchRead1 read1Index: %08d, int32Value: %d", read1Index, Utils.rawMemoryToInt32(rawRegister[read1Index])));
        return rawRegister[read1Index];
    }
    public byte[] fetchRead1(){
        System.out.println(String.format("RegisterFile.fetchRead1 read1Index: %08d, int32Value: %d", read1Index, Utils.rawMemoryToInt32(rawRegister[read1Index])));
        return rawRegister[read1Index];
    }

    public byte[] signalRead2(int index){
        this.read2Index = index;
        System.out.println(String.format("RegisterFile.fetchRead2 read2Index: %08d, int32Value: %d", read2Index, Utils.rawMemoryToInt32(rawRegister[read2Index])));
        return rawRegister[read2Index];
    }
    public byte[] fetchRead2(){
        System.out.println(String.format("RegisterFile.fetchRead2 read2Index: %08d, int32Value: %d", read2Index, Utils.rawMemoryToInt32(rawRegister[read2Index])));
        return rawRegister[read2Index];
    }

    public void signalWrite(int index, byte[] data) {
        System.out.println(String.format("RegisterFile.signalWrite index: %d", index));
        this.writeIndex = index;
        writeBuffer = data;
        System.out.println(String.format("RegisterFile.doWrite writeIndex: %08d, int32Value: %d", writeIndex, Utils.rawMemoryToInt32(writeBuffer)));
        System.arraycopy(writeBuffer, 0, rawRegister[writeIndex], 0, 4);
    }

    public void doWrite(){
        System.out.println(String.format("RegisterFile.doWrite writeIndex: %08d, int32Value: %d", writeIndex, Utils.rawMemoryToInt32(writeBuffer)));
        System.arraycopy(writeBuffer, 0, rawRegister[writeIndex], 0, 4);;
    }
}