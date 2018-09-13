package marketDataSimple;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.TreeMap;

public class InstrumentCollection {
	
	private TreeMap<Double, ArrayList<TradeOrder>> bids = new TreeMap<>();
	private TreeMap<Double, ArrayList<TradeOrder>> offers = new TreeMap<>();
	private Quote lastQuote;	
	
	public InstrumentCollection() {
		
	}
	
	///////////////////////////
	/// Public Methods ////////
	///////////////////////////
	
	/**
	 * Add the order to the collection
	 * @param order - The order to be added
	 */
	public void pushOrder(TradeOrder order) {
		ArrayList<TradeOrder> fetchedList = getOrderList(order);
		
		//Add the order to the array list
		fetchedList.add(order);
		
		//Only match if the new order is better than the last quote
		if(lastQuote == null || lastQuote.isBeatBy(order)) {
			//Find matches for the new order
			findMatches(order);
		}
	}
	
	/**
	 * Get the best bid trade order from the collection. If there is no bid
	 * then null is returned.
	 * @return The order with best bid price, null if no available orders.
	 */
	public TradeOrder getBestBid() {
		try {
			Double bestPrice = bids.lastKey();
			return bids.get(bestPrice).get(0);
		} catch (IndexOutOfBoundsException | NoSuchElementException e) {
			return null;
		}
	}
	
	/**
	 * Get the best offer order from the collection. If there is no offer then
	 * null is returned.
	 * @return The offer with the best offer price, null if no available order.
	 */
	public TradeOrder getBestOffer() {
		try {
			Double bestOffer = offers.firstKey();
			return offers.get(bestOffer).get(0);
		} catch (IndexOutOfBoundsException | NoSuchElementException e) {
			return null;
		}
	}
	
	///////////////////////////
	//// Private Methods //////
	///////////////////////////
	
	private void findMatches(TradeOrder order) {
		System.out.println("Finding matchs for: " + order.toString());
		TradeOrder matchingTrade = order.isBuyOrder()	? getBestOffer()
														: getBestBid();
		if(matchingTrade != null) {
			//Trade with the match
			Trade trade = new  Trade(order, matchingTrade);
			trade.canTrade();
			try {
				trade.trade();
			} catch (IlligalTradeException e) {
				System.err.println("Trader order over filled!: " + e);
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Get the order list for the given TradeOrder, if there is no list
	 * that already exists then a new list is created and added to either
	 * bids or offers depending on if the order was a buy or sell order.
	 * @param order - The order that will be used to fetch the list
	 * @return The ArrayList for the given TradeOrder
	 */
	private ArrayList<TradeOrder> getOrderList(TradeOrder order){
		//Does this list already exist, if so fetch it, otherwise create a new list
		ArrayList<TradeOrder> fetchedList = order.isBuyOrder()  ? bids.get(order.getPrice())
																: offers.get(order.getPrice());
		if(fetchedList != null)
				return fetchedList;
		
		//Create the new ArrayList
		ArrayList<TradeOrder> newList = new ArrayList<TradeOrder>();
		if(order.isBuyOrder())
				bids.put(order.getPrice(), newList);
		else
				offers.put(order.getPrice(), newList);
		return newList;
	}
	
	
	
}
