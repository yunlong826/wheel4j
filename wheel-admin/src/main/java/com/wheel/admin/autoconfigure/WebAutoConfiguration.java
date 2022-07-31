package com.wheel.admin.autoconfigure;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.wheel.admin.filter.TraceIdGenerateFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Description:
 *
 * @author longyun
 * @version 1.0
 * @date 2022/7/31 14:37
 */
@Configuration
public class WebAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(WebAutoConfiguration.class);

    public WebAutoConfiguration(){
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters initHttpMessageConverters(){
        logger.info("Autowiring by type from bean name {}", HttpMessageConverters.class.getName());
        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
        timeModule.addSerializer(Long.class, ToStringSerializer.instance);
        timeModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        timeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        timeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().serializationInclusion(JsonInclude.Include.NON_NULL).featuresToEnable(new Object[]{SerializationFeature.WRITE_ENUMS_USING_TO_STRING}).featuresToDisable(new Object[]{SerializationFeature.WRITE_DATES_AS_TIMESTAMPS}).modules(new Module[]{new Jdk8Module()}).modules(new Module[]{timeModule}).build();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        List<MediaType> fastMedisTypes = new ArrayList();
        fastMedisTypes.add(MediaType.APPLICATION_JSON_UTF8);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(fastMedisTypes);
        converter.setObjectMapper(objectMapper);
        return new HttpMessageConverters(new HttpMessageConverter[]{converter});
    }

    @Bean
    public TraceIdGenerateFilter traceIdGenerateFilter() {
        return new TraceIdGenerateFilter();
    }

}
