package ca;

import ca.module.SingleCycleCPU;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws Exception{
        SingleCycleCPU singleCycleCPU = new SingleCycleCPU();
        singleCycleCPU.loadCode(new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/instructions.txt"))));
        singleCycleCPU.mainLoop();
    }
}
