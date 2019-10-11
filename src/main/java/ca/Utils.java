package ca;

public class Utils {
    public static int rawMemoryToInt32(byte[] data) {
        return ((int)data[0] << 24)  | ((int)data[1] << 16) | ((int)data[2] << 8) | ((int)data[3]);
    }
    public static byte[] Int32ToRawMemory(int number){
        return new byte[]{(byte) (number >> 24), (byte) (number >> 16), (byte) (number >> 8), (byte) number};
    }
}
