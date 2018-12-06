package common;

import java.util.ArrayList;

/**
 * Created by beku on 12/5/2018.
 */
public class QuoteProcessors {
    public static ArrayList<Double> calcSpreadsNormalized(ArrayList<Quote[]> target){
        return null;
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
}
