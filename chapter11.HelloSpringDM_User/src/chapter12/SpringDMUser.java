package chapter12;

import chapter11.HelloSpringDM;

public class SpringDMUser {
	private HelloSpringDM helloSpringDM;

	public void setHelloSpringDM(HelloSpringDM helloSpringDM) {
		this.helloSpringDM = helloSpringDM;
	}
	
	public void start()
	{
		helloSpringDM.start();		
	}

}
