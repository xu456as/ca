package ca.enums;

public enum RegOut {
    STATE_0(0, "写寄存器地址，来自rt"),
    STATE_1(1, "写寄存器地址，来自rd");

    RegOut(int state, String description){
        this.state = state;
        this.description = description;
    }

    int state;
    String description;
}