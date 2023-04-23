package com.carson.beans.factory.annotation;

import cn.hutool.core.bean.BeanUtil;
import com.carson.beans.PropertyValues;
import com.carson.beans.exception.BeansException;
import com.carson.beans.factory.BeanFactory;
import com.carson.beans.factory.BeanFactoryAware;
import com.carson.beans.factory.ConfigurableListableBeanFactory;
import com.carson.beans.factory.config.InstantiationAwareBeanPostProcessor;

import java.lang.reflect.Field;

/**
 * @author carson_luo
 */
public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private ConfigurableListableBeanFactory beanFactory;


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override
    public boolean postProcessorAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        // 处理 @Value 注解
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Value valueAnnotation = field.getAnnotation(Value.class);
            if (valueAnnotation == null) {
                continue;
            }
            String value = valueAnnotation.value();
            value = beanFactory.resolveEmbeddedValue(value);
            BeanUtil.setFieldValue(bean, field.getName(), value);
        }

        // 处理 @Autowired 注解
        for (Field field : fields) {
            Autowired autowiredAnnotation = field.getAnnotation(Autowired.class);
            if (autowiredAnnotation == null) {
                continue;
            }
            Class<?> fieldType = field.getType();
            Qualifier qualifierAnnotation = field.getAnnotation(Qualifier.class);
            Object dependentBean = null;
            if (qualifierAnnotation == null) {
                dependentBean = beanFactory.getBean(fieldType);
            } else {
                dependentBean = beanFactory.getBean(qualifierAnnotation.value(), fieldType);
            }
            BeanUtil.setFieldValue(bean, field.getName(), dependentBean);
        }
        return pvs;
    }
}
