package com.carson.beans.factory.support;

import com.carson.beans.exception.BeansException;
import com.carson.core.io.DefaultResourceLoader;
import com.carson.core.io.ResourceLoader;

/**
 * @author carson_luo
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

    private ResourceLoader resourceLoader;

    private final BeanDefinitionRegistry beanRegistry;

    protected AbstractBeanDefinitionReader(BeanDefinitionRegistry beanRegistry) {
        this(new DefaultResourceLoader(), beanRegistry);
    }

    public AbstractBeanDefinitionReader(ResourceLoader resourceLoader, BeanDefinitionRegistry beanRegistry) {
        this.resourceLoader = resourceLoader;
        this.beanRegistry = beanRegistry;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    @Override
    public BeanDefinitionRegistry getRegistry() {
        return beanRegistry;
    }

    @Override
    public void loadBeanDefinitions(String[] locations) throws BeansException {
        for (String location : locations) {
            loadBeanDefinitions(location);
        }
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
