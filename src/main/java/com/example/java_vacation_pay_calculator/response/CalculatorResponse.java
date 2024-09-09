package com.example.java_vacation_pay_calculator.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculatorResponse {

    private String outputMessage;
    private String vacationPay;
}
