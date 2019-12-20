package com.sma.quartz.service;


import com.sma.common.tools.exceptions.ItemNotFoundBusinessException;
import com.sma.quartz.entity.PublicHoliday;
import com.sma.quartz.entity.SchedulerJobInfo;
import com.sma.quartz.repository.PublicHolidayRepository;
import com.sma.quartz.repository.SchedulerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;



@Service("scheduleJobInfoService")
@Slf4j
public class ScheduleJobInfoServiceImpl extends BaseService<SchedulerRepository, SchedulerJobInfo> implements ScheduleJobInfoService {

    @Autowired
    private SchedulerRepository repository;

    @Override
    public SchedulerRepository getRepository() {
        return repository;
    }


    @Override
    public SchedulerJobInfo update(Long id, SchedulerJobInfo item) {
        Optional<SchedulerJobInfo> domain = findById(id);
        if (!domain.isPresent()) {
            throw new ItemNotFoundBusinessException("Item not found");
        }

        return repository.save(item);
    }

}
