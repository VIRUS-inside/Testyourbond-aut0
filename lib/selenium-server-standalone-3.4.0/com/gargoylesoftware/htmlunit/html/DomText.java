package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.impl.SelectionDelegate;
import com.gargoylesoftware.htmlunit.html.impl.SimpleSelectionDelegate;
import java.io.PrintWriter;
import org.w3c.dom.DOMException;
import org.w3c.dom.Text;

































public class DomText
  extends DomCharacterData
  implements Text
{
  private SelectionDelegate selectionDelegate_;
  private DoTypeProcessor doTypeProcessor_;
  public static final String NODE_NAME = "#text";
  
  public DomText(SgmlPage page, String data)
  {
    super(page, data);
  }
  



  public DomText splitText(int offset)
  {
    if ((offset < 0) || (offset > getLength())) {
      throw new IllegalArgumentException("offset: " + offset + " data.length: " + getLength());
    }
    

    DomText newText = createSplitTextNode(offset);
    setData(getData().substring(0, offset));
    

    if (getParentNode() != null) {
      getParentNode().insertBefore(newText, getNextSibling());
    }
    return newText;
  }
  






  protected DomText createSplitTextNode(int offset)
  {
    return new DomText(getPage(), getData().substring(offset));
  }
  




  public boolean isElementContentWhitespace()
  {
    throw new UnsupportedOperationException("DomText.isElementContentWhitespace is not yet implemented.");
  }
  





  public String getWholeText()
  {
    return getNodeValue();
  }
  



  public Text replaceWholeText(String content)
    throws DOMException
  {
    throw new UnsupportedOperationException("DomText.replaceWholeText is not yet implemented.");
  }
  



  public short getNodeType()
  {
    return 3;
  }
  



  public String getNodeName()
  {
    return "#text";
  }
  






  protected void printXml(String indent, PrintWriter printWriter)
  {
    String data = getData();
    if (org.apache.commons.lang3.StringUtils.isNotBlank(data)) {
      printWriter.print(indent);
      if ((!(getParentNode() instanceof HtmlStyle)) || (!data.startsWith("<!--")) || (!data.endsWith("-->"))) {
        data = com.gargoylesoftware.htmlunit.util.StringUtils.escapeXmlChars(data);
      }
      printWriter.print(data);
      printWriter.print("\r\n");
    }
    printChildrenAsXml(indent, printWriter);
  }
  




  public String toString()
  {
    return asText();
  }
  



  protected boolean isTrimmedText()
  {
    return false;
  }
  







  protected void doType(char c, boolean startAtEnd, HtmlElement htmlElement, boolean lastType)
  {
    initDoTypeProcessor();
    if (startAtEnd) {
      selectionDelegate_.setSelectionStart(getData().length());
    }
    doTypeProcessor_.doType(getData(), selectionDelegate_, c, htmlElement, lastType);
  }
  








  protected void doType(int keyCode, boolean startAtEnd, HtmlElement htmlElement, boolean lastType)
  {
    initDoTypeProcessor();
    if (startAtEnd) {
      selectionDelegate_.setSelectionStart(getData().length());
    }
    doTypeProcessor_.doType(getData(), selectionDelegate_, keyCode, htmlElement, lastType);
  }
  
  private void initDoTypeProcessor() {
    if (selectionDelegate_ == null) {
      selectionDelegate_ = new SimpleSelectionDelegate();
      doTypeProcessor_ = new DoTypeProcessor(this);
    }
  }
  






  protected boolean acceptChar(char c)
  {
    return ((c < 57344) || (c > 63743)) && ((c == ' ') || (!Character.isWhitespace(c)));
  }
}
