package edu.hitwh.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定相对位置
 * 给出 全限定类名 和 待增强的方法名
 * 写死，不支持重载（方法名唯一），不支持切点表达式（无特殊功能）
 * 上述作用和 Pointcut 一致
 * 除此之外，还能通过注解间接获取 advice（提供增强功能的方法名）
 */
@Target(ElementType.METHOD) //参考 Spring AOP,表明该方法为 advice
@Retention(RetentionPolicy.RUNTIME)
public @interface After {
    String className();

    String methodName();
}
