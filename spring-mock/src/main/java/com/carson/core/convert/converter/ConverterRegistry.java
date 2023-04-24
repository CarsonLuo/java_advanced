package com.carson.core.convert.converter;

/**
 * 类型转换器注册接口
 *
 * @author carson_luo
 */
public interface ConverterRegistry {

    void addConverter(Converter<?, ?> converter);

    void addConvertFactory(ConverterFactory<?, ?> converterFactory);

    void addConverter(GenericConverter converter);
}
