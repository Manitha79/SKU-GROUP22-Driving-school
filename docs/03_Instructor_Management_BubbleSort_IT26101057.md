# 👨‍🏫 Module 03: Instructor Management & Bubble Sort Ranking
> **Coursework:** SLIIT – Object-Oriented Programming (OOP)  
> **Student Name:** Kumarawansha W.K.R.D  
> **Student ID:** `IT26101057`  
> **Allocated Role:** Instructor Management & Sorting Algorithm Lead  
> **Git Feature Branch:** `feature/instructor-management`  

---

## 📌 1. Module Overview
The **Instructor Management Module** administers driving school certified instructors, their specialized vehicle classes (Manual Car, Auto Car, Heavy Vehicle, Motorcycle), and years of active training experience. It incorporates a **Custom Bubble Sort Algorithm** to dynamically rank instructors based on experience for optimal student matching.

---

## 📂 2. Key Files Contributed
| File Path | Component Type | Responsibility |
|:---|:---|:---|
| `src/main/java/lk/drivingschool/model/Instructor.java` | Subclass Model | Extends `Person`, adding `licenseCategory`, `experienceYears`, `status`. |
| `src/main/java/lk/drivingschool/datastructure/BubbleSort.java` | Custom Algorithm | Bubble Sort implementation with early-exit optimization flag. |
| `src/main/java/lk/drivingschool/servlet/InstructorServlet.java` | Controller | CRUD operations, sorting triggers (`desc`/`asc`), and JSP forwarding. |
| `src/main/webapp/WEB-INF/views/instructors.jsp` | View Layer (JSP) | Instructor roster, sort toggles, viva concept badges. |
| `data/instructors.txt` | File Database | Persistent flat-file storage for instructor records. |

---

## 💡 3. Algorithm Implementation (Optimized Bubble Sort)

### Algorithm Mechanics (`BubbleSort.java`):
Bubble Sort steps through the list, compares adjacent instructor elements, and swaps them if their `experienceYears` are in the wrong order.

```java
public class BubbleSort {

    /**
     * Sorts instructors by experience descending (Highest experience first).
     */
    public static void sortByExperienceDescending(List<Instructor> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false; // Optimization flag
            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j).getExperienceYears() < list.get(j + 1).getExperienceYears()) {
                    Collections.swap(list, j, j + 1);
                    swapped = true;
                }
            }
            // If no elements were swapped in this pass, list is already sorted!
            if (!swapped) {
                break;
            }
        }
    }
}
```

### Time Complexity Analysis:
* **Worst Case:** $O(n^2)$ – When elements are in reverse order.
* **Average Case:** $O(n^2)$
* **Best Case:** $O(n)$ – Thanks to the `swapped` boolean flag, if the input is already sorted, it terminates after a single pass.
* **Space Complexity:** $O(1)$ – In-place sorting requiring no auxiliary data allocation.

---

## 💡 4. OOP Implementation (Inheritance & Polymorphism)
`Instructor` inherits from `Person.java`:
```java
public class Instructor extends Person {
    private String licenseCategory;
    private int experienceYears;
    private String status;

    public Instructor(String id, String name, String phone, 
                      String licenseCategory, int experienceYears, 
                      String email, String status) {
        super(id, name, email, phone);
        this.licenseCategory = licenseCategory;
        this.experienceYears = experienceYears;
        this.status = status;
    }

    @Override
    public String getRole() {
        return "INSTRUCTOR";
    }
}
```

---

## 🧪 5. Testing & Viva Walkthrough
1. **Navigate to:** `http://localhost:8080/DrivingSchoolSystem/instructors`
2. **Observe Table:** Check initial instructor list.
3. **Trigger Sorting:** Click **"Sort: Experience (Highest First)"** -> Notice instructors with 15 and 12 years of experience float to the top.
4. Click **"Sort: Experience (Lowest First)"** -> Reverses ranking using Bubble Sort.

---

## 🎯 6. Expected Viva Questions & Answers
* **Q1: How did you optimize Bubble Sort?**  
  * *Answer:* By adding a `swapped` boolean flag. If an entire pass completes without a single swap, the list is already sorted, and the algorithm breaks immediately, reducing best-case time from $O(n^2)$ to $O(n)$.
* **Q2: Why use Inheritance for Instructor?**  
  * *Answer:* Both `Student` and `Instructor` share common person attributes (`name`, `email`, `phone`). Putting them in `Person` follows the DRY (Don't Repeat Yourself) principle.
