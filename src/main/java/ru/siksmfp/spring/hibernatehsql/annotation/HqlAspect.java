package ru.siksmfp.spring.hibernatehsql.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.engine.spi.SessionFactoryDelegatingImpl;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.query.Query;
import org.springframework.context.ApplicationContext;
import ru.siksmfp.spring.hibernatehsql.exception.DAOException;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Artem Karnov @date 4/20/2018.
 * @email artem.karnov@t-systems.com
 */
@Aspect
public class HqlAspect {

    private SessionFactory sessionFactory;

    @Around("@annotation(ru.siksmfp.spring.hibernatehsql.annotation.Hql)")
    public Object princessHql(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Object[] methodArguments = joinPoint.getArgs();
        Class returnType = methodSignature.getReturnType();

        String[] strings = new String[methodArguments.length];
        for (int i = 0; i < methodArguments.length; i++) {
            strings[i] = (String) methodArguments[i];
        }

        Hql hqlValues = methodSignature.getMethod().getAnnotation(Hql.class);
        String parameterlessHqlQuery = hqlValues.value();

        String queryString = wiveParametersToQuery(parameterlessHqlQuery, strings);

        List resultList;
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Query query = session.createQuery(queryString);
            resultList = query.getResultList();
            tx.commit();
        } catch (Exception ex) {
            throw new DAOException("Can't perform query " + queryString, ex);
        }

        return resultList;
    }

    private String wiveParametersToQuery(String query, String[] parameters) {
        for (int i = 0; i < parameters.length; i++) {
            String replacePattern = "\\?" + (i + 1);
            query = query.replaceFirst(replacePattern, parameters[i]);
        }
        return query;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
