package marketDataSimple;

import java.util.LinkedList;
import java.util.List;

import com.zacheryharley.zava.structure.Row;

public class OrderBook{

	LinkedList<TradeOrder> offers = new LinkedList<>();
	LinkedList<TradeOrder> bids = new LinkedList<>();
	Exchange exchange;
	String sym;
	
	/**
	 * Create a new order book for the given company.
	 * @param company - The company the orderbook is for
	 */
	public OrderBook(String company, Exchange exchange) {
		this.sym = company;
		this.exchange = exchange;
	}
	
	/**
	 * Add an oder to the order book. After the order is added it will be matched with any
	 * orders that allow for a trade.
	 * @param order The order to add to the order book
	 */
	public void addOrder(TradeOrder order) {
		if(order.isBuyOrder()) {
			this.bids.add(order);
		} else {
			this.offers.add(order);
		}
		//Match any trades
		matchTrades();
		
		if(getHighestBid() != null && getLowestOffer() != null) {
			int lowSize = getSizeAt(offers, getLowestOffer().getPrice());
			int lowCount = getCountAt(offers, getLowestOffer().getPrice());
			int buySize = getSizeAt(bids, getHighestBid().getPrice());
			int buyCount = getCountAt(bids, getHighestBid().getPrice());
			Quote q = new Quote(this.sym, exchange, getLowestOffer().getPrice(), lowSize, lowCount, getHighestBid().getPrice(), buySize, buyCount);
			Main.time = Main.time.plusNanos(5);
			q.setTime(Main.time);
			Main.quoteData.addRow(q.toCSV());
		}
	}
	
	/**
	 * Match the trades that can be matched in the order book. Complete trades will not be matched.
	 */
	public void matchTrades() {
		boolean tradeMade = true;
		
		while(tradeMade) {
			TradeOrder buyer = getHighestBid();
			TradeOrder seller = getLowestOffer();
			
			if(buyer != null && seller != null) {
				if(seller.getPrice() <= buyer.getPrice()) {
					makeTrade(seller, buyer);
					continue;
				}
			}
			tradeMade = false;
		}
		
	}
	
	
	/**
	 * Make a trade between a buyer and a seller
	 * @param seller The seller of the shares
	 * @param buyer The buyer of the shares
	 */
	public void makeTrade(TradeOrder seller, TradeOrder buyer) {
		
		//The seller has more than the buyer wants
		if(seller.leftUnfulfilled() >= buyer.leftUnfulfilled()) {
			//Empty the buyer out and as much of the seller as possible
			seller.addFulfilled(buyer.leftUnfulfilled());
			buyer.addFulfilled(buyer.leftUnfulfilled());
		} else {
			//Buyer has more than the seller so empty the seller as much as possible
			buyer.addFulfilled(seller.leftUnfulfilled());
			seller.addFulfilled(seller.leftUnfulfilled());
		}
	}
	
	public void updateQuote() {
		
	}
	
	/**
	 * Find the lowest offer that exists on the market at the current time
	 * @return The lowest TradeOrder
	 */
	public TradeOrder getLowestOffer() {
		TradeOrder lowest = null;
		for(TradeOrder order : offers) {
			//If the order is already complete skip it
			if(order.isOrderFilled()) continue;
			if(exchange.isInternalExchange() && order.isInExchange()) continue;
			
			//If the lowest order is not yet set, then set it
			if(lowest == null) {
				lowest = order;
				continue;
			}
			
			//If the price of the order is lower than the lowest then replace it 
			if(order.getPrice() < lowest.getPrice()) {
				lowest = order;
			}
		}
		return lowest;
	}
	
	/**
	 * Find the highest bid on the market at the current time.
	 * @return The highest bid trade order
	 */
	public TradeOrder getHighestBid() {
		TradeOrder highest = null;
		for(TradeOrder order : bids) {
			//Skip filled orders
			if(order.isOrderFilled()) continue;
			if(exchange.isInternalExchange() && order.isInExchange()) continue;
			
			//If the highest order is not set, set it
			if(highest == null) {
				highest = order;
				continue;
			}
			
			//Replace the highest if the current order is higher
			if(order.getPrice() > highest.getPrice()) {
				highest = order;
			}
		}
		return highest;
	}
	
	/**
	 * Get the open from the order book
	 * @return The open price
	 */
	public double getOpenPrice() {
		return bids.getFirst().getPrice();
	}
	
	/**
	 * Get the closing price from the order book
	 * @return The closing price
	 */
	public double getClosePrice() {
		return bids.getLast().getPrice();
	}
	
	/**
	 * Get the average share cost across all orders. This is not weighted and so the size of
	 * the order has no effect on the average price.
	 * @return The average price of the shares
	 */
	public double getAverage() {
		List<TradeOrder> subset = offers.subList(1, offers.size()-1);
		subset.addAll(bids.subList(1, bids.size()-1));
		
		double total = 0;
		for(TradeOrder order : subset) {
			total += order.getPrice();
		}
		
		return total/subset.size();
	}
	
	/**
	 * Calculate the weighted average for all trade orders across the day.
	 * Being weighted the size of the order affects the average also.
	 * @return The weighted average price
	 */
	public double getWeightedAverage() {
		int shares = 0;
		double total = 0;
		
		List<TradeOrder> subset = offers.subList(1, offers.size()-1);
		subset.addAll(bids.subList(1, bids.size()-1));
		
		for(TradeOrder order : subset) {
			total += order.getPrice() * order.originalSharesAvailable();
			shares += order.originalSharesAvailable();
		}
		
		return total/shares;
	}
	
	public double getLocalWeightedAverage() {
		int shares = 0;
		double total = 0;
		
		List<TradeOrder> subset = offers.subList(1, offers.size()-1);
		subset.addAll(bids.subList(1, bids.size()-1));
		
		for(TradeOrder order : subset) {
			if(order.isInExchange()) continue;
			total += order.getPrice() * order.originalSharesAvailable();
			shares += order.originalSharesAvailable();
		}
		
		return total/shares;
	}
	
	public int countPendingOrders() {
		LinkedList<TradeOrder> allOrders = getAllOrders();
		int count = 0;
		for(TradeOrder order : allOrders) {
			if(!order.isOrderFilled() && !order.isInExchange()){
				count++;
			}
		}
		return count;
	}
	
	public LinkedList<TradeOrder> getAllOrders(){
		LinkedList<TradeOrder> allOrders = new LinkedList<>();
		allOrders.addAll(bids);
		allOrders.addAll(offers);
		return allOrders;
	}
	
	public TradeOrder getOldestTrade(LinkedList<TradeOrder> list) {
		TradeOrder oldest = null;
		for(TradeOrder order : list) {
			if(order.isOrderFilled()) continue;
			if(order.isInExchange()) continue;
			if(oldest == null) oldest = order;
			if(order.getTime().isBefore(oldest.getTime())){
				oldest = order;
			}
		}
		return oldest;
	}
	
	public int getSizeAt(LinkedList<TradeOrder> list, double price) {
		int size = 0;
		for(TradeOrder order : list) {
			if(order.getPrice() == price) {
				size += order.leftUnfulfilled();
			}
		}
		return size;
	}
	
	public int getCountAt(LinkedList<TradeOrder> list, double price) {
		int count = 0;
		for(TradeOrder order : list) {
			if(order.getPrice() == price && order.leftUnfulfilled() > 0) {
				count++;
			}
		}
		return count;
	}
	
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append("Orderbook for: " + this.sym + "\n");
		output.append("Opening Price: £" + this.getOpenPrice() + "\n");
		output.append("Closing Price: £" + this.getClosePrice() + "\n");
		output.append("Average Price: £" + this.getAverage() + "\n");
		output.append("Average Weighted Price: £" + this.getWeightedAverage() + "\n");
		output.append("Average Weighted price (Interal Crosses): £" + this.getLocalWeightedAverage() + "\n");
		return output.toString();
	}

	
}
