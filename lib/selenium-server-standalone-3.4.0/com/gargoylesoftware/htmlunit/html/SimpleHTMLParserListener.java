package com.gargoylesoftware.htmlunit.html;

import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
































































class SimpleHTMLParserListener
  implements HTMLParserListener
{
  private static final Log LOG = LogFactory.getLog(HTMLParserListener.class);
  
  SimpleHTMLParserListener() {}
  
  public void error(String message, URL url, String html, int line, int column, String key) {
    LOG.error(format(message, url, html, line, column));
  }
  

  public void warning(String message, URL url, String html, int line, int column, String key)
  {
    LOG.warn(format(message, url, html, line, column));
  }
  
  private static String format(String message, URL url, String html, int line, int column)
  {
    StringBuilder builder = new StringBuilder(message);
    builder.append(" (");
    builder.append(url.toExternalForm());
    builder.append(" ");
    builder.append(line);
    builder.append(":");
    builder.append(column);
    if (html != null) {
      builder.append(" htmlSnippet: '");
      builder.append(html);
      builder.append("'");
    }
    builder.append(")");
    return builder.toString();
  }
}
