package com.carson.common;

import com.carson.core.convert.converter.GenericConverter;

import java.util.Collections;
import java.util.Set;

/**
 * @author carson_luo
 */
public class StringToBooleanConverter implements GenericConverter {

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, Boolean.class));
    }

    @Override
    public Object convert(Object source, Class<?> sourceType, Class<?> targetType) {
        return Boolean.valueOf((String) source);
    }
}
