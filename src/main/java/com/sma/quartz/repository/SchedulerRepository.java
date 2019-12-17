package com.sma.quartz.repository;

import com.sma.quartz.entity.SchedulerJobInfo;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchedulerRepository extends JpaRepository<SchedulerJobInfo, Long> {
}
