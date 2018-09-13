package org.endevel.rttest;

import org.endevel.rttest.entity.CircleBuffer;
import org.endevel.rttest.entity.Consumer;
import org.endevel.rttest.entity.Producer;

import java.util.Arrays;

/**
 * Используется в качестве стенда для тестовых прогонов работы с CircleBuffer
 */
public class AppTestRunner {

    public static void main(String[] args) throws InterruptedException {
        int countOfProducers = 3;
        CircleBuffer circleBuffer = new CircleBuffer(8);

        Consumer firstConsumer = new Consumer(circleBuffer,
                                       Producer.DATA_LENGTH * countOfProducers);
        firstConsumer.setName("Consumer");

        for (int i = 0; i < countOfProducers; i++) {
            Producer p = new Producer(circleBuffer);
            p.setName("Producer #" + (i + 1));
            p.start();
        }

        firstConsumer.start();
        firstConsumer.join();
        System.out.println(Arrays.toString(firstConsumer.getResultData()));
    }

}
