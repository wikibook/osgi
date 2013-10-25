package chapter07.configurationconsole;


import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

public class CmCommandProcessor {
	private BundleContext context;	
	private ConfigurationAdmin cm;
	
	
	// http://blog.bpsite.net/item/71/Switch on String in Java.html  [ http://durl.kr/gst ]
	// - by Peter Boughton 
	private enum Command
	{ help , list , get , put , putv, del , create , createf ,  nocmd ;
		public static Command fromString(String Str)
		{
			try {return valueOf(Str);}
			catch (Exception ex){return nocmd;}
		}
	};
	
	public CmCommandProcessor(BundleContext context) {
		this.context = context;
		
		ServiceReference serviceReference = context.getServiceReference(ConfigurationAdmin.class.getName());
		if (serviceReference != null) {
			cm = (ConfigurationAdmin) context.getService(serviceReference);
		}		
	}	
	
	public void execute(List<String> args) {		
		if (args.size() >= 1) {
			String cmd = (String) args.get(0);
						
			switch (Command.fromString(cmd)) {
			case list:
				runCmdList(args);
				break;
			case get:
				runCmdGet(args);
				break;				
			case put:
				runCmdPut(args,false);
				break;
			case putv:
				runCmdPut(args,true);
				break;						
			case del:
				runCmdDel(args);
				break;
			case create:
				runCmdCreate(args, false);
				break;				
			case createf:
				runCmdCreate(args, true);
				break;				

			default:
				runHelp();
				break;
			}
		}
		else {
			runHelp();
		}
	}

	private void runCmdList(List<String> args) {		
		try {
			Configuration[] configurations = cm.listConfigurations(null);
			out("Configuration List : PID | bundleLocation");
			printLine();
			if (configurations == null || configurations.length == 0) {
				out("\t없음");				
			} else {		
				for (Configuration c: configurations) 
					out(c.getPid() + "\t" + c.getBundleLocation());
			}
			printLine();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}	

	private void runCmdGet(List<String> args) {
		String pid = null;
		
		if (args.size() >= 2) 
			pid = (String) args.get(1);
		else
			pid = "*";  // PID 지정 안할경우 모든것을 찾아서 출력한다.
		
		// getConfiguration 은 없을경우 생성하는 기능이 있으므로 listConfiguration 을 이용하여 가져오도록 한다.
		int cfgCount = 0;
		try {
			Configuration[] cfgs = cm.listConfigurations("(service.pid=" + pid + ")");
			
			for (Configuration cfg : cfgs) {
				printConfiguration(cfg);
				cfgCount++;
			}
			
		} catch (Exception e) {}
		
		if (cfgCount == 0)		
			err("no configuration for pid '" + pid + "' (use 'create' to create one)");
		else
			out("Total " + cfgCount + " Configurations");
	}
	
	private void printConfiguration(Configuration cfg)
	{		
	    String format = "%-20s %-40s" + " %s";
	    printLine();
        out(String.format(format, "key", "value", "type"));
        out(String.format(format, "------", "------", "------"));
        
        Dictionary props = cfg.getProperties();
        Enumeration keys = props.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            Object value = props.get(key);
            out(String.format(format, key, value != null? value.toString(): "<null>", value != null? value.getClass().getName(): ""));
        }
        printLine();
	}
	
	private void runCmdCreate(List<String> args, boolean isFactory) {		
		String pid = null;
		if (args.size() >= 2) {
			pid = (String) args.get(1);
			
			try {
				Configuration config;
				if (isFactory) {
					if (args.size() == 3) 					
						config = cm.createFactoryConfiguration(pid, getBundleLocation((String) args.get(2)));
					else
						config = cm.createFactoryConfiguration(pid);
					
					out("FactoryConfiguration created with PID : " + config.getPid());
				} else {
					if (args.size() == 3) 					
						cm.getConfiguration(pid, getBundleLocation((String) args.get(2)));
					else
						cm.getConfiguration(pid);
				}				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else
			err("PID missing");
	}
	
	private String getBundleLocation(String arg){
		Long bundleId = -1L;
		try {
			bundleId = Long.parseLong(arg);
		} catch (Exception e) {						
		}
		
		if (bundleId != -1L) {						
			for (Bundle bd : context.getBundles()) {
				if (bd.getBundleId() == bundleId )					
					return bd.getLocation();
			}
		}
		return arg;
	}

	private void runCmdDel(List<String> args) {
		String pid = null;
		if (args.size() >= 2) { 
			pid = (String) args.get(1);
			
			try {
				cm.getConfiguration(pid).delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			err("PID missing");
	}
	
	private void runCmdPut(List<String> args,boolean parseValue) {
		
	    if (args.size() >= 3) {
	    	String pid = (String) args.get(1);
            Object value = null;
	        if (parseValue) {
	            value = parseValue((String) args.get(3));
	        }
	        else {
	            String stringValue = "";
	            // 스트링 앞뒤에 "" 입력을 안한 경우 합쳐서 하나의 문자열로 만든다. 
	            for (int i = 3; i < args.size(); i++) {
	                if (i > 3)	               
	                    stringValue += " ";
	                stringValue += (String) args.get(i);
	            }
	            value = stringValue;
	        }
	        if (value != null) {
	            Configuration config;
				try {
					config = cm.getConfiguration(pid);
		            Dictionary properties = config.getProperties();
		            if (properties == null)
		                properties = new Hashtable();

		            properties.put(args.get(2), value);
		            config.update(properties);
				} catch (Exception e) {
					e.printStackTrace();
				}

	        }
	    }
	    else
	        err("cm put: missing argument(s), expected <pid> <key> <value>");
	       
	}
	
	private void runCmdPutF(List<String> args) {		
		if (args.size() == 5) {
	    	String pid = (String) args.get(1);
            Object value = parseValue(args.get(4));
	        
	        if (value != null) {
	            Configuration config;
				try {
					config = cm.createFactoryConfiguration(pid, getBundleLocation((String) args.get(2)));
		            Dictionary properties = config.getProperties();
		            if (properties == null)
		                properties = new Hashtable();

		            properties.put(args.get(3), value);
		            config.update(properties);
				} catch (Exception e) {
					e.printStackTrace();
				}

	        }
	    }
	    else
	        err("cm put: missing argument(s), expected <pid> <bundleid> <key> <value>");
		
	}
	
	private Object parseValue(String rawValue) {
	    Object value;        
        
        try {
	        if (rawValue.equalsIgnoreCase("true")) {
	            return true;
	        }
	        else if (rawValue.equalsIgnoreCase("false")) {
	            return false;
	        }        
	        else if (rawValue.endsWith("l") || rawValue.endsWith("L")) {
	            value = Long.parseLong(rawValue.substring(0, rawValue.length() - 1));
	        }
	        else if (rawValue.endsWith("f") || rawValue.endsWith("F")) {
	            value = Float.parseFloat(rawValue.substring(0, rawValue.length() - 1));
	        }
	        else if (rawValue.endsWith("d") || rawValue.endsWith("D")) {
	            value = Double.parseDouble(rawValue.substring(0, rawValue.length() - 1));
	        }
	        else if (rawValue.endsWith("i") || rawValue.endsWith("I")) {
	            value = Integer.parseInt(rawValue.substring(0, rawValue.length() - 1));
	        } 
	        else {
	        	value = Integer.parseInt(rawValue);
	            if (value == null) {
	                err("cannot parse argument \"" + rawValue + "\"");
	            }
	        }
        } catch (NumberFormatException e) {
        	 err("invalid argument (\"" + rawValue + "\"), cannot parse");
             return null;
        }
        return value;
    }


	private void runHelp() {			
		err("Usage for CM Command:");
		err(" cm help                  Show help");
		err(" cm list                  Show Configuration List");
        err(" cm get <pid>             Show Configuration for <PID>");
        err(" cm del <pid>             Delete Configuration for <PID>");
        err(" cm create <pid> [<loc>]  Create Configuration for <PID> ( with optional bundle location )");
        err(" cm put <pid> key value   Set Configuration with Key & Value to <PID>");        
        err(" cm putv <pid> key value  Set \"simple\" value to <PID>. Default is integer"); 
        err("                          true/false , i (Integer), l (Long), f (Float), d (Double)"); 
        err("                          Boolean Value : true or false");
        err("                          Integer Value : 43125 or 43125i");        
        err("                          Double  Value : 43.123d");
	}
	
	protected void out(String line) {
		System.out.println(line);
	}	

	private void printLine() {
		out("------------------------------------------------------------------------------------");
	}	
	
	protected void err(String line) {
		System.err.println(line);
	}
}
