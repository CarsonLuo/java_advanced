package com.carson.beans.factory.config;

import com.carson.beans.PropertyValues;

/**
 * 实例保存bean的信息, 包括class类型, 方法构造参数, 是否为单例等
 * <p>
 * spring中, 定义bean的初始化&销毁的方式有三种:
 * 1. xml 定义 init-method, destroy-method
 * 2. 继承自{@link com.carson.beans.factory.InitializingBean} 和 {@link com.carson.beans.factory.DisposableBean}
 * 3. 对应方法上添加 @PostConstruct @PreDestroy
 *
 * @author carson_luo
 */
public class BeanDefinition {

    private Class<?> beanClass;

    private PropertyValues propertyValues;

    private String initMethodName;

    private String destroyMethodName;

    public BeanDefinition(Class<?> beanClass) {
        this(beanClass, null);
    }

    public BeanDefinition(Class<?> beanClass, PropertyValues propertyValues) {
        this.beanClass = beanClass;
        this.propertyValues = propertyValues != null ? propertyValues : new PropertyValues();
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

    public String getInitMethodName() {
        return initMethodName;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }
}
