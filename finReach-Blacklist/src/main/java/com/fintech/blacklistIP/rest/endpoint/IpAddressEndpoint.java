package com.fintech.blacklistIP.rest.endpoint;

import com.fintech.blacklistIP.rest.domain.IpAddress;
import com.fintech.blacklistIP.rest.service.IpAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class IpAddressEndpoint extends BaseEndpoint {

	static final int DEFAULT_PAGE_SIZE = 10;

	@Autowired
	IpAddressService ipAddressService;

	@RequestMapping(path = "/v1/ipAddresss", method = RequestMethod.GET)
	@ApiOperation(
			value = "Get all ipAddresss",
			response = Page.class)
	public Page<IpAddress> getAll(
			@ApiParam("The size of the page to be returned") @RequestParam(required = false) Integer size,
			@ApiParam("Zero-based page index") @RequestParam(required = false) Integer page) {

		if (size == null) {
			size = DEFAULT_PAGE_SIZE;
		}
		if (page == null) {
			page = 0;
		}

		Pageable pageable = new PageRequest(page, size);
		Page<IpAddress> ipAddress = ipAddressService.findAll(pageable);

		return ipAddress;
	}

	@RequestMapping(path = "/v1/ipAddress/{id}", method = RequestMethod.GET)
	@ApiOperation(
			value = "Get ipAddress by id",
			notes = "Returns ipAddress for id specified.",
			response = IpAddress.class)
	@ApiResponses(value = {@ApiResponse(code = 404, message = "IpAddress not found")})
	public ResponseEntity<IpAddress> get(@ApiParam("IpAddress id") @PathVariable("id") Long id) {

		IpAddress ipAddress = ipAddressService.findOne(id);
		return (ipAddress == null ? ResponseEntity.status(HttpStatus.NOT_FOUND) : ResponseEntity.ok()).body(ipAddress);
	}

	@RequestMapping(path = "/v1/ipAddress", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ApiOperation(
			value = "Create new or update existing ipAddress",
			response = IpAddress.class)
	public ResponseEntity<IpAddress> add(
			@RequestBody IpAddress ipAddress) {

		ipAddress = ipAddressService.save(ipAddress);
		return ResponseEntity.ok().body(ipAddress);
	}
	@RequestMapping(path = "/v1/ipAddress", method = RequestMethod.DELETE, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ApiOperation(
			value = "Delete existing ipAddress",
			response = IpAddress.class)
	public ResponseEntity<IpAddress> delete(
			@RequestBody IpAddress ipAddress) {

		ipAddressService.delete(ipAddress);
		return ResponseEntity.ok().body(ipAddress);
	}

}