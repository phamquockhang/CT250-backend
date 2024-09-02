package com.dvk.ct250backend.infrastructure.config;

import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class DateTimeConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        new DateTimeFormatterRegistrar() {{
            setUseIsoFormat(true);
            registerFormatters(registry);
        }};
    }
}