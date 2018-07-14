package com.fintech.blacklistIP.rest.endpoint;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.UUID;

import javax.persistence.EntityManager;

import com.fintech.blacklistIP.rest.domain.IpAddress;
import com.fintech.blacklistIP.rest.service.IpAddressService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import net.minidev.json.JSONArray;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class IpAddressEndpointTest extends BaseEndpointTest {

	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private IpAddressService ipAddressService;
	
	private IpAddress testIpAddress;
	private long timestamp;
	
    @Before
    public void setup() throws Exception {
    	super.setup();

    	timestamp = new Date().getTime();

    	// create test ipAddresss
    	ipAddressService.save(createIpAddress("10.10.0.11"));
    	ipAddressService.save(createIpAddress("10.10.0.12"));
    	ipAddressService.save(createIpAddress("10.10.0.13"));
    	ipAddressService.save(createIpAddress("10.10.0.14"));
    	ipAddressService.save(createIpAddress("10.10.0.1"));

    	Page<IpAddress> ipAddresss = ipAddressService.findAll(new PageRequest(0, IpAddressEndpoint.DEFAULT_PAGE_SIZE));
		assertNotNull(ipAddresss);
		assertEquals(5L, ipAddresss.getTotalElements());
		
		testIpAddress = ipAddresss.getContent().get(0);
		entityManager.refresh(testIpAddress);
    }

    @Test
    public void getIpAddressById() throws Exception {
    	Long id = testIpAddress.getId();
    	
    	mockMvc.perform(get("/v1/ipAddress/{id}", id))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentType(JSON_MEDIA_TYPE))
			.andExpect(jsonPath("$.id", is(id.intValue())))
			.andExpect(jsonPath("$.ipAddress", is(testIpAddress.getIpAddress())))
    	;
    }

    /**
     * Test JSR-303 bean validation. 
     */
    @Test
    public void createIpAddressValidationErrorLastName() throws Exception {
    	
    	//ipAddress with missing last name
    	IpAddress ipAddress = createIpAddress("first");
    	String content = json(ipAddress);
		mockMvc.perform(
				put("/v1/ipAddress")
				.accept(JSON_MEDIA_TYPE)
				.content(content)
				.contentType(JSON_MEDIA_TYPE))
		.andDo(print())
		.andExpect(status().isBadRequest())
		.andExpect(content().contentType(JSON_MEDIA_TYPE))
		.andExpect(jsonPath("$", isA(JSONArray.class)))
		.andExpect(jsonPath("$.length()", is(1)))
    	.andExpect(jsonPath("$.[?(@.field == 'lastName')].message", hasItem("may not be null")))
		;
    }

    /**
     * Test custom bean validation. 
     */
    @Test
    public void createIpAddressValidationErrorMiddleName() throws Exception {
    	
    	//ipAddress with missing middle name - custom validation
    	IpAddress ipAddress = createIpAddress("10.10.0.11");
    	String content = json(ipAddress);
		mockMvc.perform(
				put("/v1/ipAddress")
				.accept(JSON_MEDIA_TYPE)
				.content(content)
				.contentType(JSON_MEDIA_TYPE))
		.andDo(print())
		.andExpect(status().isBadRequest())
		.andExpect(content().contentType(JSON_MEDIA_TYPE))
		.andExpect(jsonPath("$", isA(JSONArray.class)))
		.andExpect(jsonPath("$.length()", is(1)))
		;
    }

    /**
     * Test JSR-303 bean object graph validation with nested entities. 
     */
    @Test
    public void createIpAddressValidationAddress() throws Exception {
    	
    	IpAddress ipAddress = createIpAddress("10.10.0.11");

    	String content = json(ipAddress);
		mockMvc.perform(
				put("/v1/ipAddress")
				.accept(JSON_MEDIA_TYPE)
				.content(content)
				.contentType(JSON_MEDIA_TYPE))
		.andDo(print())
		.andExpect(status().isBadRequest())
		.andExpect(content().contentType(JSON_MEDIA_TYPE))
		.andExpect(jsonPath("$.length()", is(4)))
    	.andExpect(jsonPath("$.[?(@.field == 'addresses[].line1')].message", hasItem("may not be null")))
    	.andExpect(jsonPath("$.[?(@.field == 'addresses[].state')].message", hasItem("may not be null")))
    	.andExpect(jsonPath("$.[?(@.field == 'addresses[].city')].message", hasItem("may not be null")))
    	.andExpect(jsonPath("$.[?(@.field == 'addresses[].zip')].message", hasItem("may not be null")))
		;
    }

    @Test
    public void createIpAddressValidationToken() throws Exception {
    	
    	IpAddress ipAddress = createIpAddress("first");
    	
    	String content = json(ipAddress);
    	mockMvc.perform(
    			put("/v1/ipAddress")
    			.accept(JSON_MEDIA_TYPE)
    			.content(content)
    			.contentType(JSON_MEDIA_TYPE))
    	.andDo(print())
    	.andExpect(status().isBadRequest())
    	.andExpect(content().contentType(JSON_MEDIA_TYPE))
    	.andExpect(jsonPath("$.length()", is(1)))
    	.andExpect(jsonPath("$.[?(@.field == 'add.arg2')].message", hasItem("token size 2-40")))
    	;
    }
    
    @Test
    public void createIpAddressValidationUserId() throws Exception {
    	
    	IpAddress ipAddress = createIpAddress("10.10.0.11");
    	
    	String content = json(ipAddress);
    	mockMvc.perform(
    			put("/v1/ipAddress")
    			.accept(JSON_MEDIA_TYPE)
    			.content(content)
    			.contentType(JSON_MEDIA_TYPE))
    	.andDo(print())
    	.andExpect(status().isBadRequest())
    	.andExpect(content().contentType(JSON_MEDIA_TYPE))
    	.andExpect(jsonPath("$.message", containsString("Missing request header '")))
    	;
    }
    
    @Test
    public void createIpAddress() throws Exception {
    	
    	IpAddress ipAddress = createIpAddress("10.10.0.11");

    	String content = json(ipAddress);
		
    	mockMvc.perform(
				put("/v1/ipAddress")
				.accept(JSON_MEDIA_TYPE)
				.content(content)
				.contentType(JSON_MEDIA_TYPE))
		.andDo(print())
		.andExpect(status().isOk())
    	.andExpect(jsonPath("$.id", isA(Number.class)))
    	.andExpect(jsonPath("$.ipAddress", is(ipAddress.getIpAddress())))
		;
    }

    @Test
    public void createIpAddressWithDateVerification() throws Exception {
    	
    	IpAddress ipAddress = createIpAddress("10.10.0.11");

    	String content = json(ipAddress);
    	
    	mockMvc.perform(
    			put("/v1/ipAddress")
    			.accept(JSON_MEDIA_TYPE)
    			.content(content)
    			.contentType(JSON_MEDIA_TYPE))
    	.andExpect(status().isOk())
    	.andExpect(jsonPath("$.id", isA(Number.class)))
    	.andExpect(jsonPath("$.ipAddress", is(ipAddress.getIpAddress())));
    	
    }

    @Test
    public void requestBodyValidationInvalidJsonValue() throws Exception {

    	String content = json(testIpAddress);

    	mockMvc.perform(
    			put("/v1/ipAddress")
    			.accept(JSON_MEDIA_TYPE)
    			.content(content)
    			.contentType(JSON_MEDIA_TYPE))
    	.andDo(print())
    	.andExpect(status().isBadRequest())
    	.andExpect(content().contentType(JSON_MEDIA_TYPE))
    	.andExpect(jsonPath("$.message", containsString("Could not read document: Can not deserialize value of type IpAddress$Gender")))
    	;
    }
    
    @Test
    public void requestBodyValidationInvalidJson() throws Exception {
    	
    	String content = json("not valid json");
    	mockMvc.perform(
    			put("/v1/ipAddress")
    			.accept(JSON_MEDIA_TYPE)
    			.content(content)
    			.contentType(JSON_MEDIA_TYPE))
    	.andDo(print())
    	.andExpect(status().isBadRequest())
    	.andExpect(content().contentType(JSON_MEDIA_TYPE))
    	.andExpect(jsonPath("$.message", containsString("Could not read document: Can not construct instance of IpAddress")))
    	;
    }

    @Test
    public void handleHttpRequestMethodNotSupportedException() throws Exception {
    	
    	String content = json(testIpAddress);
    	
    	mockMvc.perform(
    			delete("/v1/ipAddress") //not supported method
    			.accept(JSON_MEDIA_TYPE)
    			.content(content)
    			.contentType(JSON_MEDIA_TYPE))
    	.andDo(print())
    	.andExpect(status().isMethodNotAllowed())
    	.andExpect(content().string(""))
    	;
    }

	private IpAddress createIpAddress(String ip_Address) {
		IpAddress ipAddress = new IpAddress(1L,"ip_Address");
		return ipAddress;
	}

}
