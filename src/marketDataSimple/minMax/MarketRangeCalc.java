package marketDataSimple.minMax;

import java.util.HashMap;

import marketDataSimple.replay.ReplayQuote;

public class MarketRangeCalc {
	HashMap<String, MarketRange> marketRanges = new HashMap<>();
	
	public void processQuote(ReplayQuote quote) {
		MarketRange range = marketRanges.get(quote.getSym());
		if(range == null) {
			range = new MarketRange(quote.getSym());
			marketRanges.put(quote.getSym(), range);
		}
		range.updateRange(quote);
	}
	
	public void printSummery() {
		for(MarketRange range : marketRanges.values()) {
			System.out.println(range.toString());
		}
	}
	
}
