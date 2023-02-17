import org.example.QuickSort.QuickSort;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class QuickSortTest {

    @Test
    public void testSort_emptyArray() {
        Integer[] arr = {};
        QuickSort qs = new QuickSort(true, true);
        qs.sort(arr);
        assertTrue(Arrays.equals(new Integer[]{}, arr));
    }

    @Test
    public void testSort_oneElementArray() {
        Integer[] arr = {5};
        QuickSort qs = new QuickSort(true, true);
        qs.sort(arr);
        assertTrue(Arrays.equals(new Integer[]{5}, arr));
    }
    @Test
    public void testSort_multipleElementArray() {
        Integer[] arr = {5, 1, 4, 2, 8};
        QuickSort qs = new QuickSort(true, true);
        qs.sort(arr);
        assertTrue(Arrays.equals(new Integer[]{1, 2, 4, 5, 8}, arr));
    }
    @Test
    public void testSort_duplicateElementsArray() {
        Integer[] arr = {5, 1, 4, 2, 8, 1, 5};
        QuickSort qs = new QuickSort(true, true);
        qs.sort(arr);
        assertTrue(Arrays.equals(new Integer[]{1, 1, 2, 4, 5, 5, 8}, arr));
    }
    @Test
    public void testSort_stringArray() {
        String[] arr = {"banana", "apple", "orange", "pear"};
        QuickSort qs = new QuickSort(true, true);
        qs.sort(arr);
        assertTrue(Arrays.equals(new String[]{"apple", "banana", "orange", "pear"}, arr));
    }
    @Test
    public void testSort_useMedianOfThree() {
        Integer[] arr = {5, 1, 4, 2, 8};
        QuickSort qs = new QuickSort(true, true);
        qs.sort(arr);
        assertTrue(Arrays.equals(new Integer[]{1, 2, 4, 5, 8}, arr));
    }
    @Test
    public void testSort_useOneRecursiveCall() {
        Integer[] arr = {5, 1, 4, 2, 8};
        QuickSort qs = new QuickSort(false, true);
        qs.sort(arr);
        assertTrue(Arrays.equals(new Integer[]{1, 2, 4, 5, 8}, arr));
    }

    @Test
    public void testSortEmptyArray() {
        QuickSort quickSort = new QuickSort(false, false);
        Integer[] arr = new Integer[]{};
        quickSort.sort(arr);
        assertArrayEquals(new Integer[]{}, arr);
    }

    @Test
    public void testSortAlreadySortedArray() {
        QuickSort quickSort = new QuickSort(false, false);
        Integer[] arr = new Integer[]{1, 2, 3, 4, 5};
        quickSort.sort(arr);
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    public void testSortReverseSortedArray() {
        QuickSort quickSort = new QuickSort(false, false);
        Integer[] arr = new Integer[]{5, 4, 3, 2, 1};
        quickSort.sort(arr);
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    public void testSortRandomArray() {
        QuickSort quickSort = new QuickSort(false, false);
        Integer[] arr = new Integer[]{5, 1, 3, 2, 4};
        quickSort.sort(arr);
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    public void testSortArrayWithDuplicates() {
        QuickSort quickSort = new QuickSort(false, false);
        Integer[] arr = new Integer[]{5, 1, 3, 2, 4, 3, 5};
        quickSort.sort(arr);
        assertArrayEquals(new Integer[]{1, 2, 3, 3, 4, 5, 5}, arr);
    }

    @Test
    public void testSortWithComparator() {
        QuickSort quickSort = new QuickSort(false, false);
        Integer[] arr = new Integer[]{5, 1, 3, 2, 4};
        Comparator<Integer> reverseComparator = Comparator.reverseOrder();
        quickSort.sort(arr, reverseComparator);
        assertArrayEquals(new Integer[]{5, 4, 3, 2, 1}, arr);
    }

    @Test
    public void testSortWithMedianOfThree() {
        QuickSort quickSort = new QuickSort(true, false);
        Integer[] arr = new Integer[]{5, 1, 3, 2, 4};
        quickSort.sort(arr);
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    public void testSortWithOneRecursiveCall() {
        QuickSort quickSort = new QuickSort(false, true);
        Integer[] arr = new Integer[]{5, 1, 3, 2, 4};
        quickSort.sort(arr);
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5}, arr);
    }
}