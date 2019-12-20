package com.sma.quartz.service;


import com.sma.quartz.entity.SchedulerJobInfo;
import com.sma.quartz.repository.SchedulerRepository;

public interface ScheduleJobInfoService extends BaseServiceInterface<SchedulerJobInfo>  {

    SchedulerRepository getRepository();

}
