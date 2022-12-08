package com.sparta.hanghaememo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootTest
class HanghaememoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AnnotationConfigApplicationContext annotationConfigApplicationContext;

    @Test
    void test1(){
        //String[] beans = applicationContext.getBeanDefinitionNames();
        String[] beans = (String[]) annotationConfigApplicationContext.getBean("@Controller");
        for (String bean : beans) {
            System.out.println("bean : " + bean);
        }
    }
}
