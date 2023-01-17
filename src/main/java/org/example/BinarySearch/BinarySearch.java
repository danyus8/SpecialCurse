package org.example.BinarySearch;

public class BinarySearch {
    public static int find(int[] array, int element) {
        var lowerBound = 0;
        var upperBound = array.length - 1;
        return BinarySearch(array, element, lowerBound, upperBound);
    }

    //iterative binary search
    private static int BinarySearch(int[] array, int element, int lowerBound, int upperBound) {
        while (lowerBound <= upperBound) {
            int middle = lowerBound + ((upperBound - lowerBound) / 2);
            if (element > array[middle]) {
                lowerBound = middle + 1;
            } else if (element < array[middle]) {
                upperBound = middle - 1;
            } else if (element == array[middle]) {
                return middle;
            }
        }

        return -1;
    }
}
