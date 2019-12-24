package com.sma.quartz.controller;

import com.sma.common.tools.exceptions.ItemNotFoundBusinessException;
import com.sma.common.tools.response.ResponseList;
import com.sma.quartz.component.JobScheduleCreator;
import com.sma.quartz.entity.Field;
import com.sma.quartz.entity.SchedulerJobInfo;
import com.sma.quartz.repository.SchedulerRepository;
import com.sma.quartz.service.ScheduleJobInfoService;
import com.sma.quartz.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.impl.calendar.HolidayCalendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.*;

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
    private ScheduleJobInfoService scheduleJobInfoService;

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

    @PostMapping("v1/json")
    public SchedulerJobInfo createNewJobWithExcludingCarlendar(@RequestBody SchedulerJobInfo body) {


        final SchedulerJobInfo jobInfo = new SchedulerJobInfo();

        jobInfo.setCronJob(true);
        jobInfo.setJobClass("com.sma.quartz.jobs.ExecuteBashCronJob");
        jobInfo.setJobName(body.getJobName());
        jobInfo.setJobGroup("Group_" + body.getJobName());
        jobInfo.setCronExpression(body.getCronExpression());
        jobInfo.setBashText(body.getBashText());

        schedulerService.scheduleNewJobWithCalendar(jobInfo, setHolidayCalendar2019());
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

        return info;
    }

    /**
     * schema api : Content-Type: application/x-www-form-urlencoded
     * example json value
     * <p>
     * {
     * primaryKeyName: "id",
     * tableName: "Country",
     * primaryKeyType: "long",
     * columns: {
     * comingSoon: "boolean",
     * flagImageUrl: "text",
     * isoCode: "text",
     * name: "text",
     * state: "long",
     * tcsUrl: "text",
     * price : "number",
     * createdDate: "datetime"
     * }
     * }
     */
    @RequestMapping(value = "v1/schema", method = {RequestMethod.GET}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, Object>> getSchema() {
        final Map<String, Object> body = new HashMap<String, Object>();

        final List<Field> columns = new ArrayList<>();
        columns.add(new Field("jobName", "text"));
        columns.add(new Field("cronExpression", "text"));
        columns.add(new Field("cronJob", "boolean"));
        columns.add(new Field("bashText", "text"));
        body.put("columns", columns);
        body.put("tableName", "scheduler_job_info");
        body.put("primaryKeyName", "id");
        body.put("primaryKeyType", "long");

        return new ResponseEntity<>(body, HttpStatus.OK);
    }


    /**
     * Get nodes with pagination
     *
     * @param request
     * @param limit
     * @param offset
     * @return
     */
    @GetMapping(value = "/v1", produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public ResponseList<SchedulerJobInfo> getPage(HttpServletRequest request,
                                               @RequestParam(value = "limit", defaultValue = "10") int limit,
                                               @RequestParam(value = "offset", defaultValue = "0") String offset) {
        log.info("====get limit {} , offset {} ====", limit, offset);
        int pageNumber = Integer.parseInt(StringUtils.isEmpty(offset) ? "0" : offset);
        final PageRequest pageable = PageRequest.of(pageNumber, limit);

        final Page<SchedulerJobInfo> page = scheduleJobInfoService.getPage(pageable);

        return new ResponseList<>(page.getContent(), page.getTotalElements(),page.hasNext(), pageNumber + 1, limit,null);
    }

    /**
     * find node by id
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "v1/{id}", method = RequestMethod.GET)
    @ResponseBody
    public SchedulerJobInfo findById(@PathVariable Long id) {
        // return ResponseUtil.wrapOrNotFound(dao.findById(id));
        Optional<SchedulerJobInfo> record = scheduleJobInfoService.findById(id);
        if( ! record.isPresent()) {
            throw new ItemNotFoundBusinessException("not found");
        }
        return record.get();
    }

    /**
     * delete item by id
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "v1/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable(value = "id") Long id) {
        log.info("delete id {}", id);
        schedulerService.deleteJob(scheduleJobInfoService.findById(id).get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    /**
     * update node with id
     *
     * @param id
     * @param body
     * @return
     */
    @RequestMapping(value = "v1/{id}/json", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<SchedulerJobInfo> update(@PathVariable Long id, @RequestBody SchedulerJobInfo body) {
        final SchedulerJobInfo domain = scheduleJobInfoService.update(id, body);
        schedulerService.updateScheduleJob(domain);
        return new ResponseEntity<>(domain, HttpStatus.OK);
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

