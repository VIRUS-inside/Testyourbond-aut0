package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition;
import com.gargoylesoftware.htmlunit.javascript.host.event.MouseEvent;
import java.util.List;
import net.sourceforge.htmlunit.corejs.javascript.Context;








































@JsxClass(domClass=HtmlTableCell.class)
public class HTMLTableCellElement
  extends HTMLTableComponent
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLTableCellElement() {}
  
  public int getOffsetHeight()
  {
    MouseEvent event = MouseEvent.getCurrentMouseEvent();
    if (isAncestorOfEventTarget(event)) {
      return super.getOffsetHeight();
    }
    
    if (isDisplayNone()) {
      return 0;
    }
    ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(this, null);
    boolean includeBorder = getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_TABLE_CELL_OFFSET_INCLUDES_BORDER);
    return style.getCalculatedHeight(includeBorder, true);
  }
  



  public int getOffsetWidth()
  {
    float w = super.getOffsetWidth();
    MouseEvent event = MouseEvent.getCurrentMouseEvent();
    if (isAncestorOfEventTarget(event)) {
      return (int)w;
    }
    
    if (isDisplayNone()) {
      return 0;
    }
    
    ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(this, null);
    if ("collapse".equals(style.getStyleAttribute(StyleAttributes.Definition.BORDER_COLLAPSE))) {
      HtmlTableRow row = getRow();
      if (row != null) {
        HtmlElement thiz = getDomNodeOrDie();
        List<HtmlTableCell> cells = row.getCells();
        boolean ie = getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_TABLE_CELL_OFFSET_INCLUDES_BORDER);
        boolean leftmost = cells.indexOf(thiz) == 0;
        boolean rightmost = cells.indexOf(thiz) == cells.size() - 1;
        w = (float)(w - ((ie) && (leftmost) ? 0.0D : 0.5D) * style.getBorderLeftValue());
        w = (float)(w - ((ie) && (rightmost) ? 0.0D : 0.5D) * style.getBorderRightValue());
      }
    }
    
    return (int)w;
  }
  




  @JsxGetter
  public Integer getCellIndex()
  {
    HtmlTableCell cell = (HtmlTableCell)getDomNodeOrDie();
    HtmlTableRow row = cell.getEnclosingRow();
    if (row == null) {
      return Integer.valueOf(-1);
    }
    return Integer.valueOf(row.getCells().indexOf(cell));
  }
  



  @JsxGetter
  public String getAbbr()
  {
    return getDomNodeOrDie().getAttribute("abbr");
  }
  



  @JsxSetter
  public void setAbbr(String abbr)
  {
    getDomNodeOrDie().setAttribute("abbr", abbr);
  }
  



  @JsxGetter
  public String getAxis()
  {
    return getDomNodeOrDie().getAttribute("axis");
  }
  



  @JsxSetter
  public void setAxis(String axis)
  {
    getDomNodeOrDie().setAttribute("axis", axis);
  }
  




  @JsxGetter
  public String getBgColor()
  {
    return getDomNodeOrDie().getAttribute("bgColor");
  }
  




  @JsxSetter
  public void setBgColor(String bgColor)
  {
    setColorAttribute("bgColor", bgColor);
  }
  



  @JsxGetter
  public int getColSpan()
  {
    String s = getDomNodeOrDie().getAttribute("colSpan");
    try {
      return Integer.parseInt(s);
    }
    catch (NumberFormatException e) {}
    return 1;
  }
  



  @JsxSetter
  public void setColSpan(String colSpan)
  {
    try
    {
      int i = (int)Double.parseDouble(colSpan);
      if (i <= 0) {
        throw new NumberFormatException(colSpan);
      }
      getDomNodeOrDie().setAttribute("colSpan", Integer.toString(i));
    }
    catch (NumberFormatException e) {
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_TABLE_SPAN_THROWS_EXCEPTION_IF_INVALID)) {
        throw Context.throwAsScriptRuntimeEx(e);
      }
      getDomNodeOrDie().setAttribute("colSpan", "1");
    }
  }
  



  @JsxGetter
  public int getRowSpan()
  {
    String s = getDomNodeOrDie().getAttribute("rowSpan");
    try {
      return Integer.parseInt(s);
    }
    catch (NumberFormatException e) {}
    return 1;
  }
  



  @JsxSetter
  public void setRowSpan(String rowSpan)
  {
    try
    {
      int i = (int)Double.parseDouble(rowSpan);
      if (i <= 0) {
        throw new NumberFormatException(rowSpan);
      }
      getDomNodeOrDie().setAttribute("rowSpan", Integer.toString(i));
    }
    catch (NumberFormatException e) {
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_TABLE_SPAN_THROWS_EXCEPTION_IF_INVALID)) {
        throw Context.throwAsScriptRuntimeEx(e);
      }
      getDomNodeOrDie().setAttribute("rowSpan", "1");
    }
  }
  




  @JsxGetter
  public boolean getNoWrap()
  {
    return getDomNodeOrDie().hasAttribute("noWrap");
  }
  




  @JsxSetter
  public void setNoWrap(boolean noWrap)
  {
    if (noWrap) {
      getDomNodeOrDie().setAttribute("noWrap", "");
    }
    else {
      getDomNodeOrDie().removeAttribute("noWrap");
    }
  }
  



  private HtmlTableRow getRow()
  {
    DomNode node = getDomNodeOrDie();
    while ((node != null) && (!(node instanceof HtmlTableRow))) {
      node = node.getParentNode();
    }
    return (HtmlTableRow)node;
  }
  



  @JsxGetter(propertyName="width")
  public String getWidth_js()
  {
    boolean ie = getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_TABLE_CELL_WIDTH_DOES_NOT_RETURN_NEGATIVE_VALUES);
    Boolean returnNegativeValues = ie ? Boolean.TRUE : null;
    return getWidthOrHeight("width", returnNegativeValues);
  }
  



  @JsxSetter
  public void setWidth(String width)
  {
    setWidthOrHeight("width", width, 
      !getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_TABLE_CELL_WIDTH_DOES_NOT_RETURN_NEGATIVE_VALUES));
  }
  



  @JsxGetter(propertyName="height")
  public String getHeight_js()
  {
    boolean ie = getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_TABLE_CELL_HEIGHT_DOES_NOT_RETURN_NEGATIVE_VALUES);
    Boolean returnNegativeValues = ie ? Boolean.TRUE : null;
    return getWidthOrHeight("height", returnNegativeValues);
  }
  



  @JsxSetter
  public void setHeight(String height)
  {
    setWidthOrHeight("height", height, 
      !getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_TABLE_CELL_HEIGHT_DOES_NOT_RETURN_NEGATIVE_VALUES));
  }
  




  public void setOuterHTML(Object value)
  {
    throw Context.reportRuntimeError("outerHTML is read-only for tag '" + 
      getDomNodeOrDie().getTagName() + "'");
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getBorderColor()
  {
    return getDomNodeOrDie().getAttribute("borderColor");
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setBorderColor(String borderColor)
  {
    setColorAttribute("borderColor", borderColor);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getBorderColorDark()
  {
    return "";
  }
  





  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setBorderColorDark(String borderColor) {}
  




  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getBorderColorLight()
  {
    return "";
  }
  
  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setBorderColorLight(String borderColor) {}
}
