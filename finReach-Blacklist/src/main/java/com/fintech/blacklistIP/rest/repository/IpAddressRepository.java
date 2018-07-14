package com.fintech.blacklistIP.rest.repository;

import com.fintech.blacklistIP.rest.domain.IpAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IpAddressRepository extends JpaRepository<IpAddress, Long> {

}