package network;

import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class TestNetwork extends Network {

    public TestNetwork(){
        MultiLayerConfiguration conf=new NeuralNetConfiguration.Builder()
                .updater(new Sgd(0.1))
                .list()
                .layer(0, new DenseLayer.Builder()
                        .activation(Activation.SIGMOID)
                        .nIn(2).nOut(4).build())
                .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .activation(Activation.SOFTMAX)
                        .nIn(4).nOut(2).build())
                .backprop(true)
                .build();
        configuration=conf;
        network=new MultiLayerNetwork(configuration);
    }

    @Override
    public void evaluate(DataSet data){}


}
