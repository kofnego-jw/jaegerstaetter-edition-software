package at.kulinz.jaegerstaetter.frontendmodel.webapi;

import at.kulinz.jaegerstaetter.frontendmodel.dtomsg.AppConfigMsg;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app_config")
public interface AppConfigControllerApi {

    @GetMapping("/")
    AppConfigMsg getAppConfig();
}
