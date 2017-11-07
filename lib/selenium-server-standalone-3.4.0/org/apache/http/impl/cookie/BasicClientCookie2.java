package org.apache.http.impl.cookie;

import java.util.Date;
import org.apache.http.cookie.SetCookie2;






































public class BasicClientCookie2
  extends BasicClientCookie
  implements SetCookie2
{
  private static final long serialVersionUID = -7744598295706617057L;
  private String commentURL;
  private int[] ports;
  private boolean discard;
  
  public BasicClientCookie2(String name, String value)
  {
    super(name, value);
  }
  
  public int[] getPorts()
  {
    return ports;
  }
  
  public void setPorts(int[] ports)
  {
    this.ports = ports;
  }
  
  public String getCommentURL()
  {
    return commentURL;
  }
  
  public void setCommentURL(String commentURL)
  {
    this.commentURL = commentURL;
  }
  
  public void setDiscard(boolean discard)
  {
    this.discard = discard;
  }
  
  public boolean isPersistent()
  {
    return (!discard) && (super.isPersistent());
  }
  
  public boolean isExpired(Date date)
  {
    return (discard) || (super.isExpired(date));
  }
  
  public Object clone() throws CloneNotSupportedException
  {
    BasicClientCookie2 clone = (BasicClientCookie2)super.clone();
    if (ports != null) {
      ports = ((int[])ports.clone());
    }
    return clone;
  }
}
