package at.kulinz.jaegerstaetter.frontendmodel.webapi;

import at.kulinz.jaegerstaetter.frontendmodel.dtomsg.BasicMsg;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.PasswordDTO;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.StatElementFullDesc;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.StatReport;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/admin")
@RestController
public interface AdminControllerApi {

    @PostMapping("/clone_edition_to_preview")
    BasicMsg cloneEditionToPreview(@RequestBody PasswordDTO password) throws Exception;

    @PostMapping("/clone_edition_to_preview_and_ingest")
    BasicMsg cloneEditionAndIngestToPreview(@RequestBody PasswordDTO password) throws Exception;

    @PostMapping("/ingest_to_edition")
    BasicMsg ingestToEdition(@RequestBody PasswordDTO password) throws Exception;

    @GetMapping("/statistics")
    StatReport getStatistics() throws Exception;

    @GetMapping("/statistics/{type}/{elementName}")
    StatElementFullDesc getElementFullDesc(@PathVariable("type") String type, @PathVariable("elementName") String elementName) throws Exception;

    @GetMapping("/statistics/{type}")
    List<String> getElementNames(@PathVariable("type") String type) throws Exception;
}
