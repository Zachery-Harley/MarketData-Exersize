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
	private HashMap<String, InstrumentCollection> instruments = new HashMap<>();

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
	
	/**
	 * Get the ric this order book is trading instruments for. 
	 * @return The ric
	 */
	public Ric getRic() {
		return this.bookRic;
	}
	
	////////////////////////////////////
	/////// Public Methods /////////////
	////////////////////////////////////
	
	/**
	 * Push the order into the order book, the order book will then redirect it to the
	 * correct collection before matching the trade with any orders.
	 * @param order The new order to be pushed into the order book
	 */
	public void pushOrder(TradeOrder order) {
		//Push the order into the correct collection
		InstrumentCollection collection = getCollection(order.getUnderline(), true);
		collection.pushOrder(order);
	}	
	
	/**
	 * Get the collection used for storing instruments of the type given.
	 * If create if missing is set to as true then the collection will be created
	 * if if does not yet already exists 
	 * @param instrument - The type of instrument to get the collection from
	 * @param createIfMissing - When true if the collection is not found its created
	 * @return The collection used for storing the instrument given, if the collection does not exist and
	 * createIfMising is false then null is returned, otherwise the collection is created and returned.
	 */
	private InstrumentCollection getCollection(Class<? extends Instrument> instrument, boolean createIfMissing) {
		InstrumentCollection collection = instruments.get(instrument.getSimpleName());

		//The instrument does not exist yet, make a collection for it
		if(collection == null && createIfMissing) {
			collection = new InstrumentCollection();
			instruments.put(instrument.getSimpleName(), collection);
		}
		
		return collection;
	}
	
}
