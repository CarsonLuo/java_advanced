package com.carson.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import com.carson.beans.PropertyValue;
import com.carson.beans.exception.BeansException;
import com.carson.beans.factory.config.BeanDefinition;
import com.carson.beans.factory.config.BeanReference;
import com.carson.beans.factory.support.AbstractBeanDefinitionReader;
import com.carson.beans.factory.support.BeanDefinitionRegistry;
import com.carson.core.io.Resource;
import com.carson.core.io.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author carson_luo
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public static final String BEAN_ELEMENT = "bean";
    public static final String PROPERTY_ELEMENT = "property";

    public static final String ID_ATTRIBUTE = "id";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String CLASS_ATTRIBUTE = "class";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String REF_ATTRIBUTE = "ref";

    public XmlBeanDefinitionReader(BeanDefinitionRegistry beanRegistry) {
        super(beanRegistry);
    }

    public XmlBeanDefinitionReader(ResourceLoader resourceLoader, BeanDefinitionRegistry beanRegistry) {
        super(resourceLoader, beanRegistry);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) throws BeansException {
        try (InputStream inputStream = resource.getInputStream()) {
            doLoadBeanDefinitions(inputStream);
        } catch (IOException e) {
            throw new BeansException("IOException parsing XML document from " + resource, e);
        }
    }

    @Override
    public void loadBeanDefinitions(String location) throws BeansException {
        Resource resource = getResourceLoader().getResource(location);
        loadBeanDefinitions(resource);
    }

    protected void doLoadBeanDefinitions(InputStream inputStream) {
        Document document = XmlUtil.readXML(inputStream);
        Element root = document.getDocumentElement();
        NodeList childNodes = root.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node beanItem = childNodes.item(i);
            if (!(beanItem instanceof Element beanElem)) {
                continue;
            }
            if (!(BEAN_ELEMENT.equals(beanElem.getNodeName()))) {
                continue;
            }
            String id = beanElem.getAttribute(ID_ATTRIBUTE);
            String name = beanElem.getAttribute(NAME_ATTRIBUTE);
            String className = beanElem.getAttribute(CLASS_ATTRIBUTE);

            Class<?> clazz;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new BeansException("Cannot find class [" + className + "]");
            }

            // <bean id="person" name="person" class="com.carson.beans.factory.bean.Person"/>
            // 'id' 优先于 'name'
            String beanName = StrUtil.isEmpty(id) ? name : id;
            if (StrUtil.isEmpty(beanName)) {
                // id 和 name 都为空, 则将类名的第一个字母转为小写作为Bean 的名称
                beanName = StrUtil.lowerFirst(clazz.getSimpleName());
            }
            // 判断 beanName 是否重复
            if (getRegistry().containsBeanDefinition(beanName)) {
                throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
            }
            // 分析 & 构建 BeanDefinition
            BeanDefinition beanDefinition = new BeanDefinition(clazz);
            for (int j = 0; j < beanElem.getChildNodes().getLength(); j++) {
                Node propertyNode = beanElem.getChildNodes().item(j);
                if (!(propertyNode instanceof Element propertyElem)) {
                    continue;
                }
                if (!PROPERTY_ELEMENT.equals(propertyElem.getNodeName())) {
                    continue;
                }
                // 解析 property 标签
                String nameAttribute = propertyElem.getAttribute(NAME_ATTRIBUTE);
                String valueAttribute = propertyElem.getAttribute(VALUE_ATTRIBUTE);
                String refAttribute = propertyElem.getAttribute(REF_ATTRIBUTE);
                if (StrUtil.isEmpty(nameAttribute)) {
                    throw new BeansException("The name attribute cannot be null or empty");
                }
                Object value = valueAttribute;
                if (StrUtil.isNotEmpty(refAttribute)) {
                    value = new BeanReference(refAttribute);
                }
                PropertyValue propertyValue = new PropertyValue(nameAttribute, value);
                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
            }
            // 注册 BeanDefinition
            getRegistry().registerBeanDefinition(beanName, beanDefinition);
        }
    }
}
