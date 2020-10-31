package deques;

public class LinkedDeque<T> extends AbstractDeque<T> {
    private int size;
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    Node<T> front;
    Node<T> back;
    // Feel free to add any additional fields you may need, though.
    T sentinelObj;

    public LinkedDeque() {
        size = 0;
        // might need to test further, if it fails invariant checks
        sentinelObj = (T) new Object();
        front = new Node<>(sentinelObj);
        back = new Node<>(sentinelObj);
        front.next = back;
        back.prev = front;
    }

    /**
     * Need to test
     * Edit: passes all tests
     */
    public void addFirst(T item) {
        size += 1;
        Node<T> newNode = new Node<T>(item);
        newNode.next = front.next;
        newNode.prev = front;
        front.next.prev = newNode;
        front.next = newNode;
    }

    /**
     * Need to test
     * Edit: passes all tests
     */
    public void addLast(T item) {
        Node<T> newNode = new Node<T>(item);
        newNode.next = back;
        newNode.prev = back.prev;
        back.prev.next = newNode;
        back.prev = newNode;
        size += 1;
    }

    /**
     * Need to test
     * Edit: passes all tests
     */
    public T removeFirst() {
        if (size == 0) {
            return null;
        } else {
            T firstElement = this.front.next.value;
            front.next = front.next.next;
            front.next.prev = front;
            size -= 1;
            return firstElement;
        }
    }

    /**
     * Need to test
     * Edit: passes all tests
     */
    public T removeLast() {
        if (size == 0) {
            return null;
        } else {
            T lastElement = back.prev.value;
            back.prev = back.prev.prev;
            back.prev.next = back;
            size -= 1;
            return lastElement;
        }
    }

    public T get(int index) {
        if ((index >= size) || (index < 0)) {
            return null;
        } else {
            Node<T> current;
            if (index > size / 2 - 1) {
                current = back;
                for (int i = size - 1; i >= index; i--) {
                    current = current.prev;
                }
            } else {
                current = front;
                for (int i = 0; i <= index; i++) {
                    current = current.next;
                }
            }
            return current.value;
        }
    }

    public int size() {
        return size;
    }
}
