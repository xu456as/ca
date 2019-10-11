package ca.enums;

public enum DataMemRW {
    STATE_0(0, "读数据寄存器"),
    STATE_1(1, "写数据寄存器");

    DataMemRW(int state, String description){
        this.state = state;
        this.description = description;
    }

    int state;
    String description;
}
