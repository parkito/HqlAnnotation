package ru.siksmfp.spring.hibernatehsql.annotation;

import org.reflections.Reflections;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import ru.siksmfp.spring.hibernatehsql.repository.api.IGenericRepository;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Artem Karnov @date 4/26/2018.
 * @email artem.karnov@t-systems.com
 */
public class ContextStartedEventListener implements ApplicationListener<ContextRefreshedEvent> {
    private Map<Class, Object> beanMap;

    private HqlInvocationHandler hqlInvocationHandler;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        beanMap = new HashMap<>();
        Set<Class<? extends IGenericRepository>> repos = findAllRepositoriesInClassPath("ru");

        for (Class beanClass : repos) {
            Object beanInstance = Proxy.newProxyInstance(
                    this.getClass().getClassLoader(),
                    new Class[]{beanClass},
                    hqlInvocationHandler);
            beanMap.put(beanClass, beanInstance);
        }

        String[] beanNames = event.getApplicationContext().getBeanDefinitionNames();

        for (String beanName : beanNames) {
            Object bean = event.getApplicationContext().getBean(beanName);
            Field[] beanFields = bean.getClass().getDeclaredFields();
            for (Field beanField : beanFields) {
                List<Class> clss = Arrays.asList(beanField.getType().getInterfaces());
                if (clss.contains(IGenericRepository.class)) {
                    beanField.setAccessible(true);
                    try {
                        if (beanField.get(bean) == null) {
                            beanField.set(bean, beanField.getType().cast(beanMap.get(beanField.getType())));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private Set<Class<? extends IGenericRepository>> findAllRepositoriesInClassPath(String rootPackage) {
        Reflections reflections = new Reflections(rootPackage);
        return reflections.getSubTypesOf(IGenericRepository.class);
    }

    public void setHqlInvocationHandler(HqlInvocationHandler hqlInvocationHandler) {
        this.hqlInvocationHandler = hqlInvocationHandler;
    }

    public HqlInvocationHandler getHqlInvocationHandler() {
        return hqlInvocationHandler;
    }

}
