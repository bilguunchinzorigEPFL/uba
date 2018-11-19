import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.eval.RegressionEvaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;

import java.util.logging.Logger;

/**
 * Created by beku on 11/8/2018.
 */
public class Main {
    private static Logger logger=Logger.getLogger("Logs");
    public static void main(String... args){
        DataGenerator generator=new FCHJGenerator(0.8,100,1);
        MultiLayerNetwork network=new MultiLayerNetwork(NetworkConfiguration.LSTM.getConfig());
        network.init();
        int epochs=10;
        for (int i = 0; i < epochs; i++) {
            network.fit(generator.getTrainDataSet());
            logger.info("training epoch:"+Integer.toString(i));
        }
        INDArray preds=network.output(generator.getTestDataSet().getFeatures());
        RegressionEvaluation eval=new RegressionEvaluation(1);
        eval.eval(generator.getTestDataSet().getLabels(),preds);
        String result=eval.stats();
        System.out.println(result);
    }
}
