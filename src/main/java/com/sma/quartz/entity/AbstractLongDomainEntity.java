package com.sma.quartz.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * @author dmiuser
 * @Data is a Project Lombok annotation to autogenerate getters, setters, constructors, toString, hash, equals, and other things.
 * It cuts down on the boilerplate.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class AbstractLongDomainEntity implements Serializable {
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_VERSION = "version";
    public static final String COLUMN_NAME_STATE = "state";
    private static final long serialVersionUID = 1982069997503175834L;


    /**
     * version number changes
     */
    @JsonIgnore
    private Long version;
    /**
     * state
     */
    @Column(name = "state")
    private Long state;
    /**
     * created by
     */

    @CreatedBy
    @Column(name = "createdBy", nullable = false, length = 50, updatable = false)
    @JsonIgnore
    private String createdBy;
    /**
     * created date
     */
    // @JsonIgnore
    @CreatedDate
    @Column(name = "createdDate", updatable = false)
    @Temporal(TIMESTAMP)
    private Date createdDate;
    /**
     * updated by
     */
    @JsonIgnore
    @LastModifiedBy
    @Column(name = "updatedBy", length = 50)
    private String updatedBy;
    /**
     * updated date
     */

    @LastModifiedDate
    @Temporal(TIMESTAMP)
    @Column(name = "updatedDate")
    //@JsonIgnore
    private Date updatedDate;

}
