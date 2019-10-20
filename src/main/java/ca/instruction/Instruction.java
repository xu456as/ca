package ca.instruction;

import ca.module.ALU;
import ca.module.ControlUnit;
import ca.module.DataMemory;
import ca.module.RegisterFile;

public abstract class Instruction {
    byte[] rawInstruction;
    Instruction(byte[] rawInstruction){
        this.rawInstruction = rawInstruction;
    }
    public abstract void ID(RegisterFile registerFile, ControlUnit controlUnit);
    public abstract void EXE(RegisterFile registerFile, ALU alu, ControlUnit controlUnit);

    public abstract void MEM(RegisterFile registerFile, DataMemory dataMemory, ALU alu, ControlUnit controlUnit);

    public abstract void WB(RegisterFile registerFile, ALU alu, ControlUnit controlUnit);
}
