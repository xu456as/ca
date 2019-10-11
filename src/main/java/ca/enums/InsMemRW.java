package ca.enums;

public enum  InsMemRW {

    STATE_0(0, "读指令寄存器"),
    STATE_1(1, "写指令寄存器");

    InsMemRW(int state, String description){
        this.state = state;
        this.description = description;
    }

    int state;
    String description;
}
