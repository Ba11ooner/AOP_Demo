package edu.hitwh.utils;

import edu.hitwh.aspect.AspectInfo;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static edu.hitwh.constant.Constant.*;

public class CglibProxyUtils {
    /**
     * @param target      待增强对象
     * @param aspectInfos 增强信息
     * @return 增强后对象
     * @throws Exception
     */
    public static Object getProxy(final Object target, List<AspectInfo> aspectInfos) throws Exception {

        //此处的 aspectInfos 包含所有待增强信息
        //先按方法名分类，再按位置属性细分
        Set<String> methodNames = new LinkedHashSet<>();
        for (int i = 0; i < aspectInfos.size(); i++) {
            methodNames.add(aspectInfos.get(i).getMethod());
        }
        //多次增强，引入临时变量
        Object proxy = null;
        //引入循环，实现多次代理
        for (String methodName : methodNames) {
            AspectInfo beforeInfo = null;
            AspectInfo afterInfo = null;
            Class cls = target.getClass();
            for (AspectInfo aspectInfo : aspectInfos) {
                //如果名字不匹配，则跳过
                if (!aspectInfo.getMethod().equals(methodName)) continue;
                if (aspectInfo.getPos().equals(BEFORE)) beforeInfo = aspectInfo;
                if (aspectInfo.getPos().equals(AFTER)) afterInfo = aspectInfo;
            }
            AspectInfo finalBeforeInfo = beforeInfo;
            AspectInfo finalAfterInfo = afterInfo;
            proxy = Enhancer.create(cls,
                    (MethodInterceptor) (obj, method, args, methodProxy) -> {
                        //动态获取 Before 方法并调用
                        if (finalBeforeInfo != null) {
                            Method before = Class.forName(finalBeforeInfo.getAspect()).getMethod(finalBeforeInfo.getAdvice());
                            before.invoke(Class.forName(finalBeforeInfo.getAspect()).newInstance(), null);
                        }
                        //调用 target 原有方法
                        Object res = method.invoke(target, args);
                        //动态获取 After 方法并调用
                        if (finalAfterInfo != null) {
                            Method after = Class.forName(finalAfterInfo.getAspect()).getMethod(finalAfterInfo.getAdvice());
                            after.invoke(Class.forName(finalAfterInfo.getAspect()).newInstance(), null);
                        }
                        return res;
                    });
        }
        return proxy;
    }

}
