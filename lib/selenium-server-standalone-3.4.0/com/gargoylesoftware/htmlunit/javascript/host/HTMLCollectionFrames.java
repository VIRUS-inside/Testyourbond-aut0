package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.BaseFrameElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection;
import java.util.List;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;











































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































class HTMLCollectionFrames
  extends HTMLCollection
{
  private static final Log LOG = LogFactory.getLog(HTMLCollectionFrames.class);
  
  HTMLCollectionFrames(HtmlPage page) {
    super(page, false);
  }
  
  protected boolean isMatching(DomNode node)
  {
    return node instanceof BaseFrameElement;
  }
  
  protected Scriptable getScriptableForElement(Object obj) {
    WebWindow window;
    WebWindow window;
    if ((obj instanceof BaseFrameElement)) {
      window = ((BaseFrameElement)obj).getEnclosedWindow();
    }
    else {
      window = ((FrameWindow)obj).getFrameElement().getEnclosedWindow();
    }
    
    return window.getScriptableObject();
  }
  
  protected Object getWithPreemption(String name)
  {
    List<Object> elements = getElements();
    
    for (Object next : elements) {
      BaseFrameElement frameElt = (BaseFrameElement)next;
      WebWindow window = frameElt.getEnclosedWindow();
      if (name.equals(window.getName())) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Property \"" + name + "\" evaluated (by name) to " + window);
        }
        return getScriptableForElement(window);
      }
      if ((getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_WINDOW_FRAMES_ACCESSIBLE_BY_ID)) && (frameElt.getId().equals(name))) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Property \"" + name + "\" evaluated (by id) to " + window);
        }
        return getScriptableForElement(window);
      }
    }
    
    return NOT_FOUND;
  }
  
  protected void addElementIds(List<String> idList, List<Object> elements)
  {
    for (Object next : elements) {
      BaseFrameElement frameElt = (BaseFrameElement)next;
      WebWindow window = frameElt.getEnclosedWindow();
      String windowName = window.getName();
      if (windowName != null) {
        idList.add(windowName);
      }
    }
  }
}
