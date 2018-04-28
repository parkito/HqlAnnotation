package ru.siksmfp.spring.hibernatehsql;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.siksmfp.spring.hibernatehsql.entity.Data;
import ru.siksmfp.spring.hibernatehsql.service.EntityService;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/app-context.xml"})
public class EntityDaoTest {

    private static final Data DATA_1 = new Data("str1", 1.0, true);
    private static final Data DATA_2 = new Data("str2", 2.0, true);
    private static final Data DATA_3 = new Data("str3", 3.0, true);
    private static final Data DATA_4 = new Data("str4", 4.0, true);
    private static final Data DATA_5 = new Data("str5", 5.0, true);
    private static final Data DATA_6 = new Data("str6", 6.0, true);
    private static final Data DATA_7 = new Data("str7", 7.0, true);
    private static final Data DATA_8 = new Data("str8", 8.0, true);
    private static final Data DATA_9 = new Data("str8", 9.0, true);
    private static final Data DATA_10 = new Data("str10", 1.0, true);
    private static final Data DATA_11 = new Data("str10", 1.0, true);

    @Autowired
    EntityService entityDao;

    @Before
    public void setUp() {
        entityDao.deleteAll();
    }

    @Test
    public void createTest() {
        entityDao.save(DATA_1);
        entityDao.save(DATA_2);
        entityDao.save(DATA_3);
        entityDao.save(DATA_4);
        entityDao.save(DATA_5);
        entityDao.save(DATA_6);
        entityDao.save(DATA_7);
        entityDao.save(DATA_8);
        entityDao.save(DATA_9);
        entityDao.save(DATA_10);
        entityDao.save(DATA_11);

        List<Data> all = entityDao.getAll();

        Assert.assertEquals(11, all.size());

        Assert.assertTrue(all.contains(DATA_1));
        Assert.assertTrue(all.contains(DATA_2));
        Assert.assertTrue(all.contains(DATA_3));
        Assert.assertTrue(all.contains(DATA_4));
        Assert.assertTrue(all.contains(DATA_5));
        Assert.assertTrue(all.contains(DATA_6));
        Assert.assertTrue(all.contains(DATA_7));
        Assert.assertTrue(all.contains(DATA_8));
        Assert.assertTrue(all.contains(DATA_9));
        Assert.assertTrue(all.contains(DATA_10));
        Assert.assertTrue(all.contains(DATA_11));
    }

    @Test
    public void deleteTest() {
        entityDao.save(DATA_1);
        entityDao.save(DATA_2);
        entityDao.save(DATA_3);

        List<Data> all = entityDao.getAll();

        Assert.assertEquals(3, all.size());

        entityDao.delete(DATA_1);
        entityDao.delete(DATA_2);

        all = entityDao.getAll();

        Assert.assertEquals(1, all.size());
        Assert.assertTrue(all.contains(DATA_3));
    }

    @Test
    public void findTest() {
        entityDao.save(DATA_1);
        entityDao.save(DATA_2);
        entityDao.save(DATA_3);

        Data foundData1 = entityDao.find(DATA_1.getId());
        Data foundData2 = entityDao.find(DATA_2.getId());
        Data foundData3 = entityDao.find(DATA_3.getId());

        Assert.assertEquals(foundData1, DATA_1);
        Assert.assertEquals(foundData2, DATA_2);
        Assert.assertEquals(foundData3, DATA_3);
    }

    @Test
    public void batchSaveTest() {
        List<Data> batch = Arrays.asList(DATA_1, DATA_2, DATA_3);

        entityDao.batchSave(batch);

        List<Data> all = entityDao.getAll();

        Assert.assertEquals(3, all.size());

        Assert.assertTrue(all.contains(DATA_1));
        Assert.assertTrue(all.contains(DATA_2));
        Assert.assertTrue(all.contains(DATA_3));
    }

    @Test
    public void annotationTest() {
        entityDao.save(DATA_1);
        entityDao.save(DATA_2);
        List<Data> dataByString = entityDao.findDataByString(DATA_1.getDataString());

        Assert.assertEquals(DATA_1, dataByString.get(0));
    }
}