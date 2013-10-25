package chapter10;

public class ErrorPrinter implements Printer {
	
	public void out(String msg) {
		System.err.println("[Err]" + msg);
	}

}
