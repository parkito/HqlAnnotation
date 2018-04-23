package ru.siksmfp.spring.hibernatehsql.annotation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import ru.siksmfp.spring.hibernatehsql.repository.api.IGenericRepository;

import java.lang.reflect.Proxy;

/**
 * @author Artem Karnov @date 4/22/2018.
 * @email artem.karnov@t-systems.com
 */
public class HqlBeanPostProcessor implements BeanPostProcessor {

    private HqlInvocationHandler hqlInvocationHandler;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        //find all interfaces that extended IGenericRepository
        //create instance of interface
        //extend Generic abstract class

        if (bean instanceof IGenericRepository) {
            return bean.getClass().cast(
                    Proxy.newProxyInstance(
                            HqlBeanPostProcessor.class.getClassLoader(),
                            new Class[]{bean.getClass()},
                            hqlInvocationHandler));
        } else {
            return bean;
        }
    }

//    @Override
//    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        return null;
//    }
}
