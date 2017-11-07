package org.jsoup.nodes;

import java.io.IOException;
import org.jsoup.helper.StringUtil;











public class DocumentType
  extends Node
{
  public static final String PUBLIC_KEY = "PUBLIC";
  public static final String SYSTEM_KEY = "SYSTEM";
  private static final String NAME = "name";
  private static final String PUB_SYS_KEY = "pubSysKey";
  private static final String PUBLIC_ID = "publicId";
  private static final String SYSTEM_ID = "systemId";
  
  public DocumentType(String name, String publicId, String systemId, String baseUri)
  {
    super(baseUri);
    
    attr("name", name);
    attr("publicId", publicId);
    if (has("publicId")) {
      attr("pubSysKey", "PUBLIC");
    }
    attr("systemId", systemId);
  }
  






  public DocumentType(String name, String pubSysKey, String publicId, String systemId, String baseUri)
  {
    super(baseUri);
    
    attr("name", name);
    if (pubSysKey != null) {
      attr("pubSysKey", pubSysKey);
    }
    attr("publicId", publicId);
    attr("systemId", systemId);
  }
  
  public String nodeName()
  {
    return "#doctype";
  }
  
  void outerHtmlHead(Appendable accum, int depth, Document.OutputSettings out) throws IOException
  {
    if ((out.syntax() == Document.OutputSettings.Syntax.html) && (!has("publicId")) && (!has("systemId")))
    {
      accum.append("<!doctype");
    } else {
      accum.append("<!DOCTYPE");
    }
    if (has("name"))
      accum.append(" ").append(attr("name"));
    if (has("pubSysKey"))
      accum.append(" ").append(attr("pubSysKey"));
    if (has("publicId"))
      accum.append(" \"").append(attr("publicId")).append('"');
    if (has("systemId"))
      accum.append(" \"").append(attr("systemId")).append('"');
    accum.append('>');
  }
  

  void outerHtmlTail(Appendable accum, int depth, Document.OutputSettings out) {}
  
  private boolean has(String attribute)
  {
    return !StringUtil.isBlank(attr(attribute));
  }
}
