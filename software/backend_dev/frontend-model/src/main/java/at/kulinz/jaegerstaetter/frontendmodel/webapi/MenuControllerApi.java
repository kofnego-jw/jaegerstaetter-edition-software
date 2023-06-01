package at.kulinz.jaegerstaetter.frontendmodel.webapi;

import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtomsg.CommentDocMsg;
import at.kulinz.jaegerstaetter.frontendmodel.dtomsg.MenuItemListMsg;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RequestMapping("/api/menu")
@RestController
public interface MenuControllerApi {

    @GetMapping("/start")
    CommentDocMsg startCommentDoc() throws FrontendModelException;

    @GetMapping("/digitale_edition")
    MenuItemListMsg digitalEditionMenu() throws FrontendModelException;

    @GetMapping("/digitale_edition/{key}")
    CommentDocMsg digitalEditionCommentDoc(@PathVariable("key") String key) throws FileNotFoundException, FrontendModelException;

    @GetMapping("/register_dokument/{key}")
    CommentDocMsg registryDocument(@PathVariable("key") String key) throws FileNotFoundException, FrontendModelException;

    @GetMapping("/biography/index.xml")
    CommentDocMsg biographyIndex() throws FileNotFoundException, FrontendModelException;

    @GetMapping("/materials")
    MenuItemListMsg materialMenu() throws FrontendModelException;

    @GetMapping("/materials/{key}")
    CommentDocMsg materialDocument(@PathVariable("key") String key) throws FileNotFoundException, FrontendModelException;

    @GetMapping("/contact")
    CommentDocMsg contactCommentDoc() throws FileNotFoundException, FrontendModelException;

    @GetMapping("/gdpr")
    CommentDocMsg gdprCommentDoc() throws FileNotFoundException, FrontendModelException;

    @GetMapping("/imprint")
    CommentDocMsg imprintCommentDoc() throws FileNotFoundException, FrontendModelException;

    @GetMapping("/acknowledgements")
    CommentDocMsg acknowledgementsCommentDoc() throws FileNotFoundException, FrontendModelException;

    @GetMapping("/special_correspondence")
    CommentDocMsg specialCorrespCommentDoc() throws FileNotFoundException, FrontendModelException;
}
