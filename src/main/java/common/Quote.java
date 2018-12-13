package common;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Created by beku on 12/5/2018.
 */
public class Quote{
    LocalDateTime timeStamp;
    Double bidPrice;
    Double askPrice;

    public Quote(LocalDateTime timeStamp, Double bidPrice, Double askPrice) {
        this.timeStamp = timeStamp;
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
    }

    public double getMidPrice(){
        return (askPrice+bidPrice)*0.5;
    }

    public static ArrayList<Double> calcSpreadsNormalized(ArrayList<Quote[]> target,double[] vector){
        //Lazy normalization
        ArrayList<ArrayList<Quote>> unzipped=unZipQuotes(target);
        ArrayList<double[]> meanAndStds=new ArrayList<>();
        for (ArrayList<Quote> quotes : unzipped) {
            meanAndStds.add(Helpers.meanAndStd(midPrices(quotes)));
        }
        ArrayList<Double> result=new ArrayList<>();
        for (Quote[] quotes : target) {
            double spread=0;
            for (int i = 0; i < vector.length; i++) {
                spread+=vector[i]*(quotes[i].bidPrice*0.5+quotes[i].askPrice*0.5-meanAndStds.get(i)[0])/meanAndStds.get(i)[1];
            }
            result.add(spread);
        }
        return result;
    }

    public static ArrayList<Quote[]> zipQuotes(ArrayList<Quote>... target){
        ArrayList<Quote[]> result=new ArrayList<>();
        for (int i = 0; i < target[0].size(); i++) {
            Quote[] row=new Quote[target.length];
            for (int i1 = 0; i1 < row.length; i1++) {
                row[i1]=target[i1].get(i);
            }
            result.add(row);
        }
        return result;
    }

    public static ArrayList<ArrayList<Quote>> unZipQuotes(ArrayList<Quote[]> target){
        ArrayList<ArrayList<Quote>> result=new ArrayList<>();
        for (int i = 0; i < target.get(0).length; i++) {
            ArrayList<Quote> col=new ArrayList<>();
            for (int i1 = 0; i1 < target.size(); i1++) {
                col.add(target.get(i1)[i]);
            }
            result.add(col);
        }
        return result;
    }

    public static ArrayList<Double> midPrices(ArrayList<Quote> target){
        ArrayList<Double> result=new ArrayList<>();
        for (Quote quote : target) {
            result.add(quote.askPrice*0.5+quote.bidPrice*0.5);
        }
        return result;
    }
}
