package ca.module;

import ca.Utils;

public class DataMemory {
    byte[] rawMemory = new byte[1024 * 4];

    int address;
    int lengthInByte;
    byte[] writeBuffer;

    public DataMemory(){
        /* mem[0 - 4] = (0X0000000A) */
        rawMemory[3] = 10;
        /* mem[4 - 8] = (0X00000004) */
        rawMemory[7] = 4;
        /* mem[8 - 12] = (0X00000005) */
        rawMemory[11] = 5;
        /* mem[12 - 16] = (0X00000006) */
        rawMemory[15] = 6;
    }

    public void signalRead(int address, int lengthInByte){
        this.address = address;
        this.lengthInByte = lengthInByte;
    }

    public byte[] fetchRead(){
        byte[] ret = new byte[lengthInByte];
        System.arraycopy(rawMemory, address, ret, 0, lengthInByte);

        System.out.println(String.format("DataMemory.fetchRead address: %08d, int32Value: %d", address, Utils.rawMemoryToInt32(ret)));

        return ret;
    }

    public void signalWrite(int address, byte[] data){
        this.address = address;
        this.writeBuffer = data;
    }

    public void doWrite(){
        System.out.println(String.format("DataMemory.doWrite address: %08d, int32Value: %d", address, Utils.rawMemoryToInt32(writeBuffer)));
        System.arraycopy(writeBuffer, 0, rawMemory, address, writeBuffer.length);
    }
}
