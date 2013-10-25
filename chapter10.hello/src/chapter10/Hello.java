package chapter10;

import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Hello {
	private Printer myPrinter;
	
	public void setMyPrinter(Printer myPrinter) {
		this.myPrinter = myPrinter;
	}

	public void hello(String msg) {
		myPrinter.out("Hello " + msg);
	}
	
	public static void main(String[] args) {
		FileSystemXmlApplicationContext appContext = 
            new FileSystemXmlApplicationContext(new String[] {"spring/spring.xml"});
		
		Hello hello = (Hello) appContext.getBean("hello");
		
		hello.hello("test");
	}
}
