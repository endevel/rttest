package org.endevel.rttest.entity;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class CircleBufferTest {

    @org.testng.annotations.AfterMethod
    public void tearDown() {
    }

    @Test
    public void onlyCreateTest() {
        CircleBuffer cb = new CircleBuffer(10);
        assertNotNull(cb);
        assertTrue(cb.isEmpty());
        assertFalse(cb.isFull());
    }

    @Test
    public void checkFullTest() {
        CircleBuffer cb = new CircleBuffer(4);

        cb.add(1);
        cb.add(2);
        cb.add(3);
        cb.add(4);

        assertTrue(cb.isFull());
    }

    /**
     * Простой сеанс работы с буфером
     */
    @Test
    public void addAndGetTest() {
        CircleBuffer cb = new CircleBuffer(8);
        cb.add(1);
        cb.add(2);
        cb.add(3);
        cb.add(4);

        Integer n1 = (Integer) cb.get();
        Integer n2 = (Integer) cb.get();
        Integer n3 = (Integer) cb.get();
        Integer n4 = (Integer) cb.get();

        // Буфер пустой, вернет null
        Integer nn = (Integer) cb.get();

        assertEquals(1, n1.intValue());
        assertEquals(2, n2.intValue());
        assertEquals(3, n3.intValue());
        assertEquals(4, n4.intValue());

        assertNull(nn);
    }

    /**
     * Сценарий переполнения с перезаписью "старых" элементов
     */
    @Test
    public void overflowTest() {
        CircleBuffer cb = new CircleBuffer(4);

        cb.add(1);
        cb.add(2);
        cb.add(3);
        cb.add(4);

        // Переполнение, 5 затирает 1, 2-ка становится самой "старой"
        cb.add(5);

        Integer n1 = (Integer) cb.get();
        assertEquals(2, n1.intValue());

        n1 = (Integer)cb.get();
        assertEquals(3, n1.intValue());

        cb.add(6);
        cb.add(7);

        n1 = (Integer)cb.get();
        assertEquals(4, n1.intValue());
        n1 = (Integer)cb.get();
        assertEquals(5, n1.intValue());
        n1 = (Integer)cb.get();
        assertEquals(6, n1.intValue());
        n1 = (Integer)cb.get();
        assertEquals(7, n1.intValue());
    }

    /**
     * Сценарий переполнения с выбросом исключения
     */
    @Test(expectedExceptions = IllegalStateException.class)
    public void overflowWithExceptionTest() {
        CircleBuffer cb = new CircleBuffer(4, false);
        cb.add(1);
        cb.add(2);
        cb.add(3);
        cb.add(4);

        cb.add(5);
    }
}