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
            if(line.startsWith("#")) {
                continue;
            }
            byte[] rawInst = new byte[4];
            rawInst[0] = Utils.stringToByte(line.substring(0, 8));
            rawInst[1] = Utils.stringToByte(line.substring(8, 16));
            rawInst[2] = Utils.stringToByte(line.substring(16, 24));
            rawInst[3] = Utils.stringToByte(line.substring(24, 32));
            instructionMemory.rawInstruction[curIdx++] = rawInst;
            instructionMemory.instructionCount = curIdx;
        }
    }

    public void mainLoop() throws Exception {
        int currentInstruction = 0;
        while (currentInstruction < instructionMemory.instructionCount) {
            instructionCycle.IF(pc, instructionMemory, controlUnit);
            instructionCycle.ID(registerFile, controlUnit);
            instructionCycle.EXE(registerFile, alu, controlUnit);
            instructionCycle.MEM(registerFile, dataMemory, alu, controlUnit, pc);
            instructionCycle.WB(registerFile, alu, controlUnit, dataMemory);
            ++currentInstruction;
        }
    }

    static class InstructionCycle {
        byte[] currentRawInstruction;
        Instruction decodedInstruction;

        void IF(PC pc, InstructionMemory instructionMemory, ControlUnit controlUnit) throws Exception {
            System.out.println(String.format("======================================== CPU.IF start ========================================"));
            pc.plus4();
            instructionMemory.signalGet(pc.fetch());
            currentRawInstruction = instructionMemory.getInstruction();
            System.out.println(String.format("======================================== CPU.IF end ========================================"));
            Thread.sleep(0, 20);
        }

        public void ID(RegisterFile registerFile, ControlUnit controlUnit) throws Exception {
            System.out.println(String.format("======================================== CPU.ID start ========================================"));
            String opType =  Utils.byte32ToString(currentRawInstruction).substring(0, 6);
            Instruction instruction = null;
            switch (opType) {
                case "000000":
                case "000001":
                case "000010":
                    instruction = new ArithmeticInstruction(currentRawInstruction);
                    break;
                case "010000":
                case "010001":
                case "010010":
                    instruction = new LogicInstruction(currentRawInstruction);
                    break;
                case "100000":
                    instruction = new MoveInstruction(currentRawInstruction);
                    break;
                case "100110":
                case "100111":
                    instruction = new StoreAndLoadInstruction(currentRawInstruction);
                    break;
                case "110000":
                    instruction = new BranchInstruction(currentRawInstruction);
                    break;
            }
            decodedInstruction = instruction;
            decodedInstruction.ID(registerFile, controlUnit);
            System.out.println(String.format("======================================== CPU.ID end ========================================"));
            Thread.sleep(0, 20);
        }

        public void EXE(RegisterFile registerFile, ALU alu, ControlUnit controlUnit) throws Exception {
            System.out.println(String.format("======================================== CPU.EXE start ========================================"));
            decodedInstruction.EXE(registerFile, alu, controlUnit);
            System.out.println(String.format("======================================== CPU.EXE end ========================================"));
            Thread.sleep(0, 40);
        }

        public void MEM(RegisterFile registerFile, DataMemory dataMemory, ALU alu, ControlUnit controlUnit, PC pc) throws Exception {
            System.out.println(String.format("======================================== CPU.MEM start ========================================"));
            decodedInstruction.MEM(registerFile, dataMemory, alu, controlUnit, pc);
            System.out.println(String.format("======================================== CPU.MEM end ========================================"));
            Thread.sleep(0, 20);
        }

        public void WB(RegisterFile registerFile, ALU alu, ControlUnit controlUnit, DataMemory dataMemory) throws Exception {
            System.out.println(String.format("======================================== CPU.WB start ========================================"));
            decodedInstruction.WB(registerFile, alu, controlUnit, dataMemory);
            System.out.println(String.format("======================================== CPU.WB end ========================================"));
            Thread.sleep(0, 20);
        }
    }
}
