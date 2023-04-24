package com.carson.core.convert.converter;

/**
 * 类型转换工厂
 *
 * @author carson_luo
 */
public interface ConverterFactory<S, R> {

    <T extends R> Converter<S, T> getConvert(Class<T> targetType);
}
