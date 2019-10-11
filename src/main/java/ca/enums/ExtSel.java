package ca.enums;

public enum ExtSel {
    STATE_0(0, "zero extend"),
    STATE_1(1, "sign extend");

    ExtSel(int state, String description){
        this.state = state;
        this.description = description;
    }

    int state;
    String description;
}
