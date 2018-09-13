package org.endevel.rttest.entity;

public class Consumer extends Thread {

    public Consumer(CircleBuffer buffer, int maxSize) {
        circleBuffer = buffer;
        resultData = new Object[maxSize];
    }


    public Object[] getResultData() {
        return resultData;
    }

    public void getDataFromBuffer() {
        Object obj;
        System.out.println(getName() + " Consumer #" + getId() + " get data start");
        synchronized (circleBuffer) {
            if ((obj = circleBuffer.get()) == null) {
                try {
                    System.out.println(getName() + " Consumer #" + getId() + " will wait");
                    circleBuffer.wait(1000);
                    System.out.println(getName() + " Consumer #" + getId() + " woke up");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            circleBuffer.notifyAll();
        }

        System.out.println(getName() + " #" + getId() + " read: " + obj + " size: " + nextIndex);
        resultData[nextIndex++] = obj;
    }

    @Override
    public void run() {
        System.out.println("Consumer #" + getId() + " started.");

        while (nextIndex < resultData.length) {
            getDataFromBuffer();
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Consumer #" + getId() + " finished.");
    }

    private CircleBuffer circleBuffer;
    private int nextIndex;
    private final Object[] resultData;
}
