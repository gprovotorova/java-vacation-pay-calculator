package com.example.java_vacation_pay_calculator.controller;

import com.example.java_vacation_pay_calculator.response.CalculatorResponse;
import com.example.java_vacation_pay_calculator.service.CalculatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import java.time.LocalDate;

@RestController
@Slf4j
@RequestMapping
@Validated
@RequiredArgsConstructor
public class CalculatorController {

    private final CalculatorService calculatorService;
    public static final String DATE_FORMAT = "dd.MM.yyyy";

    @GetMapping("/calculate")
    public CalculatorResponse calculateVacationPay(@RequestParam @Positive Long averageSalary,
                                                   @RequestParam(required = false, defaultValue = "0") Integer vacationDays,
                                                   @RequestParam(required = false)
                                                   @DateTimeFormat(pattern = DATE_FORMAT) LocalDate startVacationDate,
                                                   @RequestParam(required = false)
                                                   @DateTimeFormat(pattern = DATE_FORMAT) LocalDate endVacationDate) {
        return calculatorService.calculateVacationPay(averageSalary, vacationDays, startVacationDate, endVacationDate);
    }

}
