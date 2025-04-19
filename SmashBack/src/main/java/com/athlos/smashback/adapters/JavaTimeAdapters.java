package com.athlos.smashback.adapters;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class JavaTimeAdapters {

    public static GsonBuilder registerAll(GsonBuilder builder) {
        return builder
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(LocalDate.class,     new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class,     new LocalTimeAdapter());
    }

    private static class LocalDateTimeAdapter
            implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

        private static final DateTimeFormatter F = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        @Override
        public JsonElement serialize(LocalDateTime src, Type type, JsonSerializationContext ctx) {
            return new JsonPrimitive(src.format(F));
        }

        @Override
        public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext ctx)
                throws JsonParseException {
            return LocalDateTime.parse(json.getAsString(), F);
        }
    }

    private static class LocalDateAdapter
            implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

        private static final DateTimeFormatter F = DateTimeFormatter.ISO_LOCAL_DATE;

        @Override
        public JsonElement serialize(LocalDate src, Type type, JsonSerializationContext ctx) {
            return new JsonPrimitive(src.format(F));
        }

        @Override
        public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext ctx)
                throws JsonParseException {
            return LocalDate.parse(json.getAsString(), F);
        }
    }

    private static class LocalTimeAdapter
            implements JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {

        private static final DateTimeFormatter F = DateTimeFormatter.ISO_LOCAL_TIME;

        @Override
        public JsonElement serialize(LocalTime src, Type type, JsonSerializationContext ctx) {
            return new JsonPrimitive(src.format(F));
        }

        @Override
        public LocalTime deserialize(JsonElement json, Type type, JsonDeserializationContext ctx)
                throws JsonParseException {
            return LocalTime.parse(json.getAsString(), F);
        }
    }
}
