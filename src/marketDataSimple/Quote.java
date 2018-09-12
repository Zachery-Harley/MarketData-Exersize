package marketDataSimple;

public class Quote {

	private Ric ric;
	private Exchange exchange;
	private double bestOffer;
	private double bestBid;
	
	/**
	 * Create a quote for the order book given showing the best prices for the
	 * given RIC;
	 * @param book - The book to generate the quote from
	 * @param ric - The Ric of the instrument to build the quote for
	 */
	public Quote(OrderBook book, Ric ric, Class<? extends Instrument> instrument, int topX) {
		this.ric = ric;
		this.exchange = exchange;
		this.bestOffer = bestOffer;
		this.bestBid = bestBid;
		
		System.out.println(this.toString());
	}

	/////////////////////////
	//// Public methods /////
	/////////////////////////
	
	/**
	 * Is the given order better or equal to the best values as defined in
	 * this quote.
	 * @param order - The order to compare against this quote
	 * @return True if the order is better or equal, false otherwise
	 */
	public boolean isBeatBy(TradeOrder order) {
		if(order.isBuyOrder()) {
			return (this.bestBid <= order.getPrice());
		} else {
			return (this.bestOffer >= order.getPrice());
		}
	}
	
	
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append("Updated Quote for: "+ric.ticker+"\n");
		output.append("Exchange: "+this.exchange.getName()+"\n");
		output.append("Best Offer: £"+this.bestOffer+"\n");
		output.append("Best Bid: £"+this.bestBid+"\n");
		return output.toString();
	}
	
}
