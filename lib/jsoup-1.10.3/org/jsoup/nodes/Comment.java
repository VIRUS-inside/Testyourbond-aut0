package org.jsoup.nodes;

import java.io.IOException;







public class Comment
  extends Node
{
  private static final String COMMENT_KEY = "comment";
  
  public Comment(String data, String baseUri)
  {
    super(baseUri);
    attributes.put("comment", data);
  }
  
  public String nodeName() {
    return "#comment";
  }
  



  public String getData()
  {
    return attributes.get("comment");
  }
  
  void outerHtmlHead(Appendable accum, int depth, Document.OutputSettings out) throws IOException {
    if (out.prettyPrint()) {
      indent(accum, depth, out);
    }
    

    accum.append("<!--").append(getData()).append("-->");
  }
  
  void outerHtmlTail(Appendable accum, int depth, Document.OutputSettings out) {}
  
  public String toString()
  {
    return outerHtml();
  }
}
