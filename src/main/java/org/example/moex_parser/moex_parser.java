package org.example.moex_parser;

public class moex_parser {

    public static void main() {
        System.out.println("---------------------");
        var dataProcessor = new DataProcessor();
        var tickersProcessor = dataProcessor.getProcessor();
        dataProcessor.showResults(tickersProcessor);
        System.out.println("Program finished successfully");
    }

}
