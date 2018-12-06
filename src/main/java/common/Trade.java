package common;

import java.time.LocalDateTime;

/**
 * Created by beku on 12/6/2018.
 */
public class Trade{
    public LocalDateTime timeStamp;
    public double executed_price;
    public double quantity;
    public double position;
    public double pnl;

    public Trade(LocalDateTime timeStamp, double executed_price, double quantity, double position) {
        this.timeStamp = timeStamp;
        this.executed_price = executed_price;
        this.quantity = quantity;
        this.position = position;
    }

    @Override
    public String toString() {
        return timeStamp.toString()+","+
                Double.toString(executed_price)+","+
                Double.toString(quantity)+","+
                Double.toString(position)+","+
                Double.toString(pnl);
    }
}
