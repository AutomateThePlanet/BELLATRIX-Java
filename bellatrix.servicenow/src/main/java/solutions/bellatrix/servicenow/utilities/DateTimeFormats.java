package solutions.bellatrix.servicenow.utilities;

public class DateTimeFormats {
        public static final String YEAR_MONTH_DAY = "yyyy/MM/dd";
        /**
         * Convert object from type LocalDate to string:
         * Use case handler: Fri, 16 Feb, 2024
         *
         * @return string representation of LocalDate in following format "MMMM dd, yyyy"
         */
        public static final String DAY_MONTH_YEAR_FORMAT = "EEE, dd MMM, yyyy";
}