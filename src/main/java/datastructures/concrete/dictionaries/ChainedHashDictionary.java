package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
// import misc.exceptions.NotYetImplementedException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @see IDictionary and the assignment page for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    private final double lambda;

    // You MUST use this field to store the contents of your dictionary.
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;
    private int size;
    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary() {
        this.lambda = 1;
        this.chains = makeArrayOfChains(50);
        size = 0;
    }
    public ChainedHashDictionary(double lambda) {
        this.lambda = lambda;
        this.chains = makeArrayOfChains(50);
        size = 0;
    }



    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int arraySize) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[arraySize];
    }

    @Override
    public V get(K key) {
        int index = testNull(key);
        if (containsKey(key)) {
            return chains[index].get(key);
        }else {
            throw new NoSuchKeyException();
        }
    }

    private int testNull(K key){
        int index = 0;
        if (key != null){
            index = Math.abs(key.hashCode()) % chains.length;
        }
        return index;
    }

    @Override
    public void put(K key, V value) {
        //load factor calculation
        //when should i use this. and not use it
        double loadFactor = this.size / chains.length;
        if (lambda < loadFactor){
            //copy the old array, rehash into new array
            IDictionary<K, V>[] resizedChains = makeArrayOfChains(chains.length * 2);
            for (int i = 0; i < chains.length; i++){
                //check if it is null, if it is not null,
                if (chains[i] != null){
                    for (KVPair<K, V> pair : chains[i]){
                        int newIndex;
                        if (pair.getKey() == null){
                            newIndex = 0;
                        }else {
                            newIndex = Math.abs(pair.getKey().hashCode()) % resizedChains.length;
                        }if (resizedChains[newIndex] == null){
                            resizedChains[newIndex] = new ArrayDictionary<>();
                        }
                        resizedChains[newIndex].put(pair.getKey(), pair.getValue());
                    }
                }
            }
            this.chains = resizedChains;
        }
        int index = testNull(key);

        if (!containsKey(key)){
            size++;
        }
        if (chains[index] == null) {
            chains[index] = new ArrayDictionary<>();
            chains[index].put(key, value);
        }else {
            chains[index].put(key, value);
        }
    }

    @Override
    public V remove(K key) {
        int index = testNull(key);

        if (containsKey(key)) {
            size--;
            return chains[index].remove(key);
        }else {
            throw new NoSuchKeyException("");
        }

    }


    @Override
    public boolean containsKey(K key) {
        int index = testNull(key);

        if (chains[index] == null){
            return false;
        } else {
            return chains[index].containsKey(key);
        }
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     *    We STRONGLY recommend you spend some time doing this before
     *    coding. Getting the invariants correct can be tricky, and
     *    running through your proposed algorithm using pencil and
     *    paper is a good way of helping you iron them out.
     *
     * 3. Think about what exactly your *invariants* are. As a
     *    reminder, an *invariant* is something that must *always* be
     *    true once the constructor is done setting up the class AND
     *    must *always* be true both before and after you call any
     *    method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int counter;
        private Iterator<KVPair<K, V>> iterator;
        private IDictionary<K, V> chainsDictionary;


        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            this.counter = 0;
            this.iterator = null;
        }

        @Override
        public boolean hasNext() {
            if (iterator == null || !iterator.hasNext()) {
                if (counter < chains.length) {
                    int newCounter = counter++;
                    chainsDictionary = chains[newCounter];
                    if (chainsDictionary != null) {
                        iterator = chainsDictionary.iterator();
                    }
                    return hasNext();
                }
                return false;
            }
            return true;
        }

        @Override
        public KVPair<K, V> next() {
            if (hasNext()) {
                return iterator.next();
            }else {
                throw new NoSuchElementException();
            }
        }
    }
}
