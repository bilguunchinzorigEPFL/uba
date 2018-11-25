package network;

import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;

public interface Network {

    public MultiLayerConfiguration getConfig();

    public Evaluation getEval();

}
