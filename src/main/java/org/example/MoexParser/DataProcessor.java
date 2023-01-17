package org.example.MoexParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DataProcessor {
    private static final List<String> supportedMarkets = List.of("TQBR", "FQBR");

    public TickersMap getTickersMap(String fileName) throws FileNotFoundException {
        // Open the file
        FileInputStream stream;
        try {
            stream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File not found");
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

    public void showResults(TickersMap tickersProcessor) {
        // Show results
        System.out.println("Best tickers are: ");
        tickersProcessor.showBest(10);
        System.out.println("Worst tickers are: ");
        tickersProcessor.showWorst(10);
//        tickersProcessor.showAll();
    }

    //TODO: Вынести в отдельный класс ТикерсМэп
    public static class TickersMap {
        HashMap<String, TickerInfo> tickersMap = new HashMap<>();

        public void put(String fileLine) {
            TickerInfo buff = new TickerInfo();
            buff.parseFileLine(fileLine);
            String lineTicker = buff.ticker;
            String lineMarket = buff.market;
            if (!supportedMarkets.contains(lineMarket)) {
                return;
            }
            TickerInfo ticketInstance;
            if (!tickersMap.containsKey(lineTicker)) {
                ticketInstance = new TickerInfo();
            } else {
                ticketInstance = tickersMap.get(lineTicker);
            }
            ticketInstance.parseFileLine(fileLine);
            tickersMap.put(ticketInstance.ticker, ticketInstance);
        }

        public void showBest(int numberBest) {
            List<TickerInfo> values = new ArrayList<>(tickersMap.values());
            Collections.sort(values);
            Collections.reverse(values);
            for (int i = 0; i < Integer.min(numberBest, values.size()); i++) {
                System.out.println(values.get(i));
            }
        }

        public void showWorst(int numberBest) {
            List<TickerInfo> values = new ArrayList<>(tickersMap.values());
            Collections.sort(values);
            for (int i = 0; i < Integer.min(numberBest, values.size()); i++) {
                System.out.println(values.get(i));
            }
        }
    }
}
