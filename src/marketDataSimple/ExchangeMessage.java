package marketDataSimple;

import java.time.LocalDateTime;

import com.zacheryharley.zava.structure.Row;

public abstract class ExchangeMessage {

	protected String sym;
	protected LocalDateTime time;
	protected LocalDateTime exTime;
	
	
	public LocalDateTime getTime() {
		return this.time;
	}
	
	public void setTime(LocalDateTime time) {
		this.exTime = time;
		Main.time.plusNanos(6);
		this.time = Main.time;
	}
	
	public String getSym() {
		return this.sym;
	}
	
	public abstract Row toCSV();
}
