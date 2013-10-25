package chapter03.autoinstaller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

public class AutoInstaller extends Thread {
	BundleContext context;
	boolean working = true;
	
	int delay = 2000;	
	File jardir;
	
	public AutoInstaller(BundleContext context) {
		super();
		this.context = context;
		
		jardir = new File("./bundles");
		jardir.mkdirs();
	}

	public void close()
	{		
		working = false;
		interrupt();
		try {
			join(10000);
		} catch (InterruptedException ie) {
			
		}
	}


	public void run() {
		System.out.println("Starting AutoInstaller Worker");
		while (working)
			try {
				// 파일탐색
				Map<String,File> bundlesInDir = getBundlesInDir(jardir);
				
				// 파일설치
				installBundles(bundlesInDir, jardir);
				
				Thread.sleep(delay);
			} catch (InterruptedException e) {

			} 
		System.out.println("Quitting AutoInstaller Worker");
	}
	
	
	private void installBundles(Map<String, File> bundlesInDir, File dir) {
		Bundle bundles[] = context.getBundles();
		
		for (Bundle bundle : bundles) 
		{			
			String location = bundle.getLocation();
			File file = (File) bundlesInDir.get(location);			
			if (file != null) { //설치된적이 있는 파일이면				
				if (file.lastModified() > bundle.getLastModified()) // 최종 설치후에 변경되었다면
				{					
					try {
						InputStream in;
						in = new FileInputStream(file);						
						bundle.update(in);											
						bundle.start();
						in.close();
						System.out.println("번들 업데이트 : " + location);
					} catch (Exception e) {
						System.out.println("번들 설치실패 : " + e);
					}					
				} else if (bundle.getState() != Bundle.ACTIVE)
					try {
						bundle.start();
					} catch (BundleException e) {
						System.out.println("번들 시작실패 : " + e);						
					}
				
				bundlesInDir.remove(location); // 이 번들은 처리가 되었으므로 리스트에서 지운다.
			} else {
				if (bundle.getLocation().startsWith(dir.getAbsolutePath())) {
					try {
						bundle.uninstall();						
						System.out.println("번들 삭제완료 : " + location);
					} catch (Exception e) {
						System.out.println("번들 설치실패 : " + e);
					}
				}
			}			
		}
		
		for(File file : bundlesInDir.values())
		{			
			try {
				InputStream in = new FileInputStream(file);
				Bundle bundle = context.installBundle(file.getAbsolutePath(),in);		
				in.close();			
				bundle.start();
				System.out.println("번들 설치완료 : " + file.getAbsolutePath());	
			} catch (Exception e) {
				System.out.println("번들 설치실패 : " + e);				
			}
		}	
	}

	private Map<String,File> getBundlesInDir(File dir) {
		Map<String,File> bundles = new HashMap<String,File>();
		String filelist[] = dir.list();
		
		for (String jarfile : filelist) {			
			File file = new File(dir, jarfile);
			if (jarfile.endsWith(".jar")) {
				bundles.put(file.getAbsolutePath(), file);
			}
		}	
		
		return bundles;
	}
	
	
	

}
