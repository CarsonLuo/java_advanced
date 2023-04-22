package com.carson.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import com.carson.beans.PropertyValue;
import com.carson.beans.exception.BeansException;
import com.carson.beans.factory.config.BeanDefinition;
import com.carson.beans.factory.config.BeanReference;
import com.carson.beans.factory.support.AbstractBeanDefinitionReader;
import com.carson.beans.factory.support.BeanDefinitionRegistry;
import com.carson.context.annotation.ClassPathBeanDefinitionScanner;
import com.carson.core.io.Resource;
import com.carson.core.io.ResourceLoader;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
    public static final String SCOPE_ATTRIBUTE = "scope";

    public static final String INIT_METHOD_ATTRIBUTE = "init-method";
    public static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";

    public static final String BASE_PACKAGE_ATTRIBUTE = "base-package";
    public static final String COMPONENT_SCAN_ELEMENT = "component-scan";

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
        } catch (IOException | DocumentException e) {
            throw new BeansException("IOException parsing XML document from " + resource, e);
        }
    }

    @Override
    public void loadBeanDefinitions(String location) throws BeansException {
        Resource resource = getResourceLoader().getResource(location);
        loadBeanDefinitions(resource);
    }

    protected void doLoadBeanDefinitions(InputStream inputStream) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        Element root = document.getRootElement();

        // 解析context:component-scan标签并扫描指定包中的类, 提取类信息, 组装成BeanDefinition
        Element componentScan = root.element(COMPONENT_SCAN_ELEMENT);
        if (componentScan != null) {
            String scanPath = componentScan.attributeValue(BASE_PACKAGE_ATTRIBUTE);
            if (StrUtil.isEmpty(scanPath)) {
                throw new BeansException("The value of base-package attribute can not be empty or null");
            }
            scanPackage(scanPath);
        }

        List<Element> beanList = root.elements(BEAN_ELEMENT);
        for (Element beanElem : beanList) {
            String id = beanElem.attributeValue(ID_ATTRIBUTE);
            String name = beanElem.attributeValue(NAME_ATTRIBUTE);
            String className = beanElem.attributeValue(CLASS_ATTRIBUTE);
            String initMethod = beanElem.attributeValue(INIT_METHOD_ATTRIBUTE);
            String destroyMethod = beanElem.attributeValue(DESTROY_METHOD_ATTRIBUTE);
            String scope = beanElem.attributeValue(SCOPE_ATTRIBUTE);

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
            beanDefinition.setInitMethodName(initMethod);
            beanDefinition.setDestroyMethodName(destroyMethod);
            if (StrUtil.isNotEmpty(scope)) {
                beanDefinition.setScope(scope);
            }

            List<Element> propertyElems = beanElem.elements(PROPERTY_ELEMENT);
            for (Element propertyElem : propertyElems) {
                // 解析 property 标签
                String nameAttribute = propertyElem.attributeValue(NAME_ATTRIBUTE);
                String valueAttribute = propertyElem.attributeValue(VALUE_ATTRIBUTE);
                String refAttribute = propertyElem.attributeValue(REF_ATTRIBUTE);
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

    /**
     * 扫描@Component的类, 提取信息, 组装成BeanDefinition
     */
    private void scanPackage(String scanPath) {
        String[] basePackages = StrUtil.splitToArray(scanPath, ",");
        var scanner = new ClassPathBeanDefinitionScanner(getRegistry());
        scanner.doScan(basePackages);
    }
}
