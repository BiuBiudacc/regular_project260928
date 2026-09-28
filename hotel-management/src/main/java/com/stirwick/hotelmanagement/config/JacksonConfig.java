package com.stirwick.hotelmanagement.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    private static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_PATTERN);
            // 序列化：对象LocalDateTime输出JSON字符串 yyyy‑MM‑dd HH:mm:ss
            builder.serializers(new LocalDateTimeSerializer(formatter));
            // 反序列化：接收前端 "2026-09-15 08:00:00" 空格格式
            builder.deserializers(new LocalDateTimeDeserializer(formatter));
        };
    }
}
