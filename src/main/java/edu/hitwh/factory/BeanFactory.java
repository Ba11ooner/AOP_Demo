package edu.hitwh.factory;

import edu.hitwh.annotation.Bean;
import edu.hitwh.annotation.ComponentScan;
import edu.hitwh.annotation.Configuration;
import edu.hitwh.aspect.AspectInfo;
import edu.hitwh.config.Config;
import edu.hitwh.utils.CglibProxyUtils;

import java.util.*;

import static edu.hitwh.utils.GetClassesFromPackage.getClasses;
import static edu.hitwh.aspect.AspectProc.aspectInfos;

/**
 * 相当于 IOC 容器，功能包括
 * 1.获取配置 用于 setBean
 * 2.提供对象 表现为 getBean
 */
public class BeanFactory {
    private static HashMap<String, String> beans = new HashMap<>();
    public static HashMap<String, String> configurations = new HashMap<>();
    //取根目录
    private static String root = BeanFactory.class.getPackage().getName().split("\\.")[0];

    // 在java中，被static修饰的代码块被称作静态代码块。
    // 静态代码块在类被加载时，就会被执行，并且只会执行一次（类只会加载一次）
    // 避免重复配置
    static {
        //获取配置
        try {
            //获取带 @Configuration 注解的类
            getConfigurations();
            // 在带 @Configuration 注解的类中获取扫描的包
            // 在待扫描包中获取 Bean
            getBeans();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //获取带 @Configuration 注解的类
    public static void getConfigurations() throws Exception {

        Set<Class<?>> classes = getClasses(root);
        for (Class c : classes) {
            //获取带 @Configuration 的类
            if (c.isAnnotationPresent(Configuration.class)) {
                Configuration configuration
                        = (Configuration) c.getAnnotation(Configuration.class);
                if (!configuration.value().isEmpty()) {
                    //System.out.println("name:" + configuration.value());
                    //System.out.println("className:" + c.getName());
                    configurations.put(configuration.value(), c.getName());
                } else {
                    //System.out.println("name:" + c.getSimpleName());
                    //System.out.println("className:" + c.getName());
                    configurations.put(c.getSimpleName(), c.getName());
                }
            }
        }
    }

    public static void getBeans() throws Exception {
        //默认扫描根目录下所有Bean
        if (configurations.isEmpty()) {
            Set<Class<?>> classes = getClasses(root);
            for (Class c : classes) {
                if (c.isAnnotationPresent(Bean.class)) {
                    Bean bean = (Bean) c.getAnnotation(Bean.class);
                    if (!bean.value().isEmpty()) {
                        //System.out.println("name:" + bean.value());
                        //System.out.println("className:" + c.getName());
                        beans.put(bean.value(), c.getName());
                    } else {
                        //System.out.println("bean:" + c.getSimpleName());
                        //System.out.println("bean:" + c.getName());
                        beans.put(c.getSimpleName(), c.getName());
                    }
                }
            }
        } else {//扫描 @ComponentScan 中指定的 Class
            List<String> paths = new ArrayList<>();
            //获取 @Configuration 对象 → 获得 @ComponentScan 中指定的包地址
            for (HashMap.Entry<String, String> entry : configurations.entrySet()) {
                Config config = (Config) Class.forName(entry.getValue()).newInstance();
                ComponentScan componentScan = config.getClass().getAnnotation(ComponentScan.class);
                paths.add(componentScan.value());
            }
            //扫描指定 Paths 下所有Bean
            Set<Class<?>> classes = new LinkedHashSet<>();
            for (String path : paths) {
                classes = getClasses(path);
            }
            for (Class c : classes) {
                if (c.isAnnotationPresent(Bean.class)) {
                    Bean bean = (Bean) c.getAnnotation(Bean.class);
                    if (!bean.value().isEmpty()) {
                        //System.out.println("name:" + bean.value());
                        //System.out.println("className:" + c.getName());
                        beans.put(bean.value(), c.getName());
                    } else {
                        //System.out.println("bean:" + c.getSimpleName());
                        //System.out.println("bean:" + c.getName());
                        beans.put(c.getSimpleName(), c.getName());
                    }
                }
            }

        }
    }

    //提供对象，相当于提供了通过注解实现 IOC 配置-获取机制
    public static Object getBean(String name) {
        //从配置中获取类名
        String className = beans.get(name);
        //System.out.println(className + " is creating");
        try {
            Object obj = Class.forName(className).newInstance();
            // 查看是否有相关的 Aspect By 核对全限定类名
            //System.out.println(aspectInfos.size());
            // 存储相关 Aspect 中附加的增强信息
            List<AspectInfo> aspectInfosForProxy = new ArrayList<>();
            for (AspectInfo aspectInfo : aspectInfos) {
                // 如果没有相关的 Aspect，直接返回源对象，不做代理
                if (!aspectInfo.getBean().equals(className)) {
                    return obj;
                } else {// 如果有，筛选出 AspectInfo
                    aspectInfosForProxy.add(aspectInfo);
                }
            }
            //由于引入动态机制，通过接口实现动态代理的方式行不通，只能用 cglib 实现动态代理
            //针对 Aspect 中指定的增强对象，返回代理对象
            Object proxy = null;
            try {
                proxy = CglibProxyUtils.getProxy(obj,aspectInfosForProxy);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return proxy;
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
