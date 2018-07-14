package com.fintech.blacklistIP.rest.service;

import com.fintech.blacklistIP.rest.domain.IpAddress;
import com.fintech.blacklistIP.rest.repository.IpAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IpAddressService {

	@Autowired 
	private IpAddressRepository repository;

	@Transactional(readOnly = true)
	public Page<IpAddress> findAll(Pageable pageable) {
		
		return repository.findAll(pageable);
	}
	
	@Transactional(readOnly = true)
	public IpAddress findOne(Long id) {
		
		return repository.findOne(id);
	}
	
	public IpAddress save(IpAddress ipAddress) {
		
		return repository.saveAndFlush(ipAddress);
	}

	public void delete(IpAddress ipAddress) {

		repository.delete(ipAddress);

	}
}
