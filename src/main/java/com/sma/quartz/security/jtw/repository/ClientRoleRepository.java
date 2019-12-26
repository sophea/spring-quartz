package com.sma.quartz.security.jtw.repository;

import com.sma.quartz.security.jtw.model.ClientRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRoleRepository extends JpaRepository<ClientRole, String> {


}
