package com.carson.beans.factory;

import com.carson.beans.PropertyValue;
import com.carson.beans.PropertyValues;
import com.carson.beans.exception.BeansException;
import com.carson.beans.factory.config.BeanDefinition;
import com.carson.beans.factory.config.BeanFactoryPostProcessor;
import com.carson.core.io.DefaultResourceLoader;
import com.carson.core.io.Resource;

import java.io.IOException;
import java.util.Properties;

/**
 * 将配置信息在 properties文件中的数据, 解析到 XML文件中的 '占位符'
 * <p>
 * 实现思路: 在bean实例化之前, 编辑 BeanDefinition
 * BeanFactoryPostProcessor 具有编辑 BeanDefinition 的能力
 *
 * @author carson_luo
 */
public class PropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {

    public static final String PLACEHOLDER_PREFIX = "${";

    public static final String PLACEHOLDER_SUFFIX = "}";

    private String location;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        /*
         * 加载属性配置文件
         */
        Properties properties = loadProperties();

        /*
         * 属性值替换占位符
         */
        processProperties(beanFactory, properties);
    }

    /**
     * 加载属性配置文件
     */
    private Properties loadProperties() {
        try {
            DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource(location);
            Properties properties = new Properties();
            properties.load(resource.getInputStream());
            return properties;
        } catch (IOException e) {
            throw new BeansException("Could not load properties", e);
        }
    }

    /**
     * 属性值替换占位符
     */
    private void processProperties(ConfigurableListableBeanFactory beanFactory, Properties properties) throws BeansException {
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            var beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
            resolvePropertyValues(beanDefinition, properties);
        }
    }

    /**
     * 1. 遍历: BeanDefinition.PropertyValue, 分析出PropertyValue.value值包含占位符的
     * 2. PropertyValue.value 解析出 properties.key, e.g ${name} -> name
     * 3. 将 PropertyValue.value 替换, 重新加入到 PropertyValues
     */
    private void resolvePropertyValues(BeanDefinition beanDefinition, Properties properties) {
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
            Object value = propertyValue.getValue();
            if (value instanceof String valurStr) {
                // TODO 仅简单支持一个占位符的格式
                StringBuffer buf = new StringBuffer(valurStr);
                int startIdx = valurStr.indexOf(PLACEHOLDER_PREFIX);
                int endIdx = valurStr.indexOf(PLACEHOLDER_SUFFIX);
                if (startIdx != -1 && endIdx != -1 && startIdx < endIdx) {
                    String propKey = valurStr.substring(startIdx + 2, endIdx);
                    String propValue = properties.getProperty(propKey);
                    buf.replace(startIdx, endIdx + 1, propValue);
                    propertyValues.addPropertyValue(new PropertyValue(propertyValue.getName(), buf.toString()));
                }
            }
        }
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
