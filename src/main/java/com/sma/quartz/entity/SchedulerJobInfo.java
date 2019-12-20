package com.sma.quartz.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Sophea Mak
 */
@Getter
@Setter
@Entity
@Table(catalog = "quartz_demo_db", name = "scheduler_job_info")
public class SchedulerJobInfo  extends AbstractLongDomainEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jobName;

    private String jobGroup;

    private String jobClass;

    private String cronExpression;

    private Long repeatTime;

    private Boolean cronJob;

    private String bashText;
}