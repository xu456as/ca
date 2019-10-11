package ca.enums;

public enum ALUM2Reg {
    STATE_0(0, "来自ALU运算结果输出"),
    STATE_1(1, "来自DataMemory输出");

    ALUM2Reg(int state, String description){
        this.state = state;
        this.description = description;
    }

    int state;
    String description;
}
