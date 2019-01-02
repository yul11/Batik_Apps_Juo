package batik.apps.juo;

import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.UIEvent;
import org.w3c.dom.views.AbstractView;

public interface MouseEvent_aux extends UIEvent {
    public int getScreenX();

    public int getScreenY();

    public int getClientX();

    public int getClientY();

    public boolean getCtrlKey();

    public boolean getShiftKey();

    public boolean getAltKey();

    public boolean getMetaKey();

    public short getButton();

    public EventTarget getRelatedTarget();

    public void initMouseEvent(String typeArg, 
                               boolean canBubbleArg, 
                               boolean cancelableArg, 
                               AbstractView viewArg, 
                               int detailArg, 
                               int screenXArg, 
                               int screenYArg, 
                               int clientXArg, 
                               int clientYArg, 
                               boolean ctrlKeyArg, 
                               boolean altKeyArg, 
                               boolean shiftKeyArg, 
                               boolean metaKeyArg, 
                               short buttonArg, 
                               EventTarget relatedTargetArg);
}
