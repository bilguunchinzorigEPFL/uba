import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import data.DataGenerator;
import data.FCHJGenerator;
import network.LSTMNetwork;
import network.Network;

/**
 * Created by beku on 12/4/2018.
 */
public class Application {
    public static void main(String... args){
        DataGenerator data=new FCHJGenerator(0.8,100,1,false);
        Trainer trainer=new Trainer(
                new LSTMNetwork(
                    new int[]{1,5,2},
                    new Adam(0.1),
                    LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD.getILossFunction(),
                    false
                ),
                data,
                1000
        );
        trainer.train();
        trainer.trainNetwork.simulate("Test run",data.getAllQuotes());
    }
}
