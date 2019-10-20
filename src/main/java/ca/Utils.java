package ca;

public class Utils {
    public static int rawMemoryToInt32(byte[] data) {
        return ((int)data[0] << 24)  | ((int)data[1] << 16) | ((int)data[2] << 8) | ((int)data[3]);
    }
    public static byte[] Int32ToRawMemory(int number){
        return new byte[]{(byte) (number >> 24), (byte) (number >> 16), (byte) (number >> 8), (byte) number};
    }

    /* 8bit string */
    public static byte stringToByte(String string){
        byte result = 0;
        for(int i = 0; i < 8; ++i) {
            char ch = string.charAt(i);
            int bit = ch - '0';
            if(bit == 1) {
                result |= (1<<(7 - i));
            }
        }
        return result;
    }

    public static String byte32ToString(byte[] bytes) {
        StringBuilder builder = new StringBuilder("");
        for(int i = 0; i < 4; i++) {
            byte thisByte = bytes[i];
            for(int j = 0; j < 8; j++) {
                boolean test = (thisByte & ( 1 << (7-j))) != 0;
                builder.append((char)('0' + (test ? 1 : 0)));
            }
        }
        return builder.toString();
    }
}
