package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ArrayMap<K, V> extends AbstractIterableMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1;
    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    SimpleEntry<K, V>[] entries;

    // You may add extra fields or helper methods though!
    private int size;

    public ArrayMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public ArrayMap(int initialCapacity) {
        this.entries = this.createArrayOfEntries(initialCapacity);
        this.size = 0;
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code Entry<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     */
    @SuppressWarnings("unchecked")
    private SimpleEntry<K, V>[] createArrayOfEntries(int arraySize) {
        /*
        It turns out that creating arrays of generic objects in Java is complicated due to something
        known as "type erasure."

        We've given you this helper method to help simplify this part of your assignment. Use this
        helper method as appropriate when implementing the rest of this class.

        You are not required to understand how this method works, what type erasure is, or how
        arrays and generics interact.
        */
        return (SimpleEntry<K, V>[]) (new SimpleEntry[arraySize]);
    }

    @Override
    public V get(Object key) {
        if (containsKey(key)) {
            return this.entries[getIndexOfKey(key)].getValue();
        } else {
            return null;
        }
    }

    @Override
    public V put(K key, V value) {
        // overwrite old value for the key with new value
        if (containsKey(key)) {
            // V oldValue = this.entries[getIndexOfKey(key)].getValue();
            // this.entries[getIndexOfKey(key)].setValue(value);
            // return oldValue;
            return this.entries[getIndexOfKey(key)].setValue(value);
        }

        this.size++;

        if (this.size > this.entries.length) {
            /*
            If array is full and the user inserts a new key, create a new array
            that is double the size of the original array and copy over the old
            elements
            */
            SimpleEntry<K, V>[] temp = this.entries;
            this.entries = createArrayOfEntries(this.entries.length * 2);

            for (int i = 0; i < temp.length; i++) {
                this.entries[i] = temp[i];
            }
        }

        this.entries[this.size - 1] = new SimpleEntry<>(key, value);
        return null;
    }

    @Override
    public V remove(Object key) {
        V removedValue = null;

        if (containsKey(key)) {
            int removedValueIndex = getIndexOfKey(key);
            removedValue = this.entries[removedValueIndex].getValue();
            this.entries[removedValueIndex] = this.entries[this.size - 1];
            this.size--;
        }

        return removedValue;
    }

    @Override
    public void clear() {
        this.entries = createArrayOfEntries(this.entries.length);
        this.size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return getIndexOfKey(key) != -1;
    }

    /**
     * private method to allow us to access the index of
     * the key we are searching for
     */
    private int getIndexOfKey(Object key) {
        for (int i = 0; i < this.size; i++) {
            // changed from key.equals(this.entries[i].getKey())
            if (Objects.equals(key, this.entries[i].getKey())) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: you won't need to change this method (unless you add more constructor parameters)
        return new ArrayMapIterator<>(this.entries, this.size);
    }

    private static class ArrayMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private final SimpleEntry<K, V>[] entries;
        // You may add more fields and constructor parameters
        private int currentIndex = 0;
        private final int size;

        public ArrayMapIterator(SimpleEntry<K, V>[] entries, int size) {
            this.entries = entries;
            this.size = size;
        }

        @Override
        public boolean hasNext() {
            return this.currentIndex < this.size;
        }

        @Override
        public Map.Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No element found");
            } else {
                Map.Entry<K, V> next = this.entries[this.currentIndex];
                this.currentIndex++;
                return next;
            }
        }
    }
}
