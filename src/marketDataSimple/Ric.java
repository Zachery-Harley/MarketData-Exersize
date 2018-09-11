package marketDataSimple;

public class Ric {
	public String companyName;
	public String ticker;
	public String exchangeID;
	
	public Ric(String ticker, String companyName) {
		this.ticker = ticker;
		this.companyName = companyName;
	}
	
	public Ric setExchangeID(String id) {
		Ric newRic = new Ric(ticker, companyName);
		newRic.exchangeID = id;
		return newRic;
	}
	
	public boolean equals(Object obj) {
		if(obj==this)
			return true;
		
		if(obj instanceof Ric) {
			Ric compare = (Ric)obj;
			return compare.companyName.equals(companyName)
					&& compare.ticker.equals(ticker);
		}
		return false;
	}
}
