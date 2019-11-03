package ca;

import ca.module.PipelinedCPU;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws Exception{
        PipelinedCPU singleCycleCPU = new PipelinedCPU();
        singleCycleCPU.loadCode(new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/LoadAddStore.txt"))));
        singleCycleCPU.mainLoop();
    }
}
