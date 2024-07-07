package net.trickycreations.storyteamfight.utilities.strings;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0");

    public static String getCurrentDateFormatted() {
        return LocalDateTime.now(ZoneId.of("Europe/Rome"))
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    public static String timer(long end) {
        return DECIMAL_FORMAT.format(Math.max(0.0D, (end - System.currentTimeMillis()) / 1000.0D));
    }

    public static String parseStringTime(long seconds) {
        long[] values = {seconds / 86400, seconds % 86400 / 3600, seconds % 3600 / 60, seconds % 60};
        String[] labels = {"d ", "h ", "m ", "s"};
        StringBuilder formattedTime = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            if (values[i] > 0 || formattedTime.length() > 0) {
                formattedTime.append(values[i]).append(labels[i]);
            }
        }
        return formattedTime.toString();
    }

    public static long parseTimeString(String timeString) {
        long seconds = 0;
        for (String part : timeString.split("\\s+")) {
            seconds += (long) Integer.parseInt(part.substring(0, part.length() - 1)) *
                    (part.endsWith("d") ? 86400 : part.endsWith("h") ? 3600 : part.endsWith("m") ? 60 : 1);
        }
        return seconds;
    }
}