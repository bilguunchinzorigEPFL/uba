package common;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import data.DataGenerator;

/**
 * Created by beku on 12/7/2018.
 */
public class Helpers {
    public static double[] meanAndStd(Collection<Double> values){
        double sum=0;
        for (Double value : values) {
            sum+=value;
        }
        double mean=sum/values.size();
        double sqSum=0;
        for (Double value : values) {
            sqSum+=Math.pow(value-mean,2);
        }
        return new double[]{mean,Math.sqrt(sqSum/(values.size()-1))};
    }

    public static ArrayList<Double> normalize(ArrayList<Double> target){
        double[] meanAndStds=meanAndStd(target);
        ArrayList<Double> result=new ArrayList<>();
        for (int i = 0; i < target.size(); i++) {
            result.add((target.get(i)-meanAndStds[0])/meanAndStds[1]);
        }
        return result;
    }
}
