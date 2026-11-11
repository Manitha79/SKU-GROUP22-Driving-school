package lk.drivingschool.datastructure;

import lk.drivingschool.model.Lesson;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Custom FIFO (First-In, First-Out) Queue for Lesson Requests.
 * Member 2: Thanneliyadda T.L.K.S
 * 
 * Queue Principles:
 * - enqueue(): Adds a lesson to the back of the line.
 * - dequeue(): Removes and returns the lesson from the front of the line.
 * - peek(): Looks at the next lesson without removing it.
 */
public class LessonQueue {
    private LinkedList<Lesson> queue = new LinkedList<>();

    // Add to the end of the queue
    public void enqueue(Lesson lesson) {
        queue.addLast(lesson);
    }

    // Remove from the front of the queue
    public Lesson dequeue() {
        if (isEmpty()) {
            return null;
        }
        return queue.removeFirst();
    }

    // View the front item without removing it
    public Lesson peek() {
        if (isEmpty()) {
            return null;
        }
        return queue.getFirst();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }

    public List<Lesson> getAll() {
        return new ArrayList<>(queue);
    }
}
