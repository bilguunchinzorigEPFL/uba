import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.Layer;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.distribution.UniformDistribution;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;

/**
 * Created by beku on 11/8/2018.
 */
public class Main {
    static MultiLayerConfiguration testConfig(){
        MultiLayerConfiguration conf=new NeuralNetConfiguration.Builder()
            .updater(new Sgd(0.1))
            .list()
            .layer(0, new DenseLayer.Builder()
                .activation(Activation.SIGMOID)
                .nIn(2).nOut(4).build())
            .layer(1, new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD)
                .activation(Activation.SOFTMAX)
                .nIn(4).nOut(2).build())
            .backprop(true)
            .build();
        return conf;
    }

    public static void main(String... args){
        DataGenerator generator=new FakeDataGenerator(1,10,0);
        MultiLayerNetwork network=new MultiLayerNetwork(testConfig());
        network.init();
        int epochs=10000;
        for (int i = 0; i < epochs; i++) {
            network.fit(generator.getTrainDataSet());
        }
        INDArray preds=network.output(generator.getTestDataSet().getFeatures());
        Evaluation eval=new Evaluation(2);
        eval.eval(generator.getTestDataSet().getLabels(),preds);
        String result=eval.stats();
        System.out.println(result);
    }
}
