import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

import lombok.Builder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;

import org.example.BinarySearch.BinarySearch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class BinarySearchTest {
    private static final int REPETITIONS = 100;

    @Test
    @DisplayName("BinarySearch works for empty arrays.")
    void BinarySearchTest_when_EmptyArray_then_NotFound(){
        //prepare
        var array = new int[0];
        var element = 0;

        //act
        var result = BinarySearch.find(array, element);

        //assert
        assertEquals(-1, result);
    }

    @Test
    @DisplayName("BinarySearch works for one-element arrays.")
    void BinarySearchTest_when_OnlyTargetInArray_then_FoundTarget(){
        //prepare
        var element = ThreadLocalRandom.current().nextInt();
        var array = new int[]{element};

        //act
        var result = BinarySearch.find(array, element);

        //assert
        assertEquals(0, result);
    }

    @Test
    @DisplayName("BinarySearch works for same values in arrays.")
    void BinarySearchTest_when_SameElementsInArray_then_FoundAnyTarget(){
        //prepare
        var element = 4;
        var sameValues = new SortedArray.SameValues(5, element);
        var array = SortedArray.builder().sameValues(sameValues).build().createArray();

        //act
        var result = BinarySearch.find(array, element);

        //assert
        assertEquals(element, array[result]);
    }

    @RepeatedTest(REPETITIONS)
    @DisplayName("BinarySearch works for random arrays and finds element.")
    void BinarySearchTest_when_SortedArray_then_FindTarget(RepetitionInfo repetitionInfo){
        //prepare
        var array = SortedArray.builder().distinct(true).build().createArray();
        var index = randomIndexFromArray(array);
        var element = array[index];
        //act
        var result = BinarySearch.find(array, element);

        //assert
        assertTrue(result > -1);
    }

    @Test
    @DisplayName("BinarySearch works for elements that do not exist in arrays and returns -1.")
    void BinarySearchTest_when_FindNonExistingElement_then_NotFound(){
        //prepare
        var array = SortedArray.builder()
                .distinct(true)
                .upperBound(Integer.MAX_VALUE - 1)
                .build()
                .createArray();

        var element = Integer.MAX_VALUE;

        //act
        var result = BinarySearch.find(array, element);

        //assert
        assertEquals(-1, result);
    }

    @RepeatedTest(REPETITIONS)
    @DisplayName("Check binary search for random arrays and finds correct position.")
    void BinarySearchTest_when_SortedArray_then_FindCorrectPosition(RepetitionInfo repetitionInfo){
        //prepare
        var array = SortedArray.builder().distinct(true).build().createArray();
        var randIndex = randomIndexFromArray(array);
        var element = array[randIndex];
        //act
        var actualResult = BinarySearch.find(array, element);

        //assert
        assertEquals(randIndex, actualResult);
    }

    @Builder(toBuilder = true)
    static class SortedArray {
        private Integer sizeLimit;
        @Builder.Default private int upperBound = Integer.MAX_VALUE;
        @Builder.Default private boolean distinct = false;
        @Builder.Default private SameValues sameValues = new SameValues(0, 0);


        private int[] createArray() {
            //calculate size
            var size = sizeLimit == null
                    ? ThreadLocalRandom.current().nextInt(10, 100)
                    : sizeLimit - sameValues.count();

            var ints = new ArrayList<Integer>(size);

            // add rand values
            for (int i = 0; i < size; i++) {
                ints.add(ThreadLocalRandom.current().nextInt(upperBound));
            }

            //add same values
            for (int i = 0; i < sameValues.count(); i++) {
                ints.add(sameValues.value());
            }

            //sort
            Collections.sort(ints);

            var stream = ints.stream().mapToInt(i -> i);

            //make distinct array
            if (distinct) {
                return stream.distinct().toArray();
            }
            return stream.toArray();
        }

        record SameValues(
                int count,
                int value
        ){}
    }

    private int randomIndexFromArray(int[] array) {
        return ThreadLocalRandom.current().nextInt(0, array.length);
    }
}