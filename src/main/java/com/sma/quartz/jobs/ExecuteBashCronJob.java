package com.sma.quartz.jobs;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.*;

@Slf4j
@DisallowConcurrentExecution
public class ExecuteBashCronJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("ExecuteBashCronJob Start................");

//        final boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
//        final String homeDirectory = System.getProperty("user.home");
//
//        if (isWindows) {
//           log.info("=============WindowOS================" + homeDirectory);
//        } else {
//           log.info("=============LinuxOS================" + homeDirectory);
//        }

        final String bashScript = (String) context.getMergedJobDataMap().get("data");

        log.info("bashScript {}", bashScript);

        // Run a shell script
        final InputStream in;
        try {
            in = IOUtils.toInputStream(bashScript, "utf-8");
        } catch (IOException e) {
            throw new RuntimeException("could not read input stream");
        }

        final File tmpFolder = Helper.getTmpFolder();
        //copy to tmp and execute
        final File tmpFile = new File(tmpFolder, StringUtils.replace(context.getJobDetail().getKey().getName(), " ", "").trim() + ".sh");
        try (OutputStream stream = new FileOutputStream(tmpFile)) {
            IOUtils.copy(in, stream);
        } catch (IOException e) {
            throw new RuntimeException("could not read input stream");
        }
        try {
            //check mod +x
            Helper.command("chmod +x " + tmpFile.getAbsolutePath());
            //execute it
            Helper.command(String.format("%s version-1", tmpFile.getAbsolutePath()), true);
        } finally {
            //cleanup tmp folder
            try {
                FileUtils.deleteDirectory(tmpFolder);
            } catch (IOException e) {
            }
            log.info("=============COMPLETED================");
            log.info("ExecuteBashCronJob End..................");

        }
    }

//    public static void main(String[] args) {
//        log.info(DateFormatUtils.format(System.currentTimeMillis(), "YYYY_MM_DD_HH_mm_ss"));
//    }

}
