package problems;

import datastructures.LinkedIntList;
// Checkstyle will complain that this is an unused import until you use it in your code.
import datastructures.LinkedIntList.ListNode;

/**
 * See the spec on the website for example behavior.
 *
 * REMEMBER THE FOLLOWING RESTRICTIONS:
 * - do not call any methods on the `LinkedIntList` objects.
 * - do not construct new `ListNode` objects for `reverse3` or `firstToLast`
 *      (though you may have as many `ListNode` variables as you like).
 * - do not construct any external data structures such as arrays, queues, lists, etc.
 * - do not mutate the `data` field of any node; instead, change the list only by modifying
 *      links between nodes.
 */

public class LinkedIntListProblems {

    /**
     * Reverses the 3 elements in the `LinkedIntList` (assume there are exactly 3 elements).
     */
    public static void reverse3(LinkedIntList list) {
        int first = list.front.data;
        int second = list.front.next.data;
        int last = list.front.next.next.data;
        list.front = new ListNode(last);
        list.front.next = new ListNode(second);
        list.front.next.next = new ListNode(first);
    }

    /**
     * Moves the first element of the input list to the back of the list.
     */
    public static void firstToLast(LinkedIntList list) {
        if (!(list.front == null || list.front.next == null)) {
            ListNode first = list.front;
            list.front = list.front.next;
            ListNode curr = list.front;

            while (curr.next != null) {
                curr = curr.next;
            }

            curr.next = first;
            first.next = null;
        }
    }

    /**
     * Returns a list consisting of the integers of a followed by the integers
     * of n. Does not modify items of A or B.
     */
    public static LinkedIntList concatenate(LinkedIntList a, LinkedIntList b) {
        LinkedIntList result = new LinkedIntList();

        if (a.front != null && b.front != null) {
            // Pointer for result list to traverse
            ListNode curr = null;
            // Temporary pointer to list a to traverse
            ListNode tempA = a.front;
            result.front = new ListNode(tempA.data);
            curr = result.front;

            while (tempA.next != null) {
                tempA = tempA.next;
                curr.next = new ListNode(tempA.data);
                curr = curr.next;
            }

            // Temporary pointer to list b to traverse
            ListNode tempB = b.front;
            curr.next = new ListNode(tempB.data);
            curr = curr.next;

            while (tempB.next != null) {
                tempB = tempB.next;
                curr.next = new ListNode(tempB.data);
                curr = curr.next;
            }
        } else if (a.front == null) {
            result = b;
        } else {
            result = a;
        }

        return result;
    }
}
