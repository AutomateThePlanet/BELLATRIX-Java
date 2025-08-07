package solutions.bellatrix.servicenow.utilities.converters;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DateTimeConverter {
    private static String convertLocalDateToString(LocalDate date, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(date);
    }

    /**
     * Convert object from type LocalDate to string
     *
     * @return string representation of LocalDate in following format "yyyy-MM-dd"
     */
    public static String splitByDash(LocalDate date) {
        return convertLocalDateToString(date, "yyyy-MM-dd");
    }

    public static LocalDateTime getDateTimeSplitByDash(String date) {
        var dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return LocalDateTime.parse(date, dateFormat);
    }

    /**
     * Convert object from type LocalDate to string
     *
     * @return string representation of LocalDate in following format "MMMM dd, yyyy"
     */
    public static String fullMonthSpaceDayCommaSpaceYear(LocalDate date) {
        return convertLocalDateToString(date, "MMMM dd, yyyy");
    }

    /**
     * Convert object from type LocalDate to string
     *
     * @return string representation of LocalDate in following format "MMM dd, yyyy"
     */
    public static String threeLetterMonthSpaceDayCommaSpaceYear(LocalDate date) {
        return convertLocalDateToString(date, "MMM dd, yyyy");
    }

    /**
     * Convert object from type LocalDate to string
     *
     * @return string representation of LocalDate in following format "MM/dd/yyyy"
     */
    public static String getSlashDate(LocalDate date) {
        return convertLocalDateToString(date, "MM/dd/yyyy");
    }

    /**
     * Convert object from type LocalDate to string
     *
     * @return string representation of LocalDate in following format "M/dd/yyyy"
     */
    public static String getSlashDateWithShortMount(LocalDate date) {
        return convertLocalDateToString(date, "M/dd/yyyy");
    }

    /**
     * Convert object from type LocalDate to string
     *
     * @return string representation of LocalDate in following format "M/d/yyyy"
     */
    public static String getSlashDateWithShortMountAndShortDate(LocalDate date) {
        return convertLocalDateToString(date, "M/d/yyyy");
    }

    /**
     * Convert object from type LocalDate to string
     *
     * @return string representation of LocalDate in following format "yyyy/MM/dd"
     */
    public static String getSlashDateYearMonthDay(LocalDate date) {
        return convertLocalDateToString(date, "yyyy/MM/dd");
    }

    /**
     * Convert object from type LocalDate to string
     *
     * @return string representation of LocalDate in following format "M/yyyy"
     */
    public static String getShortMonthYear(LocalDate date) {
        return convertLocalDateToString(date, "M/yyyy");
    }


    public static String getFirstDayOfYear() {
        return DateTimeFormatter.ofPattern("MM/dd/yyyy").format(LocalDate.now().with(firstDayOfYear()));
    }

    public static String getCurrentDate() {
        return DateTimeFormatter.ofPattern("MM/dd/yyyy").format(LocalDate.now());
    }

    public static String getDateTimeYesterday() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(LocalDateTime.now().minusDays(1));
    }

    public static String getDateTimeTomorrow() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(LocalDateTime.now().plusDays(1));
    }

    public static String getCurrentShortMonthYear() {
        return DateTimeFormatter.ofPattern("M/yyyy").format(LocalDate.now());
    }

    public static String getDefaultStringFromLocalDateTime(LocalDateTime expectedHeader) {
        var date = splitByDash(expectedHeader.toLocalDate());
        var time = expectedHeader.toLocalTime().toString();

        return date + " " + time;
    }

    public static LocalDate getDateSplitByMinusSign(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static String getDateSplitBySlash(LocalDate date) {
        return DateTimeFormatter.ofPattern("M/d/yyyy").format(date);
    }

    public static LocalDate getDateSplitBySlash(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("M/d/yyyy"));
    }

    public static List<LocalDate> getDatesSplitByMinusSign(List<String> dates) {
        return dates.stream().map(DateTimeConverter::getDateSplitByMinusSign).toList();
    }

    public static List<LocalDate> getDatesSplitBySlash(List<String> dates) {
        return dates.stream().map(DateTimeConverter::getDateSplitBySlash).toList();
    }

    public static String getStringRepresentationOfDateByCustomPattern(LocalDate date, String customPattern) {
        return date.format(DateTimeFormatter.ofPattern(customPattern));
    }
    public static String getStringRepresentationOfDateByCustomPattern(LocalDateTime date, String customPattern) {
        return date.format(DateTimeFormatter.ofPattern(customPattern));
    }


    public static LocalDate getDateRepresentationOfStringByCustomPattern(String date, String customPattern) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(customPattern));
    }

    public static LocalDateTime getDateRepresentationOfStringByCustomPattern1(String date, String customPattern) {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(customPattern));
    }

}