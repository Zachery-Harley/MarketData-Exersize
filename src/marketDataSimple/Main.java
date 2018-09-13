package marketDataSimple;

import java.time.LocalTime;
import java.util.Random;

public class Main {

	static InternalExchange market = new InternalExchange("JPMorgan");
	static Ric HSBC = new Ric("HSBC", "HSBC");
	static Ric VOD = new Ric("VOD", "Vodafone");
	static Ric BT = new Ric("BT", "British Telecomunications");
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		market.newOrderBook(VOD);
		market.newOrderBook(BT);
		market.newOrderBook(HSBC);
		
		generateOrders();
		System.err.println(System.currentTimeMillis() - start);
	}
	
	public static void generateOrders() {
		LocalTime time = LocalTime.now();
		Random ran1 = new Random();
		Random ran2 = new Random();
		Random ran3 = new Random();
		
		ran1.setSeed(System.nanoTime());
		ran2.setSeed(System.currentTimeMillis());
		ran3.setSeed(System.nanoTime()/2);
		
		for(int i = 0; i < 33; i++) {
			time.plusMinutes(i);
			generateRandomTrade(ran1, time, HSBC);
			generateRandomTrade(ran2, time, VOD);
			generateRandomTrade(ran3, time, BT);
		}
	}
	
	public static void generateRandomTrade(Random rand, LocalTime time, Ric company) {
		boolean buy = rand.nextBoolean();
		int amount = rand.nextInt(100);
		double cost = (rand.nextDouble() * 10);
		TradeOrder order = new TradeOrder(company, buy, amount, cost, EqInstrument.class);
		market.pushOrder(order);
	}

}
