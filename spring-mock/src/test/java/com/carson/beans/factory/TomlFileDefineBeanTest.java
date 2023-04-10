package com.carson.beans.factory;

import com.carson.beans.factory.bean.Person;
import com.carson.beans.factory.support.DefaultListableBeanFactory;
import com.carson.beans.factory.toml.TomlBeanDefinitionReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author carson_luo
 */
public class TomlFileDefineBeanTest {

    @Test
    public void TestTomlFileDefineBean(){
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        TomlBeanDefinitionReader tomlBeanDefinitionReader = new TomlBeanDefinitionReader(beanFactory);
        tomlBeanDefinitionReader.loadBeanDefinitions("classpath:spring.toml");

        Person person = (Person) beanFactory.getBean("person");
        Assertions.assertEquals(person.getName(), "carson");
        Assertions.assertEquals(person.getCar().getBrand(), "benz");
    }
}
