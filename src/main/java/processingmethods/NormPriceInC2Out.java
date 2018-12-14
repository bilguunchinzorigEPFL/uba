package processingmethods;

import org.nd4j.linalg.api.ndarray.INDArray;

import common.Quote;

/**
 * Created by beku on 12/14/2018.
 */
public class NormPriceInC2Out implements ProcessingMethod {

    int numOfInst;
    int inSize;
    int outSize;

    public NormPriceInC2Out(int numOfInst){
        this.numOfInst=numOfInst;
        inSize=numOfInst;
        outSize=numOfInst*2;
    }

    @Override
    public double[] calcPositions(INDArray signal) {
        double[] result=new double[numOfInst];
        for (int i = 0; i < numOfInst; i++) {
            result[i]=0;
        }
        int argmax=signal.argMax().getInt(0);
        if(argmax<2){
            result[argmax]=1;
        } else {
            result[argmax-2]=-1;
        }
        return result;
    }

    @Override
    public double[] calcInput(Quote[] quotes) {
        double[] result=new double[outSize];
        for (int i = 0; i < inSize; i++) {
            result[i]=quotes[i].getMidPrice();
        }
        return result;
    }

    @Override
    public double[] calcOutput(Quote[] now, Quote[] future) {
        double[] result=new double[outSize];
        int idxMax=0;
        double valMax=0;
        int idxMin=0;
        double valMin=10;
        for (int i = 0; i < inSize; i++) {
            double ret=(future[i].getMidPrice()-now[i].getMidPrice())/now[i].getMidPrice();
            if(valMax<ret){ idxMax=i; valMax=ret;}
            if(valMin>ret){ idxMin=i; valMin=ret;}
            result[i]=0;
            result[inSize+i]=0;
        }
        result[idxMax]=1;
        result[inSize+idxMin]=1;
        return result;
    }

    @Override
    public int getInputSize() {
        return inSize;
    }

    @Override
    public int getOutputSize() {
        return outSize;
    }

//    private DataSet prepSpreadInCOut(ArrayList<Quote[]> quotes, double trainPercent, int sequenceLength, int shift_amount){
//        ArrayList<Double> spread= Quote.calcSpreadsNormalized(quotes,new double[]{1,-1});
//        int numOfSamples=spread.size()-sequenceLength-shift_amount+1;
//        trainSize=(int)(trainPercent*numOfSamples);
//
//        double[][][] data=new double[numOfSamples][1][sequenceLength];
//        double[][][] dataShifted=new double[numOfSamples][2][sequenceLength];
//        for (int i = 0; i < numOfSamples; i++) {
//            for (int i1 = 0; i1 < sequenceLength; i1++) {
//                data[i][0][i1]=spread.get(i+i1);
//                int idx=0;
//                if((spread.get(i+i1+shift_amount)-spread.get(i+i1))>0){idx=1;}
//                dataShifted[i][idx][i1]=1;
//            }
//        }
//        return new org.nd4j.linalg.dataset.DataSet(Nd4j.create(data),Nd4j.create(dataShifted));
//    }
}
