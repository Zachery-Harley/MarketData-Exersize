package marketDataSimple;

import java.util.HashMap;

public class InternalExchange extends Exchange{
	
	HashMap<String, Exchange> externalExchanges = new HashMap<>();
	int localTradeBuffer = 5;
	
	public InternalExchange(String name) {
		super(name);
		super.internalExchange = true;
		externalExchanges.put("L", new Exchange("London"));
	}
	
	@Override
	public void pushOrder(TradeOrder newOrder) {
		super.pushOrder(newOrder);
	}
	
	
	

}
