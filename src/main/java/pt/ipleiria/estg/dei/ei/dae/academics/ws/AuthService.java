package pt.ipleiria.estg.dei.ei.dae.academics.ws;

import pt.ipleiria.estg.dei.ei.dae.academics.dto.AuthDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dto.NewPasswordDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dto.UserDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.UserBean;
import pt.ipleiria.estg.dei.ei.dae.academics.security.Authenticated;
import pt.ipleiria.estg.dei.ei.dae.academics.security.TokenIssuer;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

@Path("auth")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class AuthService {
    @Inject
    private TokenIssuer issuer;

    @EJB
    private UserBean userBean;

    @Context
    private SecurityContext securityContext;

    @POST
    @Path("/login")
    public Response authenticate(@Valid AuthDTO authDTO) {
        if (userBean.canLogin(authDTO.getUsername(), authDTO.getPassword())) {
            String token = issuer.issue(authDTO.getUsername());
            return Response.ok(token).build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @POST
    @Authenticated
    @Path("/set-password")
    public Response setPassword(@Valid NewPasswordDTO passwordDTO) {
        Principal principal = securityContext.getUserPrincipal();
        if (userBean.updatePassword(principal.getName(), passwordDTO.getOldPassword(), passwordDTO.getNewPassword()))
            return Response.ok().build();
        else
            return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Authenticated
    @Path("/user")
    public Response getAuthenticatedUser() {
        var username = securityContext.getUserPrincipal().getName();
        var user = userBean.findOrFail(username);
        return Response.ok(UserDTO.from(user)).build();
    }
}