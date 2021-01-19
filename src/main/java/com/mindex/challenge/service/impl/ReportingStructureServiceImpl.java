package com.mindex.challenge.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

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
	 * I interpret this to mean all distinct reports under the given employee
	 */
    private int getNumberOfReports(Employee employee) {
    	List<Employee> distinctReports = new ArrayList<Employee>();
    	Queue<Employee> allReports = new LinkedList<Employee>();
    	
    	allReports.add(employee);
    	
    	while(!allReports.isEmpty()) {
    		Employee currentEmployee = allReports.remove();
    		// The array list returned by getDirectReports contains only the employeeId, 
    		// so we need to use the repository to get the full employee object
    		currentEmployee = employeeRepository.findByEmployeeId(currentEmployee.getEmployeeId());
    		List<Employee> directReports = currentEmployee.getDirectReports();
    		if(directReports != null && directReports.size() > 0) {
    			for(Employee report: directReports) {
    				if(!distinctReports.contains(report)) {
    					distinctReports.add(report);
    					allReports.add(report);
    				}
    			}
    		}
    	}
    	return distinctReports.size();
    }
	
	
}
