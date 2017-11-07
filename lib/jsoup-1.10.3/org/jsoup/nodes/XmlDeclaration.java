package org.jsoup.nodes;

import java.io.IOException;
import org.jsoup.helper.Validate;









public class XmlDeclaration
  extends Node
{
  private final String name;
  private final boolean isProcessingInstruction;
  
  public XmlDeclaration(String name, String baseUri, boolean isProcessingInstruction)
  {
    super(baseUri);
    Validate.notNull(name);
    this.name = name;
    this.isProcessingInstruction = isProcessingInstruction;
  }
  
  public String nodeName() {
    return "#declaration";
  }
  




  public String name()
  {
    return name;
  }
  



  public String getWholeDeclaration()
  {
    return attributes.html().trim();
  }
  

  void outerHtmlHead(Appendable accum, int depth, Document.OutputSettings out)
    throws IOException
  {
    accum.append("<").append(isProcessingInstruction ? "!" : "?").append(name);
    attributes.html(accum, out);
    accum
      .append(isProcessingInstruction ? "!" : "?")
      .append(">");
  }
  
  void outerHtmlTail(Appendable accum, int depth, Document.OutputSettings out) {}
  
  public String toString()
  {
    return outerHtml();
  }
}
