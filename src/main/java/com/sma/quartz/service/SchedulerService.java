package com.sma.quartz.service;

import com.sma.quartz.entity.SchedulerJobInfo;
import org.quartz.Calendar;

public interface SchedulerService {

    void startAllSchedulers();

    void scheduleNewJob(SchedulerJobInfo jobInfo);

    void scheduleNewJobWithCalendar(SchedulerJobInfo jobInfo, Calendar calendar);

    void updateScheduleJob(SchedulerJobInfo jobInfo);

    boolean unScheduleJob(String jobName);

    boolean deleteJob(SchedulerJobInfo jobInfo);

    boolean pauseJob(SchedulerJobInfo jobInfo);

    boolean resumeJob(SchedulerJobInfo jobInfo);

    boolean startJobNow(SchedulerJobInfo jobInfo);

}
