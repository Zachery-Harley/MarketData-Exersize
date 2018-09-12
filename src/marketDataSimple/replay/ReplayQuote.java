package marketDataSimple.replay;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.naming.directory.InvalidAttributeValueException;

import com.zacheryharley.zava.structure.Row;

import javafx.util.converter.LocalDateTimeStringConverter;
import marketDataSimple.Quote;

public class ReplayQuote extends ReplayAbstract{
	
	private double bestBid;
	private int bidSize;
	private int bidCount;
	private double bestOffer;
	private int offerSize;
	private int offerCount;
	DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss:SSS");
	
	public ReplayQuote(Row data) throws InvalidAttributeValueException {
		this.exTime = LocalDateTime.parse(data.get(0), format);
		this.time = LocalDateTime.parse(data.get(1), format);
		this.sym = data.get(2);
		this.bestOffer = Double.parseDouble(data.get(3));
		this.offerSize = Integer.parseInt(data.get(4));
		this.offerCount = Integer.parseInt(data.get(5));
		this.bestBid = Double.parseDouble(data.get(6));
		this.bidSize = Integer.parseInt(data.get(7));
		this.bidCount = Integer.parseInt(data.get(8));
		isValid();
	}
	
	public void isValid() throws InvalidAttributeValueException {
		if(sym.length() == 0) {
			throw new InvalidAttributeValueException("sym cannot be blank on quote: " + this.time.toString());
		}
		if(bestOffer <= 0) {
			throw new InvalidAttributeValueException("bestOffer cannot be negative on quote: " + this.time.toString());
		}
		if(offerSize <= 0) {
			throw new InvalidAttributeValueException("offerSize cannot be less than 1 on quote: " + this.time.toString());
		}
		if(offerCount <= 0 ) {
			throw new InvalidAttributeValueException("offerCount cannot be less than 1 on quote: " + this.time.toString());
		}
		if(bestBid <= 0 ) {
			throw new InvalidAttributeValueException("bestBid cannot be negative on quote: " + this.time.toString());
		}
		if(bidSize <= 0 ) {
			throw new InvalidAttributeValueException("bidSize cannot be less than 1 on quote: " + this.time.toString());
		}
		if(bidCount <= 0) {
			throw new InvalidAttributeValueException("bidCount on quote: " + this.time.toString());
		}
	}
	
	public String getSym() {
		return this.sym;
	}
	
	public double getBestBid() {
		return this.bestBid;
	}
	
	public double getBestOffer() {
		return this.bestOffer;
	}
	
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append("Bid: ");
		output.append(bidSize + "@" + bestBid + "\n");
		output.append("Offer: ");
		output.append(offerSize + "@" + bestOffer + "\n");
		return output.toString();
	}
	
}
