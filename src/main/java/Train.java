import data.DataGenerator;
import data.FCHJGenerator;
import data.FakeDataGenerator;
import network.LSTMNetwork;
import network.Network;
import network.TestNetwork;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;

/**
 * Created by beku on 11/8/2018.
 */
public class Train {
    static Network trainNetwork=new LSTMNetwork(
            new int[]{1,5,2},
            new Sgd(0.1),
            LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD.getILossFunction(),
            false
    );
    static DataGenerator generator=new FCHJGenerator(0.8,100,1,false);
    static int epochs=1000;

    public static void main(String... args){
        MultiLayerNetwork network=new MultiLayerNetwork(trainNetwork.getConfig());
        Evaluation eval=trainNetwork.getEval();
        network.init();
        for (int i = 0; i < epochs; i++) {
            network.fit(generator.getTrainDataSet());
            INDArray preds=network.output(generator.getValidationDataSet().getFeatures());
            eval.eval(generator.getValidationDataSet().getLabels(),preds);
            String result=eval.stats();
            System.out.println(result);
        }
        INDArray preds=network.output(generator.getTestDataSet().getFeatures());
        eval.eval(generator.getTestDataSet().getLabels(),preds);
        String result=eval.stats();
        System.out.println(result);
    }
}
