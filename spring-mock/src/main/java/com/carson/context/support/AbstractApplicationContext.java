package com.carson.context.support;

import com.carson.beans.exception.BeansException;
import com.carson.beans.factory.ConfigurableListableBeanFactory;
import com.carson.beans.factory.config.BeanFactoryPostProcessor;
import com.carson.beans.factory.config.BeanPostProcessor;
import com.carson.context.ApplicationEvent;
import com.carson.context.ApplicationListener;
import com.carson.context.ConfigurableApplicationContext;
import com.carson.context.event.ApplicationEventMulticaster;
import com.carson.context.event.ContextClosedEvent;
import com.carson.context.event.ContextRefreshedEvent;
import com.carson.context.event.SimpleApplicationEventMulticaster;
import com.carson.core.io.DefaultResourceLoader;

import java.util.Collection;
import java.util.Map;

/**
 * @author carson_luo
 * @see #invokeBeanFactoryPostProcessors,#registerBeanPostProcessors
 * 自动识别了 BeanFactoryPostProcessor, BeanPostProcessor
 */
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

    public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

    private ApplicationEventMulticaster applicationEventMulticaster;

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

        // 添加 ApplicationContextAwareProcessor, 让继承自ApplicationContextAware的bean能感知bean
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

        // 在 Bean 实例化之前, 执行 BeanFactoryPostProcessor
        invokeBeanFactoryPostProcessors(beanFactory);

        // BeanPostProcessor 需要提前与其他bean实例化之前注册
        registerBeanPostProcessors(beanFactory);

        // 初始化事件发布者
        initApplicationEventMulticaster();

        // 注册事件监听器
        registerListeners();

        // 提前实例化所有单例Bean
        beanFactory.preInstantiateSingletons();

        // 发布容器刷新完成事件
        finishRefresh();
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
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(requiredType);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        applicationEventMulticaster.multicastEvent(event);
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

    /**
     * 初始化事件发布者
     */
    protected void initApplicationEventMulticaster() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        beanFactory.addSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, applicationEventMulticaster);
    }

    /**
     * 注册事件监听器
     */
    protected void registerListeners() {
        Collection<ApplicationListener> applicationListeners = getBeansOfType(ApplicationListener.class).values();
        for (ApplicationListener applicationListener : applicationListeners) {
            applicationEventMulticaster.addApplicationListener(applicationListener);
        }
    }

    /**
     * 发布 '容器刷新完成事件'
     */
    protected void finishRefresh() {
        publishEvent(new ContextRefreshedEvent(this));
    }

    /**
     * 为了确保销毁方法在虚拟机关闭之前执行, 向虚拟机中注册一个钩子方法
     * <p>
     * 非web应用需要手动调用该方法, 或者调用 #close() 关闭容器
     */
    public void registerShutdownHook() {
        Thread shutdownHook = new Thread(this::doClose);
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    public void close() {
        doClose();
    }

    protected void doClose() {

        // 发布容器关闭事件
        publishEvent(new ContextClosedEvent(this));

        // 执行单例bean的销毁方法
        destroyBean();
    }

    protected void destroyBean() {
        getBeanFactory().destroySingletons();
    }
}
