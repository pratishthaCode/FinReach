package com.fintech.blacklistIP.rest.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
public class IpAddress {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="ip_address_id")
    private Long id;

    @NotNull
    @Column(name="ip_address", nullable = false)
    private String ip_address;


    protected IpAddress() {}

    public IpAddress(Long id, String ip_Address) {
    	this.id = id;
        this.ip_address = ip_address;
    }

    
    public Long getId() {
		return id;
	}

	public String getIpAddress() {
		return ip_address;
	}

	public void setIpAddress(String ip_address) {
		this.ip_address = ip_address;
	}


}