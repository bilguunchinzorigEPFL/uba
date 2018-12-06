package common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by beku on 12/5/2018.
 */
public class SimulationResult {
    public String name;
    public Double sharpe;
    public HashMap<LocalDate,Double> dailypnl;

    public SimulationResult(String name,int numOfInstrument){
        this.name=name;
        trades=new ArrayList<>();
        prevTrades=new Trade[numOfInstrument];
    }

    public ArrayList<Trade> trades;
    private Trade[] prevTrades;
    public void process(Quote[] quotes, double[] positions){
        for (int i = 0; i < prevTrades.length; i++) {
            Trade prevTrade=prevTrades[i];
            Quote quote=quotes[i];
            double quantity=positions[i]-prevTrade.position;
            double executed_price=quotes[i].askPrice;
            if(quantity<0){ executed_price=quotes[i].bidPrice;}
            //TODO verify pnl calculation
            prevTrade.pnl=(executed_price-prevTrade.executed_price)*(prevTrade.position);
            trades.add(prevTrade);
            prevTrades[i]=new Trade(quote.timeStamp,executed_price,quantity,positions[i]);
        }
    }

    public void calculateOverall(){
        HashMap<LocalDate,ArrayList<Trade>> tradesGroupByDate=new HashMap<>();
        for (Trade trade : trades) {
            LocalDate key=trade.timeStamp.toLocalDate();
            tradesGroupByDate.putIfAbsent(key,new ArrayList<>());
            tradesGroupByDate.get(key).add(trade);
        }
        dailypnl=new HashMap<>();
        double sum=0;
        for (LocalDate localDate : tradesGroupByDate.keySet()) {
            double totalPnl=0;
            for (Trade trade : tradesGroupByDate.get(localDate)) {
                totalPnl+=trade.pnl;
            }
            sum+=totalPnl;
            dailypnl.put(localDate,totalPnl);
        }
        double mean=sum/dailypnl.size();
        double sqSum=0;
        for (LocalDate localDate : dailypnl.keySet()) {
            sqSum+=Math.pow(dailypnl.get(localDate)-mean,2);
        }
        sharpe=Math.sqrt(sqSum/(dailypnl.size()-1));
    }
}
