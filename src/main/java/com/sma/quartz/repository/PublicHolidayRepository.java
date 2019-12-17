package com.sma.quartz.repository;

import com.sma.quartz.entity.PublicHoliday;
import com.sma.quartz.entity.SchedulerJobInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicHolidayRepository extends JpaRepository<PublicHoliday, Long> {
}
