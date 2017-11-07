package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.io.PrintWriter;
import org.w3c.dom.CDATASection;


























public class DomCDataSection
  extends DomText
  implements CDATASection
{
  public DomCDataSection(SgmlPage page, String data)
  {
    super(page, data);
  }
  



  public short getNodeType()
  {
    return 4;
  }
  



  public String getNodeName()
  {
    return "#cdata-section";
  }
  



  protected void printXml(String indent, PrintWriter printWriter)
  {
    printWriter.print("<![CDATA[");
    printWriter.print(getData());
    printWriter.print("]]>");
  }
  



  protected DomText createSplitTextNode(int offset)
  {
    return new DomCDataSection(getPage(), getData().substring(offset));
  }
}
