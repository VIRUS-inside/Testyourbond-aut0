package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.io.PrintWriter;
import org.w3c.dom.DOMException;
import org.w3c.dom.ProcessingInstruction;


























public class DomProcessingInstruction
  extends DomNode
  implements ProcessingInstruction
{
  private final String target_;
  private String data_;
  
  public DomProcessingInstruction(SgmlPage page, String target, String data)
  {
    super(page);
    target_ = target;
    setData(data);
  }
  




  public short getNodeType()
  {
    return 7;
  }
  



  public String getNodeName()
  {
    return target_;
  }
  



  public String getTarget()
  {
    return getNodeName();
  }
  



  public String getData()
  {
    return getNodeValue();
  }
  


  public void setData(String data)
    throws DOMException
  {
    setNodeValue(data);
  }
  



  public void setNodeValue(String value)
  {
    data_ = value;
  }
  



  public String getNodeValue()
  {
    return data_;
  }
  



  public void setTextContent(String textContent)
  {
    setNodeValue(textContent);
  }
  



  protected void printXml(String indent, PrintWriter printWriter)
  {
    printWriter.print("<?");
    printWriter.print(getTarget());
    printWriter.print(" ");
    printWriter.print(getData());
    printWriter.print("?>");
  }
}
