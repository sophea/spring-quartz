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
    private Object defaultValue;
    private boolean readOnly;
    private List<Data> data;

    public Field(String name, String type) {
        this(name, type, null, false);
    }

    public <T> Field(String name, String type, T defaultValue) {
        this(name, type, defaultValue, false);
    }

    public <T> Field(String name, String type, T defaultValue, boolean readOnly) {
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
        this.readOnly = readOnly;
    }

    public Field(String name, String type, List<Data> data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }

}
