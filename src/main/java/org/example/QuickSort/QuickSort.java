package org.example.QuickSort;

import com.google.common.annotations.VisibleForTesting;
import lombok.AllArgsConstructor;

import java.util.Comparator;

@AllArgsConstructor
public class QuickSort {
    private final boolean useMedianOfThree;
    private final boolean useOneRecursiveCall;

    public <T extends Comparable<T>> void sort(T[] array) {
        sort(array, Comparable::compareTo);
    }

    public <T> void sort(T[] array, Comparator<T> cmp) {
        if (useOneRecursiveCall) {
            applyQuickSortWithOneRecursiveCall(array, 0, array.length - 1, cmp);
        } else {
            applyQuickSort(array, 0, array.length - 1, cmp);
        }
    }

    private <T> void applyQuickSort(T[] array, int start, int end, Comparator<T> cmp) {
        if (start < end) {
            int partitionIndex = getPartitionIndex(array, start, end, cmp);
            applyQuickSort(array, start, partitionIndex - 1, cmp);
            applyQuickSort(array, partitionIndex + 1, end, cmp);
        }
    }


    private <T> void applyQuickSortWithOneRecursiveCall(T[] array, int start, int end, Comparator<T> cmp) {
        while (start < end) {
            var partitionIndex = getPartitionIndex(array, start, end, cmp);

            if (partitionIndex - start > end - partitionIndex) {
                applyQuickSortWithOneRecursiveCall(array, partitionIndex + 1, end, cmp);
                end = partitionIndex- 1;
            } else {
                applyQuickSortWithOneRecursiveCall(array, start, partitionIndex - 1, cmp);
                start = partitionIndex + 1;
            }
        }
    }

    private <T> Integer getPartitionIndex(T[] array, int start, int end, Comparator<T> cmp) {
        if (useMedianOfThree) {
            var middle = start + (end - start) / 2;
            medianOfThree(array, start, middle, end, cmp);
            swap(array, middle, end - 1);
        }
        return partition(array, start, end, cmp);
    }

    private <T> Integer partition(T[] array, int start, int end, Comparator<T> cmp) {
        var cursor = start;
        var pivot = array[end];
        for (int i = start; i < end + 1; i++) {
            if (cmp.compare(array[i], pivot) <= 0) {
                swap(array, i, cursor);
                cursor++;
            }
        }

        return cursor - 1;
    }

    // find median of the first, middle and last element and sort them in array
    @VisibleForTesting
    public static <T> void medianOfThree(T[] array, int start, int middle, int end, Comparator<T> cmp) {
        if (cmp.compare(array[start], array[middle]) < 0) {
            if (cmp.compare(array[middle], array[end]) >= 0) {
                if (cmp.compare(array[start], array[end]) < 0) {
                    swap(array, middle, end);
                } else {
                    swap(array, start, middle, end);
                }
            }
        } else {
            if (cmp.compare(array[middle], array[end]) < 0) {
                if (cmp.compare(array[start], array[end]) < 0) {
                    swap(array, start, middle);
                } else {
                    swap(array, middle, start, end);
                }
            } else {
                swap(array, start, end);
            }
        }
    }

    private static void swap(Object[] array, int i, int j) {
        var tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    // ->
    private static void swap(Object[] array, int i, int j, int k) {
        var tmp = array[j];
        array[j] = array[i];
        array[i] = array[k];
        array[k] = tmp;
    }

    @VisibleForTesting
    static void swap(Object[] array, int center, int start, int startOccurrences, int end, int endOccurrences) {
        var leftCenterShift = center - start - startOccurrences;
        if (startOccurrences > 0 && leftCenterShift > 0) {
            System.arraycopy(array, center - leftCenterShift, array, start, leftCenterShift);
            for (int i = 0; i < startOccurrences; i++) {
                array[center - 1 - i] = array[center];
            }
        }

        var leftEndShift = end - center - endOccurrences;
        if (endOccurrences > 0 && leftEndShift > 0) {
            System.arraycopy(array, center + 1, array, end - leftEndShift + 1, leftEndShift);
            for (int i = 0; i < endOccurrences; i++) {
                array[center + 1 + i] = array[center];
            }
        }
    }


    record IntTuple(
            int _1,
            int _2
    ) {
        int left() {
            return _1;
        }

        int right() {
            return _2;
        }
    }
}