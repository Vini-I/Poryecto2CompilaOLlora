
package Utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;


public abstract class UtilDate {

    public static int calculateAge(LocalDate date) {
        return Period.between(date, LocalDate.now()).getYears();
    }

    public static boolean isNotFutureDate(LocalDate date) {
        return !date.isAfter(LocalDate.now());
    }

    public static boolean isLegalAge(LocalDate date) {
        return calculateAge(date) >= 18;
    }

    public static LocalDate toLocalDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public static String toString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public static boolean validateYear(int year) {
        int currentYear = LocalDate.now().getYear();
        int minYear = currentYear - 20;
        return year >= minYear && year <= currentYear;
    }

}
