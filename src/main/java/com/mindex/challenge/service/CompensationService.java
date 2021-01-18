package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;

public interface CompensationService {
	Compensation read(String employeeId);
	Compensation create(Compensation compensation);
}
