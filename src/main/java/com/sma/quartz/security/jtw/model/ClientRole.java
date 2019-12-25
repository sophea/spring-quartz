package com.sma.quartz.security.jtw.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class ClientRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String role;

    public String getRole() {
        return role;
    }
}
