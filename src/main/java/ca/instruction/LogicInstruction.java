package ca.instruction;

import ca.module.ALU;
import ca.module.ControlUnit;
import ca.module.DataMemory;
import ca.module.RegisterFile;

public class LogicInstruction extends Instruction {

    public LogicInstruction(byte[] rawInstruction) {
        super(rawInstruction);
    }

    @Override
    public void ID(ControlUnit controlUnit) {

    }

    @Override
    public void EXE(RegisterFile registerFile, ALU alu, ControlUnit controlUnit) {

    }

    @Override
    public void MEM(RegisterFile registerFile, DataMemory dataMemory, ALU alu, ControlUnit controlUnit) {

    }

    @Override
    public void WB(RegisterFile registerFile, ALU alu, ControlUnit controlUnit) {

    }
}
