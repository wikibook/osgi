package chapter03.autoinstaller;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	
	AutoInstaller installer;
	
	public void start(BundleContext context) throws Exception {
		System.out.println("Bundle Autoinstaller Starting");
		
		installer = new AutoInstaller(context);
		new Thread(installer).start();		
	}
	
	public void stop(BundleContext context) throws Exception {
		installer.close();
		System.out.println("Bundle Autoinstaller Exiting");
	}

}
