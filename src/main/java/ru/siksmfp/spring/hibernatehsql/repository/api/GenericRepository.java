package ru.siksmfp.spring.hibernatehsql.repository.api;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import ru.siksmfp.spring.hibernatehsql.exception.DAOException;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author Artem Karnov @date 4/17/2018.
 * @email artem.karnov@t-systems.com
 */
public class GenericRepository<E, K extends Serializable> implements IGenericRepository<E, K> {
    @Autowired
    private SessionFactory sessionFactory;

    private Class<E> daoType;

    private int batchSize = 10;

    public GenericRepository() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        this.daoType = (Class<E>) pt.getActualTypeArguments()[0];
    }

    @Override
    public void save(E entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(entity);
            tx.commit();
        } catch (Exception ex) {
            throw new DAOException("Can't save " + entity, ex);
        }
    }

    @Override
    public void batchSave(List<E> entityList) {
        try (Session session = sessionFactory.openSession()) {
            int tableSize = entityList.size();
            for (int i = 0; i < tableSize; i += batchSize) {
                Transaction tx = session.beginTransaction();
                Query internalQuery = session.createQuery("from " + daoType.getSimpleName());
                int lastIndex = (i + batchSize) < tableSize ? (i + batchSize) : tableSize;
                internalQuery.setFirstResult(0);
                internalQuery.setMaxResults(batchSize);
                for (E currentEntity : entityList.subList(i, lastIndex)) {
                    if (currentEntity != null)
                        session.save(currentEntity);
                }
                tx.commit();
            }
        } catch (Exception ex) {
            throw new DAOException("Can't save batch ", ex);
        }
    }

    @Override
    public E find(K key) {
        E result;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            result = session.get(daoType, key);
            tx.commit();
        } catch (Exception ex) {
            throw new DAOException("Can't find " + key, ex);
        }
        return result;
    }

    @Override
    public void update(E entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(entity);
            tx.commit();
        } catch (Exception ex) {
            throw new DAOException("Can't update " + entity, ex);
        }
    }

    @Override
    public void delete(E entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(entity);
            tx.commit();
        } catch (Exception ex) {
            throw new DAOException("Can't delete " + entity, ex);
        }
    }

    @Override
    public List<E> getAll() {
        List result;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Query query = session.createQuery("from " + daoType.getSimpleName());
            result = query.list();
            tx.commit();
        } catch (Exception ex) {
            throw new DAOException("Can't find all ", ex);
        }
        return (List<E>) result;
    }

    @Override
    public void deleteAll() {
        try (Session session = sessionFactory.openSession()) {
            long tableSize = countElements();
            for (int i = 0; i < tableSize; i += batchSize) {
                Transaction tx = session.beginTransaction();
                Query internalQuery = session.createQuery("from " + daoType.getSimpleName());
                internalQuery.setFirstResult(0);
                internalQuery.setMaxResults(batchSize);
                List<E> list = internalQuery.list();
                for (E e : list) {
                    if (e != null)
                        session.delete(e);
                }
                tx.commit();
            }
        } catch (Exception ex) {
            throw new DAOException("Can't delete all ", ex);
        }
    }

    @Override
    public long countElements() {
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("select count(1) from " + daoType.getSimpleName());
            return (long) query.uniqueResult();
        } catch (Exception ex) {
            throw new DAOException("Can't calculate table size of " + daoType.getSimpleName(), ex);
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public int getBatchSize() {
        return batchSize;
    }

    private void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

}
