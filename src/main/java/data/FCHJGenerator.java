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

    public FCHJGenerator(String filename,double trainPercent,int shift_amount){
        ArrayList<Double> spread=new ArrayList<>();
        ArrayList<LocalDate> dates=new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
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
        trainSize=(int)(trainPercent*(spread.size()-shift_amount));
        double[] data=new double[spread.size()-shift_amount];
        double[] dataShifted=new double[spread.size()-shift_amount];
        for (int i = 0; i < spread.size()-shift_amount; i++) {
            data[i]=spread.get(i);
            dataShifted[i]=spread.get(i+shift_amount);
        }
        dataSet=new org.nd4j.linalg.dataset.DataSet(Nd4j.create(data),Nd4j.create(dataShifted));
    }

    public DataSet getTrainDataSet() {
        return dataSet.getRange(0,trainSize);
    }

    public DataSet getTestDataSet() {
        return dataSet.getRange(trainSize,dataSet.numExamples());
    }
}
