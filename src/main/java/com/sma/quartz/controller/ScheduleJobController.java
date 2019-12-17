package com.sma.quartz.controller;

import com.sma.quartz.component.JobScheduleCreator;
import com.sma.quartz.entity.SchedulerJobInfo;
import com.sma.quartz.repository.SchedulerRepository;
import com.sma.quartz.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.*;
import org.quartz.impl.calendar.DailyCalendar;
import org.quartz.impl.calendar.HolidayCalendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

@Slf4j
@RestController
@RequestMapping("api/schedule-jobs")
public class ScheduleJobController {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private JobScheduleCreator scheduleCreator;
    @Autowired
    private ApplicationContext context;

    @Autowired
    private SchedulerService schedulerService;

    @Autowired
    private SchedulerRepository schedulerRepository;

    @PostMapping("create")
    public SchedulerJobInfo createNewJob(@RequestParam String name, @RequestParam String cronExpression, @RequestParam String bashText) {


        final SchedulerJobInfo jobInfo = new SchedulerJobInfo();

        jobInfo.setCronJob(true);
        jobInfo.setJobClass("com.sma.quartz.jobs.ExecuteBashCronJob");
        jobInfo.setJobName(name);
        jobInfo.setJobGroup("Group_" + name);
        jobInfo.setCronExpression(cronExpression);
        jobInfo.setBashText(bashText);
        schedulerService.scheduleNewJob(jobInfo);

//        Scheduler scheduler = schedulerFactoryBean.getScheduler();
//
//        schedulerService.startJobNow(jobInfo);
//        try {
//            JobDetail jobDetail = JobBuilder.newJob((Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()))
//                    .withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup()).build();
//            log.info("JobName {} - {}", jobInfo.getJobName(), jobInfo.getCronExpression());
//            Trigger trigger = null;
//            jobDetail = scheduleCreator.createJob((Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()),
//                    false, context, jobInfo.getJobName(), jobInfo.getJobGroup(), jobInfo.getBashText());
//
//            if (jobInfo.getCronJob() && CronExpression.isValidExpression(jobInfo.getCronExpression())) {
//                trigger = scheduleCreator.createCronTrigger(jobInfo.getJobName(), new Date(),
//                        jobInfo.getCronExpression(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
//            }
//
//            scheduler.scheduleJob(jobDetail, trigger);
//
//        } catch (ClassNotFoundException e) {
//            log.error("Class Not Found - {}", jobInfo.getJobClass(), e);
//        } catch (SchedulerException e) {
//            log.error(e.getMessage(), e);
//        }


        return jobInfo;
    }

    @PostMapping("create-calendar")
    public SchedulerJobInfo createNewJobWithExcludingCarlendar(@RequestParam String name, @RequestParam String cronExpression, @RequestParam String bashText) {


        final SchedulerJobInfo jobInfo = new SchedulerJobInfo();

        jobInfo.setCronJob(true);
        jobInfo.setJobClass("com.sma.quartz.jobs.ExecuteBashCronJob");
        jobInfo.setJobName(name);
        jobInfo.setJobGroup("Group_" + name);
        jobInfo.setCronExpression(cronExpression);
        jobInfo.setBashText(bashText);

        schedulerService.scheduleNewJobWithCalendar(jobInfo, setHolidayCalendar2019());
        return jobInfo;
    }

    @PostMapping("delete")
    public SchedulerJobInfo deleteJob(@RequestParam String name) {

        SchedulerJobInfo info = new SchedulerJobInfo();
        info.setJobName(name);

        Optional<SchedulerJobInfo> jobInfo = schedulerRepository.findOne(Example.of(info));
        if (jobInfo.isPresent()) {
            schedulerService.deleteJob(jobInfo.get());
        }

        return jobInfo.get();
    }

    private HolidayCalendar setHolidayCalendar2019() {
        final HolidayCalendar holidayCalendar = new HolidayCalendar(TimeZone.getTimeZone(ZoneId.of("Asia/Phnom_Penh")));

        /***/
        holidayCalendar.addExcludedDate(getDate("2019-11-09"));
        holidayCalendar.addExcludedDate(getDate("2019-11-10"));
        holidayCalendar.addExcludedDate(getDate("2019-11-11"));
        holidayCalendar.addExcludedDate(getDate("2019-11-12"));

        holidayCalendar.addExcludedDate(getDate("2019-12-10"));

        return holidayCalendar;
    }

    private Date getDate(String yyyymmdd) {
        final Date date;
        try {
            date = DateUtils.parseDate(yyyymmdd, "yyyy-mm-dd");
            return date;
        } catch (ParseException e) {
            return null;
        }
    }
}

