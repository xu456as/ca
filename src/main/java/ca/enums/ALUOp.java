package ca.enums;

public enum ALUOp {
    ADD(0, "加"),
    SUB(1, "减"),
    OR(2, "or"),
    AND(3, "and"),
    XOR(4, "xor");

    ALUOp(int state, String description){
        this.state = state;
        this.description = description;
    }

    int state;
    String description;
}