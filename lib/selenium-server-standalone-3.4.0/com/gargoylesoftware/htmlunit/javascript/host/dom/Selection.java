package com.gargoylesoftware.htmlunit.javascript.host.dom;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.impl.SimpleRange;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.htmlunit.corejs.javascript.Context;
































@JsxClass
public class Selection
  extends SimpleScriptable
{
  private String type_ = "None";
  




  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public Selection() {}
  



  public Object getDefaultValue(Class<?> hint)
  {
    if ((getPrototype() != null) && ((String.class.equals(hint)) || (hint == null))) {
      StringBuilder sb = new StringBuilder();
      for (org.w3c.dom.ranges.Range r : getRanges()) {
        sb.append(r.toString());
      }
      return sb.toString();
    }
    return super.getDefaultValue(hint);
  }
  



  @JsxGetter
  public Node getAnchorNode()
  {
    org.w3c.dom.ranges.Range last = getLastRange();
    if (last == null) {
      return null;
    }
    return (Node)getScriptableNullSafe(last.getStartContainer());
  }
  



  @JsxGetter
  public int getAnchorOffset()
  {
    org.w3c.dom.ranges.Range last = getLastRange();
    if (last == null) {
      return 0;
    }
    return last.getStartOffset();
  }
  



  @JsxGetter
  public Node getFocusNode()
  {
    org.w3c.dom.ranges.Range last = getLastRange();
    if (last == null) {
      return null;
    }
    return (Node)getScriptableNullSafe(last.getEndContainer());
  }
  



  @JsxGetter
  public int getFocusOffset()
  {
    org.w3c.dom.ranges.Range last = getLastRange();
    if (last == null) {
      return 0;
    }
    return last.getEndOffset();
  }
  



  @JsxGetter
  public boolean getIsCollapsed()
  {
    List<org.w3c.dom.ranges.Range> ranges = getRanges();
    return (ranges.isEmpty()) || ((ranges.size() == 1) && (((org.w3c.dom.ranges.Range)ranges.get(0)).getCollapsed()));
  }
  



  @JsxGetter
  public int getRangeCount()
  {
    return getRanges().size();
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public String getType()
  {
    return type_;
  }
  



  @JsxFunction
  public void addRange(Range range)
  {
    getRanges().add(range.toW3C());
  }
  



  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void removeRange(Range range)
  {
    getRanges().remove(range.toW3C());
  }
  


  @JsxFunction
  public void removeAllRanges()
  {
    getRanges().clear();
  }
  





  @JsxFunction
  public Range getRangeAt(int index)
  {
    List<org.w3c.dom.ranges.Range> ranges = getRanges();
    if ((index < 0) || (index >= ranges.size())) {
      throw Context.reportRuntimeError("Invalid range index: " + index);
    }
    org.w3c.dom.ranges.Range range = (org.w3c.dom.ranges.Range)ranges.get(index);
    Range jsRange = 
      new Range(range);
    jsRange.setParentScope(getWindow());
    jsRange.setPrototype(getPrototype(Range.class));
    
    return jsRange;
  }
  




  @JsxFunction
  public void collapse(Node parentNode, int offset)
  {
    List<org.w3c.dom.ranges.Range> ranges = getRanges();
    ranges.clear();
    ranges.add(new SimpleRange(parentNode.getDomNodeOrDie(), offset));
  }
  


  @JsxFunction
  public void collapseToEnd()
  {
    org.w3c.dom.ranges.Range last = getLastRange();
    if (last != null) {
      List<org.w3c.dom.ranges.Range> ranges = getRanges();
      ranges.clear();
      ranges.add(last);
      last.collapse(false);
    }
  }
  


  @JsxFunction
  public void collapseToStart()
  {
    org.w3c.dom.ranges.Range first = getFirstRange();
    if (first != null) {
      List<org.w3c.dom.ranges.Range> ranges = getRanges();
      ranges.clear();
      ranges.add(first);
      first.collapse(true);
    }
  }
  


  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public void empty()
  {
    type_ = "None";
  }
  




  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public void extend(Node parentNode, int offset)
  {
    org.w3c.dom.ranges.Range last = getLastRange();
    if (last != null) {
      last.setEnd(parentNode.getDomNodeOrDie(), offset);
    }
  }
  



  @JsxFunction
  public void selectAllChildren(Node parentNode)
  {
    List<org.w3c.dom.ranges.Range> ranges = getRanges();
    ranges.clear();
    ranges.add(new SimpleRange(parentNode.getDomNodeOrDie()));
  }
  



  private List<org.w3c.dom.ranges.Range> getRanges()
  {
    HtmlPage page = (HtmlPage)getWindow().getDomNodeOrDie();
    return page.getSelectionRanges();
  }
  




  private org.w3c.dom.ranges.Range getFirstRange()
  {
    List<org.w3c.dom.ranges.Range> ranges = new ArrayList(getRanges());
    
    org.w3c.dom.ranges.Range first = null;
    for (org.w3c.dom.ranges.Range range : ranges) {
      if (first == null) {
        first = range;
      }
      else {
        org.w3c.dom.Node firstStart = first.getStartContainer();
        org.w3c.dom.Node rangeStart = range.getStartContainer();
        if ((firstStart.compareDocumentPosition(rangeStart) & 0x2) != 0) {
          first = range;
        }
      }
    }
    return first;
  }
  




  private org.w3c.dom.ranges.Range getLastRange()
  {
    List<org.w3c.dom.ranges.Range> ranges = new ArrayList(getRanges());
    
    org.w3c.dom.ranges.Range last = null;
    for (org.w3c.dom.ranges.Range range : ranges) {
      if (last == null) {
        last = range;
      }
      else {
        org.w3c.dom.Node lastStart = last.getStartContainer();
        org.w3c.dom.Node rangeStart = range.getStartContainer();
        if ((lastStart.compareDocumentPosition(rangeStart) & 0x4) != 0) {
          last = range;
        }
      }
    }
    return last;
  }
  


  private SimpleScriptable getScriptableNullSafe(Object object)
  {
    SimpleScriptable scriptable;
    
    SimpleScriptable scriptable;
    
    if (object != null) {
      scriptable = getScriptableFor(object);
    }
    else {
      scriptable = null;
    }
    return scriptable;
  }
}
