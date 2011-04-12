
import javax.ws.rs.Path
import javax.ws.rs.GET
import javax.ws.rs.PathParam
import javax.ws.rs.core.Response
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.CacheControl
import javax.ws.rs.Produces
import javax.ws.rs.core.SecurityContext
import javax.ws.rs.core.Context

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.component.ComponentPlugin;
import org.exoplatform.services.security.Identity;
import org.exoplatform.services.security.IdentityRegistry;

@Path("/system")
@Produces("application/json")
public class SystemInformationService {
  
 
  @GET
  @Path("information")
  public Response getSystemInfo(@Context SecurityContext sc) {
    String groupToCheck = "/platform/administrators";
    SimpleResponseWrapper response = new SimpleResponseWrapper();
    String status = "";
    if (sc.getUserPrincipal() == null || !this.isMemberOf(sc.getUserPrincipal().getName(), groupToCheck) ) {
      response.status = "NOT-ALLOWED";
      } else {
        response.status = "OK";
        response.data = System.getProperties();
     
     }  
       
       CacheControl cacheControl = new CacheControl();
       cacheControl.setNoCache(true);
       cacheControl.setNoStore(true);
       return Response.ok(  response   , MediaType.APPLICATION_JSON).cacheControl(cacheControl).build();    
      }
      
      
      private boolean isMemberOf(String username,String role) {
        ExoContainer container = ExoContainerContext.getCurrentContainer();
        IdentityRegistry identityRegistry = (IdentityRegistry) container.getComponentInstanceOfType(IdentityRegistry.class);
        Identity identity = identityRegistry.getIdentity(username);
        return identity.isMemberOf( role );
      }
      
    }
  
public class SimpleResponseWrapper {
      String status;
      Object data;
}
      
    
