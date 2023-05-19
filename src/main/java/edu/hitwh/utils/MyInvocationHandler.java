package edu.hitwh.utils;

import edu.hitwh.aspect.AspectInfo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Deprecated
/**
 * @Description:由于引入动态机制，不带接口，所以不能用JDK实现代理
 */
public class MyInvocationHandler<T> implements InvocationHandler {
    T target;
    Class aspect;
    Method before;
    Method after;

    public MyInvocationHandler(T target, AspectInfo aspectInfo) {
        this.target = target;
        try {
            aspect = Class.forName(aspectInfo.getAspect());
            if (aspectInfo.getPos().equals("before"))
                this.before = Class.forName(aspectInfo.getAspect()).getMethod(aspectInfo.getAdvice());
            if (aspectInfo.getPos().equals("after"))
                this.after = Class.forName(aspectInfo.getAspect()).getMethod(aspectInfo.getAdvice());
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before.invoke(aspect.newInstance(), args);
        Object obj = method.invoke(target, args);
        after.invoke(aspect.newInstance(), args);
        return obj;
    }

}
