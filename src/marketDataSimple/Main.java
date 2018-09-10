package marketDataSimple;

import java.time.LocalTime;
import java.util.Random;

public class Main {

	static InternalExchange market = new InternalExchange("JPMorgan");
	static Company HSBC = new Company("HSBC");
	static Company VOD = new Company("VOD");
	static Company BT = new Company("BT");
	
	public static void main(String[] args) {
		generateOrders();
		System.out.println("\nDone:\n"+market.toString());
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
	
	public static void generateRandomTrade(Random rand, LocalTime time, Company company) {
		boolean buy = rand.nextBoolean();
		int amount = rand.nextInt(100);
		double cost = (rand.nextDouble() * 10);
		TradeOrder order = new TradeOrder(company, buy, amount, cost);
		order.setTime(time);
		market.pushOrder(order);
	}

}
