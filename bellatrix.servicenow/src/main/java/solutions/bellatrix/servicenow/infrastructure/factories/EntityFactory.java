package solutions.bellatrix.servicenow.infrastructure.factories;

import com.github.javafaker.Faker;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;

import java.time.LocalDate;

public abstract class EntityFactory {
    private final Faker faker;

    public EntityFactory() {
        faker = new Faker();
    }

    public Faker getFaker() {
        return faker;
    }

    protected static LocalDate filterWeekdaysForRange(final LocalDate start) {
        LocalDate result = start;

        while (result.getDayOfWeek() == SATURDAY || result.getDayOfWeek() == SUNDAY) {
            result = result.plusDays(1);
        }

        return result;
    }
}