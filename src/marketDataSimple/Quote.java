package marketDataSimple;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import com.zacheryharley.zava.structure.Row;

import javafx.util.converter.LocalDateStringConverter;
import javafx.util.converter.LocalDateTimeStringConverter;

public class Quote extends ExchangeMessage{

	protected Exchange exchange;
	protected double bestOffer;
	protected double bestBid;
	protected int bidCount;
	protected int bidSize;
	protected int offerCount;
	protected int offerSize;
	
	public Quote(String company, Exchange exchange, double bestOffer, int offerSize, int offerCount, double bestBid, int bidSize, int bidCount) {
		this.sym = company;
		this.exchange = exchange;
		this.bestOffer = bestOffer;
		this.bestBid = bestBid;
		this.bidCount = bidCount;
		this.bidSize = bidSize;
		this.offerCount = offerCount;
		this.offerSize = offerSize;
	}
	
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append("Updated Quote for: "+sym+"\n");
		output.append("Exchange: "+this.exchange.getName()+"\n");
		output.append("Best Offer: £"+this.bestOffer+"\n");
		output.append("Best Bid: £"+this.bestBid+"\n");
		return output.toString();
	}

	@Override
	public Row toCSV() {
		Row output = new Row();
		output.add(exTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss:SSS")));
		output.add(time.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss:SSS")));
		output.add(sym);
		output.add(Double.toString(bestOffer));
		output.add(Integer.toString(offerSize));
		output.add(Integer.toString(offerCount));
		output.add(Double.toString(bestBid));
		output.add(Integer.toString(bidSize));
		output.add(Integer.toString(bidCount));
		return output;
	}
}
