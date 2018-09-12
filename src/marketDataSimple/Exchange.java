package marketDataSimple;

import java.util.LinkedList;

public class Exchange {
	
	LinkedList<OrderBook> orderBooks = new LinkedList<>();
	private String exchangeName;
	protected boolean internalExchange = false;
	
	public Exchange(String name) {
		//Create and add the order books to the list 
		orderBooks.add(new OrderBook(new Company("HSBC").getName(), this));
		orderBooks.add(new OrderBook(new Company("BT").getName(), this));
		orderBooks.add(new OrderBook(new Company("VOD").getName(), this));
		this.exchangeName = name;
	}
	
	/**
	 * Push an order to the exchange where it can then be matched and a trade made
	 * @param newOrder - The new order to pushed to the exchange
	 */
	public void pushOrder(TradeOrder newOrder) {
		OrderBook orderbook = getOrderBook(newOrder.getSym());
		orderbook.addOrder(newOrder);
	}
	
	/**
	 * Get an order book for a specified company if it exists. If
	 * no book exists for the given company, then null is returned.
	 * @param company - The company to find the oder book of
	 * @return The order book for the given company, null otherwise
	 */
	public OrderBook getOrderBook(String company) {
		for(OrderBook book : orderBooks) {
			if(book.sym.equals(company)) {
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
