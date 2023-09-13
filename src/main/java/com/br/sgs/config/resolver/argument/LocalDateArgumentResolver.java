package com.br.sgs.config.resolver.argument;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;

import org.springframework.format.Formatter;

public class LocalDateArgumentResolver implements Formatter<LocalDate> {

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        return LocalDate.parse(text); // Isso assume que o formato é "yyyy-MM-dd".
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        return object.toString();
    }
}
