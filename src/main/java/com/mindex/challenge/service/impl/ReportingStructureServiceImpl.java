package com.mindex.challenge.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);
	
	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public ReportingStructure read(String id) {
		LOG.debug("getting reporting structure with Employee Id [{}]", id);
		
		Employee employee = employeeRepository.findByEmployeeId(id);
		
		if(employee == null) {
			throw new RuntimeException("Invalid employeeId: " + id);
		}
		
		ReportingStructure reportingStructure = new ReportingStructure();
		reportingStructure.setEmployee(employee);
		reportingStructure.setNumberOfReports(getNumberOfReports(employee));
		return reportingStructure;
	}
	
	/*
	 * The implementation of this method depends on the interpretation of --> 
	 * "The number of reports is determined to be the number of directReports for an employee and all of their distinct reports."
	 * I have interpreted this to mean the employees direct reports, and all distinct reports one layer further down the reporting structure. 
	 * The implementation would change if we are also counting reports further 
	 */
    private int getNumberOfReports(Employee employee) { 
    	List<Employee> distinctReports = employee.getDirectReports(); 
    	List<Employee> subReports = new ArrayList<Employee>(); 
    	if(distinctReports == null || distinctReports.size() == 0) {
    		return 0;
    	} else {
	    	int numberOfReports = distinctReports.size();
		  
		    for(Employee e: distinctReports) { 
		    	Employee directReport = employeeRepository.findByEmployeeId(e.getEmployeeId());
		        if(directReport.getDirectReports() != null) {
		        	subReports.addAll(directReport.getDirectReports());
		        } 
		    } 
		    for(int i = 0; i < subReports.size(); i++) { 
		    	if(!distinctReports.contains(subReports.get(i))) {
		    		numberOfReports++; 
		    		} 
		    	} 
		    return numberOfReports;
    	}
	}
	 
	
	
}
