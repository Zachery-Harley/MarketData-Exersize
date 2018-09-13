package marketDataSimple;

public class Trade {

	private TradeOrder bid;	//Buy
	private TradeOrder offer; //Sell
	private boolean traded = false;
	private double price;
	private int size;
	
	public Trade(TradeOrder tradeA, TradeOrder tradeB) {
		//Trade A wants to be the buy order
		if(!tradeA.isBuyOrder()) {
			TradeOrder temp = tradeA;
			tradeA = tradeB;
			tradeB = temp;
		}
		this.bid = tradeA;
		this.offer = tradeB;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean canTrade() {
		//Return false if the bid is lower than the offer
		if(bid.getPrice() < offer.getPrice()) {
			return false;
		}
		
		//Calculate the price and the size
		this.price = (bid.getPrice() + offer.getPrice())/2;
		if(bid.getRemainingSize() >= offer.getRemainingSize()) {
			size = offer.getRemainingSize();
		} else {
			size = bid.getRemainingSize();
		}
		if(size == 0) return  false;
		
		return true;
	}
	
	/**
	 * Complete the trade between the buyer and the seller.
	 * @throws IlligalTradeException 
	 */
	public void trade() throws IlligalTradeException {
		this.bid.addTrade(this);
		this.offer.addTrade(this);
		System.out.println(this.toString());
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n" + bid.toString() + "\n");
		builder.append(offer.toString() + "\n");
		builder.append("Actual Trade: " + this.size + "@" + this.price);
		return builder.toString();
		
	}
	
}
