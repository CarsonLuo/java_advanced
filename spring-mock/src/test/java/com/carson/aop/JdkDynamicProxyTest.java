package com.carson.aop;

import com.carson.aop.aspectj.AspectJExpressionPointcut;
import com.carson.aop.framework.JdkDynamicAopProxy;
import com.carson.common.WorkServiceInterceptor;
import com.carson.service.WorkService;
import com.carson.service.WorkServiceImpl;
import org.junit.jupiter.api.Test;

/**
 * @author carson_luo
 */
public class JdkDynamicProxyTest {

    @Test
    public void TestJdkDynamicProxy(){
        var advisedSupport = new AdvisedSupport();
        advisedSupport.setTargetSource(new TargetSource(new WorkServiceImpl()));
        advisedSupport.setMethodInterceptor(new WorkServiceInterceptor());
        advisedSupport.setMethodMatcher(new AspectJExpressionPointcut("execution(* com.carson.service.WorkService.explode(..))"));

        WorkService proxy = (WorkService) new JdkDynamicAopProxy(advisedSupport).getProxy();
        proxy.explode();
    }
}
