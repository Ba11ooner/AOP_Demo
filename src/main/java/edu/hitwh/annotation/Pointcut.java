package edu.hitwh.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定 Joinpoint
 * 给出 全限定类名 和 待增强的方法名
 * 写死，不支持重载（方法名唯一），不支持切点表达式（无特殊功能）
 */
@Deprecated
@Target(ElementType.METHOD) //参考 Spring AOP 用来标注切点的方法必须是一个空方法
@Retention(RetentionPolicy.RUNTIME)
public @interface Pointcut {
    String className();

    String methodName();

}
