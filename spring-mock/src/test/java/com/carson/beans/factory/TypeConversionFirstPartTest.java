package com.carson.beans.factory;

import com.carson.common.StringToBooleanConverter;
import com.carson.common.StringToIntegerConverter;
import com.carson.core.convert.converter.Converter;
import com.carson.core.convert.converter.GenericConverter;
import com.carson.core.convert.support.GenericConversionService;
import com.carson.core.convert.support.StringToNumberConvertFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author carson_luo
 */
public class TypeConversionFirstPartTest {

    @Test
    public void testStringToIntegerConverter() {
        var stringToIntegerConverter = new StringToIntegerConverter();
        Integer i = stringToIntegerConverter.convert("999");
        Assertions.assertEquals(i, 999);
    }

    @Test
    public void testStringToNumberConverterFactory() {
        var stringToNumberConvertFactory = new StringToNumberConvertFactory();

        Converter<String, Integer> stringToIntegerConverter = stringToNumberConvertFactory.getConvert(Integer.class);
        Assertions.assertEquals(999, stringToIntegerConverter.convert("999"));

        Converter<String, Long> stringToLongConverter = stringToNumberConvertFactory.getConvert(Long.class);
        Assertions.assertEquals(999L, stringToLongConverter.convert("999"));
    }

    @Test
    public void testGenericConverter() {
        GenericConverter stringToBooleanConverter = new StringToBooleanConverter();
        Boolean flag = (Boolean) stringToBooleanConverter.convert("true", String.class, Boolean.class);
        Assertions.assertEquals(Boolean.TRUE, flag);
    }

    @Test
    public void testGenericConversionService() {
        GenericConversionService conversionService = new GenericConversionService();

        conversionService.addConverter(new StringToIntegerConverter());
        Integer i = conversionService.convert("999", Integer.class);
        Assertions.assertTrue(conversionService.canConvert(String.class, Integer.class));
        Assertions.assertEquals(i, 999);

        conversionService.addConvertFactory(new StringToNumberConvertFactory());
        Long l = conversionService.convert("999", Long.class);
        Assertions.assertTrue(conversionService.canConvert(String.class, Long.class));
        Assertions.assertEquals(l, 999L);

        conversionService.addConverter(new StringToBooleanConverter());
        Boolean flag = conversionService.convert("false", Boolean.class);
        Assertions.assertTrue(conversionService.canConvert(String.class, Boolean.class));
        Assertions.assertEquals(flag, Boolean.FALSE);
    }
}
