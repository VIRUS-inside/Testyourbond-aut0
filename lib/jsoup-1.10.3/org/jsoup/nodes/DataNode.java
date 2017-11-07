package org.jsoup.nodes;

import java.io.IOException;







public class DataNode
  extends Node
{
  private static final String DATA_KEY = "data";
  
  public DataNode(String data, String baseUri)
  {
    super(baseUri);
    attributes.put("data", data);
  }
  
  public String nodeName() {
    return "#data";
  }
  



  public String getWholeData()
  {
    return attributes.get("data");
  }
  




  public DataNode setWholeData(String data)
  {
    attributes.put("data", data);
    return this;
  }
  
  void outerHtmlHead(Appendable accum, int depth, Document.OutputSettings out) throws IOException {
    accum.append(getWholeData());
  }
  
  void outerHtmlTail(Appendable accum, int depth, Document.OutputSettings out) {}
  
  public String toString()
  {
    return outerHtml();
  }
  





  public static DataNode createFromEncoded(String encodedData, String baseUri)
  {
    String data = Entities.unescape(encodedData);
    return new DataNode(data, baseUri);
  }
}
