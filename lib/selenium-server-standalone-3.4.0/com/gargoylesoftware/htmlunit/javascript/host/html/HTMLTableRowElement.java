package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;





































@JsxClass(domClass=HtmlTableRow.class)
public class HTMLTableRowElement
  extends HTMLTableComponent
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLTableRowElement() {}
  
  @JsxGetter
  public int getRowIndex()
  {
    HtmlTableRow row = (HtmlTableRow)getDomNodeOrDie();
    HtmlTable table = row.getEnclosingTable();
    if (table == null) {
      return -1;
    }
    return table.getRows().indexOf(row);
  }
  






  @JsxGetter
  public int getSectionRowIndex()
  {
    DomNode row = getDomNodeOrDie();
    HtmlTable table = ((HtmlTableRow)row).getEnclosingTable();
    if (table == null) {
      return -1;
    }
    int index = -1;
    while (row != null) {
      if ((row instanceof HtmlTableRow)) {
        index++;
      }
      row = row.getPreviousSibling();
    }
    return index;
  }
  



  @JsxGetter
  public Object getCells()
  {
    final HtmlTableRow row = (HtmlTableRow)getDomNodeOrDie();
    new HTMLCollection(row, false)
    {
      protected List<Object> computeElements() {
        return new ArrayList(row.getCells());
      }
    };
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
  








  @JsxFunction
  public Object insertCell(Object index)
  {
    int position = -1;
    if (index != Undefined.instance) {
      position = (int)Context.toNumber(index);
    }
    HtmlTableRow htmlRow = (HtmlTableRow)getDomNodeOrDie();
    
    boolean indexValid = (position >= -1) && (position <= htmlRow.getCells().size());
    if (indexValid) {
      DomElement newCell = ((HtmlPage)htmlRow.getPage()).createElement("td");
      if ((position == -1) || (position == htmlRow.getCells().size())) {
        htmlRow.appendChild(newCell);
      }
      else {
        htmlRow.getCell(position).insertBefore(newCell);
      }
      return getScriptableFor(newCell);
    }
    throw Context.reportRuntimeError("Index or size is negative or greater than the allowed amount");
  }
  






  @JsxFunction
  public void deleteCell(Object index)
  {
    int position = -1;
    if (index != Undefined.instance) {
      position = (int)Context.toNumber(index);
    }
    else if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_TABLE_ROW_DELETE_CELL_REQUIRES_INDEX)) {
      throw Context.reportRuntimeError("No enough arguments");
    }
    
    HtmlTableRow htmlRow = (HtmlTableRow)getDomNodeOrDie();
    
    if (position == -1) {
      position = htmlRow.getCells().size() - 1;
    }
    boolean indexValid = (position >= -1) && (position <= htmlRow.getCells().size());
    if (!indexValid) {
      throw Context.reportRuntimeError("Index or size is negative or greater than the allowed amount");
    }
    
    htmlRow.getCell(position).remove();
  }
  




  public void setOuterHTML(Object value)
  {
    throw Context.reportRuntimeError("outerHTML is read-only for tag 'tr'");
  }
  




  public void setInnerText(Object value)
  {
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_INNER_TEXT_READONLY_FOR_TABLE)) {
      throw Context.reportRuntimeError("innerText is read-only for tag 'tr'");
    }
    super.setInnerText(value);
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
    return getDomNodeOrDie().getAttribute("borderColorDark");
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setBorderColorDark(String borderColor)
  {
    setColorAttribute("borderColorDark", borderColor);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getBorderColorLight()
  {
    return getDomNodeOrDie().getAttribute("borderColorLight");
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setBorderColorLight(String borderColor)
  {
    setColorAttribute("borderColorLight", borderColor);
  }
}
