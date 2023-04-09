package com.carson.context.support;

import com.carson.beans.exception.BeansException;
import com.carson.beans.factory.ConfigurableListableBeanFactory;
import com.carson.beans.factory.config.BeanFactoryPostProcessor;
import com.carson.beans.factory.config.BeanPostProcessor;
import com.carson.context.ConfigurableApplicationContext;
import com.carson.core.io.DefaultResourceLoader;

import java.util.Map;

/**
 * @author carson_luo
 *
 * @see #invokeBeanFactoryPostProcessors,#registerBeanPostProcessors
 * 自动识别了 BeanFactoryPostProcessor, BeanPostProcessor
 */
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

    public abstract ConfigurableListableBeanFactory getBeanFactory();

    /**
     * 抽象方法: 创建BeanFactory, 并加载 BeanDefinition
     */
    protected abstract void refreshBeanFactory() throws BeansException;

    @Override
    public void refresh() throws BeansException {
        // 创建BeanFactory, 并加载 BeanDefinition
        refreshBeanFactory();

        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        // 在 Bean 实例化之前, 执行 BeanFactoryPostProcessor
        invokeBeanFactoryPostProcessors(beanFactory);

        // BeanPostProcessor 需要提前与其他bean实例化之前注册
        registerBeanPostProcessors(beanFactory);

        // 提前实例化所有单例Bean
        beanFactory.preInstantiateSingletons();
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(name, requiredType);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public Object getBean(String name) throws BeansException {
        return getBeanFactory().getBean(name);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    /**
     * 在 bean 实例化之前, 执行 BeanFactoryPostProcessor
     */
    protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        beanFactoryPostProcessorMap.forEach((_ignore, beanFactoryPostProcessor) -> beanFactoryPostProcessor.postProcessBeanFactory(beanFactory));
    }

    /**
     * 注册 BeanPostProcessor
     */
    protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
        beanPostProcessorMap.forEach((_ignore, beanPostProcessor) -> beanFactory.addBeanPostProcessor(beanPostProcessor));
    }
}
