package edu.hitwh.aspect;

public class AspectInfo {
    //源对象信息
    String bean;
    String method;
    //增强信息
    String aspect;
    String advice;
    String pos;//位置

    public AspectInfo(String bean, String method, String aspect, String advice, String pos) {
        this.bean = bean;
        this.method = method;
        this.aspect = aspect;
        this.advice = advice;
        this.pos = pos;
    }

    public String getAdvice() {
        return advice;
    }

    public String getAspect() {
        return aspect;
    }

    public String getBean() {
        return bean;
    }

    public String getMethod() {
        return method;
    }

    public String getPos() {
        return pos;
    }

    @Override
    public String toString() {
        return "AspectInfo{" +
                "bean='" + bean + '\'' +
                ", method='" + method + '\'' +
                ", aspect='" + aspect + '\'' +
                ", advice='" + advice + '\'' +
                '}';
    }
}
