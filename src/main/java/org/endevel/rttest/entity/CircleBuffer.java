package org.endevel.rttest.entity;

public class CircleBuffer {

    public CircleBuffer (int bufferSize) {
        head = bufferSize - 1;
        tail = 0;
        size = 0;

        this.bufferSize = bufferSize;
        data = new Object[this.bufferSize];
    }

    public CircleBuffer(int size, boolean isOverrideMode) {
        this(size);
        this.isOverrideMode = isOverrideMode;
    }

    public boolean isFull() {
        return size == bufferSize;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public synchronized void add(Object o) {
        if (isFull()) {
            if (!isOverrideMode) {
                throw new IllegalStateException("Buffer is full");
            } else {
                head = nextPosition(head);
                data[head] = o;
                tail = nextPosition(tail);
            }
        } else {
            head = nextPosition(head);
            data[head] = o;
            size++;
        }
    }

    public synchronized Object get() {
        if (isEmpty())
            return null;

        Object o = data[tail];
        tail = nextPosition(tail);

        if (size > 0) {
            size--;
        }

        return o;
    }

    private int nextPosition(int pos) {
        return (pos + 1) % bufferSize;
    }

    private boolean isOverrideMode = true;
    private final int bufferSize;
    private final Object[] data;
    private int size;
    private int head;
    private int tail;
}
