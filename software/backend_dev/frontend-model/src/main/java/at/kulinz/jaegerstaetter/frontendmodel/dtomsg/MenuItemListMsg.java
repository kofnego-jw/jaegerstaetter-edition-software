package at.kulinz.jaegerstaetter.frontendmodel.dtomsg;

import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.MenuItem;

import java.util.Collections;
import java.util.List;

public class MenuItemListMsg extends BasicMsg {
    public final List<MenuItem> menuItemList;

    public MenuItemListMsg(List<MenuItem> menuItemList) {
        this.menuItemList = menuItemList;
    }

    public MenuItemListMsg(String message) {
        super(message);
        this.menuItemList = Collections.emptyList();
    }
}
