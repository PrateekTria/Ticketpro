package com.ticketpro.util;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LongDeserializer implements JsonDeserializer<Long> {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    @Override
    public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String dateStr = json.getAsString();
        try {
            // Parse the date string to a Date object
            Date date = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).parse(dateStr);
            // Return the time in milliseconds
            return date != null ? date.getTime() : null;
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }
}
