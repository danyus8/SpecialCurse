package org.example;
import org.jetbrains.annotations.NotNull;
import java.io.*;
import java.util.*;

public class DataProcessor {
    private static final List<String> supportedMarkets = List.of("TQBR", "FQBR");
    private static final String fileName = "/home/daniil/IdeaProjects/SpecialCurse/src/main/resources/trades.txt";

    public void ReadFileByLine() {
        // Open the file
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream(fileName);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        //Read File Line By Line and process it:
        TickersMap tickersProcessor = new TickersMap();
        br.lines().skip(1)
                .forEach(tickersProcessor::put);

        // Show results
        System.out.println("Best tickers are: ");
        tickersProcessor.showBest(10);
        System.out.println("Worst tickers are: ");
        tickersProcessor.showWorst(10);
//        tickersProcessor.showAll();

        //Close the input stream
        try {
            fstream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static class tickerInfo implements Comparable <tickerInfo> {
        String ticker;
        String market;
        Double minPrice;
        Double maxPrice;
        Double firstPrice;
        Double firstDate;
        Double lastPrice;
        Double lastDate;
        Double tradeAmount;

        public void ParseFileLine(@NotNull String line) {
            var data = line.split("\t");
//            int tradeNumber = Integer.parseInt(data[0]);
            Double date = Double.parseDouble(data[1]);
            this.market = data[2];
            this.ticker = data[3];
            Double price = Double.parseDouble(data[4]);
//            Double volume = Double.parseDouble(data[5]);
//            Double accruedInt = Double.parseDouble(data[6]);
//            Double yield = Double.parseDouble(data[7]);

            // Increase amount on reading if it is not null
            tradeAmount = tradeAmount == null ? Double.parseDouble(data[8]) : tradeAmount + Double.parseDouble(data[8]);
            // Update minPrice
            if (minPrice == null) {
                minPrice = price;
            } else {
                if (price < minPrice) {
                    minPrice = price;
                }
            }
            // Update maxPrice
            if (maxPrice == null) {
                maxPrice = price;
            } else {
                if (price > maxPrice) {
                    maxPrice = price;
                }
            }
            // Update firstPrice
            if (firstDate == null) {
                firstDate = date;
                firstPrice = price;
            } else {
                if (date < firstDate) {
                    firstDate = date;
                    firstPrice = price;
                }
            }
            // Update lastPrice
            if (lastDate == null) {
                lastDate = date;
                lastPrice = price;
            } else {
                if (date > lastDate) {
                    lastDate = date;
                    lastPrice = price;
                }
            }
        }

        public double getIncrement(){
            return lastPrice/firstPrice - 1.;
        }
        @Override
        public int compareTo(@NotNull DataProcessor.tickerInfo otherTicker) {
            return Double.compare(getIncrement(), otherTicker.getIncrement());
        }

        @Override
        public String toString() {
            String result = String.format("Ticker: %s: %n   " +
                    "IntraDay Increment%%: %.2f%n   " +
                    "Turnover: %.2f%n   " +
                    "firstPrice: %.2f%n   " +
                    "lastPrice: %.2f%n" +
                    "------------------------------------------------"
                    , ticker, (lastPrice/firstPrice - 1)*100., tradeAmount, firstPrice, lastPrice);
            return result;
        }
    }


    private class TickersMap {
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
            for (int i=0; i < Integer.min(numberBest, values.size()); i++){
                System.out.println(values.get(i));
            }
        }
        public void showWorst(int numberBest) {
            List<tickerInfo> values = new ArrayList<>(tickersMap.values());
            Collections.sort(values);
            for (int i=0; i < Integer.min(numberBest, values.size()); i++){
                System.out.println(values.get(i));
            }
        }
        public void showAll() {
            List<tickerInfo> values = new ArrayList<>(tickersMap.values());
            for (int i=0; i < values.size(); i++){
                System.out.println(values.get(i));
            }
        }
    }
}