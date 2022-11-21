package com.yrgo.advice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.stereotype.Component;

@Component
public class PerformanceTimingAdvice {

    public Object timeMethod(ProceedingJoinPoint method) throws Throwable {

        //Before our method
        long startTime = System.nanoTime();

        // run our method
        try {
            return method.proceed();
        }
        // after our method has executed
        finally {
            long endTime = System.nanoTime();
            long diff = endTime - startTime;
            long timeToExecuteMethod = diff / 1000000;
            ;

            System.out.printf("The method %s from the class %s took %d ms to execute \n", method.getSignature().getName(), method.getSignature().getDeclaringType(), timeToExecuteMethod);
        }

    }

}
