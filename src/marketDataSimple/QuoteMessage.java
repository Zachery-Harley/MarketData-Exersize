package marketDataSimple;

public class QuoteMessage {

	private Company company;
	private double bestBid;
	private double bestAsk;
	
	public QuoteMessage(Company company, double bestBid, double bestAsk) {
		this.company = company;
		this.bestAsk = bestAsk;
		this.bestBid = bestBid;
	}
	
}
