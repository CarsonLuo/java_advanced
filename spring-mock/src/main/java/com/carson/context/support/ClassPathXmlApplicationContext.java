package com.carson.context.support;

/**
 * XML 文件 -> 加载BeanDefinition -> BeanFactoryPostProcessor 修改 BeanDefinition
 * -> bean实例化
 * -> BeanPostProcessor前置处理 ->  执行bean的初始化方法 -> BeanPostProcessor后置处理
 * -> 使用
 *
 * @author carson_luo
 */
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {

    private String[] configLocations;

    public ClassPathXmlApplicationContext(String[] configLocations) {
        this.configLocations = configLocations;
        refresh();
    }

    public ClassPathXmlApplicationContext(String configLocation) {
        this(new String[]{configLocation});
    }

    protected String[] getConfigLocations() {
        return this.configLocations;
    }
}
