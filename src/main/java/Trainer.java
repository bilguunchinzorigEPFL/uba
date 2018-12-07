import data.DataGenerator;
import data.FCHJGenerator;
import network.LSTMNetwork;
import network.Network;

import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;

/**
 * Created by beku on 11/8/2018.
 */
public class Trainer {
    Network trainNetwork=new LSTMNetwork(
            new int[]{1,5,2},
            new Adam(0.1),
            LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD.getILossFunction(),
            false
    );
    DataGenerator generator=new FCHJGenerator(0.8,100,1,false);
    int epochs=1000;

    public Trainer(){}

    public Trainer(Network trainNetwork, DataGenerator generator, int epochs) {
        this.trainNetwork = trainNetwork;
        this.generator = generator;
        this.epochs = epochs;
    }

    public void train(){
        trainNetwork.network.init();
        for (int i = 0; i < epochs; i++) {
            System.out.print("epoch:"+Integer.toString(i));
            trainNetwork.network.fit(generator.getTrainDataSet());
            trainNetwork.evaluate(generator.getValidationDataSet());
        }
        System.out.print("test result");
        trainNetwork.evaluate(generator.getTestDataSet());
    }
}
