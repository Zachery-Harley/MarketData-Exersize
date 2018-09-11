package marketDataSimple;

public class Quote {

	private Ric ric;
	private Exchange exchange;
	private double bestOffer;
	private double bestBid;
	
	public Quote(Ric ric, Exchange exchange, double bestOffer, double bestBid) {
		this.ric = ric;
		this.exchange = exchange;
		this.bestOffer = bestOffer;
		this.bestBid = bestBid;
		
		System.out.println(this.toString());
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
