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

    public static final String SCOPE_SINGLETON = "singleton";

    public static final String SCOPE_PROTOTYPE = "prototype";

    private Class<?> beanClass;

    private PropertyValues propertyValues;

    private String initMethodName;

    private String destroyMethodName;

    private String scope = SCOPE_SINGLETON;

    private boolean singleton = true;

    private boolean prototype = false;

    public BeanDefinition(Class<?> beanClass) {
        this(beanClass, null);
    }

    public BeanDefinition(Class<?> beanClass, PropertyValues propertyValues) {
        this.beanClass = beanClass;
        this.propertyValues = propertyValues != null ? propertyValues : new PropertyValues();
    }

    public void setScope(String scope) {
        this.scope = scope;
        this.singleton = SCOPE_SINGLETON.equals(scope);
        this.prototype = SCOPE_PROTOTYPE.equals(scope);
    }

    public boolean isSingleton() {
        return this.singleton;
    }

    public boolean isPrototype() {
        return this.prototype;
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
