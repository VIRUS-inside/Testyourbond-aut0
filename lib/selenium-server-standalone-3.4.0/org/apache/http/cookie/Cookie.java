package org.apache.http.cookie;

import java.util.Date;
import org.apache.http.annotation.Obsolete;

public abstract interface Cookie
{
  public abstract String getName();
  
  public abstract String getValue();
  
  @Obsolete
  public abstract String getComment();
  
  @Obsolete
  public abstract String getCommentURL();
  
  public abstract Date getExpiryDate();
  
  public abstract boolean isPersistent();
  
  public abstract String getDomain();
  
  public abstract String getPath();
  
  @Obsolete
  public abstract int[] getPorts();
  
  public abstract boolean isSecure();
  
  @Obsolete
  public abstract int getVersion();
  
  public abstract boolean isExpired(Date paramDate);
}
