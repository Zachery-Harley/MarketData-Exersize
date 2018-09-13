package marketDataSimple;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class TradeOrder {

	private LocalDateTime exTime;
	private LocalDateTime time;
	private Ric ric;
	private String currency = "GBp";
	private ArrayList<Trade> completedTrades = new ArrayList<Trade>();
	private ArrayList<TradeOrder> childOrders = new ArrayList<TradeOrder>();
	private int size;
	private double price;
	private boolean buyOrder;
	private boolean condition;
	private Class<? extends Instrument> underline;
	
	////////////////////////////////
	///////// Constructor //////////
	////////////////////////////////
	
	/**
	 * Make a new trade order that can be sent to an exchange.
	 * @param ric - The ric of the company whose instruments are being traded
	 * @param buyOrder - True to buy the instrument, false to sell
	 * @param size - The size of the order
	 * @param price - The price to be paid for each item in the order
	 * @param instrument - The type of instrument to be traded 
	 */
	public TradeOrder(Ric ric, boolean buyOrder, int size, double price, Class<? extends Instrument> instrument) {
		this.ric = ric;
		this.buyOrder = buyOrder;
		this.size = size;
		this.price = price;
		this.time = LocalDateTime.now();
		this.underline = instrument;
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
	 * @return The underlining instrument class.
	 */
	public Class<? extends Instrument> getUnderline() {
		return this.underline;
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

	/**
	 * Get the RIC of the company that this trade is trading instruments in
	 * @return The Ric
	 */
	public Ric getRic() {
		return this.ric;
	}
	
	/**
	 * Get the time the bank sent/received the trade order
	 * @return The banks trade time
	 */
	public LocalDateTime getBankTime() {
		return this.time;
	}
	
	/**
	 * Get the time this the exchange send/received the trade order
	 * @return The exchanges trade time
	 */
	public LocalDateTime getExchangeTime() {
		return this.exTime;
	}
	
	////////////////////////////
	///// Public Methods ///////
	////////////////////////////
	
	
	/**
	 * Add the trade to the order, this needs to be done for both buyer and seller
	 * @param trade - The trade object
	 * @return True if this order is now complete, false otherwise
	 * @throws IlligalTradeException - The trade was illegal consuming more than what the order could offer
	 */
	public boolean addTrade(Trade trade) throws IlligalTradeException{
		this.completedTrades.add(trade);
		//Count the remaining and if its less than 0 return true
		int remaining = this.getRemainingSize();
		if(remaining == 0) 
			return true;
		if(remaining > 0)
			return false;
		//The remaining is negative, remove the trade and throw an exception
		this.completedTrades.remove(trade);
		System.err.println(trade.toString());
		throw new IlligalTradeException();
	}

	
	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		//output.append(symbol);
		output.append(this.buyOrder	? "BUY \t"
									: "SELL\t");
		output.append(this.ric.ticker + "\t" + this.underline.getSimpleName() + "\t");
		output.append(size + "@" + price + currency);
		return output.toString();
	}
}
