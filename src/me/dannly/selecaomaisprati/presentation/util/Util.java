package me.dannly.selecaomaisprati.presentation.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Function;
import java.util.function.Supplier;

public class Util {

    public static LocalDate parseDateOrNull(DateTimeFormatter dateFormatter, String input) {
        try {
            return LocalDate.parse(input, dateFormatter);
        } catch (final DateTimeParseException ignored) {
            return null;
        }
    }

    public static Long parseLongOrNull(String input) {
        try {
            return Long.parseLong(input);
        } catch (final NumberFormatException ignored) {
            return null;
        }
    }

    public static Double parseDoubleOrNull(String input) {
        try {
            return Double.parseDouble(input);
        } catch (final NumberFormatException ignored) {
            return null;
        }
    }

    public static Integer parseIntOrNull(String input) {
        try {
            return Integer.parseInt(input);
        } catch (final NumberFormatException ignored) {
            return null;
        }
    }

    private static <T> T supplierCatch(Supplier<T> action) {
        try {
            return action.get();
        } catch (final Exception ignored) {
            return null;
        }
    }

    public static <T> T scanNext(String requestInputText, Function<T, Boolean> requestAgainCondition, Supplier<T> onWrongFirstInput, Supplier<T> scanValue) {
        T object;
        do {
            System.out.println(requestInputText);
            object = supplierCatch(scanValue);
            if (object == null && onWrongFirstInput != null) {
                object = onWrongFirstInput.get();
                break;
            }
        } while (requestAgainCondition != null && requestAgainCondition.apply(object));
        return object;
    }
}

