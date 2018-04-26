package ru.siksmfp.spring.hibernatehsql.repository.api;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.siksmfp.spring.hibernatehsql.exception.DAOException;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author Artem Karnov @date 4/17/2018.
 * @email artem.karnov@t-systems.com
 */
public class GenericRepository {

    private SessionFactory sessionFactory;

    private int batchSize = 10;

    public void save(Object entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(entity);
            tx.commit();
        } catch (Exception ex) {
            throw new DAOException("Can't save " + entity, ex);
        }
    }

    public void batchSave(List<Object> entityList, Class daoType) {
        try (Session session = sessionFactory.openSession()) {
            int tableSize = entityList.size();
            for (int i = 0; i < tableSize; i += batchSize) {
                Transaction tx = session.beginTransaction();
                Query internalQuery = session.createQuery("from " + daoType.getSimpleName());
                int lastIndex = (i + batchSize) < tableSize ? (i + batchSize) : tableSize;
                internalQuery.setFirstResult(0);
                internalQuery.setMaxResults(batchSize);
                for (Object currentEntity : entityList.subList(i, lastIndex)) {
                    if (currentEntity != null)
                        session.save(currentEntity);
                }
                tx.commit();
            }
        } catch (Exception ex) {
            throw new DAOException("Can't save batch ", ex);
        }
    }

    public Object find(Object key, Class daoType) {
        Object result;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            result = session.get(daoType, (Serializable) key);
            tx.commit();
        } catch (Exception ex) {
            throw new DAOException("Can't find " + key, ex);
        }
        return result;
    }

    public void update(Object entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(entity);
            tx.commit();
        } catch (Exception ex) {
            throw new DAOException("Can't update " + entity, ex);
        }
    }

    public void delete(Object entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(entity);
            tx.commit();
        } catch (Exception ex) {
            throw new DAOException("Can't delete " + entity, ex);
        }
    }

    public List<Object> getAll(Class daoType) {
        List result;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Query query = session.createQuery("from " + daoType.getSimpleName());
            result = query.list();
            tx.commit();
        } catch (Exception ex) {
            throw new DAOException("Can't find all ", ex);
        }
        return (List<Object>) result;
    }

    public void deleteAll(Class daoType) {
        try (Session session = sessionFactory.openSession()) {
            long tableSize = countElements(daoType);
            for (int i = 0; i < tableSize; i += batchSize) {
                Transaction tx = session.beginTransaction();
                Query internalQuery = session.createQuery("from " + daoType.getSimpleName());
                internalQuery.setFirstResult(0);
                internalQuery.setMaxResults(batchSize);
                List<Object> list = internalQuery.list();
                for (Object e : list) {
                    if (e != null)
                        session.delete(e);
                }
                tx.commit();
            }
        } catch (Exception ex) {
            throw new DAOException("Can't delete all ", ex);
        }
    }

    public long countElements(Class daoType) {
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("select count(1) from " + daoType.getSimpleName());
            return (long) query.uniqueResult();
        } catch (Exception ex) {
            throw new DAOException("Can't calculate table size of " + daoType.getSimpleName(), ex);
        }
    }

    public Object performQuery(String queryString, Class returningType) {
        List resultList;
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Query query = session.createQuery(queryString);
            resultList = query.getResultList();
            tx.commit();
        } catch (Exception ex) {
            throw new DAOException("Can't perform query " + queryString, ex);
        }
        if (Collection.class.isAssignableFrom(returningType)) {
            return resultList;
        } else {
            if (resultList.size() > 1) {
                return returningType.cast(resultList);
            } else {
                throw new IllegalStateException("Found more than one elements");
            }
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
