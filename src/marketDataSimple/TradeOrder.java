package marketDataSimple;

import java.time.LocalTime;
import java.util.ArrayList;

public class TradeOrder {

	private LocalTime exTime;
	private LocalTime time;
	private Ric ric;
	private String currency = "GBp";
	private ArrayList<Trade> completedTrades = new ArrayList<Trade>();
	private ArrayList<TradeOrder> childOrders = new ArrayList<TradeOrder>();
	private int size;
	private double price;
	private boolean buyOrder;
	private boolean condition;
	
	////////////////////////////////
	///////// Constructor //////////
	////////////////////////////////
	
	public TradeOrder(Ric ric, boolean buyOrder, int size, double price) {
		this.ric = ric;
		this.buyOrder = buyOrder;
		this.size = size;
		this.price = price;
	}
	
	///////////////////////////////
	///////// Getters and setters /
	///////////////////////////////
	
	/**
	 * Get the size of the trade order
	 */
	public int getSize() {
		return this.size;
	}
	
	/**
	 * Get the underlying type of trade. E.g bond, equity, swap
	 * @return The underlining instrument
	 */
	public Instrument getUnderline() {
		return new Instrument();
	}
	
	/**
	 * Get the reaming size of the trade order. 0 Meaning that there are
	 * no more Instruments to buy or sell
	 * @return The number of remaining instruments. 
	 */
	public int getRemainingSize() {
		return this.size-calcFulfilledSize();
	}
	
	/**
	 * Calculate the amount of the order that has been fulfilled. 0 being when the
	 * order has not yet been filled partial or completely. 
	 * @return The amount of the share that has been fulfilled
	 */
	public int calcFulfilledSize() {
		int total = 0;
		for(Trade trade : completedTrades) {
			total += trade.getSize();
		}
		
		for(TradeOrder child : childOrders) {
			total += child.calcFulfilledSize();
		}
		
		return total;
	}
	
	/**
	 * Get the price being offered by the order
	 * @return The price of the order
	 */
	public double getPrice() {
		return this.price;
	}
	
	/**
	 * Return if the current Trade order is a buy order
	 * @return True if buy order, false if sell order
	 */
	public boolean isBuyOrder() {
		return this.buyOrder;
	}
	
	/**
	 * Has this trade been reported to an exchange
	 * @return True if reported, false otherwise
	 */
	public boolean isReported() {
		return this.condition;
	}
	
	/**
	 * Set whether or not this trade has been reported to an exchange
	 * @param condition 
	 */
	public void setReported(boolean condition) {
		this.condition = condition;
	}
	
	public void setTime(LocalTime time) {
		this.time = time;
	}
	
	public Ric getRic() {
		return this.ric;
	}
	
	////////////////////////////
	///// Public Methods ///////
	////////////////////////////
	
	public void addTrade(Trade trade) {
		this.completedTrades.add(trade);
	}

	
	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		//output.append(symbol);
		if(this.buyOrder) output.append(" <--BUY-- ");
		else output.append(" --SELL--> ");
		output.append(size + "@" + price + currency);
		return output.toString();
	}
}
