package data;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.factory.Nd4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import common.Helpers;
import processingmethods.ProcessingMethod;
import common.Quote;

/**
 * Created by beku on 11/13/2018.
 */
public class SilverGoldGenerator implements DataGenerator {

    DataSet dataSet;
    int trainSize;
    int sampleRate=0;
    ArrayList<Quote[]> quotes;

    public SilverGoldGenerator(double trainPercent, int sequenceLength, int shift_amount, ProcessingMethod method, int sampleRate){
        quotes=new ArrayList<>();
        this.sampleRate=sampleRate;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("SilverGold.csv"));
            String line = "";
            int waited=0;
            while ((line = reader.readLine()) != null) {
                waited+=1;
                if(waited==sampleRate){
                    String[] elems = line.split(";");
                    if(elems.length==5){
                        LocalDateTime timeStamp= ZonedDateTime.parse(elems[0]).toLocalDateTime();
                        quotes.add(new Quote[]{
                                new Quote(timeStamp,Double.valueOf(elems[2]),Double.valueOf(elems[1])),
                                new Quote(timeStamp,Double.valueOf(elems[4]),Double.valueOf(elems[3]))
                        });
                    }
                    waited=0;
                }
            }
            reader.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        dataSet=prepDataSet(method,quotes,trainPercent,sequenceLength,shift_amount);
    }

    private DataSet prepDataSet(ProcessingMethod method,ArrayList<Quote[]> quotes, double trainPercent, int sequenceLength, int shift_amount){
        int numOfSamples=quotes.size()-shift_amount;
        int numOfInstrument=quotes.get(0).length;
        trainSize=(int)(trainPercent*numOfSamples);
        double[][] datain=new double[numOfSamples][method.getInputSize()];
        double[][] dataout=new double[numOfSamples][method.getOutputSize()];
        for (int i = 0; i < numOfSamples; i++) {
            datain[i]=method.calcInput(quotes.get(i));
            dataout[i]=method.calcOutput(quotes.get(i),quotes.get(i+shift_amount));
        }
        numOfSamples-=sequenceLength+1;
        double[][][] datainTS=new double[numOfSamples][method.getInputSize()][sequenceLength];
        double[][][] dataoutTS=new double[numOfSamples][method.getOutputSize()][sequenceLength];
        for (int i = 0; i < sequenceLength; i++) {
            for (int i1 = 0; i1 < numOfSamples; i1++) {
                for (int i2 = 0; i2 < numOfInstrument; i2++) {
                    datainTS[i1][i2][i]=datain[i1+i][i2];
                    dataoutTS[i1][i2][i]=dataout[i1+i][i2];
                }
            }
        }
        return new org.nd4j.linalg.dataset.DataSet(Nd4j.create(datainTS),Nd4j.create(dataoutTS));
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

    @Override
    public ArrayList<Quote[]> getAllQuotes() {
        return quotes;
    }

    @Override
    public DataSet getDataSet() {
        return dataSet;
    }
}
