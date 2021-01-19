package com.mindex.challenge.service.impl;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.impl.utils.EmployeeEquivalence;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {
	
	private String compensationUrl;
	private String compensationIdUrl;
	
	@LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Before
    public void setup() {
    	compensationUrl = "http://localhost:" + port + "/compensation";
    	compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";
    }
    
    @Test
    public void testCreateRead() {
    	Compensation testCompensation = new Compensation();
    	Employee testEmployee = employeeRepository.findByEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");
    	testCompensation.setEmployee(testEmployee);
    	testCompensation.setSalary(new BigDecimal(80000));
    	testCompensation.setEffectiveDate(LocalDate.now());
    	
    	// Test Create
    	Compensation createdCompensation = restTemplate.postForEntity(compensationUrl, testCompensation, Compensation.class).getBody();
    	
    	assertCompensationEquals(testCompensation, createdCompensation);
    	
    	// Test Read
    	Compensation readCompensation = restTemplate.getForEntity(compensationIdUrl, Compensation.class, createdCompensation.getEmployee().getEmployeeId()).getBody();
    	
    	assertCompensationEquals(createdCompensation, readCompensation);
    }
    
    private static void assertCompensationEquals(Compensation expected, Compensation actual) {
    	EmployeeEquivalence.assertEmployeeEquivalence(expected.getEmployee(), actual.getEmployee());
    	assertEquals(expected.getSalary(), actual.getSalary());
    	assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
    }

}
