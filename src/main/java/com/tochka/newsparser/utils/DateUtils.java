package com.tochka.newsparser.utils;

import com.tochka.newsparser.controller.rest.RestExceptionHandler;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtils {

    private static final String DATE_PATTERN = "dd.MM.yyyy";
    private static final Map<String, String> DATE_FORMAT_REGEXPS = new HashMap<String, String>() {{
        put("^\\d{8}$", "yyyyMMdd");
        put("^\\d{1,2}-\\d{1,2}-\\d{4}$", "dd-MM-yyyy");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}$", "yyyy-MM-dd");
        put("^\\d{1,2}/\\d{1,2}/\\d{4}$", "MM/dd/yyyy");
        put("^\\d{4}/\\d{1,2}/\\d{1,2}$", "yyyy/MM/dd");
        put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$", "dd MMM yyyy");
        put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$", "dd MMMM yyyy");
        put("^\\d{12}$", "yyyyMMddHHmm");
        put("^\\d{8}\\s\\d{4}$", "yyyyMMdd HHmm");
        put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}$", "dd-MM-yyyy HH:mm");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy-MM-dd HH:mm");
        put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}$", "MM/dd/yyyy HH:mm");
        put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy/MM/dd HH:mm");
        put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMM yyyy HH:mm");
        put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMMM yyyy HH:mm");
        put("^\\d{14}$", "yyyyMMddHHmmss");
        put("^\\d{8}\\s\\d{6}$", "yyyyMMdd HHmmss");
        put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd-MM-yyyy HH:mm:ss");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy-MM-dd HH:mm:ss");
        put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "MM/dd/yyyy HH:mm:ss");
        put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy/MM/dd HH:mm:ss");
        put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMM yyyy HH:mm:ss");
        put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMMM yyyy HH:mm:ss");
        put("^\\d{1,2}:\\d{2}\\,\\s\\d{1,2}\\s[a-zа-я]{3,}\\s\\d{4}$", "HH:mm, dd MMM yyyy");
        put("^\\d{1,2}\\s[a-zа-я]{3,}\\s\\d{4}\\s[a-zа-я ]{4,}\\d{1,2}:\\d{2}$", "dd MMM yyyy года в HH:mm");
    }};

    @SneakyThrows
    public static Timestamp getTimestampFromString(String date) {
        date = prepareDate(date);
        String format = determineDateFormat(date);
        if (format == null) return null;

        DateFormat formatter = new SimpleDateFormat(format);
        Date parseDate = formatter.parse(date);

        return new Timestamp(parseDate.getTime());
    }

    private static String prepareDate(String date) {
        date = date.replaceFirst("вчера", DateFormatUtils.format(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000),
                "dd MMM yyyy") + " года")
                .replaceFirst("сегодня", DateFormatUtils.format(new Date(System.currentTimeMillis()),
                        "dd MMM yyyy") + " года");
        return date;
    }

    public static String determineDateFormat(String dateString) {
        for (String regexp : DATE_FORMAT_REGEXPS.keySet()) {
            if (dateString.toLowerCase().matches(regexp)) {
                return DATE_FORMAT_REGEXPS.get(regexp);
            }
        }
        return null; // Unknown format.
    }

    // TODO: news dashboard datetime filter
    public static Timestamp prepareFromTimestamp(String fromDate) {
        Timestamp from;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

        try {
            fromDate = fromDate != null ? fromDate : "01.01.0001";
            from = Timestamp.valueOf(LocalDate.parse(fromDate, formatter).atTime(0, 0));
        } catch (DateTimeParseException e) {
            throw new RestExceptionHandler.ErrorWithMessage("Wrong date format! (" + DATE_PATTERN + "). Example: 21.01.2000");
        }

        return from;
    }

    public static Timestamp prepareToTimestamp(String toDate) {
        Timestamp to;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

        try {
            toDate = toDate != null ? toDate : new SimpleDateFormat(DATE_PATTERN).format(new Date());
            to = Timestamp.valueOf(LocalDate.parse(toDate, formatter).atTime(23, 59));
        } catch (DateTimeParseException e) {
            throw new RestExceptionHandler.ErrorWithMessage("Wrong date format! (" + DATE_PATTERN + "). Example: 21.01.2000");
        }

        return to;
    }
}
