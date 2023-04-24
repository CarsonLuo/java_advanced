package com.carson.core.convert.support;

import com.carson.core.convert.converter.Converter;
import com.carson.core.convert.converter.ConverterFactory;

/**
 * @author carson_luo
 */
public class StringToNumberConvertFactory implements ConverterFactory<String, Number> {

    @Override
    public <T extends Number> Converter<String, T> getConvert(Class<T> targetType) {
        return new StringToNumber<>(targetType);
    }

    private static final class StringToNumber<T extends Number> implements Converter<String, T> {

        private final Class<T> targetType;

        public StringToNumber(Class<T> targetType) {
            this.targetType = targetType;
        }

        @Override
        public T convert(String source) {
            if (source.length() == 0) {
                return null;
            }
            if (targetType.equals(Integer.class)) {
                return targetType.cast(Integer.valueOf(source));
            }
            if (targetType.equals(Long.class)) {
                return targetType.cast(Long.valueOf(source));
            }
            // TODO 其他类型
            throw new IllegalArgumentException("Cannot convert String [" + source + "] to target class [" + targetType.getName() + "]");
        }
    }
}
