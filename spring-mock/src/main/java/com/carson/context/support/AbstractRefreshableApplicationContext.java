package com.carson.context.support;

import com.carson.beans.exception.BeansException;
import com.carson.beans.factory.support.DefaultListableBeanFactory;

/**
 * @author carson_luo
 */
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {

    private DefaultListableBeanFactory beanFactory;

    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory factory) throws BeansException;

    /**
     * 创建 Bean工厂
     */
    protected DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory();
    }

    /**
     * 创建bean工厂 并加载 BeanDefinition
     */
    protected final void refreshBeanFactory() throws BeansException {
        DefaultListableBeanFactory createdBeanFactory = createBeanFactory();
        loadBeanDefinitions(createdBeanFactory);
        this.beanFactory = createdBeanFactory;
    }

    @Override
    public DefaultListableBeanFactory getBeanFactory() {
        return beanFactory;
    }
}
