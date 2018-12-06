package network;

import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.dataset.api.DataSet;

import java.util.ArrayList;

import common.Quote;
import common.SimulationResult;

public abstract class Network {
    public MultiLayerNetwork network;
    public MultiLayerConfiguration configuration;

    public void evaluate(DataSet data){}

    public SimulationResult simulate(String name,ArrayList<Quote[]> quotes){return null;}
}
