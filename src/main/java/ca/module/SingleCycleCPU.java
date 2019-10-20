package ca.module;

import ca.Utils;
import ca.instruction.*;

import java.io.BufferedReader;

public class SingleCycleCPU {
    ALU alu = new ALU();
    DataMemory dataMemory = new DataMemory();
    InstructionMemory instructionMemory = new InstructionMemory();
    PC pc = new PC();
    RegisterFile registerFile = new RegisterFile();
    SignZeroExtend signZeroExtend = new SignZeroExtend();
    ControlUnit controlUnit = new ControlUnit();
    InstructionCycle instructionCycle = new InstructionCycle();

    public void loadCode(BufferedReader reader) throws Exception{
        String line = null;
        int curIdx = 0;
        while (null != (line = reader.readLine())){
            byte[] rawInst = new byte[4];
            rawInst[0] = Utils.stringToByte(line.substring(0, 8));
            rawInst[1] = Utils.stringToByte(line.substring(8, 16));
            rawInst[2] = Utils.stringToByte(line.substring(16, 24));
            rawInst[3] = Utils.stringToByte(line.substring(24, 32));
            instructionMemory.rawInstruction[curIdx++] = rawInst;
        }
    }

    public void mainLoop() throws Exception {
        while (true) {
            instructionCycle.IF(pc, instructionMemory, controlUnit);
            instructionCycle.ID(controlUnit);
            instructionCycle.EXE(registerFile, alu, controlUnit);
            instructionCycle.MEM(registerFile, dataMemory, alu, controlUnit);
            instructionCycle.WB(registerFile, alu, controlUnit);
        }
    }

    static class InstructionCycle {
        byte[] currentRawInstruction;
        Instruction decodedInstruction;

        void IF(PC pc, InstructionMemory instructionMemory, ControlUnit controlUnit) throws Exception {
            pc.plus4();
            instructionMemory.signalGet(pc.fetch());
            currentRawInstruction = instructionMemory.getInstruction();
            Thread.sleep(0, 20);
        }

        public void ID(ControlUnit controlUnit) throws Exception {
            byte opBit =  (byte) (currentRawInstruction[0] >>> 26);
            Instruction instruction = null;
            switch (Byte.toString(opBit)) {
                case "00000000":
                case "00000001":
                case "00000010":
                    instruction = new ArithmeticInstruction(currentRawInstruction);
                    break;
                case "00010000":
                case "00010001":
                case "00010010":
                    instruction = new LogicInstruction(currentRawInstruction);
                    break;
                case "00100000":
                    instruction = new MoveInstruction(currentRawInstruction);
                    break;
                case "00100110":
                case "00100111":
                    instruction = new StoreAndLoadInstruction(currentRawInstruction);
                    break;
                case "00110000":
                    instruction = new BranchInstruction(currentRawInstruction);
                    break;
            }
            decodedInstruction = instruction;
            decodedInstruction.ID(controlUnit);
            Thread.sleep(0, 20);
        }

        public void EXE(RegisterFile registerFile, ALU alu, ControlUnit controlUnit) throws Exception {
            decodedInstruction.EXE(registerFile, alu, controlUnit);
            Thread.sleep(0, 40);
        }

        public void MEM(RegisterFile registerFile, DataMemory dataMemory, ALU alu, ControlUnit controlUnit) throws Exception {
            decodedInstruction.MEM(registerFile, dataMemory, alu, controlUnit);
            Thread.sleep(0, 20);
        }

        public void WB(RegisterFile registerFile, ALU alu, ControlUnit controlUnit) throws Exception {
            decodedInstruction.WB(registerFile, alu, controlUnit);
            Thread.sleep(0, 20);
        }
    }
}
