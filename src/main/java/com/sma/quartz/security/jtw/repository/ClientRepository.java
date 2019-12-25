package com.sma.quartz.security.jtw.repository;

import com.sma.quartz.entity.PublicHoliday;
import com.sma.quartz.security.jtw.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {

    /*
     * After upgrading Spring Boot from 1.5.7.RELEASE to 2.1.2.RELEASE
     * List<ClientGroup> and List<ClientRole> within IntegrationExecution is just
     * not loaded if retrieving by normal findById method.
     *
     * @Query works fine in Spring Boot 2.1.2.RELEASE need to investigate further
     */
    @Query("from Client where Id = ?1")
    Optional<Client> findByIdWorkaround(String id);
}
