package org.apache.http.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.util.CharArrayBuffer;





































public class HeaderGroup
  implements Cloneable, Serializable
{
  private static final long serialVersionUID = 2608834160639271617L;
  private final Header[] EMPTY = new Header[0];
  

  private final List<Header> headers;
  


  public HeaderGroup()
  {
    headers = new ArrayList(16);
  }
  


  public void clear()
  {
    headers.clear();
  }
  





  public void addHeader(Header header)
  {
    if (header == null) {
      return;
    }
    headers.add(header);
  }
  




  public void removeHeader(Header header)
  {
    if (header == null) {
      return;
    }
    headers.remove(header);
  }
  






  public void updateHeader(Header header)
  {
    if (header == null) {
      return;
    }
    


    for (int i = 0; i < headers.size(); i++) {
      Header current = (Header)headers.get(i);
      if (current.getName().equalsIgnoreCase(header.getName())) {
        headers.set(i, header);
        return;
      }
    }
    headers.add(header);
  }
  






  public void setHeaders(Header[] headers)
  {
    clear();
    if (headers == null) {
      return;
    }
    Collections.addAll(this.headers, headers);
  }
  










  public Header getCondensedHeader(String name)
  {
    Header[] hdrs = getHeaders(name);
    
    if (hdrs.length == 0)
      return null;
    if (hdrs.length == 1) {
      return hdrs[0];
    }
    CharArrayBuffer valueBuffer = new CharArrayBuffer(128);
    valueBuffer.append(hdrs[0].getValue());
    for (int i = 1; i < hdrs.length; i++) {
      valueBuffer.append(", ");
      valueBuffer.append(hdrs[i].getValue());
    }
    
    return new BasicHeader(name.toLowerCase(Locale.ROOT), valueBuffer.toString());
  }
  










  public Header[] getHeaders(String name)
  {
    List<Header> headersFound = null;
    


    for (int i = 0; i < headers.size(); i++) {
      Header header = (Header)headers.get(i);
      if (header.getName().equalsIgnoreCase(name)) {
        if (headersFound == null) {
          headersFound = new ArrayList();
        }
        headersFound.add(header);
      }
    }
    return headersFound != null ? (Header[])headersFound.toArray(new Header[headersFound.size()]) : EMPTY;
  }
  










  public Header getFirstHeader(String name)
  {
    for (int i = 0; i < headers.size(); i++) {
      Header header = (Header)headers.get(i);
      if (header.getName().equalsIgnoreCase(name)) {
        return header;
      }
    }
    return null;
  }
  








  public Header getLastHeader(String name)
  {
    for (int i = headers.size() - 1; i >= 0; i--) {
      Header header = (Header)headers.get(i);
      if (header.getName().equalsIgnoreCase(name)) {
        return header;
      }
    }
    
    return null;
  }
  




  public Header[] getAllHeaders()
  {
    return (Header[])headers.toArray(new Header[headers.size()]);
  }
  











  public boolean containsHeader(String name)
  {
    for (int i = 0; i < headers.size(); i++) {
      Header header = (Header)headers.get(i);
      if (header.getName().equalsIgnoreCase(name)) {
        return true;
      }
    }
    
    return false;
  }
  






  public HeaderIterator iterator()
  {
    return new BasicListHeaderIterator(headers, null);
  }
  









  public HeaderIterator iterator(String name)
  {
    return new BasicListHeaderIterator(headers, name);
  }
  






  public HeaderGroup copy()
  {
    HeaderGroup clone = new HeaderGroup();
    headers.addAll(headers);
    return clone;
  }
  
  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }
  
  public String toString()
  {
    return headers.toString();
  }
}
