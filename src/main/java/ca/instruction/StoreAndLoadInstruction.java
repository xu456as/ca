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

    volatile byte[] aluResult = null;
    DataMemRW dataMemRW = null;
    volatile byte[] data;

    byte[] rsValue;
    byte[] rtValue;

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
        dataMemRW = controlUnit.DataMemRW;
        System.out.println(String.format("StoreAndLoadInstruction.ID dataMemRW is %s", controlUnit.DataMemRW));
    }

    @Override
    public void EXE(RegisterFile registerFile, ALU alu, ControlUnit controlUnit) {
        rsValue = registerFile.signalRead1(rs);
        rtValue = registerFile.signalRead2(rt);

        aluResult = alu.add(rsValue, Utils.Int32ToRawMemory(immediate << 2));
    }

    @Override
    public void MEM(RegisterFile registerFile, DataMemory dataMemory, ALU alu, ControlUnit controlUnit, PC pc) {
        byte[] memValue = aluResult;
        if(dataMemRW == DataMemRW.STATE_0) {
            data = dataMemory.signalRead(Utils.rawMemoryToInt32(memValue), 4);
            if(data == null) {
                System.out.println("nullData");
            }
        }
        else if(dataMemRW == DataMemRW.STATE_1){
            dataMemory.signalWrite(Utils.rawMemoryToInt32(memValue), rtValue);
        }
        System.out.println(String.format("StoreAndLoadInstruction.MEM memValue is %d, registerFileIndex is %d", Utils.rawMemoryToInt32(memValue), rt));
    }

    @Override
    public void WB(RegisterFile registerFile, ALU alu, ControlUnit controlUnit, DataMemory dataMemory) {
        if(dataMemRW == DataMemRW.STATE_0) {
            registerFile.signalWrite(rt, data);
            System.out.println("data:" + Utils.byte32ToString(data));
        }
    }
}
