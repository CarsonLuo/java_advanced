package com.carson.beans.factory.support;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.carson.beans.exception.BeansException;
import com.carson.beans.factory.DisposableBean;
import com.carson.beans.factory.config.BeanDefinition;

import java.lang.reflect.Method;

/**
 * @author carson_luo
 */
public class DisposableBeanAdapter implements DisposableBean {

    private final Object bean;

    private final String beanName;

    private final String destroyMethodName;

    public DisposableBeanAdapter(Object bean, String beanName, BeanDefinition beanDefinition) {
        this.bean = bean;
        this.beanName = beanName;
        this.destroyMethodName = beanDefinition.getDestroyMethodName();
    }

    @Override
    public void destroy() throws Exception {
        if (bean instanceof DisposableBean disposableBean) {
            disposableBean.destroy();
        }
        if (StrUtil.isEmpty(this.destroyMethodName)) {
            return;
        }
        // 避免同时继承自 DisposableBean, 且自定义方法与DisposableBean 方法同名, 销毁方法同时执行两次的情况
        if (bean instanceof DisposableBean && "destroy".equals(this.destroyMethodName)) {
            return;
        }
        Method destoryMethod = ClassUtil.getPublicMethod(bean.getClass(), destroyMethodName);
        if (destoryMethod == null) {
            throw new BeansException("Couldn't find a destroy method named '" + destroyMethodName + "' on bean with name '" + beanName + "'");
        }
        destoryMethod.invoke(bean);
    }
}
