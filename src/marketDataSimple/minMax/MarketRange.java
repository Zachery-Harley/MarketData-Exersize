package marketDataSimple.minMax;

import marketDataSimple.replay.ReplayAbstract;
import marketDataSimple.replay.ReplayQuote;

public class MarketRange {
	
	double highestBid;
	double minOffer;
	boolean offerInit = false;
	boolean bidInit = false;
	String sym;
	
	public MarketRange(String sym) {
		this.sym = sym;
	}
	
	public void updateRange(ReplayAbstract replay) {
		if(replay instanceof ReplayQuote) {
			ReplayQuote q = (ReplayQuote) replay;
			if(!bidInit || q.getBestBid() > highestBid) {
				highestBid = q.getBestBid();
				bidInit = true;
			}
			
			if(!offerInit || q.getBestOffer() < minOffer) {
				minOffer = q.getBestOffer();
				offerInit = true;
			}
		}
	}
	
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append("Sym: " + sym + "\n");
		output.append("Highest bid: " + highestBid + "\n");
		output.append("Lowest offer: " + minOffer + "\n");
		output.append("\n");
		return output.toString();
	}
	
}
