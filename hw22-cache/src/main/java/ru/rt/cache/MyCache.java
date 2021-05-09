package ru.rt.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.util.*;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(MyCache.class);

    private final WeakHashMap<K, V> cache = new WeakHashMap<>();
    private final List<WeakReference<HwListener<K, V>>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notify(key,value,"put");
    }

    @Override
    public void remove(K key) {
        var value = cache.remove(key);
        notify(key,value,"remove");
    }

    @Override
    public V get(K key) {
        var value = cache.get(key);
        notify(key,value,"get");
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(new WeakReference<>(listener));
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.removeIf(ref -> ref.get() == null || Objects.equals(ref.get(), listener));
    }

    private void notify(K key, V value, String action){
        for (WeakReference<HwListener<K, V>> ref: listeners) {
            var listener = ref.get();
            if (listener != null) {
                try {
                    listener.notify(key, value, action);
                }catch(RuntimeException e){
                    logger.error("{} Stack: {}", e.getMessage(), Arrays.toString(e.getStackTrace()));
                }
            }
            else{
                listeners.remove(ref);
            }
        }
    }

}
