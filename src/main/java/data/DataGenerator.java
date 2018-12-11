package data;

import org.nd4j.linalg.dataset.api.DataSet;

import java.util.ArrayList;

import common.Quote;

/**
 * Created by beku on 11/12/2018.
 */
public interface DataGenerator {
    DataSet getDataSet();
    DataSet getTrainDataSet();
    DataSet getValidationDataSet();
    DataSet getTestDataSet();
    ArrayList<Quote[]> getAllQuotes();
}
