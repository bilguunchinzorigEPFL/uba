package data;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.factory.Nd4j;

/**
 * Created by beku on 11/12/2018.
 */
public class FakeDataGenerator implements DataGenerator{
    int trainSize;
    int testSize;
    DataSet data;
    public double bias;

    public FakeDataGenerator(int trainSize,int testSize, double noiseRatio){
        this.trainSize=trainSize;
        this.testSize=testSize;

        int totalSize=trainSize+testSize;
        double[][] inputraw=new double[4*trainSize][2];
        double[][] outputraw=new double[4*trainSize][2];

        for (int i = 0; i < trainSize; i++) {
            inputraw[i+0]=new double[]{0,0};
            inputraw[i+1]=new double[]{0,1};
            inputraw[i+2]=new double[]{1,0};
            inputraw[i+3]=new double[]{1,1};
            outputraw[i+0]=new double[]{1,0};
            outputraw[i+1]=new double[]{0,1};
            outputraw[i+2]=new double[]{0,1};
            outputraw[i+3]=new double[]{1,0};
        }

        INDArray inputs = Nd4j.create(inputraw);
        INDArray outputs = Nd4j.create(outputraw);
        data=new org.nd4j.linalg.dataset.DataSet(inputs,outputs);
    }

    public DataSet getTrainDataSet() {
        return data;//.getRange(0,trainSize);
    }

    public DataSet getTestDataSet() {
        return data;//.getRange(trainSize,trainSize+testSize);
    }
}
