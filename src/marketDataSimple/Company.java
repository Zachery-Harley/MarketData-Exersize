package marketDataSimple;

public class Company {
	
	private String name;
	
	public Company(String name) {
		this.name = name;
	}
	
	/**
	 * Create a new buy order for shares in the company
	 * @param amount - The number of shares to be bought
	 * @param offer - The bid per share
	 * @return The created buy order
	 */
	public TradeOrder newBuyOrder(int amount, double offer) {
		return new TradeOrder(this.name, true, amount, offer);
	}
	
	/**
	 * Create a new sell order for shares in the company
	 * @param amount - The number of shares to be sold
	 * @param offer - The offer per share
	 * @return The created sell order
	 */
	public TradeOrder newSellOrder(int amount, double offer) {
		return new TradeOrder(this.name, false, amount, offer);
	}
	
	
	public boolean equals(Company company) {
		if (this.name.equals(company.name)){
			return true;
		}
		return false;
	}

	public String getName() {
		return this.name;
	}
	
}
