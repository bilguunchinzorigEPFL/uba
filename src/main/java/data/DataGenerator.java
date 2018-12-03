package data;

import org.nd4j.linalg.dataset.api.DataSet;

/**
 * Created by beku on 11/12/2018.
 */
public interface DataGenerator {
    DataSet getTrainDataSet();
    DataSet getValidationDataSet();
    DataSet getTestDataSet();
}
