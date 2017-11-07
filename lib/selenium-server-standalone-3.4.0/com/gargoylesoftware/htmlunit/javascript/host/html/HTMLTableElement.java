package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import net.sourceforge.htmlunit.corejs.javascript.Context;

































@JsxClass(domClass=HtmlTable.class)
public class HTMLTableElement
  extends RowContainer
{
  private static final List<String> VALID_RULES_ = Arrays.asList(new String[] { "none", "groups", "rows", "cols" });
  




  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLTableElement() {}
  




  @JsxGetter
  public Object getCaption()
  {
    List<HtmlElement> captions = getDomNodeOrDie().getElementsByTagName("caption");
    if (captions.isEmpty()) {
      return null;
    }
    return getScriptableFor(captions.get(0));
  }
  



  @JsxSetter
  public void setCaption(Object o)
  {
    if (!(o instanceof HTMLTableCaptionElement)) {
      throw Context.reportRuntimeError("Not a caption");
    }
    

    deleteCaption();
    
    HTMLTableCaptionElement caption = (HTMLTableCaptionElement)o;
    getDomNodeOrDie().appendChild(caption.getDomNodeOrDie());
  }
  




  @JsxGetter
  public Object getTFoot()
  {
    List<HtmlElement> tfoots = getDomNodeOrDie().getElementsByTagName("tfoot");
    if (tfoots.isEmpty()) {
      return null;
    }
    return getScriptableFor(tfoots.get(0));
  }
  



  @JsxSetter
  public void setTFoot(Object o)
  {
    if ((!(o instanceof HTMLTableSectionElement)) || 
      (!"TFOOT".equals(((HTMLTableSectionElement)o).getTagName()))) {
      throw Context.reportRuntimeError("Not a tFoot");
    }
    

    deleteTFoot();
    
    HTMLTableSectionElement tfoot = (HTMLTableSectionElement)o;
    getDomNodeOrDie().appendChild(tfoot.getDomNodeOrDie());
  }
  




  @JsxGetter
  public Object getTHead()
  {
    List<HtmlElement> theads = getDomNodeOrDie().getElementsByTagName("thead");
    if (theads.isEmpty()) {
      return null;
    }
    return getScriptableFor(theads.get(0));
  }
  



  @JsxSetter
  public void setTHead(Object o)
  {
    if ((!(o instanceof HTMLTableSectionElement)) || 
      (!"THEAD".equals(((HTMLTableSectionElement)o).getTagName()))) {
      throw Context.reportRuntimeError("Not a tHead");
    }
    

    deleteTHead();
    
    HTMLTableSectionElement thead = (HTMLTableSectionElement)o;
    getDomNodeOrDie().appendChild(thead.getDomNodeOrDie());
  }
  



  @JsxGetter
  public Object getTBodies()
  {
    final HtmlTable table = (HtmlTable)getDomNodeOrDie();
    new HTMLCollection(table, false)
    {
      protected List<Object> computeElements() {
        return new ArrayList(table.getBodies());
      }
    };
  }
  






  @JsxFunction
  public Object createCaption()
  {
    return getScriptableFor(getDomNodeOrDie().appendChildIfNoneExists("caption"));
  }
  






  @JsxFunction
  public Object createTFoot()
  {
    return getScriptableFor(getDomNodeOrDie().appendChildIfNoneExists("tfoot"));
  }
  






  @JsxFunction
  public Object createTHead()
  {
    return getScriptableFor(getDomNodeOrDie().appendChildIfNoneExists("thead"));
  }
  





  @JsxFunction
  public void deleteCaption()
  {
    getDomNodeOrDie().removeChild("caption", 0);
  }
  





  @JsxFunction
  public void deleteTFoot()
  {
    getDomNodeOrDie().removeChild("tfoot", 0);
  }
  





  @JsxFunction
  public void deleteTHead()
  {
    getDomNodeOrDie().removeChild("thead", 0);
  }
  





  protected boolean isContainedRow(HtmlTableRow row)
  {
    DomNode parent = row.getParentNode();
    return (parent != null) && (parent.getParentNode() == getDomNodeOrDie());
  }
  





  public Object insertRow(int index)
  {
    List<?> rowContainers = 
      getDomNodeOrDie().getByXPath("//tbody | //thead | //tfoot");
    if ((rowContainers.isEmpty()) || (index == 0)) {
      HtmlElement tBody = getDomNodeOrDie().appendChildIfNoneExists("tbody");
      return ((RowContainer)getScriptableFor(tBody)).insertRow(0);
    }
    return super.insertRow(index);
  }
  



  @JsxGetter(propertyName="width")
  public String getWidth_js()
  {
    return getDomNodeOrDie().getAttribute("width");
  }
  



  @JsxSetter
  public void setWidth(String width)
  {
    getDomNodeOrDie().setAttribute("width", width);
  }
  



  @JsxGetter
  public String getCellSpacing()
  {
    return getDomNodeOrDie().getAttribute("cellspacing");
  }
  



  @JsxSetter
  public void setCellSpacing(String cellSpacing)
  {
    getDomNodeOrDie().setAttribute("cellspacing", cellSpacing);
  }
  



  @JsxGetter
  public String getCellPadding()
  {
    return getDomNodeOrDie().getAttribute("cellpadding");
  }
  



  @JsxSetter
  public void setCellPadding(String cellPadding)
  {
    getDomNodeOrDie().setAttribute("cellpadding", cellPadding);
  }
  



  @JsxGetter
  public String getBorder()
  {
    String border = getDomNodeOrDie().getAttribute("border");
    return border;
  }
  



  @JsxSetter
  public void setBorder(String border)
  {
    getDomNodeOrDie().setAttribute("border", border);
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
  



  public void setInnerText(Object value)
  {
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_INNER_TEXT_READONLY_FOR_TABLE)) {
      throw Context.reportRuntimeError("innerText is read-only for tag 'table'");
    }
    super.setInnerText(value);
  }
  



  public String getInnerText()
  {
    return getDomNodeOrDie().asText();
  }
  



  public Object appendChild(Object childObject)
  {
    Object appendedChild = super.appendChild(childObject);
    getWindow().clearComputedStyles(this);
    return appendedChild;
  }
  



  public Object removeChild(Object childObject)
  {
    Object removedChild = super.removeChild(childObject);
    getWindow().clearComputedStyles(this);
    return removedChild;
  }
  



  @JsxGetter
  public String getSummary()
  {
    return getDomNodeOrDie().getAttribute("summary");
  }
  



  @JsxSetter
  public void setSummary(String summary)
  {
    setAttribute("summary", summary);
  }
  



  @JsxGetter
  public String getRules()
  {
    String rules = getDomNodeOrDie().getAttribute("rules");
    if ((getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_TABLE_VALIGN_SUPPORTS_IE_VALUES)) && 
      (!VALID_RULES_.contains(rules))) {
      rules = "";
    }
    return rules;
  }
  



  @JsxSetter
  public void setRules(String rules)
  {
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_TABLE_VALIGN_SUPPORTS_IE_VALUES)) {
      rules = rules.toLowerCase(Locale.ROOT);
      if ((!rules.isEmpty()) && (!VALID_RULES_.contains(rules))) {
        throw Context.throwAsScriptRuntimeEx(new Exception("Invalid argument"));
      }
    }
    setAttribute("rules", rules);
  }
}
