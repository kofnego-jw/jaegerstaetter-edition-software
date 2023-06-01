package at.kulinz.jaegerstaetter.edition.webapp.generic.web;

import at.kulinz.jaegerstaetter.frontendmodel.dtomsg.CommentDocMsg;
import at.kulinz.jaegerstaetter.frontendmodel.dtomsg.MenuItemListMsg;
import at.kulinz.jaegerstaetter.frontendmodel.dtomsg.ResourceListMsg;
import at.kulinz.jaegerstaetter.frontendmodel.dtomsg.SearchResultMsg;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.CommentDoc;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.MenuItem;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.ResourceFWDTO;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.SearchResult;

import java.util.List;

public class ControllerMsgHelper {

    public static MenuItemListMsg fromMenuItemList(List<MenuItem> list) {
        return new MenuItemListMsg(list);
    }

    public static CommentDocMsg fromCommentDoc(CommentDoc doc) {
        return new CommentDocMsg(doc);
    }

    public static ResourceListMsg fromResourceList(List<ResourceFWDTO> list) {
        return new ResourceListMsg(list);
    }

    public static SearchResultMsg fromSearchResult(SearchResult result) {
        return new SearchResultMsg(result);
    }
}
