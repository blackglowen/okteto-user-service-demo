package com.teraryumapp.ws.rest.config;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
// import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@ApplicationPath("/api/rest")
@OpenAPIDefinition(info = @Info(title = "Teraryum WS REST API", version = "v1.0.0", contact = @Contact(name = "TeraryumApp", url = "http://teraryumapp.com/contact", email = "teraryumapp@gmail.com"), license = @License(name = "Licence: MIT", url = "https://opensource.org/licenses/MIT")))
public class TeraryumAPIConfig extends Application {

}
