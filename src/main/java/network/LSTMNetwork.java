package network;

import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.LSTM;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.IUpdater;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.ILossFunction;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class LSTMNetwork implements Network{

    int[] numOfNeuronsEachLayer;
    IUpdater updater;
    ILossFunction lossFunction;
    boolean regression;

    public LSTMNetwork(int[] numOfNeuronsEachLayer, IUpdater updater, ILossFunction lossFunction, boolean regression) {
        this.numOfNeuronsEachLayer = numOfNeuronsEachLayer;
        this.updater = updater;
        this.lossFunction = lossFunction;
        this.regression = regression;
    }

    public MultiLayerConfiguration getConfig(){
        NeuralNetConfiguration.ListBuilder listBuilder=new NeuralNetConfiguration.Builder()
                .updater(updater)
                .list();
        int numOfInput=numOfNeuronsEachLayer[0];
        int i = 1;
        for (; i < numOfNeuronsEachLayer.length-1; i++) {
            listBuilder.layer(i-1,new LSTM.Builder()
                    .activation(Activation.SELU)
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
        return listBuilder.backprop(true).build();
    }

    public Evaluation getEval() {
        if(regression){
            return null;
        } else {
            return new Evaluation(3);
        }
    }
}
