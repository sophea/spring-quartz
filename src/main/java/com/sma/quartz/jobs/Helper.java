package com.sma.quartz.jobs;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;

@Slf4j
public class Helper {
    public static String command(final String cmdline) {
        return command(cmdline, false);
    }

    /**
     * Returns null if it failed for some reason.
     */
    public static String command(final String cmdline, boolean debug) {
        String output = null;
        try {
            if (debug) {
                log.info("[>] " + cmdline);
            }
            final Process process = new ProcessBuilder(new String[]{"bash", "-c", cmdline})
                .redirectErrorStream(true)
                .start();
            //There should really be a timeout here.
            if (0 != process.waitFor()) {
                return null;
            }
            output = IOUtils.toString(process.getInputStream(), "utf-8");
            log.info(output);
        } catch (Exception e) {
            //Warning: doing this is no good in high quality applications.
            //Instead, present appropriate error messages to the user.
            //But it's perfectly fine for prototyping.
            System.out.println(e.getMessage());
        }
        return output;
    }

    public static File getTmpFolder () {
        final File tmpFolder = new File(FileUtils.getTempDirectory(), System.currentTimeMillis() + RandomStringUtils.randomAlphabetic(10));
        if (!tmpFolder.mkdirs()) {
            if (!tmpFolder.exists() || !tmpFolder.isDirectory()) {
                throw new RuntimeException( "could not read input stream");
            }
        }
        return tmpFolder;
    }
}
