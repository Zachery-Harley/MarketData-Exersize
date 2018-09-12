package marketDataSimple;

import java.time.format.DateTimeFormatter;

import com.zacheryharley.zava.structure.Row;


public class TradeOrder extends ExchangeMessage {

	protected boolean isBuyOrder;
	protected int shareTotal;
	protected int shareFulfilled;
	protected double offerPerShare;
	protected boolean sentToExchange = false;
	
	public TradeOrder(String company, boolean isBuyOrder, int shareTotal, double offerPerShare) {
		this.isBuyOrder = isBuyOrder;
		this.shareTotal = shareTotal;
		this.offerPerShare = offerPerShare;
		this.sym = company;
	}
	
	protected TradeOrder() {
		
	}
	
	public Row toCSV() {
		Row output = new Row();
		output.add(exTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss:SSS")));
		output.add(time.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss:SSS")));
		output.add(sym);
		output.add(Boolean.toString(isBuyOrder));
		output.add(Double.toString(offerPerShare));
		output.add(Integer.toString(shareTotal));
		output.add(Boolean.toString(sentToExchange));
		return output;
	}
	
	/**
	 * The the bid/offer price for this trade order
	 * @return The price for the order
	 */
	public double getPrice() {
		return this.offerPerShare;
	}
	
	/**
	 * The number of shares being bought or sold in the original order
	 * @return The starting number of shares
	 */
	public int originalSharesAvailable() {
		return shareTotal;
	}
	
	/**
	 * The number of shares available to be bought or sold
	 * @return The number of shares still available in the order
	 */
	public int leftUnfulfilled() {
		return this.shareTotal - this.shareFulfilled;
	}
	
	/**
	 * Add to the number of shares fulfilled in the oder.
	 * @param quantity The number of shares to buy or sell
	 */
	public void addFulfilled(int quantity) {
		this.shareFulfilled += quantity;
	}
	
	/**
	 * Return whether or not this order is a trade order
	 * @return True if a buy order
	 */
	public boolean isBuyOrder() {
		return this.isBuyOrder;
	}
	
	public boolean isInExchange() {
		return this.sentToExchange;
	}
	
	public void sendToExchange(Exchange exchange) {
		this.sentToExchange = true;
		exchange.pushOrder(this);
	}
	
	
	/**
	 * Is the order filled, or is there still unfulfilled parts of the order
	 * @return True if the oder is complete, false otherwise
	 */
	public boolean isOrderFilled() {
		if(leftUnfulfilled() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public String toString() {
		return "(Buy order: " + this.isBuyOrder + " for " + this.shareTotal + ")";
	}
}
