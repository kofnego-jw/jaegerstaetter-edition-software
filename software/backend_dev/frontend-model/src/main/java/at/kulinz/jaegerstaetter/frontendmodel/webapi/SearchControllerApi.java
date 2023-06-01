package at.kulinz.jaegerstaetter.frontendmodel.webapi;

import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtomsg.SearchResultMsg;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.SearchRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
public interface SearchControllerApi {

    @PostMapping("/")
    SearchResultMsg search(@RequestBody SearchRequest request) throws FrontendModelException;

}
