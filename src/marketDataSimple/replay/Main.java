package marketDataSimple.replay;

import marketDataSimple.minMax.MarketRangeCalc;

public class Main {
	
	static Replay replayEngine = new Replay("quoteData.csv", "tradeData.csv");
	
	public static void main(String[] args) throws InterruptedException {
		MarketRangeCalc quoteCalc = new MarketRangeCalc(replayEngine.getReplayBuffer());
		replayEngine.setNotifyable(quoteCalc);
		
		new Thread(replayEngine).start();
		new Thread(quoteCalc).start();
		synchronized (quoteCalc) {
			quoteCalc.wait();
			quoteCalc.printSummery();
		}
	}
	
}
