package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;


































@JsxClass(isJSObject=false)
public class RowContainer
  extends HTMLElement
{
  public RowContainer() {}
  
  @JsxGetter
  public Object getRows()
  {
    new HTMLCollection(getDomNodeOrDie(), false)
    {
      protected boolean isMatching(DomNode node) {
        return ((node instanceof HtmlTableRow)) && (isContainedRow((HtmlTableRow)node));
      }
    };
  }
  





  protected boolean isContainedRow(HtmlTableRow row)
  {
    return row.getParentNode() == getDomNodeOrDie();
  }
  




  @JsxFunction
  public void deleteRow(int rowIndex)
  {
    HTMLCollection rows = (HTMLCollection)getRows();
    int rowCount = rows.getLength();
    if (rowIndex == -1) {
      rowIndex = rowCount - 1;
    }
    boolean rowIndexValid = (rowIndex >= 0) && (rowIndex < rowCount);
    if (rowIndexValid) {
      SimpleScriptable row = (SimpleScriptable)rows.item(Integer.valueOf(rowIndex));
      row.getDomNodeOrDie().remove();
    }
  }
  








  @JsxFunction
  public Object insertRow(Object index)
  {
    int rowIndex = -1;
    if (index != Undefined.instance) {
      rowIndex = (int)Context.toNumber(index);
    }
    HTMLCollection rows = (HTMLCollection)getRows();
    int rowCount = rows.getLength();
    int r;
    int r; if ((rowIndex == -1) || (rowIndex == rowCount)) {
      r = Math.max(0, rowCount);
    }
    else {
      r = rowIndex;
    }
    
    if ((r < 0) || (r > rowCount)) {
      throw Context.reportRuntimeError("Index or size is negative or greater than the allowed amount (index: " + 
        rowIndex + ", " + rowCount + " rows)");
    }
    
    return insertRow(r);
  }
  




  public Object insertRow(int index)
  {
    HTMLCollection rows = (HTMLCollection)getRows();
    int rowCount = rows.getLength();
    DomElement newRow = ((HtmlPage)getDomNodeOrDie().getPage()).createElement("tr");
    if (rowCount == 0) {
      getDomNodeOrDie().appendChild(newRow);
    }
    else if (index == rowCount) {
      SimpleScriptable row = (SimpleScriptable)rows.item(Integer.valueOf(index - 1));
      row.getDomNodeOrDie().getParentNode().appendChild(newRow);
    }
    else {
      SimpleScriptable row = (SimpleScriptable)rows.item(Integer.valueOf(index));
      
      if (index > rowCount - 1) {
        row.getDomNodeOrDie().getParentNode().appendChild(newRow);
      }
      else {
        row.getDomNodeOrDie().insertBefore(newRow);
      }
    }
    return getScriptableFor(newRow);
  }
  






  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public Object moveRow(int sourceIndex, int targetIndex)
  {
    HTMLCollection rows = (HTMLCollection)getRows();
    int rowCount = rows.getLength();
    boolean sourceIndexValid = (sourceIndex >= 0) && (sourceIndex < rowCount);
    boolean targetIndexValid = (targetIndex >= 0) && (targetIndex < rowCount);
    if ((sourceIndexValid) && (targetIndexValid)) {
      SimpleScriptable sourceRow = (SimpleScriptable)rows.item(Integer.valueOf(sourceIndex));
      SimpleScriptable targetRow = (SimpleScriptable)rows.item(Integer.valueOf(targetIndex));
      targetRow.getDomNodeOrDie().insertBefore(sourceRow.getDomNodeOrDie());
      return sourceRow;
    }
    return null;
  }
  



  @JsxGetter
  public String getAlign()
  {
    return getAlign(true);
  }
  



  @JsxSetter
  public void setAlign(String align)
  {
    setAlign(align, false);
  }
}
