package com.sma.quartz.security.jtw.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(catalog = "quartz_demo_db", name = "Client")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Client {
    @Id
    private String id;
    @JsonIgnore
    private String passwordHash;
    private String name;
    private String description;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "clientId")
    private List<ClientRole> clientRoles = new ArrayList<>();

    @ManyToMany()
    @JoinTable(name = "ClientGroupRelationship", joinColumns = @JoinColumn(name = "ClientId"), inverseJoinColumns = @JoinColumn(name = "GroupId"))
    private List<ClientGroup> clientGroups = new ArrayList<>(0);

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getName() {
        return name;
    }

    public List<String> getRoles() {
        return clientRoles.stream().map(ClientRole::getRole).collect(Collectors.toList());
    }

    public List<String> getGroups() {
        Hibernate.initialize(clientGroups);
        return clientGroups.stream().map(ClientGroup::getName).collect(Collectors.toList());
    }
}
