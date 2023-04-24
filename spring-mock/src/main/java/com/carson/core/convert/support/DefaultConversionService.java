package com.carson.core.convert.support;

import com.carson.core.convert.converter.ConverterRegistry;

/**
 * @author carson_luo
 */
public class DefaultConversionService extends GenericConversionService {

    public DefaultConversionService() {
        addDefaultConverters(this);
    }

    public static void addDefaultConverters(ConverterRegistry converterRegistry) {
        converterRegistry.addConvertFactory(new StringToNumberConvertFactory());
        // TODO 添加其他ConverterFactory
    }
}
