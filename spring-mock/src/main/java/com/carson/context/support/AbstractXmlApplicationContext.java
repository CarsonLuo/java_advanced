package com.carson.context.support;

import com.carson.beans.factory.support.DefaultListableBeanFactory;
import com.carson.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * @author carson_luo
 */
public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext {

    protected abstract String[] getConfigLocations();

    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(this, beanFactory);
        String[] configLocations = getConfigLocations();
        if (configLocations != null) {
            xmlBeanDefinitionReader.loadBeanDefinitions(configLocations);
        }
    }
}
