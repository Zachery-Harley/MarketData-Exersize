package marketDataSimple.replay;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import com.zacheryharley.zava.io.CSVReader;
import com.zacheryharley.zava.structure.Table;

import marketDataSimple.minMax.MarketRangeCalc;

public class Replay implements Runnable{
	
	Table quotes = null;
	Table trades = null;
	private String quoteDataPath, tradeDataPath;
	private ArrayList<ReplayAbstract> replayBuffer = new ArrayList<>();
	private MarketRangeCalc notifyableObject;
	
	public Replay(String quoteData, String tradeData) {
		this.quoteDataPath = quoteData;
		this.tradeDataPath = tradeData;
	}
	
	public void setNotifyable(MarketRangeCalc notifyObject) {
		this.notifyableObject = notifyObject;
	}
	
	public ArrayList<ReplayAbstract> getReplayBuffer(){
		return this.replayBuffer;
	}
	
	public boolean readHistoryData() {
		try {
			CSVReader reader = new CSVReader();
			this.quotes = reader.read(this.quoteDataPath);
			this.trades = reader.read(this.tradeDataPath);
			return true;
		} catch (Exception e) {
			System.err.println("Could not read data from files.");
			e.printStackTrace();
			return false;
		}
	}
	
	public void runReplay() {
		ReplayManager manager = new ReplayManager(this.quotes, this.trades);
		
		ReplayAbstract nextReplay = null;
		while((nextReplay = manager.getNext(true)) != null) {
			if(nextReplay instanceof ReplayQuote) {
				synchronized (replayBuffer) {
					ReplayQuote q = (ReplayQuote)nextReplay;
					this.replayBuffer.add(q);
					replayBuffer.notifyAll();
				}
			}
			
			if(nextReplay instanceof ReplayTrade) {
				ReplayTrade t = (ReplayTrade)nextReplay;
			}
			
			//Pause this thread until the next task is ready to run
			waitForNextReplay(nextReplay, manager.getNext(false));
		}
	}
	
	
	public static void waitForNextReplay(ReplayAbstract current, ReplayAbstract next) {
		if(next == null) return;
		long millis = 0;
		int nano = 0;
		//Callcalte the differance in seconds and nano
		millis = current.getTime().until(next.getTime(), ChronoUnit.HOURS) * 3600;
		millis += current.getTime().until(next.getTime(), ChronoUnit.MINUTES) * 60;
		millis += current.getTime().until(next.getTime(), ChronoUnit.SECONDS);
		millis *= 1000;
		millis += (current.getTime().until(next.getTime(), ChronoUnit.MILLIS) % 1000);
		nano = (int)(current.getTime().until(next.getTime(), ChronoUnit.NANOS) % 1000000);
		
		//The the calculated time
		try {
			System.out.println("Waiting for " + millis + ":" + nano);
			Thread.sleep(millis, nano);
		} catch (InterruptedException e) {
			System.err.println("Failed to wait for specified time of: " + millis + ":" + nano);
			e.printStackTrace();
		}
	}


	@Override
	public void run() {
		System.out.println("Running replays");
		readHistoryData();
		runReplay();
		this.notifyableObject.running = false;
	}
	
}
