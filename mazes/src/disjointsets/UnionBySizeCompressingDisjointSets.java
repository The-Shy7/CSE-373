package disjointsets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;

    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */

    private HashMap<T, Integer> hashMap;

    public UnionBySizeCompressingDisjointSets() {
        this.pointers = new ArrayList<>();
        this.hashMap = new HashMap<>();
    }

    @Override
    public void makeSet(T item) {
        if (this.hashMap.containsKey(item)) {
            throw new IllegalArgumentException("item is already contained");
        }

        this.pointers.add(-1);
        this.hashMap.put(item, this.hashMap.size());
    }

    @Override
    public int findSet(T item) {
        if (!this.hashMap.containsKey(item)) {
            throw new IllegalArgumentException("item is not contained");
        }

        List<Integer> path = new ArrayList<>();
        int id = this.hashMap.get(item);
        int end = this.pointers.get(id);

        while (end >= 0) {
            path.add(id);
            id = end;
            end = this.pointers.get(id);
        }

        for (int i = 0; i < path.size(); i++) {
            this.pointers.set(path.get(i), id);
        }

        return id;
    }

    @Override
    public boolean union(T item1, T item2) {
        if (!this.hashMap.containsKey(item1) || !this.hashMap.containsKey(item2)) {
            throw new IllegalArgumentException("item1 or item2 are not contained");
        }

        int itemId1 = findSet(item1);
        int itemId2 = findSet(item2);

        if (itemId1 == itemId2) {
            return false;
        }

        int size1 = Math.abs(this.pointers.get(itemId1));
        int size2 = Math.abs(this.pointers.get(itemId2));

        if (size1 >= size2) {
            this.pointers.set(itemId2, itemId1);
            this.pointers.set(itemId1, -(size1 + size2));
        } else {
            this.pointers.set(itemId1, itemId2);
            this.pointers.set(itemId2, -(size1 + size2));
        }

        return true;
    }
}
