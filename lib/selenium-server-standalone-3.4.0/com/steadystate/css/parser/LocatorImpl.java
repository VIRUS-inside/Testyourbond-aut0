package com.steadystate.css.parser;

import com.steadystate.css.util.LangUtils;
import java.io.Serializable;
import org.w3c.css.sac.Locator;




























public class LocatorImpl
  implements Locator, Serializable
{
  private static final long serialVersionUID = 2240824537064705530L;
  private String uri_;
  private int lineNumber_;
  private int columnNumber_;
  
  public LocatorImpl(String uri, int line, int column)
  {
    uri_ = uri;
    lineNumber_ = line;
    columnNumber_ = column;
  }
  








  public String getURI()
  {
    return uri_;
  }
  


  public String getUri()
  {
    return uri_;
  }
  




  public void setUri(String uri)
  {
    uri_ = uri;
  }
  







  public int getColumnNumber()
  {
    return columnNumber_;
  }
  



  public void setColumnNumber(int column)
  {
    columnNumber_ = column;
  }
  






  public int getLineNumber()
  {
    return lineNumber_;
  }
  



  public void setLineNumber(int line)
  {
    lineNumber_ = line;
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Locator)) {
      return false;
    }
    Locator l = (Locator)obj;
    

    return (getColumnNumber() == l.getColumnNumber()) && (getLineNumber() == l.getLineNumber()) && (LangUtils.equals(getURI(), l.getURI()));
  }
  
  public int hashCode()
  {
    int hash = 17;
    hash = LangUtils.hashCode(hash, columnNumber_);
    hash = LangUtils.hashCode(hash, lineNumber_);
    hash = LangUtils.hashCode(hash, uri_);
    return hash;
  }
  


  public String toString()
  {
    return getUri() + " (" + getLineNumber() + ':' + getColumnNumber() + ')';
  }
}
