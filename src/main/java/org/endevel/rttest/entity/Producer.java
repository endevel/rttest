package org.endevel.rttest.entity;

import java.util.Arrays;
import java.util.Random;

/**
 * Поставщик данных, заполняет буффер случайными числами.
 *
 */
public class Producer extends Thread {

    public Producer(CircleBuffer buffer) {
        circleBuffer = buffer;
        randomData = generateRandomData();
    }

    public void putDataToBuffer(Object obj) {
        synchronized (circleBuffer) {
            circleBuffer.add(obj);

            if (!circleBuffer.isFull()) {
                circleBuffer.notifyAll();
                System.out.println(getName() + " Producer #" + getId() + " wrote: " + obj);
            } else {
                try {
                    System.out.println(getName() + " Producer #" + getId() + " will wait");
                    circleBuffer.wait();
                    System.out.println(getName() + " Producer #" + getId() + " woke up");
                    putDataToBuffer(obj);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run() {
        System.out.println(getName() + " Producer #" + getId() + " started.");

        while (readIndex < DATA_LENGTH) {
            putDataToBuffer(randomData[readIndex++]);
            try {
                sleep(500);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(getName() + " Producer #" + getId() + " finished.");
    }

    @Override
    public String toString() {
        return "Producer { " +
                "data = " + Arrays.toString(randomData) +
                ", readIndex = " + readIndex +
                " }";
    }

    private Object[] generateRandomData() {
        Object[] array = new Object[DATA_LENGTH];
        Random rnd = new Random();

        for ( int i = 0; i < DATA_LENGTH; i++) {
            Integer val = Integer.valueOf(rnd.nextInt(1000));
            array[i] = val;
        }

        return array;
    }

    public static final int DATA_LENGTH = 32;

    private CircleBuffer circleBuffer;
    private Object[] randomData;
    private int readIndex = 0;
}
