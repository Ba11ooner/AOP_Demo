package edu.hitwh;

import edu.hitwh.annotation.ComponentScan;
import edu.hitwh.annotation.Configuration;
import edu.hitwh.config.Config;
import edu.hitwh.factory.BeanFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static edu.hitwh.utils.GetClassesFromPackage.getClasses;

/**
 * 用作单元测试，验证基础功能的正确性
 */
public class Test {

    public static void getConfigurationTest() {
        String packageName = BeanFactory.class.getPackage().getName();
        //取根目录
        String root = packageName.split("\\.")[0];
        System.out.println(root);
        try {
            Set<Class<?>> classes = getClasses(root);
            for (Class c : classes) {
                if (c.isAnnotationPresent(Configuration.class)) {
                    Configuration configuration
                            = (Configuration) c.getAnnotation(Configuration.class);
                    if (!configuration.value().isEmpty()) {
                        System.out.println("name:" + configuration.value());
                        System.out.println("className:" + c.getName());
                        //configurations.put(configuration.value(), c.getName());
                    } else {
                        System.out.println("name:" + c.getSimpleName());
                        System.out.println("className:" + c.getName());
                        //configurations.put(c.getSimpleName(), c.getName());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void func() throws Exception {
        List<String> paths = new ArrayList<>();
        //获取 @Configuration 对象 → 获得 @ComponentScan 中指定的包地址
        for (HashMap.Entry<String, String> entry : BeanFactory.configurations.entrySet()) {
            Config config = null;
            System.out.println(entry.getValue());
            config = (Config) Class.forName(entry.getValue()).newInstance();
            ComponentScan componentScan = config.getClass().getAnnotation(ComponentScan.class);
            paths.add(componentScan.value());
        }
        System.out.println(paths.toString());
    }

    public static void main(String[] args) {
        try {
            getConfigurationTest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
