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
		//Check if the orderbook is full, if so push the oldest order to the exchange
		OrderBook orderbook = getOrderBook(newOrder.getSym());
		if(orderbook.countPendingOrders() > localTradeBuffer) {
			TradeOrder oldest = orderbook.getOldestTrade(orderbook.getAllOrders());
			oldest.sendToExchange(externalExchanges.get("L"));
		}
	}
	
	
	

}
