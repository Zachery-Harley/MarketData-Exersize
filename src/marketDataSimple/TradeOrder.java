package marketDataSimple;

import java.time.LocalTime;

public class TradeOrder {

	private boolean isBuyOrder;
	private Company company;
	private int shareTotal;
	private int shareFulfilled;
	private double offerPerShare;
	private LocalTime tradeTime;
	private boolean sentToExchange = false;
	
	public TradeOrder(Company company, boolean isBuyOrder, int shareTotal, double offerPerShare) {
		this.isBuyOrder = isBuyOrder;
		this.shareTotal = shareTotal;
		this.offerPerShare = offerPerShare;
		this.company = company;
		System.nanoTime();
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
	
	/**
	 * Get the time the order was created
	 * @return
	 */
	public LocalTime getTime() {
		return this.tradeTime;
	}
	
	/**
	 * The name of the company the trade is buying or selling for
	 * @return
	 */
	public Company getCompany() {
		return this.company;
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

	
	public void setTime(LocalTime time) {
		this.tradeTime = time;
	}
	
	
	public String toString() {
		return "(Buy order: " + this.isBuyOrder + " for " + this.shareTotal + ")";
	}
}
