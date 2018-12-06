package common;

import java.time.LocalDateTime;

/**
 * Created by beku on 12/5/2018.
 */
public class Quote{
    LocalDateTime timeStamp;
    Double bidPrice;
    Double askPrice;

    public Quote(LocalDateTime timeStamp, Double bidPrice, Double askPrice) {
        this.timeStamp = timeStamp;
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
    }
}
