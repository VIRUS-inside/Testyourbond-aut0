package org.apache.xerces.dom.events;

import org.w3c.dom.events.UIEvent;
import org.w3c.dom.views.AbstractView;

public class UIEventImpl
  extends EventImpl
  implements UIEvent
{
  private AbstractView fView;
  private int fDetail;
  
  public UIEventImpl() {}
  
  public AbstractView getView()
  {
    return fView;
  }
  
  public int getDetail()
  {
    return fDetail;
  }
  
  public void initUIEvent(String paramString, boolean paramBoolean1, boolean paramBoolean2, AbstractView paramAbstractView, int paramInt)
  {
    fView = paramAbstractView;
    fDetail = paramInt;
    super.initEvent(paramString, paramBoolean1, paramBoolean2);
  }
}
