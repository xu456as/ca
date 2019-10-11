package ca.enums;

public enum  PCSrc {
    STATE_0(0, "PC=PC+4, zero=0"),
    STATE_1(1, "PC=PC+4+(immediate<<4), zero=1");

    PCSrc(int state, String description){
        this.state = state;
        this.description = description;
    }

    int state;
    String description;
}