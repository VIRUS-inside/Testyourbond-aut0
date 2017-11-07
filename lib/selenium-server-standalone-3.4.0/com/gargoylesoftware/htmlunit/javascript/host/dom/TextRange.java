package com.gargoylesoftware.htmlunit.javascript.host.dom;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.impl.SelectableTextInput;
import com.gargoylesoftware.htmlunit.html.impl.SimpleRange;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.ranges.Range;




























@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class TextRange
  extends SimpleScriptable
{
  private static final Log LOG = LogFactory.getLog(TextRange.class);
  



  private Range range_;
  



  public TextRange() {}
  



  public TextRange(HTMLElement elt)
  {
    range_ = new SimpleRange(elt.getDomNodeOrDie());
  }
  



  public TextRange(Range range)
  {
    range_ = range.cloneRange();
  }
  



  @JsxGetter
  public String getText()
  {
    return range_.toString();
  }
  



  @JsxSetter
  public void setText(String text)
  {
    if ((range_.getStartContainer() == range_.getEndContainer()) && 
      ((range_.getStartContainer() instanceof SelectableTextInput))) {
      SelectableTextInput input = (SelectableTextInput)range_.getStartContainer();
      String oldValue = input.getText();
      input.setText(oldValue.substring(0, input.getSelectionStart()) + text + 
        oldValue.substring(input.getSelectionEnd()));
    }
  }
  



  @JsxGetter
  public String getHtmlText()
  {
    org.w3c.dom.Node node = range_.getCommonAncestorContainer();
    if (node == null) {
      return "";
    }
    HTMLElement element = (HTMLElement)getScriptableFor(node);
    return element.getOuterHTML();
  }
  




  @JsxFunction
  public Object duplicate()
  {
    TextRange range = new TextRange(range_.cloneRange());
    range.setParentScope(getParentScope());
    range.setPrototype(getPrototype());
    return range;
  }
  









  @JsxFunction
  public Node parentElement()
  {
    org.w3c.dom.Node parent = range_.getCommonAncestorContainer();
    if (parent == null) {
      if ((range_.getStartContainer() == null) || (range_.getEndContainer() == null)) {
        try {
          Window window = (Window)getParentScope();
          HtmlPage page = (HtmlPage)window.getDomNodeOrDie();
          return (Node)getScriptableFor(page.getBody());
        }
        catch (Exception localException) {}
      }
      

      return null;
    }
    return (Node)getScriptableFor(parent);
  }
  




  @JsxFunction
  public void collapse(boolean toStart)
  {
    range_.collapse(toStart);
  }
  




  @JsxFunction
  public void select()
  {
    HtmlPage page = (HtmlPage)getWindow().getDomNodeOrDie();
    page.setSelectionRange(range_);
  }
  





  @JsxFunction
  public int moveStart(String unit, Object count)
  {
    if (!"character".equals(unit)) {
      LOG.warn("moveStart('" + unit + "') is not yet supported");
      return 0;
    }
    int c = 1;
    if (count != Undefined.instance) {
      c = (int)Context.toNumber(count);
    }
    if ((range_.getStartContainer() == range_.getEndContainer()) && 
      ((range_.getStartContainer() instanceof SelectableTextInput))) {
      SelectableTextInput input = (SelectableTextInput)range_.getStartContainer();
      c = constrainMoveBy(c, range_.getStartOffset(), input.getText().length());
      range_.setStart(input, range_.getStartOffset() + c);
    }
    return c;
  }
  





  @JsxFunction
  public int move(String unit, Object count)
  {
    collapse(true);
    return moveStart(unit, count);
  }
  





  @JsxFunction
  public int moveEnd(String unit, Object count)
  {
    if (!"character".equals(unit)) {
      LOG.warn("moveEnd('" + unit + "') is not yet supported");
      return 0;
    }
    int c = 1;
    if (count != Undefined.instance) {
      c = (int)Context.toNumber(count);
    }
    if ((range_.getStartContainer() == range_.getEndContainer()) && 
      ((range_.getStartContainer() instanceof SelectableTextInput))) {
      SelectableTextInput input = (SelectableTextInput)range_.getStartContainer();
      c = constrainMoveBy(c, range_.getEndOffset(), input.getText().length());
      range_.setEnd(input, range_.getEndOffset() + c);
    }
    return c;
  }
  





  @JsxFunction
  public void moveToElementText(HTMLElement element)
  {
    range_.selectNode(element.getDomNodeOrDie());
  }
  





  @JsxFunction
  public boolean inRange(TextRange other)
  {
    Range otherRange = range_;
    
    org.w3c.dom.Node start = range_.getStartContainer();
    org.w3c.dom.Node otherStart = otherRange.getStartContainer();
    if (otherStart == null) {
      return false;
    }
    short startComparison = start.compareDocumentPosition(otherStart);
    boolean startNodeBefore = (startComparison == 0) || 
      ((startComparison & 0x8) != 0) || 
      ((startComparison & 0x2) != 0);
    if ((startNodeBefore) && ((start != otherStart) || (range_.getStartOffset() <= otherRange.getStartOffset()))) {
      org.w3c.dom.Node end = range_.getEndContainer();
      org.w3c.dom.Node otherEnd = otherRange.getEndContainer();
      short endComparison = end.compareDocumentPosition(otherEnd);
      boolean endNodeAfter = (endComparison == 0) || 
        ((endComparison & 0x8) != 0) || 
        ((endComparison & 0x4) != 0);
      if ((endNodeAfter) && ((end != otherEnd) || (range_.getEndOffset() >= otherRange.getEndOffset()))) {
        return true;
      }
    }
    
    return false;
  }
  





  @JsxFunction
  public void setEndPoint(String type, TextRange other)
  {
    Range otherRange = range_;
    int offset;
    org.w3c.dom.Node target;
    int offset;
    if (type.endsWith("ToStart")) {
      org.w3c.dom.Node target = otherRange.getStartContainer();
      offset = otherRange.getStartOffset();
    }
    else {
      target = otherRange.getEndContainer();
      offset = otherRange.getEndOffset();
    }
    
    if (type.startsWith("Start")) {
      range_.setStart(target, offset);
    }
    else {
      range_.setEnd(target, offset);
    }
  }
  






  protected int constrainMoveBy(int moveBy, int current, int textLength)
  {
    int to = current + moveBy;
    if (to < 0) {
      moveBy -= to;
    }
    else if (to >= textLength) {
      moveBy -= to - textLength;
    }
    return moveBy;
  }
  





  @JsxFunction
  public String getBookmark()
  {
    return "";
  }
  





  @JsxFunction
  public boolean moveToBookmark(String bookmark)
  {
    return false;
  }
  


  @JsxFunction
  public int compareEndPoints(String how, TextRange sourceRange)
  {
    String str;
    

    org.w3c.dom.Node otherStart;
    
    switch ((str = how).hashCode()) {case -1135841275:  if (str.equals("StartToStart")) {} break; case 409977150:  if (str.equals("StartToEnd")) break; break; case 1055254892:  if (!str.equals("EndToStart")) {
        break label154;
        org.w3c.dom.Node start = range_.getStartContainer();
        org.w3c.dom.Node otherStart = range_.getEndContainer();
        
        break label175;
        
        org.w3c.dom.Node start = range_.getStartContainer();
        org.w3c.dom.Node otherStart = range_.getStartContainer();
        break label175;
      }
      else {
        org.w3c.dom.Node start = range_.getEndContainer();
        otherStart = range_.getStartContainer(); }
      break;
    }
    label154:
    org.w3c.dom.Node start = range_.getEndContainer();
    org.w3c.dom.Node otherStart = range_.getEndContainer();
    
    label175:
    if ((start == null) || (otherStart == null)) {
      return 0;
    }
    return start.compareDocumentPosition(otherStart);
  }
}
