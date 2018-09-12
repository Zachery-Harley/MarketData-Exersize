package marketDataSimple.replay;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.naming.directory.InvalidAttributeValueException;

import com.zacheryharley.zava.structure.Row;

public class ReplayTrade extends ReplayAbstract{
	DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss:SSS");
	
	private boolean isBuyOrder;
	private int size;
	private double price;
	private boolean condition;
	
	public ReplayTrade(Row data) throws InvalidAttributeValueException {
		//exTime, time, sym, isBuy, price, size, condidtion
		this.exTime = LocalDateTime.parse(data.get(0), format);
		this.time = LocalDateTime.parse(data.get(1), format);
		this.sym = data.get(2);
		this.isBuyOrder = Boolean.parseBoolean(data.get(3));
		this.price = Double.parseDouble(data.get(4));
		this.size = Integer.parseInt(data.get(5));
		this.condition = Boolean.parseBoolean(data.get(6));
		isValid();
	}
	
	public void isValid() throws InvalidAttributeValueException {
		if(sym.length() == 0) {
			throw new InvalidAttributeValueException("sym cannot be blank on trade: " + this.time.toString());
		}
		if(price <= 0) {
			throw new InvalidAttributeValueException("price cannot be less than 0 on trade: " + this.time.toString());
		}
		if(size <= 0) { 
			throw new InvalidAttributeValueException("size cannot be less than 1 on trade: " + this.time.toString());
		}
	}
	
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append("Trade ");
		output.append(isBuyOrder ? " buy ":"sell ");
		output.append(size + "@" + price);
		return output.toString();
	}
}
