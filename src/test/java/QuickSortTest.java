import org.example.QuickSort.QuickSort;

import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.math3.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class QuickSortTest {
    private static final int NUMBER_OF_REPETITIONS = 500;
    private Integer[] generateIntArray(int size) {
        var array = new Integer[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = ThreadLocalRandom.current().nextInt();
        }
        return array;
    }

    @TestTemplate
    @ExtendWith(QuickSortTestTemplate.class)
    void QuickSort_when_randomArray_then_correctSort(final QuickSort quickSort) {
        // prepare
        var array = generateIntArray(1000);
        var expectedMap = createCounterMap(array);

        //act
        quickSort.sort(array);

        //assert
        IsSorted(array);
        arrayIsNotChanged(array, expectedMap);
    }

    @ParameterizedTest
    @MethodSource("provideArraysForMedianOfThree")
    void QuickSort_test_medianOfThree(List<Integer> list) {
        var permutations = Collections2.permutations(list);
        Comparator<Integer> comparator = Integer::compareTo;
        permutations.stream()
                .map(permutation -> permutation.toArray(new Integer[0]))
                .forEach(array -> {
                    QuickSort.medianOfThree(array, 0, 1, 2, comparator);
                    IsSorted(array);
                });
    }

    private static Stream<Arguments> provideArraysForMedianOfThree() {
        return Stream.of(
                Arguments.of(List.of(1, 2, 3)),
                Arguments.of(List.of(1, 2, 2)),
                Arguments.of(List.of(1, 1, 2)),
                Arguments.of(List.of(1, 1, 1))
        );
    }


    private Map<Object, Integer> createCounterMap(Object[] array) {
        var arrayMap = new HashMap<Object, Integer>();
        for (var el : array) {
            arrayMap.put(el, arrayMap.getOrDefault(el, 0) + 1);
        }
        return arrayMap;
    }

    private <T extends Comparable<T>> void IsSorted(T[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            Assertions.assertTrue(array[i].compareTo(array[i + 1]) <= 0);
        }
    }

    private void arrayIsNotChanged(Object[] array, Map<Object, Integer> expectedMap) {
        var arrayMap = createCounterMap(array);
        var diff = Maps.difference(arrayMap, expectedMap);
        Assertions.assertTrue(diff.areEqual());
    }


    static class QuickSortTestTemplate implements TestTemplateInvocationContextProvider {
        private static final int NUMBER_OF_REPETITIONS = 500;
        List<QuickSort> quickSortMethods = generateQuickSortMethods();

        @Override
        public boolean supportsTestTemplate(ExtensionContext context) {
            return true;
        }

        @Override
        public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
            return quickSortMethods.stream()
                    .flatMap(quickSort -> IntStream.range(1, NUMBER_OF_REPETITIONS + 1).mapToObj(i -> new Pair<>(i, quickSort)))
                    .map(pair -> invocationContext(pair.getFirst(), pair.getSecond()));
        }

        private List<QuickSort> generateQuickSortMethods() {
            var booleanSet = Set.of(false, true);
            var combinations =
                    Sets.cartesianProduct(booleanSet, booleanSet);

            var quickSortMethods = combinations.stream()
                    .map(variant -> new QuickSort(variant.get(0), variant.get(1)));

            return quickSortMethods.toList();
        }

        private TestTemplateInvocationContext invocationContext(int repeatNumber, QuickSort quickSort) {
            return new TestTemplateInvocationContext() {
                @Override
                public String getDisplayName(int invocationIndex) {
                    return repeatNumber + ": " + quickSort.toString();
                }

                @Override
                public List<Extension> getAdditionalExtensions() {
                    return Collections.singletonList(new ParameterResolver() {
                        @Override
                        public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
                            return parameterContext.getParameter().getType().equals(QuickSort.class);
                        }

                        @Override
                        public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
                            return quickSort;
                        }
                    });
                }
            };
        }
    }
}