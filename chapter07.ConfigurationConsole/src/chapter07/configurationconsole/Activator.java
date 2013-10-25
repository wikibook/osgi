package chapter07.configurationconsole;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator, CommandProvider {
	private BundleContext bundleContext;
	private ServiceRegistration registration;
	
	public void start(BundleContext context) throws Exception {
		bundleContext = context;		
		// 커맨드 프로바이더로 등록
		context.registerService(CommandProvider.class.getName(), this, null);
	}
	
	public void stop(BundleContext context) throws Exception {
		registration.unregister();
	}

	public String getHelp() {
		return "\tcm - Console command for Configuration Admin\n";		
	}
	
	public void _cm(CommandInterpreter ci) 
	{	
		List<String> args = new ArrayList<String>();
		String arg = null;
		while ((arg = ci.nextArgument()) != null) {			
			args.add(arg);
		}
		
		new CmCommandProcessor(bundleContext).execute(args);
	}
	
	public void _hello(CommandInterpreter ci)
	{
		System.out.println("Hello " + ci.nextArgument());	
	}

}
