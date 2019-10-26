package ca.module;

import ca.Utils;

import java.util.Arrays;

public class RegisterFile {
    byte[][] rawRegister = new byte[32][4];

    int read1Index;
    int read2Index;
    int writeIndex;
    byte[] writeBuffer;

    public RegisterFile() {
        for(int i = 0; i < 32;++i) {
            rawRegister[i] = new byte[4];
        }
    }

    public void signalRead1(int index){
        this.read1Index = index;
    }
    public byte[] fetchRead1(){
        System.out.println(String.format("RegisterFile.fetchRead1 read1Index: %08d, int32Value: %d", read1Index, Utils.rawMemoryToInt32(rawRegister[read1Index])));
        return rawRegister[read1Index];
    }

    public void signalRead2(int index){
        this.read2Index = index;
    }
    public byte[] fetchRead2(){
        System.out.println(String.format("RegisterFile.fetchRead2 read2Index: %08d, int32Value: %d", read2Index, Utils.rawMemoryToInt32(rawRegister[read2Index])));
        return rawRegister[read2Index];
    }

    public void signalWrite(int index, byte[] data) {
        this.writeIndex = index;
        writeBuffer = data;
    }

    public void doWrite(){
        System.out.println(String.format("RegisterFile.doWrite writeIndex: %08d, int32Value: %d", writeIndex, Utils.rawMemoryToInt32(writeBuffer)));
        System.arraycopy(rawRegister[writeIndex], 0, writeBuffer, 0, 4);;
    }
}