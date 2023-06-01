package at.kulinz.jaegerstaetter.frontendmodel.webapi;


import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtomsg.ResourceListMsg;
import at.kulinz.jaegerstaetter.frontendmodel.dtomsg.StringListMsg;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;

@RestController
@RequestMapping("/api/resource")
public interface ResourceControllerApi {

    @GetMapping("/")
    ResourceListMsg toc() throws Exception;

    @GetMapping("/metadata/{resourceId}")
    ResourceDTO metadata(@PathVariable("resourceId") String resourceId, @RequestParam(value = "date", required = false) String date) throws Exception;

    @GetMapping("/keys/{type}")
    StringListMsg getKeyValue(@PathVariable("type") String type);

    @GetMapping("/notes/{resourceId}/{noteId}")
    NoteResourceDTO getNote(@PathVariable("resourceId") String resourceId,
                            @PathVariable("noteId") String noteId,
                            @RequestParam(value = "date", required = false) String date) throws Exception;

    @GetMapping("/corresp/{filename}/{anchorName}")
    CorrespInfo getCorrespPlaces(@PathVariable("filename") String filename,
                                 @PathVariable("anchorName") String anchorName) throws Exception;

    @GetMapping("/xml/{resourceId}")
    void getXml(@PathVariable("resourceId") String resourceId,
                @RequestParam(value = "date", required = false) String date,
                @RequestParam(value = "includeHeader", required = false, defaultValue = "true") boolean includeHeader,
                HttpServletResponse response) throws FileNotFoundException, FrontendModelException;

    @GetMapping("/diplo/{resourceId}")
    void getDiplo(@PathVariable("resourceId") String resourceId,
                  @RequestParam(value = "date", required = false) String date,
                  HttpServletResponse response) throws FileNotFoundException, FrontendModelException;

    @GetMapping("/norm/{resourceId}")
    void getNorm(@PathVariable("resourceId") String resourceId,
                 @RequestParam(value = "date", required = false) String date,
                 HttpServletResponse response) throws FileNotFoundException, FrontendModelException;

    @PostMapping("/norm_highlight/{resourceId}")
    void getNormWithSearchHighlight(@PathVariable("resourceId") String resourceId,
                                    @RequestBody SearchRequest searchRequest,
                                    HttpServletResponse response) throws FileNotFoundException, FrontendModelException;

    @PostMapping("/norm_markrs/{resourceId}")
    void getNormWithMarkRsRequest(@PathVariable("resourceId") String resourceId,
                                  @RequestBody MarkRsRequest markRsRequest,
                                  HttpServletResponse response) throws FileNotFoundException, FrontendModelException;

    @GetMapping("/norm_pdf/{resourceId}.pdf")
    void getNormAsPdf(@PathVariable("resourceId") String resourceId, HttpServletResponse response) throws FileNotFoundException, FrontendModelException;

    @GetMapping("/facs/{facResourceId}")
    void getFacsimile(@PathVariable("facResourceId") String facResourceId, HttpServletResponse response) throws FileNotFoundException, FrontendModelException;


}
