import data.DataGenerator;
import data.FCHJGenerator;
import data.FakeDataGenerator;
import network.Network;
import network.TestNetwork;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;

/**
 * Created by beku on 11/8/2018.
 */
public class Train {
    static Network trainNetwork=new TestNetwork();
    static DataGenerator generator=new FakeDataGenerator(4,4,0);
    static int epochs=10000;

    public static void main(String... args){
        MultiLayerNetwork network=new MultiLayerNetwork(trainNetwork.getConfig());
        Evaluation eval=trainNetwork.getEval();
        network.init();
        for (int i = 0; i < epochs; i++) {
            network.fit(generator.getTrainDataSet());
        }
        INDArray preds=network.output(generator.getTestDataSet().getFeatures());
        eval.eval(generator.getTestDataSet().getLabels(),preds);
        String result=eval.stats();
        System.out.println(result);
    }
}
