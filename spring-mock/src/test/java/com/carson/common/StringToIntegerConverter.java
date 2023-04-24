package com.carson.common;

import com.carson.core.convert.converter.Converter;

/**
 * @author carson_luo
 */
public class StringToIntegerConverter implements Converter<String, Integer> {

    @Override
    public Integer convert(String source) {
        return Integer.valueOf(source);
    }
}
