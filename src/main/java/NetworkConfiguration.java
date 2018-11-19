import org.deeplearning4j.nn.conf.BackpropType;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.LSTM;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.conf.preprocessor.FeedForwardToRnnPreProcessor;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.RmsProp;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;

/**
 * Created by beku on 11/13/2018.
 */
public enum NetworkConfiguration {
    Test,
    Regression,
    LSTM;

    MultiLayerConfiguration getConfig(){
        switch (this){
            case Test:
                return testConfig();
            case Regression:
                return regressionConfig();
            case LSTM:
                return lstmConfig();
            default:
                return null;
        }
    }

    private MultiLayerConfiguration testConfig(){
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
        return conf;
    }

    private MultiLayerConfiguration regressionConfig(){
        MultiLayerConfiguration conf=new NeuralNetConfiguration.Builder()
            .updater(new Sgd(0.1))
            .list()
            .layer(0, new DenseLayer.Builder()
                .activation(Activation.SIGMOID)
                .nIn(2).nOut(4).build())
            .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                .activation(Activation.SOFTMAX)
                .nIn(4).nOut(1).build())
            .backprop(true)
            .inputPreProcessor(2, new FeedForwardToRnnPreProcessor())
            .build();
        return conf;
    }

    private MultiLayerConfiguration lstmConfig(){
        MultiLayerConfiguration conf=new NeuralNetConfiguration.Builder()
            .seed(123)
            .updater(new RmsProp(0.001))
            .weightInit(WeightInit.XAVIER)
            .list()
            .layer(0, new LSTM.Builder()
                .activation(Activation.SELU)
                .nIn(1).nOut(4).build())
            .layer(1, new RnnOutputLayer.Builder(LossFunctions.LossFunction.SQUARED_LOSS)
                .activation(Activation.SOFTMAX)
                .nIn(4).nOut(1).build())
            .backprop(true)
            .backpropType(BackpropType.TruncatedBPTT)
            .build();
        return conf;
    }
}
