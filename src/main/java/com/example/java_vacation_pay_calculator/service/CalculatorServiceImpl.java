package com.example.java_vacation_pay_calculator.service;

import com.example.java_vacation_pay_calculator.response.CalculatorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CalculatorServiceImpl implements CalculatorService {
    public final static int CURRENT_YEAR = LocalDate.now().getYear();

    @Override
    public Integer calculatePaidDays(Integer vacationDaysWithWeekends,
                                     LocalDate startVacationDate,
                                     LocalDate endVacationDate) {

        if (vacationDaysWithWeekends == 0 && (startVacationDate == null || endVacationDate == null)) {
            throw new ValidationException("Проверьте введенные данные. " +
                    "Количество дней не должно быть равно 0 или введите даты начала и конца отпуска.");
        }

        if (vacationDaysWithWeekends < 0 && (startVacationDate == null || endVacationDate == null)) {
            throw new ValidationException("Проверьте введенные данные. " +
                    "Количество дней не должно быть отрицательным или введите даты начала и конца отпуска.");
        }

        if (vacationDaysWithWeekends > 0  && startVacationDate != null && endVacationDate != null) {
            if (vacationDaysWithWeekends != Math.toIntExact(calculateDaysBetween(startVacationDate, endVacationDate))) {
                throw new ValidationException("Проверьте введенные данные. " +
                        "Для расчета укажите количество дней и дату начала отпуска или даты начала и конца отпуска.");
            }
        }

        if (vacationDaysWithWeekends == 0 || vacationDaysWithWeekends == null) {
            vacationDaysWithWeekends = Math.toIntExact(calculateDaysBetween(startVacationDate, endVacationDate));
        }

        if (vacationDaysWithWeekends < 0 && startVacationDate != null && endVacationDate != null) {
            vacationDaysWithWeekends = Math.toIntExact(calculateDaysBetween(startVacationDate, endVacationDate));
        }

        if(startVacationDate == null){
            startVacationDate = endVacationDate.minusDays(vacationDaysWithWeekends);
        }

        List<LocalDate> vacationDays = IntStream.range(0, vacationDaysWithWeekends)
                .mapToObj(startVacationDate::plusDays)
                .collect(Collectors.toList());

        long workDays = vacationDays.stream()
                .filter(date -> !isWeekend(date) && !isHoliday(date))
                .count();

        return Math.toIntExact(workDays);
    }

    @Override
    public CalculatorResponse calculateVacationPay(Long averageSalary,
                                                   Integer vacationDays,
                                                   LocalDate startVacationDate,
                                                   LocalDate endVacationDate) {
        if ((vacationDays == 0 || vacationDays == null) && startVacationDate == null && endVacationDate == null) {
            throw new ValidationException("Недостаточно данных для расчета. " +
                    "Проверьте, чтобы были введены количество дней отпуска и дата начала или конца отпуска.");
        }

        if (startVacationDate != null && endVacationDate != null) {
            if (startVacationDate.getYear() != endVacationDate.getYear()) {
                throw new ValidationException("Расчет должен должен производится за один финансовый год.");
            }
            if (startVacationDate.isAfter(endVacationDate)) {
                throw new ValidationException("Дата окончания отпуска не может быть раньше даты начала отпуска");
            }
        }

        Integer paidDays;
        if (startVacationDate == null && endVacationDate == null) {
            paidDays = vacationDays;
        } else {
            paidDays = calculatePaidDays(vacationDays, startVacationDate, endVacationDate);
        }

        double averageDailyEarnings = averageSalary * 12 / 351.6;

        double pay = paidDays * averageDailyEarnings;

        return CalculatorResponse.builder()
                .outputMessage("Сумма отпускных (с учетом НДФЛ)")
                .vacationPay(new DecimalFormat("#0.00").format(pay))
                .build();
    }

    public static long calculateDaysBetween(LocalDate startVacationDate,
                                            LocalDate endVacationDate) {
        return java.time.temporal.ChronoUnit.DAYS.between(startVacationDate, endVacationDate);
    }

    private boolean isHoliday(LocalDate date) {
        List<LocalDate> holidays = List.of(
                LocalDate.of(CURRENT_YEAR, 1, 1),
                LocalDate.of(CURRENT_YEAR, 1, 2),
                LocalDate.of(CURRENT_YEAR, 1, 3),
                LocalDate.of(CURRENT_YEAR, 1, 4),
                LocalDate.of(CURRENT_YEAR, 1, 5),
                LocalDate.of(CURRENT_YEAR, 1, 6),
                LocalDate.of(CURRENT_YEAR, 1, 7),
                LocalDate.of(CURRENT_YEAR, 1, 8),
                LocalDate.of(CURRENT_YEAR, 2, 23),
                LocalDate.of(CURRENT_YEAR, 3, 8),
                LocalDate.of(CURRENT_YEAR, 5, 1),
                LocalDate.of(CURRENT_YEAR, 5, 9),
                LocalDate.of(CURRENT_YEAR, 6, 12),
                LocalDate.of(CURRENT_YEAR, 11, 4)
        );

        return holidays.contains(date);
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }
}
