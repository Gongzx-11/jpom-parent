package gzx.devops.util;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 定长队列
 *
 */
public class LimitQueue<E> extends ConcurrentLinkedDeque<E> {
    private final int limit;

    public LimitQueue(int limit) {
        if (limit == 0) {
            this.limit = 10;
        } else {
            this.limit = limit;
        }
    }

    @Override
    public boolean offerFirst(E s) {
        if (full()) {
            pollLast();
        }
        return super.offerFirst(s);
    }

    @Override
    public boolean offerLast(E s) {
        if (full()) {
            pollFirst();
        }
        return super.offerLast(s);
    }

    public boolean full() {
        return size() > limit;
    }
}
