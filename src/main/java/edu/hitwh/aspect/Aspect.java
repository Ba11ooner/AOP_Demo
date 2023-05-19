package edu.hitwh.aspect;

import edu.hitwh.annotation.After;
import edu.hitwh.annotation.Before;
import edu.hitwh.annotation.Pointcut;

@edu.hitwh.annotation.Aspect()
/**
 * 只实现了两个位置的注解 @Before 和 @After
 */
public class Aspect {

    /**
     * 此处正统用法应该是可以将 Pointcut 提供的表达式信息填充到 @Before 或 @After 中
     * 既然没有引入表达式，则没必要实现 @Pointcut
     */
    @Pointcut(className = "edu.hitwh.entity.Work", methodName = "some")
    public void some() {
    }

    @Before(className = "edu.hitwh.entity.Work", methodName = "some")
    public void beforeSome() {
        System.out.println("Before Some");
    }

    @After(className = "edu.hitwh.entity.Work", methodName = "some")
    public void afterSome() {
        System.out.println("After Some");
    }

    @Before(className = "edu.hitwh.entity.Work", methodName = "other")
    public void beforeOther() {
        System.out.println("Before other");
    }

    @After(className = "edu.hitwh.entity.Work", methodName = "other")
    public void AfterOther() {
        System.out.println("After other");
    }

}
