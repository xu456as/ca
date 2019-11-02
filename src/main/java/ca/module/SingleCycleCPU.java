package ca.module;

import ca.Utils;
import ca.instruction.*;

import java.io.BufferedReader;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SingleCycleCPU {
    ALU alu = new ALU();
    DataMemory dataMemory = new DataMemory();
    InstructionMemory instructionMemory = new InstructionMemory();
    PC pc = new PC();
    RegisterFile registerFile = new RegisterFile();
    SignZeroExtend signZeroExtend = new SignZeroExtend();
    ControlUnit controlUnit = new ControlUnit();
    InstructionCycle instructionCycle = new InstructionCycle();

    static class Pair {
        int address;
        Object instruction;
        Pair(int address, Object instruction){
            this.address = address;
            this.instruction = instruction;
        }
    }

    private static Object lockObj = new Object();

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
        AtomicInteger currentInstruction = new AtomicInteger(0);

        BlockingQueue<Object>[] queues = new ArrayBlockingQueue[]{
          new ArrayBlockingQueue(5),
                new ArrayBlockingQueue(5),
                new ArrayBlockingQueue(5),
                new ArrayBlockingQueue(5),
                new ArrayBlockingQueue(5)
        };

//        Callable<Object> IF = () -> instructionCycle.IF(pc, instructionMemory, controlUnit);
//        Callable<Object> ID = () -> instructionCycle.ID(registerFile, controlUnit, obj);
//        Callable<Object> EXE = () -> instructionCycle.EXE(registerFile, alu, controlUnit);
//        Callable<Object> MEM = () -> instructionCycle.MEM(registerFile, dataMemory, alu, controlUnit, pc);
//        Callable<Object> WB = () -> instructionCycle.WB(registerFile, alu, controlUnit, dataMemory);
//        Callable<Object>[] runnables = new Callable[]{IF, ID, EXE, MEM, WB};

        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        for(int i = 0; i < 5; ++i) {
            final int index = i;
            threadPool.submit(() -> {
                try {
                    while (true) {
                        if(index == 0) {
                            Object instruction = instructionCycle.IF(pc, instructionMemory, controlUnit);
                            queues[1].offer(new Pair(currentInstruction.get() * 4, instruction));
                            currentInstruction.incrementAndGet();
                            if (currentInstruction.get() >= instructionMemory.instructionCount) {
                                break;
                            }
                        }else {
                            BlockingQueue<Object> queue = queues[index];
                            if(index == 1) {
                                Pair pair;
                                if ((pair = (Pair) queue.poll(1, TimeUnit.MILLISECONDS)) != null) {
                                    Object decodedInstruction = instructionCycle.ID(registerFile, controlUnit, (byte[]) pair.instruction, pair.address);
                                    queues[index + 1].offer(new Pair(pair.address, decodedInstruction));
                                }
                            }else if(index == 2) {
                                Pair pair;
                                Instruction instruction;
                                if ((pair = (Pair) queue.poll(1, TimeUnit.MILLISECONDS)) != null) {
                                    Object decodedInstruction = instructionCycle.EXE(registerFile, alu, controlUnit, (Instruction) pair.instruction, pair.address);
                                    queues[index + 1].offer(pair);
                                }
                            }else if(index == 3) {
                                Pair pair;
                                Instruction instruction;
                                if ((pair =  (Pair) queue.poll(1, TimeUnit.MILLISECONDS)) != null) {
                                    Object decodedInstruction = instructionCycle.MEM(registerFile, dataMemory, alu, controlUnit, pc, (Instruction) pair.instruction, pair.address);
                                    queues[index + 1].offer(pair);
                                }
                            }else {
                                Pair pair;
                                Instruction instruction;
                                if ((pair = (Pair) queue.poll(1, TimeUnit.MILLISECONDS)) != null) {
                                    instructionCycle.WB(registerFile, alu, controlUnit, dataMemory,  (Instruction)pair.instruction, pair.address);
                                }
                            }
                        }
                        if(index == 0) {
                            try {
                                Thread.sleep(2000);
                            } catch (Exception ignore) {
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
        }
        try {
            Thread.sleep(10000);
        }catch (Exception ignore){}
//        while (currentInstruction < instructionMemory.instructionCount) {
//            instructionCycle.IF(pc, instructionMemory, controlUnit);
//            instructionCycle.ID(registerFile, controlUnit);
//            instructionCycle.EXE(registerFile, alu, controlUnit);
//            instructionCycle.MEM(registerFile, dataMemory, alu, controlUnit, pc);
//            instructionCycle.WB(registerFile, alu, controlUnit, dataMemory);
//            ++currentInstruction;
//        }
    }

    static class InstructionCycle {
//        volatile byte[] currentRawInstruction;
//        volatile Instruction decodedInstruction;

        byte[] IF(PC pc, InstructionMemory instructionMemory, ControlUnit controlUnit)  {
            byte[] currentRawInstruction;
            synchronized (lockObj) {
                System.out.println(String.format("======================================== CPU.IF start ========================================"));
                pc.plus4();
                instructionMemory.signalGet(pc.fetch());
                currentRawInstruction = instructionMemory.getInstruction();
                System.out.println(String.format("======================================== CPU.IF end ========================================"));
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return currentRawInstruction;
        }

        public Instruction ID(RegisterFile registerFile, ControlUnit controlUnit, byte[] currentRawInstruction, int address)  {
            Instruction decodedInstruction;
            synchronized (lockObj) {
                System.out.println(String.format("======================================== CPU.ID start %08d ================================", address));
                String opType = Utils.byte32ToString(currentRawInstruction).substring(0, 6);
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
//                    instruction = new LogicInstruction(currentRawInstruction);
                        break;
                    case "100000":
//                    instruction = new MoveInstruction(currentRawInstruction);
                        break;
                    case "100110":
                    case "100111":
                        instruction = new StoreAndLoadInstruction(currentRawInstruction);
                        break;
                    case "110000":
//                    instruction = new BranchInstruction(currentRawInstruction);
                        break;
                    case "010101":
//                    instruction = new SLTInstruction(currentRawInstruction);
                        break;
                }
                decodedInstruction = instruction;
                decodedInstruction.ID(registerFile, controlUnit);
                System.out.println(String.format("======================================== CPU.ID end ========================================"));
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return decodedInstruction;
        }

        public Instruction EXE(RegisterFile registerFile, ALU alu, ControlUnit controlUnit, Instruction decodedInstruction, int address)  {
            synchronized (lockObj) {
                System.out.println(String.format("======================================== CPU.EXE start %08d ================================", address));
                decodedInstruction.EXE(registerFile, alu, controlUnit);
                System.out.println(String.format("======================================== CPU.EXE end ========================================"));
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return decodedInstruction;
        }

        public Instruction MEM(RegisterFile registerFile, DataMemory dataMemory, ALU alu, ControlUnit controlUnit, PC pc, Instruction decodedInstruction, int address)  {
            synchronized (lockObj) {
                System.out.println(String.format("======================================== CPU.MEM start %08d ================================",address));
                decodedInstruction.MEM(registerFile, dataMemory, alu, controlUnit, pc);
                System.out.println(String.format("======================================== CPU.MEM end ========================================"));
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return decodedInstruction;
        }

        public Instruction WB(RegisterFile registerFile, ALU alu, ControlUnit controlUnit, DataMemory dataMemory, Instruction decodedInstruction, int address) {
            synchronized (lockObj) {
                System.out.println(String.format("======================================== CPU.WB start %08d ================================", address));
                decodedInstruction.WB(registerFile, alu, controlUnit, dataMemory);
                System.out.println(String.format("======================================== CPU.WB end ========================================"));
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return decodedInstruction;
        }
    }
}
