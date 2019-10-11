package ca.module;

public class DataMemory {
    byte[] rawMemory = new byte[1024 * 4];

    int address;
    int lengthInByte;
    byte[] writeBuffer;
    public void signalRead(int address, int lengthInByte){
        this.address = address;
        this.lengthInByte = lengthInByte;
    }

    public byte[] fetchRead(){
        byte[] ret = new byte[lengthInByte];
        System.arraycopy(ret, 0, rawMemory, address % 4, lengthInByte);
        return ret;
    }

    public void signalWrite(int address, byte[] data){
        this.address = address;
        this.writeBuffer = data;
    }

    public void doWrite(){
        System.arraycopy(rawMemory, address % 4, writeBuffer, 0, writeBuffer.length);
    }
}
