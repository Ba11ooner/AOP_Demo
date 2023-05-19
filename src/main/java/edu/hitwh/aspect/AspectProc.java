package edu.hitwh.aspect;

import edu.hitwh.annotation.After;
import edu.hitwh.annotation.Aspect;
import edu.hitwh.annotation.Before;
import edu.hitwh.factory.BeanFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static edu.hitwh.constant.Constant.*;
import static edu.hitwh.utils.GetClassesFromPackage.getClasses;

/**
 * 处理 Aspect
 * 1.读取 Aspect 中包含的信息
 * 2.根据包含的信息创建 Proxy
 */
public class AspectProc {
    //取根目录
    private static String root = BeanFactory.class.getPackage().getName().split("\\.")[0];
    public static List<AspectInfo> aspectInfos = new ArrayList<>();

    static {
        try {
            getAspectInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getAspectInfo() throws Exception {
        Set<Class<?>> classes = getClasses(root);
        for (Class c : classes) {
            //获取 Aspect
            if (c.isAnnotationPresent(Aspect.class)) {
                Method[] methods = c.getMethods();
                //获取 Before
                for (Method method : methods) {
                    if (method.isAnnotationPresent(Before.class)) {
                        //System.out.println("Before");
                        String bean = method.getAnnotation(Before.class).className();
                        String beanMethod = method.getAnnotation(Before.class).methodName();
                        String aspect = c.getName();
                        String advice = method.getName();
                        //System.out.println("Class Name:" + bean);
                        //System.out.println("Method Name:" + beanMethod);
                        //System.out.println("Aspect Name:" + aspect);
                        //System.out.println("Advice Name:" + advice);
                        aspectInfos.add(new AspectInfo(bean,beanMethod,aspect,advice,BEFORE));
                    }
                }
                //获取 After
                for (Method method : methods) {
                    if (method.isAnnotationPresent(After.class)) {
                        //System.out.println("After");
                        String bean = method.getAnnotation(After.class).className();
                        String beanMethod = method.getAnnotation(After.class).methodName();
                        String aspect = c.getName();
                        String advice = method.getName();
                        //System.out.println("Class Name:" + bean);
                        //System.out.println("Method Name:" + beanMethod);
                        //System.out.println("Aspect Name:" + aspect);
                        //System.out.println("Advice Name:" + advice);
                        aspectInfos.add(new AspectInfo(bean,beanMethod,aspect,advice,AFTER));
                    }
                }
            }
        }
    }

}
