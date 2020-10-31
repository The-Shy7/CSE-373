package priorityqueues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @see ExtrinsicMinPQ
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 0;
    List<PriorityNode<T>> items;
    private HashMap<T, Integer> hashMap;

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        this.hashMap = new HashMap<>();
    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.
    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        // PriorityNode<T> parentNode = items.get(b);
        // items.add(b, items.get(a));
        // items.add(a, parentNode);

        // nodes at index a and index b
        PriorityNode<T> node1 = this.items.get(a);
        PriorityNode<T> node2 = this.items.get(b);
        // swap nodes
        items.set(a, node2);
        items.set(b, node1);
        // update nodes in hashmap
        hashMap.put(node1.getItem(), b);
        hashMap.put(node2.getItem(), a);
    }

    @Override
    public void add(T item, double priority) {
        if (item == null || contains(item)) {
            throw new IllegalArgumentException();
        } else {
            // add new node to list
            this.items.add(new PriorityNode<>(item, priority));
            // add new node to map
            this.hashMap.put(item, this.items.size() - 1);
            // swap with parent, until current parent is smaller
            percolateUp(this.items.size() - 1);
        }
    }

    @Override
    public boolean contains(T item) {
        return this.hashMap.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (this.items.size() > START_INDEX) {
            return this.items.get(START_INDEX).getItem();
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public T removeMin() {
        // if (size() == 0) {
        //     throw new NoSuchElementException("PQ is empty");
        // }
        // int minIndex = findIndexOfMin();
        // percolateDown();
        // return this.items.remove(minIndex).getItem();

        if (items.size() != 0) {
            T removedNode = peekMin();
            this.hashMap.remove(removedNode);

            // if there is only one element
            if (items.size() == 1) {
                this.items.remove(START_INDEX);
                return removedNode;
            } else {
                this.items.set(START_INDEX, this.items.remove(this.items.size() - 1));
                int temp = START_INDEX;
                int childIndex = START_INDEX;

                if (this.items.size() == 2) {
                    childIndex = 1;
                }

                if (this.items.size() >= 3) {
                    if (comparePriority(1, 2)) {
                        childIndex = 2;
                    } else {
                        childIndex = 1;
                    }
                }

                while (comparePriority(temp, childIndex)) {
                    swap(temp, childIndex);
                    temp = childIndex;

                    if (this.items.size() >= 2 * temp + 3) {
                        if (comparePriority(getLeft(temp), getRight(temp))) {
                            childIndex = getRight(temp);
                        } else {
                            childIndex = getLeft(temp);
                        }
                    } else if (this.items.size() == getRight(temp)) {
                        childIndex = getLeft(temp);
                    }
                }
            }

            return removedNode;
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void changePriority(T item, double priority) {
        if (contains(item)) {
            int index = this.hashMap.get(item);

            if (priority < this.items.get(index).getPriority()) {
                this.items.get(index).setPriority(priority);
                // swap with parent, until current parent is smaller
                percolateUp(index);
            } else {
                this.items.get(index).setPriority(priority);
                // swap parent with smallest child until parent is smaller than both children
                percolateDown(index);
            }
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public int size() {
        return this.items.size();
    }

    private void percolateUp(int index) {
        if (index > START_INDEX) {
            int parentIndex = getParent(index);

            while (comparePriority(parentIndex, index)) {
                swap(index, parentIndex);
                percolateUp(parentIndex);
            }
        }
    }

    private void percolateDown(int index) {
        int indexMovingDown = findIndexOfMin(index);

        while (comparePriority(index, indexMovingDown)) {
            swap(index, indexMovingDown);
            index = indexMovingDown;
            indexMovingDown = findIndexOfMin(index);
        }
    }

    private int findIndexOfMin(int index) {
        // return IntStream.range(0, this.items.size()).boxed()
        //     .min(Comparator.comparingDouble(i -> this.items.get(i).getPriority()))
        //     .orElseThrow();

        int leftIndex = getLeft(index);
        int rightIndex = getRight(index);

        // compare with priority values of left and right nodes
        if (leftIndex < this.items.size() && comparePriority(index, leftIndex)) {
            index = leftIndex;
        }

        if (rightIndex < this.items.size() && comparePriority(index, rightIndex)) {
            index = rightIndex;
        }

        return index;
    }

    private boolean comparePriority(int index1, int index2) {
        return items.get(index1).getPriority() > items.get(index2).getPriority();
    }

    // returns parent node index
    private int getParent(int currIndex) {
        return (currIndex - 1) / 2;
    }

    // returns left child node index
    private int getLeft(int currIndex) {
        return currIndex * 2 + 1;
    }

    // returns right child node index
    private int getRight(int currIndex) {
        return currIndex * 2 + 2;
    }
}
