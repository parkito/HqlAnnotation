package ru.siksmfp.spring.hibernatehsql;

import ru.siksmfp.spring.hibernatehsql.entity.Data;
import ru.siksmfp.spring.hibernatehsql.repository.impl.EntityRepository;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Main {
    private static final Data DATA_1 = new Data("str1", 1.0, true);
    private static final Data DATA_2 = new Data("str2", 2.0, true);
    private static final Data DATA_3 = new Data("str3", 3.0, true);

    public static void main(String[] args) {
//        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:META-INF/spring/app-context.xml");

        EntityRepository er = (EntityRepository) Proxy.newProxyInstance(
                Main.class.getClassLoader(),
                new Class[]{EntityRepository.class},
                new CustomHandler());

    }

    static class CustomHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return null;
        }
    }
}
