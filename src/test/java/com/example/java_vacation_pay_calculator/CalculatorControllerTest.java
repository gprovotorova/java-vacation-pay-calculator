package com.example.java_vacation_pay_calculator;

import com.example.java_vacation_pay_calculator.controller.CalculatorController;
import com.example.java_vacation_pay_calculator.response.CalculatorResponse;
import com.example.java_vacation_pay_calculator.service.CalculatorService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@AutoConfigureMockMvc
@WebMvcTest(CalculatorController.class)
public class CalculatorControllerTest {

    @Autowired
    protected MockMvc mvc;

    @MockBean
    CalculatorService calculatorService;

    public static final String DATE_FORMAT = "dd.MM.yyyy";
    private final Long averageSalary = 50000L;
    private final LocalDate startVacationDate = LocalDate.of(2024, 9, 9);
    private final LocalDate endVacationDate = startVacationDate.plusDays(10);

    @DisplayName("Рассчитывает и возвращает сумму отпускных " +
            "(введены средняя заработная плата, количество дней отпуска и начало отпуска)")
    @Test
    void vacationPayCalculationTest_whenGivenVacationDaysAndStartVacationDate() throws Exception {
        Mockito.when(this.calculatorService.calculateVacationPay(averageSalary,
                        10,
                        startVacationDate,
                        null))
                .thenReturn(CalculatorResponse.builder()
                        .outputMessage("Сумма отпускных (с учетом НДФЛ)")
                        .vacationPay(String.valueOf(13651.88))
                        .build());

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get("/calculate")
                        .param("averageSalary", String.valueOf(50000L))
                        .param("vacationDays", String.valueOf(10))
                        .param("startVacationDate", startVacationDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.vacationPay").value(13651.88))
                .andReturn();

        log.info(result.getResponse().getContentAsString());

    }

    @DisplayName("Рассчитывает и возвращает сумму отпускных " +
            "(введены средняя заработная плата и количество дней отпуска)")
    @Test
    void vacationPayCalculationTest_whenGivenVacationDays() throws Exception {
        Mockito.when(this.calculatorService.calculateVacationPay(averageSalary,
                        10,
                        null,
                        null))
                .thenReturn(CalculatorResponse.builder()
                        .outputMessage("Сумма отпускных (с учетом НДФЛ)")
                        .vacationPay(String.valueOf(17064.85))
                        .build());

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get("/calculate")
                        .param("averageSalary", String.valueOf(50000L))
                        .param("vacationDays", String.valueOf(10))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.vacationPay").value(17064.85))
                .andReturn();

        log.info(result.getResponse().getContentAsString());

    }

    @DisplayName("Рассчитывает и возвращает сумму отпускных (все данные введены корректно)")
    @Test
    void vacationPayCalculationTest_whenGivenStartVacationDateAndEndVacationDate() throws Exception {
        Mockito.when(this.calculatorService.calculateVacationPay(averageSalary,
                        0,
                        startVacationDate,
                        endVacationDate))
                .thenReturn(CalculatorResponse.builder()
                        .outputMessage("Сумма отпускных (с учетом НДФЛ)")
                        .vacationPay(String.valueOf(13651.88))
                        .build());

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get("/calculate")
                        .param("averageSalary", String.valueOf(50000L))
                        .param("startVacationDate", startVacationDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
                        .param("endVacationDate", endVacationDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.vacationPay").value(13651.88))
                .andReturn();

        log.info(result.getResponse().getContentAsString());
    }
}
