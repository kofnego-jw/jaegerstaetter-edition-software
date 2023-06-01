package at.kulinz.jaegerstaetter.edition.webapp.generic.web;

import at.kulinz.jaegerstaetter.frontendmodel.dtomsg.AppConfigMsg;
import at.kulinz.jaegerstaetter.frontendmodel.webapi.AppConfigControllerApi;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("edition")
public class EditionAppConfigController implements AppConfigControllerApi {

    @Override
    public AppConfigMsg getAppConfig() {
        return new AppConfigMsg(true);
    }
}
