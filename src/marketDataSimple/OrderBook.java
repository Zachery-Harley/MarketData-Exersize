package marketDataSimple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.sun.tools.classfile.Type;

public class OrderBook {

	private Ric bookRic;
	private Exchange exchange;
	
	//Order storage.
	private HashMap<Class<? extends Instrument>, InstrumentCollection> instrumentBids = new HashMap<>();
	private HashMap<Class<? extends Instrument>, InstrumentCollection> instrumentOffers = new HashMap<>();
	
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
		this.exchange = parentExchange;		
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
		//Push the order into the correct collection
		InstrumentCollection collection = getCollection(order.getUnderline().getClass(), order.isBuyOrder(), true);
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
	
	/**
	 * Get the collection used for storing instruments of the type given.
	 * If create if missing is set to as true then the collection will be created
	 * if if does not yet already exists 
	 * @param instrument - The type of instrument to get the collection from
	 * @param fromBids - True if the collection is to be searched for in the bids, false if from offers.
	 * @param createIfMissing - When true if the collection is not found its created
	 * @return The collection used for storing the instrument given, if the collection does not exist and
	 * createIfMising is false then null is returned, otherwise the collection is created and returned.
	 */
	private InstrumentCollection getCollection(Class<? extends Instrument> instrument, boolean fromBids, boolean createIfMissing) {
		InstrumentCollection collection = fromBids ? instrumentBids.get(instrument)
												   : instrumentOffers.get(instrument);
		//The instrument does not exist yet, make a collection for it
		if(collection == null && createIfMissing) {
			collection = new InstrumentCollection();
			if(fromBids)
				instrumentBids.put(instrument, collection);
			else
				instrumentOffers.put(instrument, collection);
		}
		
		return collection;
	}
	
}
