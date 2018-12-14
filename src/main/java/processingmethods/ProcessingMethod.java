package processingmethods;

import org.nd4j.linalg.api.ndarray.INDArray;

import common.Quote;

/**
 * Created by beku on 12/14/2018.
 */
public interface ProcessingMethod{
    double[] calcPositions(INDArray signal);
    double[] calcInput(Quote[] quotes);
    double[] calcOutput(Quote[] now, Quote[] future);
    int getInputSize();
    int getOutputSize();
}
