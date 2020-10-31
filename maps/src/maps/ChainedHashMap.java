package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ChainedHashMap<K, V> extends AbstractIterableMap<K, V> {
    private static final double DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD = 0.75;
    private static final int DEFAULT_INITIAL_CHAIN_COUNT = 3;
    private static final int DEFAULT_INITIAL_CHAIN_CAPACITY = 10;

    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    AbstractIterableMap<K, V>[] chains;

    // You're encouraged to add extra fields (and helper methods) though!
    double resizingLoadFactorThreshold;
    int chainInitialCapacity;
    double currentLoadFactor;
    // current number of buckets
    //int currentCapacity;
    int currentNumberOfNodes;

    public ChainedHashMap() {
        this(DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD, DEFAULT_INITIAL_CHAIN_COUNT, DEFAULT_INITIAL_CHAIN_CAPACITY);
    }

    public ChainedHashMap(double resizingLoadFactorThreshold, int initialChainCount, int chainInitialCapacity) {
        this.resizingLoadFactorThreshold = resizingLoadFactorThreshold;
        this.chains = createArrayOfChains(initialChainCount);

        this.chainInitialCapacity = chainInitialCapacity;
        //this.currentCapacity = initialChainCount;

        this.currentLoadFactor = 0.0;
        this.currentNumberOfNodes = 0;
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code AbstractIterableMap<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     * @see ArrayMap createArrayOfEntries method for more background on why we need this method
     */
    @SuppressWarnings("unchecked")
    private AbstractIterableMap<K, V>[] createArrayOfChains(int arraySize) {
        return (AbstractIterableMap<K, V>[]) new AbstractIterableMap[arraySize];
    }

    /**
     * Returns a new chain.
     *
     * This method will be overridden by the grader so that your ChainedHashMap implementation
     * is graded using our solution ArrayMaps.
     *
     * Note: You do not need to modify this method.
     */
    protected AbstractIterableMap<K, V> createChain(int initialSize) {
        return new ArrayMap<>(initialSize);
    }

    @Override
    public V get(Object key) {
        int hashCode = getHash(key, chains.length);
        if (chains[hashCode] == null || !chains[hashCode].containsKey(key)) {
            return null;
        }
        return chains[hashCode].get(key);
    }

    private int getHash(Object key, int chainCount) {
        if (key == null) {
            return 0;
        } else {
            return Math.abs(key.hashCode()) % chainCount;
        }

    }

    @SuppressWarnings("checkstyle:WhitespaceAround")
    @Override
    public V put(K key, V value) {
        if (!containsKey(key)) {
            currentNumberOfNodes++;
        }
        // calculate load factor
        currentLoadFactor = (double) currentNumberOfNodes / chains.length;
        // resizing logic
        if (currentLoadFactor > resizingLoadFactorThreshold) {
            AbstractIterableMap<K, V>[] newChains = createArrayOfChains(chains.length * 2);
            //currentCapacity = newChains.length;
            for (Entry<K, V> set : this) {
                int newHashCode = getHash(set.getKey(), chains.length * 2);
                int hashBucket = newHashCode % newChains.length;
                if (newChains[hashBucket] == null) {
                    newChains[hashBucket] = createChain(chainInitialCapacity);
                }
                newChains[hashBucket].put(set.getKey(), set.getValue());
            }
            chains = newChains;
        }

        int hashCode = getHash(key, chains.length);

        if (chains[hashCode] == null) {
            chains[hashCode] = createChain(chainInitialCapacity);
        }
        // not sure what to cast to
        V preValue = get(key);
        chains[hashCode].put(key, value);
        // check if this succeeds. V retVal = //chains[hashCode] = new ChainedHashMap<K, V>();
        return preValue;
    }

    @Override
    public V remove(Object key) {
        int hashCode = getHash(key, chains.length);
        if (chains[hashCode] == null || !chains[hashCode].containsKey(key)) {
            return null;
        } else {
            currentNumberOfNodes -= 1;
            return chains[hashCode].remove(key);
        }
    }

    @Override
    public void clear() {
        chains = createArrayOfChains(DEFAULT_INITIAL_CHAIN_COUNT);
        currentNumberOfNodes = 0;
        //currentCapacity = DEFAULT_INITIAL_CHAIN_CAPACITY;
    }

    @Override

    public boolean containsKey(Object key) {
        int hash = getHash(key, chains.length);
        if (chains[hash] == null) {
            return false;
        }
        return chains[hash].containsKey(key);
    }

    @Override
    public int size() {
        return this.currentNumberOfNodes;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: you won't need to change this method (unless you add more constructor parameters)
        return new ChainedHashMapIterator<>(this.chains);
    }

    /*
    See the assignment webpage for tips and restrictions on implementing this iterator.
     */
    private static class ChainedHashMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private AbstractIterableMap<K, V>[] chains;
        private int cur;
        private Iterator<Map.Entry<K, V>> iterator;
        // You may add more fields and constructor parameters

        public ChainedHashMapIterator(AbstractIterableMap<K, V>[] chains) {
            this.chains = chains;
            this.cur = 0;
            if (this.chains[cur] != null) {
                this.iterator = this.chains[cur].iterator();
            }
        }

        @Override
        public boolean hasNext() {
            for (int i = cur; i < chains.length; i++) {
                if (iterator != null) {
                    if (iterator.hasNext()) {
                        return true;
                    }
                }
                if (cur == chains.length - 1) {
                    return false;
                }
                this.cur++;
                if (chains[cur] != null) {
                    iterator = chains[cur].iterator();
                } else {
                    iterator = null;
                }
            }
            return false;
        }

        @Override
        public Map.Entry<K, V> next() {
            if (this.hasNext()) {
                return iterator.next();
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
