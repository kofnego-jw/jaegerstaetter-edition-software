package at.kulinz.jaegerstaetter.edition.webapp.generic.web;

import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtomsg.CommentDocMsg;
import at.kulinz.jaegerstaetter.frontendmodel.dtomsg.MenuItemListMsg;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.CommentDoc;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.MenuItem;
import at.kulinz.jaegerstaetter.frontendmodel.serviceapi.CommentDocService;
import at.kulinz.jaegerstaetter.frontendmodel.webapi.MenuControllerApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
public class CommentDocController implements MenuControllerApi {

    @Autowired
    private CommentDocService commentDocService;

    @Override
    public CommentDocMsg startCommentDoc() throws FrontendModelException {
        CommentDoc startDoc = commentDocService.getStartPageCommentDoc();
        return ControllerMsgHelper.fromCommentDoc(startDoc);
    }

    @Override
    public MenuItemListMsg digitalEditionMenu() throws FrontendModelException {
        List<MenuItem> list = commentDocService.getCommentDocMenu();
        return ControllerMsgHelper.fromMenuItemList(list);
    }

    @Override
    public CommentDocMsg digitalEditionCommentDoc(String key) throws FileNotFoundException, FrontendModelException {
        CommentDoc doc = commentDocService.getCommentDoc(key);
        return ControllerMsgHelper.fromCommentDoc(doc);
    }

    @Override
    public CommentDocMsg registryDocument(String key) throws FileNotFoundException, FrontendModelException {
        CommentDoc doc = commentDocService.getRegistryDoc(key);
        return ControllerMsgHelper.fromCommentDoc(doc);
    }

    @Override
    public CommentDocMsg biographyIndex() throws FileNotFoundException, FrontendModelException {
        CommentDoc bioIndex = commentDocService.getBiographyIndex();
        return ControllerMsgHelper.fromCommentDoc(bioIndex);
    }

    @Override
    public MenuItemListMsg materialMenu() throws FrontendModelException {
        List<MenuItem> list = commentDocService.getMaterialMenu();
        return ControllerMsgHelper.fromMenuItemList(list);
    }

    @Override
    public CommentDocMsg materialDocument(String key) throws FileNotFoundException, FrontendModelException {
        CommentDoc doc = commentDocService.getMaterialDoc(key);
        return ControllerMsgHelper.fromCommentDoc(doc);
    }

    @Override
    public CommentDocMsg contactCommentDoc() throws FileNotFoundException, FrontendModelException {
        CommentDoc contactCommentDoc = commentDocService.getContactCommentDoc();
        return ControllerMsgHelper.fromCommentDoc(contactCommentDoc);
    }

    @Override
    public CommentDocMsg gdprCommentDoc() throws FileNotFoundException, FrontendModelException {
        CommentDoc gdprCommentDoc = commentDocService.getGdprCommentDoc();
        return ControllerMsgHelper.fromCommentDoc(gdprCommentDoc);
    }

    @Override
    public CommentDocMsg imprintCommentDoc() throws FileNotFoundException, FrontendModelException {
        CommentDoc imprintCommentDoc = commentDocService.getImprintCommentDoc();
        return ControllerMsgHelper.fromCommentDoc(imprintCommentDoc);
    }

    @Override
    public CommentDocMsg acknowledgementsCommentDoc() throws FileNotFoundException, FrontendModelException {
        CommentDoc commentDoc = commentDocService.getAcknowledgementsCommentDoc();
        return ControllerMsgHelper.fromCommentDoc(commentDoc);
    }


    @Override
    public CommentDocMsg specialCorrespCommentDoc() throws FileNotFoundException, FrontendModelException {
        CommentDoc specialCorrespCommentDoc = commentDocService.getSpecialCorrespCommentDoc();
        return ControllerMsgHelper.fromCommentDoc(specialCorrespCommentDoc);
    }
}
