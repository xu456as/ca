package ca.instruction;

import ca.module.*;

public abstract class Instruction {
    byte[] rawInstruction;
    Instruction(byte[] rawInstruction){
        this.rawInstruction = rawInstruction;
    }
    public abstract void ID(ControlUnit controlUnit, RegisterFile registerFile);
    public abstract void EXE(RegisterFile registerFile, ALU alu, ControlUnit controlUnit);

    public abstract void MEM(RegisterFile registerFile, DataMemory dataMemory, ALU alu, ControlUnit controlUnit, PC pc);

    public abstract void WB(RegisterFile registerFile, ALU alu, ControlUnit controlUnit);
}
