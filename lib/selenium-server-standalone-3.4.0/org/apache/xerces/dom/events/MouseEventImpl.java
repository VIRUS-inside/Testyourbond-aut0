package org.apache.xerces.dom.events;

import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.views.AbstractView;

public class MouseEventImpl
  extends UIEventImpl
  implements MouseEvent
{
  private int fScreenX;
  private int fScreenY;
  private int fClientX;
  private int fClientY;
  private boolean fCtrlKey;
  private boolean fAltKey;
  private boolean fShiftKey;
  private boolean fMetaKey;
  private short fButton;
  private EventTarget fRelatedTarget;
  
  public MouseEventImpl() {}
  
  public int getScreenX()
  {
    return fScreenX;
  }
  
  public int getScreenY()
  {
    return fScreenY;
  }
  
  public int getClientX()
  {
    return fClientX;
  }
  
  public int getClientY()
  {
    return fClientY;
  }
  
  public boolean getCtrlKey()
  {
    return fCtrlKey;
  }
  
  public boolean getAltKey()
  {
    return fAltKey;
  }
  
  public boolean getShiftKey()
  {
    return fShiftKey;
  }
  
  public boolean getMetaKey()
  {
    return fMetaKey;
  }
  
  public short getButton()
  {
    return fButton;
  }
  
  public EventTarget getRelatedTarget()
  {
    return fRelatedTarget;
  }
  
  public void initMouseEvent(String paramString, boolean paramBoolean1, boolean paramBoolean2, AbstractView paramAbstractView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, boolean paramBoolean6, short paramShort, EventTarget paramEventTarget)
  {
    fScreenX = paramInt2;
    fScreenY = paramInt3;
    fClientX = paramInt4;
    fClientY = paramInt5;
    fCtrlKey = paramBoolean3;
    fAltKey = paramBoolean4;
    fShiftKey = paramBoolean5;
    fMetaKey = paramBoolean6;
    fButton = paramShort;
    fRelatedTarget = paramEventTarget;
    super.initUIEvent(paramString, paramBoolean1, paramBoolean2, paramAbstractView, paramInt1);
  }
}
