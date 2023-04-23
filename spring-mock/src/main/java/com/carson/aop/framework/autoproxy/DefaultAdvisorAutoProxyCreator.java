package com.carson.aop.framework.autoproxy;

import com.carson.aop.AdvisedSupport;
import com.carson.aop.Advisor;
import com.carson.aop.Pointcut;
import com.carson.aop.TargetSource;
import com.carson.aop.aspectj.AspectJExpressionPointcutAdvisor;
import com.carson.aop.framework.ProxyFactory;
import com.carson.beans.PropertyValues;
import com.carson.beans.exception.BeansException;
import com.carson.beans.factory.BeanFactory;
import com.carson.beans.factory.BeanFactoryAware;
import com.carson.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.carson.beans.factory.support.DefaultListableBeanFactory;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author carson_luo
 */
public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private DefaultListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 避免死循环
        Class<?> beanClass = bean.getClass();
        if (isInfrastructureClass(beanClass)) {
            return bean;
        }
        var advisors = beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();
        try {
            for (AspectJExpressionPointcutAdvisor advisor : advisors) {
                var classFilter = advisor.getPointcut().getClassFilter();
                if (!classFilter.matches(beanClass)) {
                    continue;
                }

                var advisedSupport = new AdvisedSupport();
                advisedSupport.setTargetSource(new TargetSource(bean));
                advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
                advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());

                // 返回代理对象
                return new ProxyFactory(advisedSupport).getProxy();
            }
        } catch (Exception ex) {
            throw new BeansException("Error create proxy bean for: " + beanName, ex);
        }
        return null;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override
    public boolean postProcessorAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        return pvs;
    }

    /**
     * Infrastructure 基础设施
     */
    private boolean isInfrastructureClass(Class<?> beanClass) {
        return Advice.class.isAssignableFrom(beanClass) ||
                Pointcut.class.isAssignableFrom(beanClass) ||
                Advisor.class.isAssignableFrom(beanClass);
    }
}
