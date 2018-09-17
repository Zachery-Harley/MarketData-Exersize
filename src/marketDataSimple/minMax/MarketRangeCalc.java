package marketDataSimple.minMax;

import java.util.ArrayList;
import java.util.HashMap;

import marketDataSimple.replay.ReplayAbstract;
import marketDataSimple.replay.ReplayQuote;

public class MarketRangeCalc implements Runnable{
	public boolean running = true;
	HashMap<String, MarketRange> marketRanges = new HashMap<>();
	private ArrayList<ReplayAbstract> buffer;
	
	public MarketRangeCalc(ArrayList<ReplayAbstract> buffer) {
		this.buffer = buffer;
	}
	
	public void processQuote(ReplayAbstract quote) {
		MarketRange range = marketRanges.get(quote.getSym());
		if(range == null) {
			range = new MarketRange(quote.getSym());
			marketRanges.put(quote.getSym(), range);
		}
		range.updateRange(quote);
	}
	
	public void printSummery() {
		for(MarketRange range : marketRanges.values()) {
			System.out.println(range.toString());
		}
	}

	@Override
	public void run() {
		try {
			while(running || !buffer.isEmpty()) {
				ReplayAbstract replay = null;
				//Wait until buffer has data
				if(buffer.isEmpty()) {
					synchronized (buffer) {
						buffer.wait(100);
					}
					
				} else {
					//Read the data from the buffer
					synchronized (buffer) {
						replay = buffer.get(0);
						buffer.remove(replay);
					}
					
					//Process the data
					processQuote(replay);
				}
			}
		}
		catch(Exception e) {
			System.err.println("Thread stopped:");
			e.printStackTrace();
		}
		synchronized (this) {
			this.notifyAll();
		}
	}
	
}
