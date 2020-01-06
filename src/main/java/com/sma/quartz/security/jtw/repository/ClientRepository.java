package com.sma.quartz.security.jtw.repository;

import com.sma.quartz.security.jtw.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {


}
