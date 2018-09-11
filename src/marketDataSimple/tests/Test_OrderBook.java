package marketDataSimple.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import marketDataSimple.Exchange;
import marketDataSimple.OrderBook;
import marketDataSimple.Ric;
import marketDataSimple.TradeOrder;

class Test_OrderBook {

	OrderBook orderBook;
	Ric HSBC = new Ric("HSBC", "HSB");
	Exchange exec = new Exchange("London");
	TradeOrder order1 = new TradeOrder(HSBC, true, 10, 1);
	
	@BeforeEach
	void setUp() throws Exception {
		orderBook = new OrderBook(HSBC, exec);
	}

	@Test
	void testGetRic() {
		assertEquals(HSBC, orderBook.getRic());
	}

	@Test
	void testPushOrder_buy() {
		TradeOrder order1 = new TradeOrder(HSBC, true, 10, 1);
		orderBook.pushOrder(order1);
		TradeOrder output = orderBook.getBestBid(order1.getUnderline().getClass());
		assertEquals(order1, output);
	}
	
	@Test
	void testPushOrder_sell() {
		TradeOrder order1 = new TradeOrder(HSBC, false, 10, 1);
		orderBook.pushOrder(order1);
		TradeOrder output = orderBook.getBestOffer(order1.getUnderline().getClass());
		assertEquals(order1, output);
	}

	@Test
	void testGetBestBid() {
		TradeOrder worstBid = new TradeOrder(HSBC, true, 10, 1);
		TradeOrder bestBid = new TradeOrder(HSBC, true, 10, 10);
		TradeOrder badBid = new TradeOrder(HSBC, true, 0, 11);
		TradeOrder sellOrder = new TradeOrder(HSBC, false, 10, 12);
		orderBook.pushOrder(worstBid);
		orderBook.pushOrder(bestBid);
		orderBook.pushOrder(badBid);
		orderBook.pushOrder(sellOrder);
		TradeOrder output = orderBook.getBestBid(bestBid.getUnderline().getClass());
		assertEquals(bestBid, output);
	}

	@Test
	void testGetBestOffer() {
		TradeOrder worstOffer = new TradeOrder(HSBC, false, 10, 100);
		TradeOrder bestOffer = new TradeOrder(HSBC, false, 10, 1);
		TradeOrder badOffer = new TradeOrder(HSBC, false, 0, 0.5);
		TradeOrder buyOrder = new TradeOrder(HSBC, true, 10, 0.2);
		orderBook.pushOrder(worstOffer);
		orderBook.pushOrder(bestOffer);
		orderBook.pushOrder(badOffer);
		orderBook.pushOrder(buyOrder);
		TradeOrder output = orderBook.getBestOffer(bestOffer.getUnderline().getClass());
		assertEquals(bestOffer, output);
	}

	@Test
	void testGetBidsOfType() {
		TradeOrder worstOffer = new TradeOrder(HSBC, true, 10, 1);
		TradeOrder bestOffer = new TradeOrder(HSBC, true, 10, 1);
		TradeOrder badOffer = new TradeOrder(HSBC, true, 0, 0.5);
		TradeOrder buyOrder = new TradeOrder(HSBC, false, 10, 10);
		orderBook.pushOrder(worstOffer);
		orderBook.pushOrder(bestOffer);
		orderBook.pushOrder(badOffer);
		orderBook.pushOrder(buyOrder);
		int count = orderBook.getBidsOfType(badOffer.getUnderline().getClass()).size();
		assertEquals(3, count);
	}

	@Test
	void testGetOffersOfType() {
		TradeOrder worstOffer = new TradeOrder(HSBC, false, 10, 100);
		TradeOrder bestOffer = new TradeOrder(HSBC, false, 10, 1);
		TradeOrder badOffer = new TradeOrder(HSBC, false, 0, 0.5);
		TradeOrder buyOrder = new TradeOrder(HSBC, true, 10, 0.2);
		orderBook.pushOrder(worstOffer);
		orderBook.pushOrder(bestOffer);
		orderBook.pushOrder(badOffer);
		orderBook.pushOrder(buyOrder);
		int count = orderBook.getOffersOfType(badOffer.getUnderline().getClass()).size();
		assertEquals(3, count);
	}

	@Test
	void testFindTrade_equal() {
		TradeOrder buyer = new TradeOrder(HSBC, true, 10, 10);
		TradeOrder seller = new TradeOrder(HSBC, false, 10, 10);
		orderBook.pushOrder(buyer);
		orderBook.findTrade(seller);
		assertEquals(0, buyer.getRemainingSize());
	}
	
	@Test
	void testFindTrade_sellerG() {
		TradeOrder buyer = new TradeOrder(HSBC, true, 10, 10);
		TradeOrder seller = new TradeOrder(HSBC, false, 100, 10);
		orderBook.pushOrder(buyer);
		orderBook.findTrade(seller);
		assertEquals(0, buyer.getRemainingSize());
	}
	
	@Test
	void testFindTrade_buyerG() {
		TradeOrder buyer = new TradeOrder(HSBC, true, 100, 10);
		TradeOrder seller = new TradeOrder(HSBC, false, 10, 10);
		orderBook.pushOrder(buyer);
		orderBook.findTrade(seller);
		assertEquals(0, seller.getRemainingSize());
	}

}
