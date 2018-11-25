package data;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.factory.Nd4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by beku on 11/13/2018.
 */
public class FCHJGenerator implements DataGenerator {

    DataSet dataSet;
    int trainSize;
    public FCHJGenerator(double trainPercent,int sequenceLength, int shift_amount){
        ArrayList<Double> spread=new ArrayList<>();
        ArrayList<LocalDate> dates=new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("FCHJ.csv"));
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] elems = line.split(",");
                if(elems.length==2){
                    dates.add(LocalDate.parse(elems[0]));
                    spread.add(Double.valueOf(elems[1]));
                }
            }
            reader.close();
        } catch (Exception e){
            String tmp="";
        }
        int numOfSamples=spread.size()-sequenceLength-shift_amount;

        trainSize=(int)(trainPercent*numOfSamples);
        double[][][] data=new double[numOfSamples][1][sequenceLength];
        double[][][] dataShifted=new double[numOfSamples][1][sequenceLength];
        for (int i = 0; i < numOfSamples; i++) {
            for (int i1 = 0; i1 < sequenceLength; i1++) {
                data[i][0][i1]=spread.get(i+i1);
                dataShifted[i][0][i1]=spread.get(i+i1+shift_amount);
            }
        }
        dataSet=new org.nd4j.linalg.dataset.DataSet(Nd4j.create(data),Nd4j.create(dataShifted));
        String tmp="";
    }

    public DataSet getTrainDataSet() {
        return dataSet.getRange(0,trainSize);
    }

    public DataSet getTestDataSet() {
        return dataSet.getRange(trainSize,dataSet.numExamples());
    }
}
