package ru.siksmfp.spring.hibernatehsql.annotation;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.annotation.PostConstruct;

/**
 * @author Artem Karnov @date 4/22/2018.
 * @email artem.karnov@t-systems.com
 */
public class HqlStarter {

    private SessionFactory sessionFactory;

    @PostConstruct
    public void setUp() {


    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
