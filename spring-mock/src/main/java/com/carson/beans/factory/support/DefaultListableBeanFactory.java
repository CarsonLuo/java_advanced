package com.carson.beans.factory.support;

import com.carson.beans.factory.ConfigurableListableBeanFactory;
import com.carson.beans.factory.config.BeanDefinition;
import com.carson.beans.exception.BeansException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Bean 容器作为 BeanDefinitionRegistry SingletonBeanRegistry 的实现类
 * 具备两者的能力, 想bean容器中注册BeanDefinition后, 使用Bean时才会实例化
 *
 * @author carson_luo
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory
        implements BeanDefinitionRegistry, ConfigurableListableBeanFactory {

    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws BeansException {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new BeansException("No bean named ' " + beanName + " ' is defined");
        }
        return beanDefinition;
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        Set<String> beanNameSet = beanDefinitionMap.keySet();
        return beanNameSet.toArray(new String[beanNameSet.size()]);
    }

    @Override
    public <T> Map<String, T> getBeanOfType(Class<T> type) throws BeansException {
        Map<String, T> ret = new HashMap<>();
        beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            if (type.isAssignableFrom(beanDefinition.getBeanClass())) {
                T bean = (T) getBean(beanName);
                ret.put(beanName, bean);
            }
        });
        return ret;
    }

    @Override
    public void preInstantiateSingletons() throws BeansException {
        beanDefinitionMap.keySet().forEach(beanName -> getBean(beanName));
    }
}
