package demo.wikiedits;

import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TalkEditEventCollectingSink implements SinkFunction<TalkEditEvent> {

    private static final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private static final Map<String, TalkEditEvent> values = new HashMap<>();

    @Override
    public final void invoke(TalkEditEvent talkEditEvent, Context context) {
        readWriteLock.writeLock().lock();
        try {
            values.put(talkEditEvent.getId(), talkEditEvent);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public Map<String, TalkEditEvent> getValues() {
        readWriteLock.readLock().lock();
        try {
            return Collections.unmodifiableMap(values);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public void clear() {
        readWriteLock.writeLock().lock();
        try {
            values.clear();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
}
