package com.example.anishsensorapi.utility;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTimeParser {
    public static LocalDateTime[] parse(String time_period) {
        /**
         * Parses a time period string and returns a date range.
         *
         * @param time_period The time period string to parse (e.g., "7 days","1 month").
         * @return A LocalDateTime array containing the start and end dates. -
         * @throws IllegalArgumentException If the time period format is invalid
         * or unsupported.
         */

        // Get the current date and time
        LocalDateTime current_date_time = LocalDateTime.now();

        //  trim spaces and convert to lowercase
        time_period = time_period.trim().toLowerCase();

        // Define a regex pattern to match amount and unit format
        Pattern pattern = Pattern.compile("(\\d+)\\s*(day|week|month|year)s?");
        Matcher matcher = pattern.matcher(time_period);

        // If the input does not match the expected format, throw an exception
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid period format");
        }

        // Extract the number and unit from the matched input
        int amount = Integer.parseInt(matcher.group(1)); 
        String unit = matcher.group(2); 

        // Calculate the start date based on the unit and amount
        switch (unit) {
            case "day" -> {
                return new LocalDateTime[]{current_date_time.minusDays(amount), current_date_time};
            }
            case "week" -> {
                return new LocalDateTime[]{current_date_time.minusWeeks(amount), current_date_time};
            }
            case "month" -> {
                return new LocalDateTime[]{current_date_time.minusMonths(amount), current_date_time};
            }
            case "year" -> {
                return new LocalDateTime[]{current_date_time.minusYears(amount), current_date_time};
            }
            default ->
                throw new IllegalArgumentException("Unsupported unit");
        }
    }
}
