package ca.module;

import ca.Utils;

public class ALU {

    volatile byte[] aluResult = null;

    public byte[] add(byte[] lhs, byte[] rhs) {
        int byte_int1 = Utils.rawMemoryToInt32(lhs);
        int byte_int2 = Utils.rawMemoryToInt32(rhs);
        int byte_int_ttl = byte_int1+byte_int2;
        aluResult = Utils.Int32ToRawMemory(byte_int_ttl);

        System.out.println(String.format("ALU.add value1: %d, value2: %d", byte_int1, byte_int2));

        return aluResult;
    }

    public byte[] minus(byte[] lhs, byte[] rhs){
        int byte_int1 = Utils.rawMemoryToInt32(lhs);
        int byte_int2 = Utils.rawMemoryToInt32(rhs);
        int byte_int_ttl = byte_int1-byte_int2;
        aluResult = Utils.Int32ToRawMemory(byte_int_ttl);

        System.out.println(String.format("ALU.minus value1: %d, value2: %d", byte_int1, byte_int2));

        return aluResult;
    }

    public byte[] and(byte[] lhs, byte[] rhs) {
        int byte_int1 = Utils.rawMemoryToInt32(lhs);
        int byte_int2 = Utils.rawMemoryToInt32(rhs);
        int byte_int_ttl = byte_int1&byte_int2;
        aluResult = Utils.Int32ToRawMemory(byte_int_ttl);

        System.out.println(String.format("ALU.and value1: %d, value2: %d", byte_int1, byte_int2));

        return aluResult;
    }

    public byte[] or(byte[] lhs, byte[] rhs) {
        int byte_int1 = Utils.rawMemoryToInt32(lhs);
        int byte_int2 = Utils.rawMemoryToInt32(rhs);
        int byte_int_ttl = byte_int1|byte_int2;
        aluResult = Utils.Int32ToRawMemory(byte_int_ttl);

        System.out.println(String.format("ALU.or value1: %d, value2: %d", byte_int1, byte_int2));

        return aluResult;
    }

    public byte[] xor(byte[] lhs, byte[] rhs){
        int byte_int1 = Utils.rawMemoryToInt32(lhs);
        int byte_int2 = Utils.rawMemoryToInt32(rhs);
        int byte_int_ttl = byte_int1^byte_int2;
        aluResult = Utils.Int32ToRawMemory(byte_int_ttl);

        System.out.println(String.format("ALU.xor value1: %d, value2: %d", byte_int1, byte_int2));

        return aluResult;
    }

    public byte[] aluResult(){
        return aluResult;
    }

    public boolean generateZero() {
        return aluResult == null ||
                (aluResult[0] == 0 && aluResult[1] == 0 && aluResult[2] == 0 && aluResult[3] == 0);
    }
}
