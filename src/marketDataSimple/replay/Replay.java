package marketDataSimple.replay;

import java.nio.file.NoSuchFileException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import javax.naming.directory.InvalidAttributeValueException;

import com.zacheryharley.zava.io.CSVReader;
import com.zacheryharley.zava.structure.Table;

import marketDataSimple.minMax.MarketRange;
import marketDataSimple.minMax.MarketRangeCalc;

public class Replay {
	
	public static void main(String[] args) {
		Table quoteData = null;
		Table tradeData = null;
		MarketRangeCalc rangeCalculator = new MarketRangeCalc();
		
		try {
			CSVReader csvReader = new CSVReader("quoteData.csv");
			quoteData = csvReader.read();
			tradeData = csvReader.read("tradeData.csv");
		} catch(Exception e) {
			System.err.println("Could not read data from file.");
			e.printStackTrace();
			System.exit(1);
		}
		
		ReplayManager replayMan = new ReplayManager(quoteData, tradeData);
		try {
			LocalDateTime startTime = new ReplayTrade(tradeData.getRow(0)).getTime();
		} catch (InvalidAttributeValueException e) {
			System.err.println("Cannot get start time due to first trade error: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		
		ReplayAbstract nextReplay = null;
		while((nextReplay = replayMan.getNext(true)) != null) {
			if(nextReplay instanceof ReplayQuote) {
				ReplayQuote q = (ReplayQuote)nextReplay;
				rangeCalculator.processQuote(q);
			}
			
			if(nextReplay instanceof ReplayTrade) {
				ReplayTrade t = (ReplayTrade)nextReplay;
			}
			//Wait for the next replay to be ready
			waitForNextReplay(nextReplay, replayMan.getNext(false));
		}
		
		rangeCalculator.printSummery();
		
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
	
}
