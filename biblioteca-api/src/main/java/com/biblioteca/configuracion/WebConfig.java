package com.biblioteca.configuracion;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverters;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import java.nio.charset.StandardCharsets;


@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void configureMessageConverters(HttpMessageConverters.ServerBuilder builder) {
        JacksonJsonHttpMessageConverter converter = new JacksonJsonHttpMessageConverter();
        converter.setDefaultCharset(StandardCharsets.UTF_8);
        builder.registerDefaults()
                .withJsonConverter(converter);
    }
}
