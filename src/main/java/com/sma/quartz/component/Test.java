package com.sma.quartz.component;

import org.apache.commons.exec.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.lang.*;
class Solution
{
    public static int minimumConcat(String initial, String goal) {
        //Put your code here
        int total =0;
        //check the goal String exist with initial

        List<Integer> totalList = new ArrayList<>();

        for(int i =0; i<goal.length(); i++) {
            if (initial.indexOf(goal.charAt(i)) == -1) {
                return -1;
            }
            total++;
        }


        int count = 0;

        for (int index =0; index<goal.length(); index++) {
            String newGoal = goal;
            String newValue = initial;
            String search = goal.substring(0, index+1);
            System.out.println("Search " + search);
            //result [xz, y]
            for(int i=0; i<search.length();i++) {
                String letter = String.valueOf(search.charAt(i));
                newValue = newValue.replace(letter, "");
            }

            List<String> items = new ArrayList<>();
            items.add(search);
            items.add(newValue);
            System.out.println(items);


            count =0;

                boolean itemFound = false;

                    for (String item : items) {
                        boolean match = true;
                        String v = newGoal.substring(0, item.length());
                        for (int i=0; i<item.length(); i++) {
                            if (v.indexOf(item.charAt(i)) == -1) {
                                match = false;
                            }
                        }

                        if (match) {
                            System.out.println(newGoal);
                            count += newGoal.split(v, -1).length - 1;
                            newGoal = newGoal.replace(v, "");
                        }
                    }

                if ( "".equals(newGoal)) {
                    break;
                }


        }






        //letter.concat(newInitial)
        // a cbz
        // abcbcz
        // a bc bc z

        //xyz
        //xzyxz   xz y xz   , xzy + xz

       // for (int index=0; index <initial.length(); index++) {

       // }

        return count;
    }


}

public class Test
{
    private static final long TIMEOUT_FIVE_MINUTES = 5*60*1000;
//    public static void main(String args[])
//    {
//        Scanner sc = new Scanner(System.in);
//
//        String initial = "xyz";
//        String goal = "xzyxz";
//        initial="acbz";
//        goal = "abcbcz";
//        int res = Solution.minimumConcat(initial, goal);
//        System.out.println(res);


  //  }

    public static void main(String a[]) throws Exception
    {
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        PumpStreamHandler psh = new PumpStreamHandler(stdout);

        File f = new File("/Users/sopheamak/workspace/spring-quartz/src/main/resources/test.sh");
//        CommandLine cl = new CommandLine("sh");
//        cl.addArgument("-x");
//        cl.addArgument(f.getPath());
//        cl.addArgument("version1");
//
//        DefaultExecutor exec = new DefaultExecutor();
//        exec.setWorkingDirectory(new File("/tmp"));
//        exec.setStreamHandler(psh);
//        exec.execute(cl);
//        System.out.println(stdout.toString());

       // execToFile("sh", f.getPath(), "log.txt", "version");

        test();
    }

    public static void test() throws Exception {
        String cmd = "/tmp/test.sh";
        File f = new File("/Users/sopheamak/workspace/spring-quartz/src/main/resources/test.sh");
        cmd = f.getPath();
        CommandLine cmdLine = CommandLine.parse("sh -x " + cmd + " version");
        FileOutputStream fileOutputStream = new FileOutputStream("log1.txt", false);
       // ByteArrayOutputStream stdout = new ByteArrayOutputStream();
       // ByteArrayOutputStream stderr = new ByteArrayOutputStream();
        PumpStreamHandler psh = new PumpStreamHandler(fileOutputStream, fileOutputStream, null);

        psh.setStopTimeout(TIMEOUT_FIVE_MINUTES); //

        ExecuteWatchdog watchdog = new ExecuteWatchdog(TIMEOUT_FIVE_MINUTES*2); // timeout in milliseconds

        Executor executor = new DefaultExecutor();
        executor.setExitValue(0);
        executor.setStreamHandler(psh);
       executor.setWatchdog(watchdog);

        int exitValue = executor.execute(cmdLine, Collections.emptyMap());
       System.out.println(exitValue);
       // System.out.println(stdout.toString());
    }

    public static int execToFile(String command, String scriptFile, String logFile, String... params) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(logFile, true);
        PumpStreamHandler streamHandler = new PumpStreamHandler(fileOutputStream, fileOutputStream, null);

        // command
        CommandLine commandline = new CommandLine(command);
        commandline.addArgument(scriptFile);
        if (params!=null && params.length>0) {
            commandline.addArguments(params);
        }

        // exec
        DefaultExecutor exec = new DefaultExecutor();
        exec.setExitValues(null);
        exec.setStreamHandler(streamHandler);
        int exitValue = exec.execute(commandline);  // exit code: 0=success, 1=error
        return exitValue;
    }
}