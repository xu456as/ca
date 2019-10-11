package ca.enums;

public enum  ALUSrcB {

    STATE_0(0, "来自寄存器堆data2输出"),
    STATE_1(1, "来自sign或extend扩展的立即数");

    ALUSrcB(int state, String description){
        this.state = state;
        this.description = description;
    }

    int state;
    String description;
}
