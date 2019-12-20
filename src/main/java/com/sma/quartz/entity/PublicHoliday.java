package com.sma.quartz.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author Mak Sophea
 * @date : 12/17/2019
 **/
@Getter
@Setter
@Entity
@Table(catalog = "quartz_demo_db", name = "public_holiday")
@EntityListeners(AuditingEntityListener.class)
public class PublicHoliday extends AbstractLongDomainEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "name_kh")
    private String nameKh;

    @Column(name = "date_value")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateValue;


}
