package marketDataSimple.replay;

import java.util.ArrayList;

import javax.naming.directory.InvalidAttributeValueException;

import com.zacheryharley.zava.structure.Table;

public class ReplayManager {
	
	ArrayList<ReplayQuote> quoteData = new ArrayList<>();
	ArrayList<ReplayTrade> tradeData = new ArrayList<>();
	int quoteIndex = 0;
	int tradeIndex = 0;
	
	public ReplayManager(Table quotes, Table trades) {
		int rowCount = quotes.countRows();
		for(int i = 0; i < rowCount; i++) {
			try {
				ReplayQuote quote = new ReplayQuote(quotes.getRow(i));
				quoteData.add(quote);
			} catch (InvalidAttributeValueException e) {
				System.err.println("Message: " + e);
				e.printStackTrace();
			}
		}
		
		rowCount = trades.countRows();
		for(int i = 0; i < rowCount; i++) {
			try {
				ReplayTrade trade = new ReplayTrade(trades.getRow(i));
				tradeData.add(trade);
			} catch (InvalidAttributeValueException e) {
				System.err.println("Message: " + e);
				e.printStackTrace();
			}
		}
	}
	
	public ReplayAbstract getNext(boolean inc){
		ReplayQuote quote = null;
		if(quoteIndex < quoteData.size()) {
			quote = quoteData.get(quoteIndex);
		}
		
		
		ReplayTrade trade = null;
		if(tradeIndex < tradeData.size()) {
			trade = tradeData.get(tradeIndex);
		}
		
		if(quote == null && trade != null) {
			if(inc)
				tradeIndex++;
			return trade;
		}
		
		if(trade == null && quote != null) {
			if(inc)
				quoteIndex++;
			return quote;
		}
		
		if(trade == null && quote == null) {
			return null;
		}
		
		if(trade.getTime().isBefore(quote.getTime())) {
			if(inc)
				tradeIndex++;
			return trade;
		}
		if(inc)
			quoteIndex++;
		return quote;
		
	}
	
	
}
