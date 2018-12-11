import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import common.SimulationResult;
import data.DataGenerator;
import data.FCHJGenerator;
import data.SilverGoldGenerator;
import network.LSTMNetwork;
import network.Network;

/**
 * Created by beku on 12/4/2018.
 */
public class Application {
    public static void main(String... args){
        DataGenerator data=new SilverGoldGenerator(0.8,5,1,false,10);
        Trainer trainer=new Trainer(
                new LSTMNetwork(
                    new int[]{1,10,10,10,2},
                    new Adam(0.1),
                    LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD.getILossFunction(),
                    false
                ),
                data,
                10
        );
        trainer.train();
        SimulationResult result=trainer.trainNetwork.simulate("Test run",data.getAllQuotes(),data.getDataSet());
        System.out.print(result.toString());
    }
}
