package chapter09.webadmin;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.MessageFormat;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.commons.fileupload.FileItemFactory;
//import org.apache.commons.fileupload.FileItemIterator;
//import org.apache.commons.fileupload.FileItemStream;
//import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleException;

public class WebAdminServlet extends HttpServlet {
	
	private BundleContext bundleContext;
	private String contextPath;	
	private static final String HTML_HEADER = 
		"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"xhtml1-transitional.dtd\">"
        + "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
        + "<head>"
        + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">"        
        + "<title>Web Administrator for OSGi</title>"          
        + "<link href=\"{0}/res/admin.css\" rel=\"stylesheet\" type=\"text/css\">"
        + "</head>"
        + "<body>"        
        + "<h1>Web Administrator for OSGi</h1>" ;
	
	private static final String TABLE_HEADER = "<table width='100%'><tr>"
		+ "<th width='20px'>Id</th>"
		+ "<th width='*'>Bundle Name</th>"
		+ "<th width='80px'>Status</th>"
		+ "<th width='200px'>Action</th></tr>";
	
	private static final String TABLE_ROW = "<tr>"
		   + "<td>{0}</td>"
		   + "<td class=tdname>{1}</td>"
		   + "<td>{2}</td>"
		   + "<td>"
		   + "<form name=\"form_{0}\" method='post'>"
		   + "<input type='hidden' name='action' value='start' />"
		   + "<input type='hidden' name='bundleId' value=\"{0}\" />"
		   + "<input class='submit' type='submit' value='Start' {3}/>"
		   + "</form>"
		   + "<form name=\"form_{0}\" method='post'>"
		   + "<input type='hidden' name='action' value='stop' />"
		   + "<input type='hidden' name='bundleId' value=\"{0}\" />"
		   + "<input class='submit' type='submit' value='Stop' {4}/>"
		   + "</form>"
		   + "<form name=\"form_{0}\" method='post'>"
		   + "<input type='hidden' name='action' value='uninstall' />"
		   + "<input type='hidden' name='bundleId' value=\"{0}\" />"
		   + "<input class='submit' type='submit' value='Uninstall' />"
		   + "</form>"
		   + "</td>"
		   + "</tr>";
	
	private static final String TABLE_INSTALL = "<tr><td colspan=4>"
		   + "<form method='post' enctype=\"multipart/form-data\">"
		   + "<input type='hidden' name='action' value='install' />"
		   + "<input class='input' type='file' name='bundlefile' style='width:500px; background:#EFEFEF'>  &nbsp; "		   
		   + "<input class='submit' type='submit' value='Install Bundle' />"
		   + "</form>"
		   + "</td>"
		   + "</tr>";
	
	private static final String HTML_FOOTER = "<h4> Copyright ¨Ï Good Company </h4>" 
		+ "</div>"
		+ "</body>"
		+ "</html>";
	
	public WebAdminServlet(BundleContext bundleContext, String contextPath) {
		this.bundleContext = bundleContext;
		this.contextPath = contextPath;
	}
	
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException,  IOException
    {   
        response.setContentType( "text/html" );
        PrintWriter pw = response.getWriter();
        String header = MessageFormat.format( HTML_HEADER, new Object[]  { contextPath } );        
        pw.println( header );        
        renderContent(request, response , pw);	    
        pw.println ( HTML_FOOTER );        
	}
    
	private enum Command
	{ start , stop , uninstall , install , nocmd ;
		public static Command fromString(String Str)
		{
			try {return valueOf(Str);}
			catch (Exception ex){return nocmd;}
		}
	};
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response ) throws ServletException,  IOException 
    {   
    	String action = null;
    	String bundleId = null;
    	
    	if (ServletFileUpload.isMultipartContent(request)) {
    		action = "install";
    	} else {    		
        	action = request.getParameter("action");
        	bundleId = request.getParameter("bundleId");        	
    	}
    	
    	try {
	    	switch (Command.fromString(action)) {
	    	case start:
	    		bundleContext.getBundle(Long.parseLong(bundleId)).start();
	    		break;
	    	case stop:
	    		bundleContext.getBundle(Long.parseLong(bundleId)).stop();
	    		break;
	    	case uninstall:
	    		bundleContext.getBundle(Long.parseLong(bundleId)).uninstall();
	    		break;
	    	case install:
	    		
	    		ServletFileUpload upload = new ServletFileUpload();
	    		
	    		try {
		    		FileItemIterator iter = upload.getItemIterator(request);
		    		while (iter.hasNext()) {
		    		    FileItemStream item = iter.next();
		    		    String name = item.getFieldName();
		    		    if (name.equals("bundlefile")) {
		    		    	bundleContext.installBundle(item.getName(), item.openStream());		    		    	
		    		    }
		    		}
	    		} catch(Exception e) {
	    			
	    		}
	    		break;	    		
	    	case nocmd:
	    		return ;
	    	}    	
    	} catch (BundleException e) {
    		e.printStackTrace();
    	}    	 	
    	
    	doGet(request,response);
    }

	private void renderContent(HttpServletRequest request, HttpServletResponse response, PrintWriter pw) {
		pw.println(TABLE_HEADER);		
				
		for (Bundle bnd : bundleContext.getBundles()){
			
			pw.println(MessageFormat.format(TABLE_ROW, new Object[] {
				bnd.getBundleId(),
				bnd.getSymbolicName(),
				getStateName(bnd.getState()),
				(bnd.getState() == Bundle.ACTIVE) ? "disabled" : "",
				(bnd.getState() == Bundle.RESOLVED) ? "disabled" : ""
			}));			
		}	
		pw.println(TABLE_INSTALL);		
		pw.println("</table>");
	}
	
	private String getStateName(int state) {		
		switch (state) {			
			case Bundle.INSTALLED :
				return "INSTALLED"; 

			case Bundle.ACTIVE :
				return "ACTIVE";			

			case Bundle.RESOLVED :
				return "RESOLVED";

			case Bundle.STARTING :				
				return "STARTING"; 
				
			case Bundle.STOPPING :
				return "STOPPING"; 			 

				
			default :
				return Integer.toHexString(state);
		}
	}
}
