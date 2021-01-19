package com.mindex.challenge.service.impl.utils;

import static org.junit.Assert.assertEquals;

import com.mindex.challenge.data.Employee;

public class EmployeeEquivalence {
	
	public EmployeeEquivalence() {
		
	}

	// This method gets used in all three Service test objects, so extracting it to reduce duplicate code. 
	public static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
