package org.example;
import java.io.*;
import java.util.*;

public class DataProcessor {
    private static final List<String> supportedMarkets = List.of("TQBR", "FQBR");
    private static final String fileName = "/home/daniil/IdeaProjects/SpecialCurse/src/main/resources/trades.txt";

    public TickersMap getProcessor() {
        // Open the file
        FileInputStream stream;
        try {
            stream = new FileInputStream(fileName);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        //Read File Line By Line and process it:
        TickersMap tickersProcessor = new TickersMap();
        br.lines().skip(1)
                .forEach(tickersProcessor::put);

        //Close the input stream
        try {
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return tickersProcessor;
    }

    public void showResults(TickersMap tickersProcessor){
        // Show results
        System.out.println("Best tickers are: ");
        tickersProcessor.showBest(10);
        System.out.println("Worst tickers are: ");
        tickersProcessor.showWorst(10);
//        tickersProcessor.showAll();
    }
    //TODO: Вынести в отдельный класс ТикерсМэп
    private static class TickersMap {
        HashMap<String, tickerInfo> tickersMap = new HashMap<>();

        public void put(String fileLine) {
            tickerInfo buff = new tickerInfo();
            buff.ParseFileLine(fileLine);
            String lineTicker = buff.ticker;
            String lineMarket = buff.market;
            if (!supportedMarkets.contains(lineMarket)) {
                return;
            }
            tickerInfo ticketInstance;
            if (!tickersMap.containsKey(lineTicker)) {
                ticketInstance = new tickerInfo();
            } else {
                ticketInstance = tickersMap.get(lineTicker);
            }
            ticketInstance.ParseFileLine(fileLine);
            tickersMap.put(ticketInstance.ticker, ticketInstance);
        }

        public void showBest(int numberBest) {
            List<tickerInfo> values = new ArrayList<>(tickersMap.values());
            Collections.sort(values);
            Collections.reverse(values);
            for (int i = 0; i < Integer.min(numberBest, values.size()); i++) {
                System.out.println(values.get(i));
            }
        }

        public void showWorst(int numberBest) {
            List<tickerInfo> values = new ArrayList<>(tickersMap.values());
            Collections.sort(values);
            for (int i = 0; i < Integer.min(numberBest, values.size()); i++) {
                System.out.println(values.get(i));
            }
        }

        public void showAll() {
            List<tickerInfo> values = new ArrayList<>(tickersMap.values());
            for (tickerInfo value : values) {
                System.out.println(value);
            }
        }
    }
}