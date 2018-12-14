import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import processingmethods.NormPriceInC2Out;
import processingmethods.ProcessingMethod;
import common.SimulationResult;
import data.DataGenerator;
import data.SilverGoldGenerator;
import network.LSTMNetwork;

/**
 * Created by beku on 12/4/2018.
 */
public class Application {
    public static void main(String... args){
        ProcessingMethod method=new NormPriceInC2Out(2);
        DataGenerator data=new SilverGoldGenerator(0.8,5,1, method,10);
        Trainer trainer=new Trainer(
                new LSTMNetwork(
                    new int[]{method.getInputSize(),10,10,10,method.getOutputSize()},
                    new Adam(0.5),
                    LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD.getILossFunction(),
                    method
                ),
                data,
                5
        );
        trainer.train();
        SimulationResult result=trainer.trainNetwork.simulate("Test run",data.getAllQuotes(),data.getDataSet());
        System.out.print(result.toString());
    }
}
