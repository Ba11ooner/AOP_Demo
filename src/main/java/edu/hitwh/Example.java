package edu.hitwh;

import edu.hitwh.entity.User;
import edu.hitwh.entity.Work;
import edu.hitwh.factory.BeanFactory;

public class Example {
    public static void main(String[] args) {
        //IOC 测试
        User user = (User) BeanFactory.getBean("User");
        user.say();
        Work work = (Work) BeanFactory.getBean("work");
        //AOP 测试
        work.some();
        work.other("others");
    }
}
