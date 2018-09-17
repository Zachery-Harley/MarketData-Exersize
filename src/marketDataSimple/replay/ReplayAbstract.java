package marketDataSimple.replay;

import java.time.LocalDateTime;

public class ReplayAbstract {

	protected String sym;
	protected LocalDateTime time;
	protected LocalDateTime exTime;
	
	public LocalDateTime getTime() {
		return this.time;
	}
	
	public String getSym() {
		return this.sym;
	}
}
