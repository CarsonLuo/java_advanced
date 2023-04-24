package com.carson.core.convert.converter;

/**
 * 类型转换抽象接口
 *
 * @author carson_luo
 */
public interface Converter<S, T> {

    /**
     * 类型转换
     */
    T convert(S source);
}
