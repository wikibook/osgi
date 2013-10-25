package chapter07.gameserver;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.PreferencesService;
import org.osgi.service.prefs.Preferences;

public class Activator implements BundleActivator {

	private ServiceTracker tracker;
	private PreferencesService service;

	public void start(BundleContext context) throws Exception {
		tracker = new ServiceTracker(context, PreferencesService.class.getName(), null);
		tracker.open();		
		service = (PreferencesService) tracker.getService();
		
		// 시스템 루트 Preferences 를 가져온다.
		Preferences prefs = service.getSystemPreferences();				
		prefs.put("ServerName", "Gul'dan");
		
		// 하위노드를 한번에 생성한다. 
		prefs = prefs.node("Guild/Heroes");		
		prefs.put("Master","Chris");
		
		 
		// 유저 루트 Preferences 를 가져온다.
		prefs = service.getUserPreferences("");
		prefs.putInt("DefaultLife",12000);
		
		
		// 유저 Chris 의 루트 Preferences 를 가져온다.
		prefs = service.getUserPreferences("Chris");
		prefs = prefs.node("Character1");
		prefs.put("Name","Warrior");
		prefs.putInt("Life",18900);
		
		prefs = prefs.node("Item");
		prefs = prefs.node("Knife");
		prefs.putInt("Max Damage",353);
		prefs.putInt("Min Damage",235);
		prefs.putFloat("Damage per Second", 163.3F);
		
		prefs = prefs.node("/Character1/Talents");
		prefs.putInt("Combat", 25);
		prefs.putInt("Assassination", 20);
				
		try {
			service.getSystemPreferences().flush();
			service.getUserPreferences("").flush();
		} catch (BackingStoreException e) {			
			e.printStackTrace();
		}	
		
		
		System.out.println("Printing System Preferences -----");
		printPreferences(service.getSystemPreferences());
		
		System.out.println("Printing User Preferences -----");
		printPreferences(service.getUserPreferences(""));
	}

	private void printPreferences(Preferences prefs) {
				
		try {			
			// 속성출력
			for (String key : prefs.keys() ) {
				System.out.println("[" + prefs.absolutePath() + " ] " + key + " : " + prefs.get(key, ""));			
			}
			
			// 모든 하위노드 출력
			for (String child : prefs.childrenNames())
				printPreferences(prefs.node(child));
			
		} catch (BackingStoreException e) {			
			e.printStackTrace();
		}		
		
	}

	public void stop(BundleContext context) throws Exception {
		// clean up
		tracker.close();
		tracker = null;
		
		service = null;
	}

}
