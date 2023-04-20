package com.carson.aop.aspectj;

import com.carson.aop.Pointcut;
import com.carson.aop.PointcutAdvisor;
import org.aopalliance.aop.Advice;

/**
 * @author carson_luo
 */
public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {

    private String expression;

    private AspectJExpressionPointcut pointcut;

    private Advice advice;

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }

    @Override
    public Pointcut getPointcut() {
        if (pointcut == null) {
            return new AspectJExpressionPointcut(expression);
        }
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }
}
