package ca.enums;

public enum RegWre {
    STATE_0(0, "无寄存器写"),
    STATE_1(1, "寄存器写");

    RegWre(int state, String description){
        this.state = state;
        this.description = description;
    }

    int state;
    String description;
}