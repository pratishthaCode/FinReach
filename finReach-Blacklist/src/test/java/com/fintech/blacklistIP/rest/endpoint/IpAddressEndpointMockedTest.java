package com.fintech.blacklistIP.rest.endpoint;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fintech.blacklistIP.rest.domain.IpAddress;
import com.fintech.blacklistIP.rest.service.IpAddressService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.NestedServletException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
class IpAddressEndpointMockedTest extends BaseEndpointTest {

	@MockBean
	private IpAddressService ipAddressService;
	
	private IpAddress testIpAddress;
	
    @Before
    public void setup() throws Exception {
    	
    	super.setup();

    	testIpAddress = new IpAddress(1L, "10.0.0.0");
		when(ipAddressService.findOne(1L)).thenReturn(testIpAddress);
    }

    @Test
    public void getIpAddressById() throws Exception {
    	
    	mockMvc.perform(get("/v1/ipAddress/{id}", 1))
    	.andDo(print())
    	.andExpect(status().isOk())
    	.andExpect(content().contentType(JSON_MEDIA_TYPE))
    	.andExpect(jsonPath("$.id", is(testIpAddress.getId().intValue())))
    	.andExpect(jsonPath("$.ipAddress", is(testIpAddress.getIpAddress())))
    	;
    }
    
    @Test(expected = NestedServletException.class)
    public void handleGenericException() throws Exception {

		when(ipAddressService.findOne(1L)).thenThrow(new RuntimeException("Failed to get ipAddress by id"));

    	mockMvc.perform(get("/v1/ipAddress/{id}", 1))
    	.andDo(print())
    	.andExpect(status().is5xxServerError())
    	.andExpect(content().string(""))
    	;
    }
}
