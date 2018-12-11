package network;

import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.LSTM;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.IUpdater;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.ILossFunction;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.util.ArrayList;

import common.Quote;
import common.SimulationResult;

public class LSTMNetwork extends Network{

    int[] numOfNeuronsEachLayer;
    IUpdater updater;
    ILossFunction lossFunction;
    boolean regression;

    public LSTMNetwork(int[] numOfNeuronsEachLayer, IUpdater updater, ILossFunction lossFunction, boolean regression) {
        this.numOfNeuronsEachLayer = numOfNeuronsEachLayer;
        this.updater = updater;
        this.lossFunction = lossFunction;
        this.regression = regression;

        NeuralNetConfiguration.ListBuilder listBuilder=new NeuralNetConfiguration.Builder()
                .updater(updater)
                .list();
        int numOfInput=numOfNeuronsEachLayer[0];
        int i = 1;
        for (; i < numOfNeuronsEachLayer.length-1; i++) {
            listBuilder.layer(i-1,new LSTM.Builder()
                    .activation(Activation.TANH)
                    .nIn(numOfInput).nOut(numOfNeuronsEachLayer[i]).build()
            );
            numOfInput=numOfNeuronsEachLayer[i];
        }
        if(regression){
            listBuilder.layer(i-1,new RnnOutputLayer.Builder(lossFunction)
                    .activation(Activation.IDENTITY)
                    .nIn(numOfInput).nOut(numOfNeuronsEachLayer[i]).build()
            );
        } else {
            listBuilder.layer(i-1,new RnnOutputLayer.Builder(lossFunction)
                    .activation(Activation.SOFTMAX)
                    .nIn(numOfInput).nOut(numOfNeuronsEachLayer[i]).build()
            );
        }
        configuration=listBuilder.backprop(true).build();
        network=new MultiLayerNetwork(configuration);
    }

    @Override
    public void evaluate(DataSet data) {
        if(regression){

        } else {
            Evaluation eval=new Evaluation(numOfNeuronsEachLayer[numOfNeuronsEachLayer.length-1]);
            INDArray preds=network.output(data.getFeatures());
            eval.evalTimeSeries(data.getLabels(),preds);
            String result=eval.stats();
            System.out.println(result);
        }
    }


    @Override
    public SimulationResult simulate(String name, ArrayList<Quote[]> quotes, DataSet data) {
        SimulationResult result=new SimulationResult("LSTM",quotes.get(0).length);
        ArrayList<Double> spreads=Quote.calcSpreadsNormalized(quotes,new double[]{1,-1});
        INDArray preds=network.output(data.getFeatures());
        INDArray tmp=preds.getColumn(0).getColumn(preds.shape()[2]-1);
        int seqLength=5;
        for (int i = seqLength; i < quotes.size(); i++) {
            double pos=tmp.getFloat(i-seqLength);
            double[] positions=new double[]{pos,-pos};
            result.process(quotes.get(i),positions);
        }
        result.calculateOverall();
        return result;
    }
}
