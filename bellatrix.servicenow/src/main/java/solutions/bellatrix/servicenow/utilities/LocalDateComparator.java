package solutions.bellatrix.servicenow.utilities;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class LocalDateComparator {

    public static void assertDatesAreUpcoming(List<LocalDate> listDates) {
        listDates.forEach(d -> assertFalse(d.isBefore(LocalDate.now()), "Date is not Upcoming."));
    }

    public static void assertDatesAreIn30Days(List<LocalDate> dates) {
        dates.forEach(d -> assertTrue(ChronoUnit.DAYS.between(LocalDate.now(), d) <= 30, "Date is not in 30 days."));
    }
}