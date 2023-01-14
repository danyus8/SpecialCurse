package org.example.moex_parser;

import org.jetbrains.annotations.NotNull;

class tickerInfo implements Comparable<tickerInfo> {
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
        double date = Double.parseDouble(data[1]);
        this.market = data[2];
        this.ticker = data[3];
        double price = Double.parseDouble(data[4]);
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

    public double getIncrement() {
        return lastPrice / firstPrice - 1.;
    }

    @Override
    public int compareTo(@NotNull tickerInfo otherTicker) {
        return Double.compare(getIncrement(), otherTicker.getIncrement());
    }

    @Override
    public String toString() {
        return String.format("Ticker: %s: %n   " +
                        "IntraDay Increment%%: %.2f%n   " +
                        "Turnover: %.2f%n   " +
                        "firstPrice: %.2f%n   " +
                        "lastPrice: %.2f%n" +
                        "------------------------------------------------"
                , ticker, (lastPrice / firstPrice - 1) * 100., tradeAmount, firstPrice, lastPrice);
    }
}
