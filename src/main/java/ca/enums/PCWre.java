package ca.enums;

public enum PCWre {

    STATE_0(0, "PC不更改"),
    STATE_1(1, "PC更改");

    PCWre(int state, String description){
        this.state = state;
        this.description = description;
    }

    int state;
    String description;
}
