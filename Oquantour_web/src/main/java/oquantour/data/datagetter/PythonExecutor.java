package oquantour.data.datagetter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by island on 5/25/17.
 */
public class PythonExecutor {
    public static final String CMD = "python";

    Paramaters paramaters;

    public PythonExecutor(){
        paramaters = new Paramaters();
    }

    public void excute(String fileName, Object... args) throws IOException, InterruptedException {
        String cmd = CMD + " " + paramaters.getPath() + fileName;
        for (Object arg : args)
            cmd += " " + arg;
        System.out.println(cmd);
        String[] args1 = new String[] { "python", paramaters.getPath() + fileName};
        Process pro = Runtime.getRuntime().exec(cmd);
        BufferedReader in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
        in.close();
        pro.waitFor();
    }

    public void excuteWrittenFile(String pyFile, String outFile) throws IOException, InterruptedException {
        outFile = paramaters.getPath() + outFile;
        excute(pyFile, outFile);
    }

    public static void main(String[] args) throws Exception {
        PythonExecutor executor = new PythonExecutor();
//        executor.excute("AllRealTimeData.py", paramaters.getPath() + "realTimeData.csv");
    }
}
