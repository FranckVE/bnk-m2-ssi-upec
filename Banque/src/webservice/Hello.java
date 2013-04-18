package webservice;

//import javax.servlet.ServletContext;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

// POJO, no interface no extends

/**
 * This is Lars' REST server application described at
 * http://www.vogella.de/articles/REST/article.html 3.2. Java class
 * 
 * The class registers itself using @GET. using the @Produces, it defines that
 * it delivers two MIME types, text and HTML. The browser always requests the
 * HTML MIME type. Also shown is how to get a hold of the HTTP ServletContext
 * you'd have if you weren't using Jersey and all of this nice annotation.
 * 
 */

// Sets the path to base URL + /hello
@Path("/login")
public class Hello {

//	@Context
//	ServletContext context;

	// This method is called if request is TEXT_HTML
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String sayPlainTextHello(
			@DefaultValue("test") @QueryParam("login") String log,
			@DefaultValue("test") @QueryParam("password") String pass) {
		// (we don't really want to use ServletContext, just show that we
		// could:)
	//	ServletContext ctx = context;

		// -------------------------------------------------------------------------------------------

		Client c = Client.create();
		WebResource resource = c
				.resource("http://wsbdd-projetcdai.rhcloud.com/WebService/rest/echotest?login="
						+ log + "&password=" + pass);
		String response = resource.get(String.class);

		// return "login =" +log +"   password = "+pass ;
		return response;

		// return "Hello Jersey in plain text";
	}

	// // This method is called if request is HTML
	// @GET
	// @Produces( MediaType.TEXT_HTML )
	// public String sayHtmlHello()
	// {
	// return "<html> "
	// + "<title>" + "Hello Jersey" + "</title>"
	// + "<body><h1>"
	// + "Hello Jersey in HTML"
	// + "</body></h1>"
	// + "</html> ";
	// }

	// This method is called if request is XML
	// @GET
	// @Produces( MediaType.TEXT_XML )
	// public String sayXMLHello()
	// {
	// return "<?xml version=\"1.0\"?>" + "<hello> Hello Jersey in XML" +
	// "</hello>";
	// }
}
