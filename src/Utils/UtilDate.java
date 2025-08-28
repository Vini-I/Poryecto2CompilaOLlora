
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
    
    public static boolean isNotPastDate(LocalDate date) {
        return !date.isBefore(LocalDate.now());
    }

    public static boolean periodIsValid(LocalDate date1, LocalDate date2) {
        return Period.between(date1, date2).getDays() >= 1 && Period.between(date1, date2).getDays() <=30;
    }
    
    public static int periodDuration(LocalDate date1, LocalDate date2) {
        return (int) Period.between(date1, date2).getDays();
    }
    
    public static boolean isOverlapping(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }
}
