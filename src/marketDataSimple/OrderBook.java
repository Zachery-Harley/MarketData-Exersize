package marketDataSimple;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.sun.tools.classfile.Type;

public class OrderBook {

	private LinkedList<TradeOrder> offers = new LinkedList<>();
	private LinkedList<TradeOrder> bids = new LinkedList<>();
	private Ric bookRic;
	private Exchange parentExchange;
	
	//////////////////////////////////////
	////////// Constructor ///////////////
	//////////////////////////////////////
	
	/**
	 * Create an order book to store bid/offer orders and allow matching
	 * to take place. 
	 * @param bookRic - The Ric object to identify the instruments stored in the book
	 * @param parentExchange - The exchange the book belongs too
	 */
	public OrderBook(Ric bookRic, Exchange parentExchange) {
		this.bookRic = bookRic;
		this.parentExchange = parentExchange;		
	}
	
	////////////////////////////////////
	////// Getters / Setter ////////////
	////////////////////////////////////
	
	public Ric getRic() {
		return this.bookRic;
	}
	
	////////////////////////////////////
	/////// Public Methods /////////////
	////////////////////////////////////
	
	public void pushOrder(TradeOrder order) {
		if(order.isBuyOrder()) {
			bids.add(order);
		} else {
			offers.add(order);
		}
		
		findTrade(order);
	}
	

	
	/**
	 * Get the best bid in the order book for the given instrument type.
	 * @param type - The type of instrument to get the price for
	 * @return The trade order for the best bid
	 */
	public TradeOrder getBestBid(Class<? extends Instrument> type) {
		TradeOrder highestBid = null;
		List<TradeOrder> list = getBidsOfType(type);
		
		//Get the bid with the highest price
		for(TradeOrder order : list) {
			if(order.getRemainingSize() <= 0) continue;
			if(highestBid == null || highestBid.getPrice() < order.getPrice()) {
				highestBid = order;
			}
		}
		return highestBid;
	}
	
	/**
	 * Get the best offer in the order book for the given instrument type
	 * @param type - The type of instrument to get the price for
	 * @return The trade order for the best offer
	 */
	public TradeOrder getBestOffer(Class<? extends Instrument> type) {
		TradeOrder lowestOffer = null;
		List<TradeOrder> list = getOffersOfType(type);
		//Get the offer with the lowest price
		for(TradeOrder order : list) {
			if(order.getRemainingSize() <= 0) continue;
			if(lowestOffer == null || lowestOffer.getPrice() > order.getPrice()) {
				lowestOffer = order;
			}
		}
		return lowestOffer;
	}
	
	/**
	 * Get all the bids from the order book where the instrument being traded is of
	 * the given type
	 * @param type - The type of the instrument being traded
	 * @return List of bids in which the instrument is that of type
	 */
	public List<TradeOrder> getBidsOfType(Class<? extends Instrument> type){
		return getInstrumentOfType(type, bids);
	}
	
	/**
	 * Get all the offers from the order book where the instrument being traded is of
	 * the given type
	 * @param type - The type of the instrument being traded
	 * @return List of offers in which the instrument is that of type
	 */
	public List<TradeOrder> getOffersOfType(Class<? extends Instrument> type){
		return getInstrumentOfType(type, offers);
	}
	
	//////////////////////////////////////
	////////// Private methods ///////////
	//////////////////////////////////////
	
	/**
	 * Iterate through the given list and extract trade order of the given baseline
	 * and return them in a list.
	 * @param type - The type of instrument
	 * @param list - The list of instruments
	 * @return A list of instruments from the given list
	 */
	private List<TradeOrder> getInstrumentOfType(Class<? extends Instrument> type, List<TradeOrder> list){
		return list.stream()
			.filter(e -> e.getUnderline().getClass().equals(type))
			.collect(Collectors.toList());
	}
	
	
	public void findTrade(TradeOrder newOrder) {
		while(true) {
			TradeOrder matchedTrade = newOrder.isBuyOrder() ? getBestOffer(newOrder.getUnderline().getClass())
															: getBestBid(newOrder.getUnderline().getClass());
			if(matchedTrade == null) break;
			if(makeTrade(matchedTrade, newOrder)) {
				continue;
			}
			break;
		}
	}
	
	
	public boolean makeTrade(TradeOrder orderA, TradeOrder orderB) {
		//Make orderA the buyer and B the seller
		if(orderB.isBuyOrder()) {
			TradeOrder temp = orderA;
			orderA = orderB;
			orderB = temp;
		}
		
		//Make the trade is possible buyer.price >= sell.price
		if(orderA.getPrice() >= orderB.getPrice()) {
			Trade trade = new Trade(orderA, orderB);
			if(trade.canTrade()) {
				trade.trade();
				return true;
			}
		}
		return false;
	}
	
}
