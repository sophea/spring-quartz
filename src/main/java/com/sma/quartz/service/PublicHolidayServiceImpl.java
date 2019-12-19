package com.sma.quartz.service;


import com.sma.common.tools.exceptions.ItemNotFoundBusinessException;
import com.sma.quartz.entity.PublicHoliday;
import com.sma.quartz.repository.PublicHolidayRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class PublicHolidayServiceImpl extends BaseService<PublicHolidayRepository, PublicHoliday> implements PublicHolidayService {

    @Autowired
    private PublicHolidayRepository repository;

    @Override
    public PublicHolidayRepository getRepository() {
        return repository;
    }



    @Override
    public PublicHoliday update(Long id, PublicHoliday item) {
        Optional<PublicHoliday> domain = findById(id);
        if (!domain.isPresent()) {
            throw new ItemNotFoundBusinessException("Item not found");
        }

        return repository.save(item);
    }

}
