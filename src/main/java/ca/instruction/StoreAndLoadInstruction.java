package ca.instruction;

import ca.Utils;
import ca.enums.DataMemRW;
import ca.module.*;

public class StoreAndLoadInstruction extends Instruction {
    public StoreAndLoadInstruction(byte[] rawInstruction) {
        super(rawInstruction);
    }

    int rs;
    int rt;
    int immediate;

    @Override
    public void ID(RegisterFile registerFile, ControlUnit controlUnit) {
        String insStr = Utils.byte32ToString(this.rawInstruction);
        String opType = insStr.substring(0, 6);
        rs = Integer.parseInt(insStr.substring(6, 11), 2);
        rt = Integer.parseInt(insStr.substring(11, 16), 2);
        immediate = Integer.parseInt(insStr.substring(16, 32), 2);
        switch (opType) {
            case "100110":
                /* sw */
                controlUnit.DataMemRW = DataMemRW.STATE_1;
                break;
            case "100111":
                /* lw */
                controlUnit.DataMemRW = DataMemRW.STATE_0;
                break;
        }
        System.out.println(String.format("StoreAndLoadInstruction.ID dataMemRW is %s", controlUnit.DataMemRW));
        registerFile.signalRead1(rs);
        registerFile.signalRead2(rt);
    }

    @Override
    public void EXE(RegisterFile registerFile, ALU alu, ControlUnit controlUnit) {
        byte[] rsValue = registerFile.fetchRead1();
        byte[] rtValue = registerFile.fetchRead2();

        alu.add(rsValue, Utils.Int32ToRawMemory(immediate << 2));
    }

    @Override
    public void MEM(RegisterFile registerFile, DataMemory dataMemory, ALU alu, ControlUnit controlUnit, PC pc) {
        byte[] memValue = alu.aluResult();
        byte[] rtValue = new byte[]{0, 0, 0, 0};
        if(controlUnit.DataMemRW == DataMemRW.STATE_0) {
            dataMemory.signalRead(Utils.rawMemoryToInt32(memValue), 4);
        }
        else if(controlUnit.DataMemRW == DataMemRW.STATE_1){
            rtValue = registerFile.fetchRead2();
            dataMemory.signalWrite(Utils.rawMemoryToInt32(memValue), rtValue);
            dataMemory.doWrite();
        }
        System.out.println(String.format("StoreAndLoadInstruction.MEM memValue is %d, registerFileIndex is %d", Utils.rawMemoryToInt32(memValue), rt));
    }

    @Override
    public void WB(RegisterFile registerFile, ALU alu, ControlUnit controlUnit, DataMemory dataMemory) {
        byte[] memValue = alu.aluResult();
        if(controlUnit.DataMemRW == DataMemRW.STATE_0) {
            byte[] data = dataMemory.fetchRead();
            registerFile.signalWrite(rt, data);
            registerFile.doWrite();
        }
    }
}
