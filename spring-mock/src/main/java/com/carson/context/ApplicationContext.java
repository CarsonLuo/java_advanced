package com.carson.context;

import com.carson.beans.factory.HierarchicalBeanFactory;
import com.carson.beans.factory.ListableBeanFactory;
import com.carson.core.io.ResourceLoader;

/**
 * ApplicationContext 拥有 BeanFactory 的所有功能
 * 除此之外, 还支持特殊类型bean : BeanFactoryPostProcessor, BeanPostProcessor的自动识别
 * 资源加载, 容器事件, 监听器, 国际化支持, 单例bean自动初始化
 *
 * BeanFactory 是 spring 的基础设施, 面向 Spring 本身
 * ApplicationContext 面向spring 的使用者, 应用场合使用 ApplicationContext
 *
 * @author carson_luo
 */
public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader, ApplicationEventPublisher {
}
