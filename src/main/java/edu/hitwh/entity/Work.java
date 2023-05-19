package edu.hitwh.entity;

import edu.hitwh.annotation.Bean;

@Bean("work")
public class Work {
    public void some() {
        System.out.println("do something...");
    }

    public void other(String str) {
        System.out.println(str);
    }
}
