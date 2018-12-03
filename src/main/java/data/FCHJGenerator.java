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
    boolean regression;
    public FCHJGenerator(double trainPercent,int sequenceLength, int shift_amount, boolean regression){
        ArrayList<Double> spread=new ArrayList<>();
        ArrayList<LocalDate> dates=new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("FCHJ.csv"));
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] elems = line.split(",");
                if(elems.length==2){
                    dates.add(LocalDate.parse(elems[0]));
                    spread.add(Double.valueOf(elems[1])/0.025);
                }
            }
            reader.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        int numOfSamples=spread.size()-sequenceLength-shift_amount;
        trainSize=(int)(trainPercent*numOfSamples);

        if(regression){
            //hehe no idea
        } else {
            double[][][] data=new double[numOfSamples][1][sequenceLength];
            double[][][] dataShifted=new double[numOfSamples][2][sequenceLength];
            for (int i = 0; i < numOfSamples; i++) {
                for (int i1 = 0; i1 < sequenceLength; i1++) {
                    data[i][0][i1]=spread.get(i+i1);
                    int idx=0;
                    if((spread.get(i+i1+shift_amount)-spread.get(i+i1))>0){idx=1;}
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
