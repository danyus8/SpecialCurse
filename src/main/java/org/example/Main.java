package org.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("---------------------");
        var dataProcessor = new DataProcessor();
        var tickersProcessor = dataProcessor.getProcessor();
        dataProcessor.showResults(tickersProcessor);
        System.out.println("Program finished successfully");
    }
}
