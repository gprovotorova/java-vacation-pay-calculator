package com.example.java_vacation_pay_calculator;

import com.example.java_vacation_pay_calculator.service.CalculatorServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ValidationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class CalculatorServiceTest {

    @InjectMocks
    private CalculatorServiceImpl calculatorService;

    private final Long averageSalary = 50000L;
    private final LocalDate startVacationDate = LocalDate.of(2024, 9, 9);
    private final LocalDate endVacationDate = startVacationDate.plusDays(10);
    private final LocalDate incorrectEndVacationDate = startVacationDate.minusDays(10);
    private final LocalDate incorrectYearVacationDate = LocalDate.of(2021, 10, 14);

    @DisplayName("Рассчитывает и возвращает сумму отпускных " +
            "(введены средняя заработная плата, количество дней отпуска и начало отпуска)")
    @Test
    void shouldReturnVacationPay_whenGivenCorrectVacationDaysAndStartVacationDate() {
        int days = 10;

        String salary =
                calculatorService.calculateVacationPay(averageSalary, days, startVacationDate, null)
                        .getVacationPay();

        assertEquals("13651,88", salary);
    }

    @DisplayName("Рассчитывает и возвращает сумму отпускных " +
            "(введены средняя заработная плата, количество дней отпуска и начало отпуска)")
    @Test
    void shouldReturnVacationPay_whenGivenCorrectVacationDaysAndEndVacationDate() {
        int days = 10;

        String salary =
                calculatorService.calculateVacationPay(averageSalary, days, null, endVacationDate)
                        .getVacationPay();

        assertEquals("13651,88", salary);
    }

    @DisplayName("Рассчитывает и возвращает сумму отпускных " +
            "(введены средняя заработная плата, даты начала и конца отпуска)")
    @Test
    void shouldReturnVacationPay_whenGivenCorrectStartVacationDateAndEndVacationDate() {

        String salary =
                calculatorService.calculateVacationPay(averageSalary, 0, startVacationDate, endVacationDate)
                        .getVacationPay();

        assertEquals("13651,88", salary);
    }

    @DisplayName("Рассчитывает и возвращает сумму отпускных " +
            "(введены средняя заработная плата и количество дней отпуска)")
    @Test
    void shouldReturnVacationPay_whenGivenVacationDays() {
        int days = 10;

        String salary =
                calculatorService.calculateVacationPay(averageSalary, days, null, null)
                        .getVacationPay();

        assertEquals("17064,85", salary);
    }

    @DisplayName("Рассчитывает и возвращает сумму отпускных (количество дней - отрицательное число)")
    @Test
    void shouldReturnVacationPay_whenGivenCorrectStartVacationDateAndEndVacationDateAndNegativeDays() {
        int days = -14;

        String salary =
                calculatorService.calculateVacationPay(averageSalary, days, startVacationDate, endVacationDate)
                        .getVacationPay();

        assertEquals("13651,88", salary);
    }

    @DisplayName("Рассчитывает и возвращает сумму отпускных (количество дней - нулевое значение)")
    @Test
    void shouldReturnVacationPay_whenGivenCorrectStartVacationDateAndEndVacationDateAndZeroDays() {
        int days = 0;
        String salary =
                calculatorService.calculateVacationPay(averageSalary, days, startVacationDate, endVacationDate)
                        .getVacationPay();

        assertEquals("13651,88", salary);
    }

    @DisplayName("Выводит ошибку: введено отрицательное значение для параметра дни")
    @Test
    void shouldThrowException_whenGivenNegativeDays() {
        int days = -10;

        assertThrows(ValidationException.class, () ->
                calculatorService.calculateVacationPay(averageSalary, days, startVacationDate, null));
    }

    @DisplayName("Выводит ошибку: введено нулевое значение для параметра дни")
    @Test
    void shouldThrowException_whenGivenZeroDays() {
        int days = 0;

        assertThrows(ValidationException.class, () ->
                calculatorService.calculateVacationPay(averageSalary, days, startVacationDate, null));
    }

    @DisplayName("Выводит ошибку: конец отпуска позже начала отпуска")
    @Test
    void shouldThrowException_whenGivenToBeforeFrom() {
        int days = 10;

        assertThrows(ValidationException.class, () ->
                calculatorService.calculateVacationPay(averageSalary, days, startVacationDate, incorrectEndVacationDate));
    }

    @DisplayName("Выводит ошибку: введены разные годы для расчета данных")
    @Test
    void shouldThrowException_whenGivenDifferentYear() {
        int days = 10;

        assertThrows(ValidationException.class,
                () -> calculatorService.calculateVacationPay(averageSalary,
                        days,
                        startVacationDate,
                        incorrectYearVacationDate));
    }

    @DisplayName("Выводит ошибку: недостаточно данных")
    @Test
    void shouldThrowException_whenGivenDaysZeroAndNullEndDate() {

        assertThrows(ValidationException.class,
                () -> calculatorService.calculateVacationPay(averageSalary,
                        0,
                        startVacationDate,
                        null));
    }

    @DisplayName("Выводит ошибку: недостаточно данных")
    @Test
    void shouldThrowException_whenGivenDaysZeroAndNullStartDate() {

        assertThrows(ValidationException.class,
                () -> calculatorService.calculateVacationPay(averageSalary,
                        0,
                        null,
                        endVacationDate));
    }

    @DisplayName("Выводит ошибку: недостаточно данных")
    @Test
    void shouldThrowException_whenGivenDaysZeroAndNullStartDateAndEndDate() {

        assertThrows(ValidationException.class,
                () -> calculatorService.calculateVacationPay(averageSalary,
                        0,
                        null,
                        null));
    }

    @DisplayName("Выводит ошибку: некорректные данные")
    @Test
    void shouldThrowException_whenGivenDaysAndBetweenDaysNotEquals() {

        assertThrows(ValidationException.class,
                () -> calculatorService.calculateVacationPay(averageSalary,
                        8,
                        startVacationDate,
                        endVacationDate));
    }
}
