/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paycraftsystems.kingstrivia.exceptions;

/**
 *
 * @author root
 */

import com.paycraftsystems.kingstrivia.resources.ErrorCodes;
import java.math.BigDecimal;
import javax.json.Json;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class KingsTrivialException extends WebApplicationException {

    public KingsTrivialException(String message, Throwable cause) {
        super(Response.status(400).header("message", message).header("cause", cause.getMessage()).build());
    }
    
    public KingsTrivialException(String message) {
        super(Response.status(400).header("message", message).build());
    }
    
    public KingsTrivialException(int status, String message, Throwable cause) {
        super(Response.status(status).header("message", message).header("cause", cause.getMessage()).entity(Json.createObjectBuilder().add("errorDesc", ErrorCodes.doErroDesc(status)).build()).build());
    }
    
    public KingsTrivialException(int status, String cause) {
        
        
        super(Response.status(status).header("message", cause).header("cause", cause).entity(Json.createObjectBuilder().add("errorDesc", ErrorCodes.doErroDesc(status)).build()).build());
    }

}
