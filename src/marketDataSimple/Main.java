package marketDataSimple;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import com.zacheryharley.zava.io.CSVWriter;
import com.zacheryharley.zava.structure.Table;

public class Main {

	public static LocalDateTime time = LocalDateTime.now();
	public static Table tradeData;
	public static Table quoteData;
	static InternalExchange market = new InternalExchange("JPMorgan");
	static Company HSBC = new Company("HSBC");
	static Company VOD = new Company("VOD");
	static Company BT = new Company("BT");
	
	public static void main(String[] args) {
		tradeData = new Table();
		quoteData = new Table();
		
		generateOrders();
		//System.out.println("\nDone:\n"+market.toString());
		
		//Save the table output
		CSVWriter quoteWriter = new CSVWriter("quoteData.csv");
		CSVWriter tradeWriter = new CSVWriter("tradeData.csv");
		quoteWriter.write(quoteData);
		tradeWriter.write(tradeData);
	}
	
	public static void generateOrders() {
		Random ran1 = new Random();
		Random ran2 = new Random();
		Random ran3 = new Random();
		
		ran1.setSeed(System.nanoTime());
		ran2.setSeed(System.currentTimeMillis());
		ran3.setSeed(System.nanoTime()/2);
		
		for(int i = 0; i < 33; i++) {
			Main.time = Main.time.plus(ran1.nextInt(50), ChronoUnit.MILLIS);
			generateRandomTrade(ran1, time, HSBC);
			Main.time = Main.time.plus(ran1.nextInt(50), ChronoUnit.MILLIS);
			generateRandomTrade(ran2, time, VOD);
			Main.time = Main.time.plus(ran1.nextInt(50), ChronoUnit.MILLIS);
			generateRandomTrade(ran3, time, BT);
		}
	}
	
	public static void generateRandomTrade(Random rand, LocalDateTime time, Company company) {
		boolean buy = rand.nextBoolean();
		int amount = rand.nextInt(100);
		double cost = (rand.nextDouble() * 10);
		TradeOrder order = new TradeOrder(company.getName(), buy, amount, cost);
		order.setTime(time);
		market.pushOrder(order);
		tradeData.addRow(order.toCSV());
	}

}
