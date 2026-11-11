# 📅 Module 02: Lesson Scheduling & FIFO Queue Management
> **Coursework:** SLIIT – Object-Oriented Programming (OOP)  
> **Student Name:** Thanneliyadda  
> **Student ID:** `IT26101056`  
> **Allocated Role:** Lesson Scheduling & Data Structures Lead  
> **Git Feature Branch:** `lesson-IT26101056`  

---

## 📌 1. Module Overview
The **Lesson Scheduling Module** handles practical driving lesson bookings between enrolled students, certified instructors, and available training vehicles. To eliminate scheduling collisions and ensure fair first-come, first-served slot allocations, it integrates a **Custom FIFO Queue** data structure.

---

## 📂 2. Key Files Contributed
| File Path | Component Type | Responsibility |
|:---|:---|:---|
| `src/main/java/lk/drivingschool/model/Lesson.java` | Domain Entity | Encapsulates lesson details, timestamps, notes, and statuses. |
| `src/main/java/lk/drivingschool/datastructure/LessonQueue.java` | Custom DSA (Queue) | Custom FIFO Queue implementation (`enqueue`, `dequeue`, `peek`). |
| `src/main/java/lk/drivingschool/servlet/LessonServlet.java` | Controller | Handles lesson requests, queue dispatching, and slot confirmation. |
| `src/main/webapp/WEB-INF/views/lessons.jsp` | View Layer (JSP) | Booking modal, queue visualizer, status indicators. |
| `data/lessons.txt` | File Database | Persistent storage for scheduled and queued driving lessons. |

---

## 💡 3. Data Structure Implementation (Custom FIFO Queue)

### Why a Queue?
Lesson bookings arrive asynchronously. A **First-In-First-Out (FIFO)** Queue guarantees fair allocation: the student who booked first is granted slot priority.

### Code Implementation (`LessonQueue.java`):
```java
public class LessonQueue {
    private List<Lesson> queue;

    public LessonQueue() {
        this.queue = new LinkedList<>();
    }

    // Add lesson request to the back of the queue - O(1)
    public void enqueue(Lesson lesson) {
        if (lesson != null) {
            queue.add(lesson);
        }
    }

    // Process and remove next lesson from the front - O(1)
    public Lesson dequeue() {
        if (!isEmpty()) {
            return queue.remove(0);
        }
        return null;
    }

    // View the next lesson in line without removal - O(1)
    public Lesson peek() {
        return isEmpty() ? null : queue.get(0);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }
}
```

### Queue Lifecycles:
* `PENDING_QUEUE`: Lesson request submitted and waiting in queue.
* `CONFIRMED`: Dispatched and assigned an instructor.
* `COMPLETED`: Practical driving session completed.
* `CANCELLED`: Booking voided.

---

## 🧪 4. Testing & Viva Walkthrough
1. **Navigate to:** `http://localhost:8080/DrivingSchoolSystem/lessons`
2. **Book a Lesson:** Click **"Book New Lesson"**:
   * Select a Student (e.g. `STU-1001`) and Instructor (e.g. `INS-2001`).
   * Select Date: `2026-11-20`, Time: `10:00 AM`.
   * Submit -> The lesson appears with status badge **"In FIFO Queue"**.
3. **Dispatch from Queue:** Click **"Confirm Next in Queue"** to demonstrate FIFO dequeuing.

---

## 🎯 5. Expected Viva Questions & Answers
* **Q1: Why did you choose a FIFO Queue for lesson scheduling?**  
  * *Answer:* To ensure fairness in slot allocation. The first student to request a lesson slot is served first (FIFO principle), preventing scheduling bias or double-booking.
* **Q2: What is the time complexity of your enqueue and dequeue operations?**  
  * *Answer:* Both operations operate with $O(1)$ constant time complexity when using linked node structures.
