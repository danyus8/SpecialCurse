import org.junit.jupiter.api.Test;
import org.example.BinarySearch.BinarySearch;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BinarySearchTest {

    @Test
    public void testSmallerElement() {
        int[] array = {1, 3, 5, 7, 9};
        int element = -1;
        int expectedIndex = -1;

        int result = BinarySearch.find(array, element);

        assertEquals(expectedIndex, result);
    }
    @Test
    public void testLargerElement() {
        int[] array = {1, 3, 5, 7, 9};
        int element = 11;
        int expectedIndex = -1;

        int result = BinarySearch.find(array, element);

        assertEquals(expectedIndex, result);
    }
    @Test
    public void testEmptyArray() {
        int[] array = {};
        int element = 5;
        int expectedIndex = -1;

        int result = BinarySearch.find(array, element);

        assertEquals(expectedIndex, result);
    }

    @Test
    public void testNegativeNumbers() {
        int[] array = {-9, -5, 0, 2, 7};
        int element = -5;
        int expectedIndex = 1;

        int result = BinarySearch.find(array, element);

        assertEquals(expectedIndex, result);
    }
    @Test
    public void testSuccessfulSearch() {
        int[] array = {1, 3, 5, 7, 9};
        int element = 5;
        int expectedIndex = 2;

        int result = BinarySearch.find(array, element);

        assertEquals(expectedIndex, result);
    }
    @Test
    public void testEvenNumberOfElements() {
        int[] array = {1, 2, 3, 4};
        int element = 4;
        int expectedIndex = 3;

        int result = BinarySearch.find(array, element);

        assertEquals(expectedIndex, result);
    }

    @Test
    public void testUnsuccessfulSearch() {
        int[] array = {1, 3, 5, 7, 9};
        int element = 4;
        int expectedIndex = -1;

        int result = BinarySearch.find(array, element);

        assertEquals(expectedIndex, result);
    }
    @Test
    public void testOddNumberOfElements() {
        int[] array = {1, 2, 3, 4, 5};
        int element = 1;
        int expectedIndex = 0;

        int result = BinarySearch.find(array, element);

        assertEquals(expectedIndex, result);
    }
    @Test
    public void testAllSameElements() {
        int[] array = {3, 3, 3, 3, 3};
        int element = 3;
        int expectedIndex = 2;

        int result = BinarySearch.find(array, element);

        assertEquals(expectedIndex, result);
    }

    @Test
    public void testLargeArray() {
        int[] array = new int[1000000];
        for (int i = 0; i < 1000000; i++) {
            array[i] = i;
        }
        int element = 999999;
        int expectedIndex = 999999;

        int result = BinarySearch.find(array, element);

        assertEquals(expectedIndex, result);
    }
}