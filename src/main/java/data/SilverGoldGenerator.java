package data;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.factory.Nd4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;

import common.Quote;
import common.QuoteProcessors;

/**
 * Created by beku on 11/13/2018.
 */
public class SilverGoldGenerator implements DataGenerator {

    DataSet dataSet;
    int trainSize;
    boolean regression;

    public SilverGoldGenerator(double trainPercent,int sequenceLength, int shift_amount, boolean regression){
        ArrayList<Quote> silver=new ArrayList<>();
        ArrayList<Quote> gold=new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("SilverGold.csv"));
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] elems = line.split(";");
                if(elems.length==2){
                    LocalDateTime timeStamp=LocalDateTime.parse(elems[0]);
                    silver.add(new Quote(timeStamp,Double.valueOf(elems[2]),Double.valueOf(elems[1])));
                    gold.add(new Quote(timeStamp,Double.valueOf(elems[4]),Double.valueOf(elems[3])));
                }
            }
            reader.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        double[] spread= QuoteProcessors.calcSpreadsNormalized(silver,gold);
        int numOfSamples=spread.length-sequenceLength-shift_amount;
        trainSize=(int)(trainPercent*numOfSamples);

        if(regression){
            //hehe no idea
        } else {
            double[][][] data=new double[numOfSamples][1][sequenceLength];
            double[][][] dataShifted=new double[numOfSamples][2][sequenceLength];
            for (int i = 0; i < numOfSamples; i++) {
                for (int i1 = 0; i1 < sequenceLength; i1++) {
                    data[i][0][i1]=spread[i+i1];
                    int idx=0;
                    if((spread[i+i1+shift_amount]-spread[i+i1])>0){idx=1;}
                    dataShifted[i][idx][i1]=1;
                }
            }
            dataSet=new org.nd4j.linalg.dataset.DataSet(Nd4j.create(data),Nd4j.create(dataShifted));
        }
    }

    @Override
    public DataSet getTrainDataSet() {
        return dataSet.getRange(0,(int)(trainSize*0.8));
    }

    @Override
    public DataSet getValidationDataSet() {
        return dataSet.getRange((int)(trainSize*0.8),trainSize);
    }

    @Override
    public DataSet getTestDataSet() {
        return dataSet.getRange(trainSize,dataSet.numExamples());
    }
}
