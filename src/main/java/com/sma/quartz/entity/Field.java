package com.sma.quartz.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Field implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private String type;
    private String defaultValue;
    private List<Data> data;

    public Field(String name, String type) {
        this.name = name;
        this.type = type;
    }
    public Field(String name, String type, String defaultValue) {
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
    }

}
