package com.carson.beans.factory.toml;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.carson.beans.PropertyValue;
import com.carson.beans.exception.BeansException;
import com.carson.beans.factory.config.BeanDefinition;
import com.carson.beans.factory.config.BeanReference;
import com.carson.beans.factory.support.AbstractBeanDefinitionReader;
import com.carson.beans.factory.support.BeanDefinitionRegistry;
import com.carson.core.io.Resource;
import com.carson.core.io.ResourceLoader;
import com.moandjiezana.toml.Toml;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author carson_luo
 */
public class TomlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public static final String BEANS_KEY = "beans";
    public static final String PROPERTY_ELEMENT = "property";

    public static final String ID_ATTRIBUTE = "id";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String CLASS_ATTRIBUTE = "class";
    public static final String REF_ATTRIBUTE = "ref";

    public TomlBeanDefinitionReader(BeanDefinitionRegistry beanRegistry) {
        super(beanRegistry);
    }

    public TomlBeanDefinitionReader(ResourceLoader resourceLoader, BeanDefinitionRegistry beanRegistry) {
        super(resourceLoader, beanRegistry);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) throws BeansException {
        try (InputStream inputStream = resource.getInputStream()) {
            doLoadBeanDefinitions(inputStream);
        } catch (IOException e) {
            throw new BeansException("IOException parsing TOML document from " + resource, e);
        }
    }

    @Override
    public void loadBeanDefinitions(String location) throws BeansException {
        Resource resource = getResourceLoader().getResource(location);
        loadBeanDefinitions(resource);
    }

    private void doLoadBeanDefinitions(InputStream inputStream) {
        Toml toml = new Toml();
        Toml doc = toml.read(inputStream);
        Map<String, Object> docMap = doc.toMap();
        List<?> beans = (List<?>) docMap.get(BEANS_KEY);
        for (Object bean : beans) {
            @SuppressWarnings("unchecked")
            Map<String, Object> beanInfoMap = (Map<String, Object>) bean;
            String id = (String) beanInfoMap.getOrDefault(ID_ATTRIBUTE, StrUtil.EMPTY);
            String name = (String) beanInfoMap.getOrDefault(NAME_ATTRIBUTE, StrUtil.EMPTY);
            String className = (String) beanInfoMap.getOrDefault(CLASS_ATTRIBUTE, StrUtil.EMPTY);

            Class<?> clazz;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new BeansException("Cannot find class [" + className + "]");
            }

            // 'id' 优先于 'name'
            String beanName = StrUtil.isEmpty(id) ? name : id;
            if (StrUtil.isEmpty(beanName)) {
                // id 和 name 都为空, 则将类名的第一个字母转为小写作为Bean 的名称
                beanName = StrUtil.lowerFirst(clazz.getSimpleName());
            }
            // 判断 beanName 是否重复
            if (getRegistry().containsBeanDefinition(beanName)) {
                throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
            }

            // 分析 & 构建 BeanDefinition
            BeanDefinition beanDefinition = new BeanDefinition(clazz);
            @SuppressWarnings("unchecked")
            Map<String, Object> propertyMap = (Map<String, Object>) beanInfoMap.get(PROPERTY_ELEMENT);
            if (CollectionUtil.isEmpty(propertyMap)) {
                continue;
            }
            propertyMap.forEach((fieldName, fieldValue) -> {
                if (REF_ATTRIBUTE.equals(fieldName)) {
                    List<?> refList = (List<?>) fieldValue;
                    refList.forEach(ref -> {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> refMap = (Map<String, Object>) ref;
                        refMap.forEach((refFieldName, refFieldValue) -> {
                            Object refValue = new BeanReference((String) refFieldValue);
                            PropertyValue propertyValue = new PropertyValue(refFieldName, refValue);
                            beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
                        });
                    });
                    return;
                }
                PropertyValue propertyValue = new PropertyValue(fieldName, fieldValue);
                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
            });
            getRegistry().registerBeanDefinition(beanName, beanDefinition);
        }
    }
}
