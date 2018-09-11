package marketDataSimple;

import java.util.LinkedList;

public class Exchange {
	
	
	LinkedList<OrderBook> orderBooks = new LinkedList<>();
	private String exchangeName;
	protected boolean internalExchange = false;
	
	public Exchange(String name) {
		this.exchangeName = name;
	}
	
	public void newOrderBook(Ric ric) {
		OrderBook newBook = new OrderBook(ric.setExchangeID(getName()), this);
		orderBooks.add(newBook);
	}
	
	
	/**
	 * Push an order to the exchange where it can then be matched and a trade made
	 * @param newOrder - The new order to pushed to the exchange
	 */
	public void pushOrder(TradeOrder newOrder) {
		OrderBook orderbook = getOrderBook(newOrder.getRic());
		orderbook.pushOrder(newOrder);
	}
	
	/**
	 * Get an order book for a specified company if it exists. If
	 * no book exists for the given company, then null is returned.
	 * @param company - The company to find the oder book of
	 * @return The order book for the given company, null otherwise
	 */
	public OrderBook getOrderBook(Ric ric) {
		for(OrderBook book : orderBooks) {
			if(book.getRic().equals(ric)) {
				return book;
			}
		}
		return null;
	}
		
	public String toString() {
		StringBuilder output = new StringBuilder();
		for(OrderBook book : orderBooks) {
			output.append(book.toString());
		}
		return output.toString();
	}

	public boolean isInternalExchange() {
		return this.internalExchange;
	}
	
	public String getName() {
		return this.exchangeName;
	}
	
}
