package ca.module;

import java.util.Arrays;

public class RegisterFile {
    byte[][] rawRegister = new byte[32][4];

    int read1Index;
    int read2Index;
    int writeIndex;
    byte[] writeBuffer;

    public RegisterFile(){
        for(int i = 0; i < 32;++i) {
            rawRegister[i] = new byte[4];
        }
    }

    public void signalRead1(int index){
        this.read1Index = index;
    }
    public byte[] fetchRead1(){
        return rawRegister[read1Index];
    }

    public void signalRead2(int index){
        this.read2Index = index;
    }
    public byte[] fetchRead2(){
        return rawRegister[read2Index];
    }

    public void signalWrite(int index, byte[] data) {
        this.writeIndex = index;
        writeBuffer = data;
    }

    public void doWrite(){
        System.arraycopy(rawRegister[writeIndex], 0, writeBuffer, 0, 4);;
    }
}