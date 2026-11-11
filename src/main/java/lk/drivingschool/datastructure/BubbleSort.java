package lk.drivingschool.datastructure;

import lk.drivingschool.model.Instructor;
import java.util.Collections;
import java.util.List;

/**
 * Classic Bubble Sort Algorithm for Instructor Ranking.
 * Member 3: Kumarawansha W.K.R.D
 * 
 * Algorithm Concept:
 * - Compares adjacent elements in the list.
 * - Swaps them if they are out of order.
 * - Time Complexity: O(n^2) worst/average case; O(n) best case with boolean flag.
 */
public class BubbleSort {

    /**
     * Sorts instructors by experience descending (Highest experience first).
     */
    public static void sortByExperienceDescending(List<Instructor> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                // Descending: If left has LESS experience than right, swap
                if (list.get(j).getExperienceYears() < list.get(j + 1).getExperienceYears()) {
                    Collections.swap(list, j, j + 1);
                    swapped = true;
                }
            }
            // If no elements were swapped in inner loop, list is already sorted
            if (!swapped) {
                break;
            }
        }
    }

    /**
     * Sorts instructors by experience ascending (Lowest experience first).
     */
    public static void sortByExperienceAscending(List<Instructor> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                // Ascending: If left has MORE experience than right, swap
                if (list.get(j).getExperienceYears() > list.get(j + 1).getExperienceYears()) {
                    Collections.swap(list, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
    }
}
