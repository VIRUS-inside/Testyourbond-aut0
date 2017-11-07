package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.io.PrintWriter;
import org.w3c.dom.Comment;



























public class DomComment
  extends DomCharacterData
  implements Comment
{
  public static final String NODE_NAME = "#comment";
  
  public DomComment(SgmlPage page, String data)
  {
    super(page, data);
  }
  




  public short getNodeType()
  {
    return 8;
  }
  



  public String getNodeName()
  {
    return "#comment";
  }
  






  protected void printXml(String indent, PrintWriter printWriter)
  {
    printWriter.print(indent);
    printWriter.print("<!--");
    printWriter.print(getData());
    printWriter.print("-->");
    printChildrenAsXml(indent, printWriter);
  }
  




  public String toString()
  {
    return asXml();
  }
}
