package com.carson.aop;

import com.carson.aop.aspectj.AspectJExpressionPointcut;
import com.carson.aop.framework.CglibAopProxy;
import com.carson.aop.framework.JdkDynamicAopProxy;
import com.carson.aop.framework.ProxyFactory;
import com.carson.aop.framework.adapter.MethodInterceptorAdapter;
import com.carson.common.WorkServiceBeforeAdvice;
import com.carson.common.WorkServiceInterceptor;
import com.carson.service.WorkService;
import com.carson.service.WorkServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author carson_luo
 */
public class DynamicProxyTest {

    private AdvisedSupport advisedSupport;

    @BeforeEach
    public void setUp() {
        advisedSupport = new AdvisedSupport();
        advisedSupport.setTargetSource(new TargetSource(new WorkServiceImpl()));
        advisedSupport.setMethodInterceptor(new WorkServiceInterceptor());
        advisedSupport.setMethodMatcher(new AspectJExpressionPointcut("execution(* com.carson.service.WorkService.explode(..))"));
    }

    @Test
    public void TestJdkDynamicProxy() {
        WorkService proxy = (WorkService) new JdkDynamicAopProxy(advisedSupport).getProxy();
        proxy.explode();
    }

    /**
     * vm options : --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED
     */
    @Test
    public void TestCglibProxy() {
        WorkService proxy = (WorkService) new CglibAopProxy(advisedSupport).getProxy();
        proxy.explode();
    }

    @Test
    public void TestProxyFactory(){
        // JDK 动态代理
        advisedSupport.setProxyTargetClass(false);
        var proxyFactory = new ProxyFactory(advisedSupport);
        WorkService proxy = (WorkService) proxyFactory.getProxy();
        proxy.explode();

        // CGLIB 动态代理
        advisedSupport.setProxyTargetClass(true);
        WorkService cglibProxy = (WorkService) new ProxyFactory(advisedSupport).getProxy();
        cglibProxy.explode();
    }

    @Test
    public void TestBeforeAdvice(){
        advisedSupport.setMethodInterceptor(new MethodInterceptorAdapter(new WorkServiceBeforeAdvice()));
        WorkService proxy = (WorkService) new ProxyFactory(advisedSupport).getProxy();
        proxy.explode();
    }
}
