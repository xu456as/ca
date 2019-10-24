package ca.module;

public class SignZeroExtend {
    public static byte[] extend(byte[] dataIn16,int extsel){
        byte[] out = new byte[32];
        for (int i = 0;i < 15;i++){
            out[i]=dataIn16[i];
        }
        if(extsel == 1){
            if(dataIn16[15] != 0){
                for (int i = 16;i < 31;i++){
                    out[i]=1;
                }
            }
            else{
                for (int i = 16;i < 31;i++){
                    out[i]=0;
                }
            }
        }
        else{
            for (int i = 16;i < 31;i++){
                out[i]=0;
            }
        }
        return out;
    }
}
