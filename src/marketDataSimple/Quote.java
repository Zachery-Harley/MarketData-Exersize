package marketDataSimple;

public class Quote {

	private Company company;
	private Exchange exchange;
	private double bestOffer;
	private double bestBid;
	
	public Quote(Company company, Exchange exchange, double bestOffer, double bestBid) {
		this.company = company;
		this.exchange = exchange;
		this.bestOffer = bestOffer;
		this.bestBid = bestBid;
		
		System.out.println(this.toString());
	}
	
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append("Updated Quote for: "+company.getName()+"\n");
		output.append("Exchange: "+this.exchange.getName()+"\n");
		output.append("Best Offer: £"+this.bestOffer+"\n");
		output.append("Best Bid: £"+this.bestBid+"\n");
		return output.toString();
	}
	
}
