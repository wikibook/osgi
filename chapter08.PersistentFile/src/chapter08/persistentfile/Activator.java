package chapter08.persistentfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.Preferences;
import org.osgi.service.prefs.PreferencesService;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {
	private ServiceTracker tracker;
	private PreferencesService service;
	private static final String COLOR = "color"; //NON-NLS-1
	
	public void start(BundleContext context) throws Exception {
		
		/*
		Properties props = new Properties();
		
		props.put("test","my value");
		
		props.store(new FileOutputStream(context.getDataFile("my.properties")),"test value");
		*/
		
//		File f = context.getDataFile("my.properties");
//		FileOutputStream fos = new FileOutputStream(f);
//		Properties props = new Properties();		
//		props.store(fos,"my comment");
//		
		
//		Properties props = new Properties();
//		props.load(new FileInputStream(context.getDataFile("my.properties")));
		
		tracker = new ServiceTracker(context, PreferencesService.class.getName(), null);
		tracker.open();
		
		// grab the service
		service = (PreferencesService) tracker.getService();
		Preferences preferences = service.getSystemPreferences();
		
		preferences.put(COLOR, "violet");
		
		Preferences pref2 = preferences.node("/abc/test/new test");
		
		System.out.println("My root node name is: [" + preferences.name()+ "]");
		System.out.println("My root absolutePath is: [" + preferences.absolutePath() + "]");
		
		System.out.println("My 2 node name is: [" + pref2.name()+ "]");
		System.out.println("My 2 absolutePath is: [" + pref2.absolutePath() + "]");
		
		pref2.put(COLOR, "violet");
		
		System.out.println("My child node name is: [" + preferences.node("abc").name()+ "]");
		
		Preferences pref3 = preferences.node("abc").node("def/test/asdfasd/asdfasdf");
		pref3.put(COLOR, "violet");
		
		
		
		System.out.println("My 2 node name is: [" + pref3.name()+ "]");
		System.out.println("My 2 absolutePath is: [" + pref3.absolutePath() + "]");
		
		Preferences pref4 = preferences.node("abc").node("def/test");
		pref4.put(COLOR, "violet");
		
		System.out.println("My 4 node name is: [" + pref4.name()+ "]");
		System.out.println("My 4 absolutePath is: [" + pref4.absolutePath() + "]");
		System.out.println("My 4 removed is: [" + pref4.nodeExists("") + "]");
		preferences.node("/abc/def").removeNode();
		pref4.flush();
		
		System.out.println("My 4 node name is: [" + pref4.name()+ "]");
		System.out.println("My 4 absolutePath is: [" + pref4.absolutePath() + "]");
		System.out.println("My 4 removed is: [" + pref4.nodeExists("") + "]");
		
		
		System.out.println("My favourite color is: " + preferences.get(COLOR, "")); 		
	}
	
	public void stop(BundleContext context) throws Exception {
		
	}

}
