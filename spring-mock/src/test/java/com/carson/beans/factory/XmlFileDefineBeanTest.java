package com.carson.beans.factory;

import com.carson.beans.factory.bean.Person;
import com.carson.beans.factory.support.DefaultListableBeanFactory;
import com.carson.beans.factory.xml.XmlBeanDefinitionReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author carson_luo
 */
public class XmlFileDefineBeanTest {

    @Test
    public void TestXmlBeanDefinitionReader(){
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        xmlBeanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");
        Person person = (Person) beanFactory.getBean("person");

        Assertions.assertEquals(person.getName(), "carson");
        Assertions.assertEquals(person.getCar().getBrand(), "benz");
    }
}
