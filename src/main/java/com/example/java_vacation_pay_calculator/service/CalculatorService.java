package com.example.java_vacation_pay_calculator.service;

import com.example.java_vacation_pay_calculator.response.CalculatorResponse;

import java.time.LocalDate;


public interface CalculatorService {

    Integer calculatePaidDays(Integer vacationDaysWithWeekends,
                              LocalDate startVacationDate,
                              LocalDate endVacationDate);

    CalculatorResponse calculateVacationPay(Long averageSalary,
                                            Integer vacationDays,
                                            LocalDate startVacationDate,
                                            LocalDate endVacationDate);
}
