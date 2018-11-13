import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;

/**
 * Created by beku on 11/8/2018.
 */
public class Main {
    public static void main(String... args){
        DataGenerator generator=new FCHJGenerator("FCHJ.csv",0.8,1);
        MultiLayerNetwork network=new MultiLayerNetwork(NetworkConfiguration.Test.getConfig());
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
