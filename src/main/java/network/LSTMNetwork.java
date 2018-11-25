package network;

import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.LSTM;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class LSTMNetwork implements Network{

    public LSTMNetwork(Object... parameters) {

    }

    public MultiLayerConfiguration getConfig(){
        MultiLayerConfiguration conf=new NeuralNetConfiguration.Builder()
                .updater(new Sgd(0.1))
                .list()
                .layer(0, new LSTM.Builder()
                        .activation(Activation.SIGMOID)
                        .nIn(2).nOut(4).build())
                .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .activation(Activation.SOFTMAX)
                        .nIn(4).nOut(1).build())
                .backprop(true)
                .build();
        return conf;
    }

    public Evaluation getEval() {
        return null;
    }
}
