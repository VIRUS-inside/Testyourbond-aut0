package org.apache.http.cookie;

import java.util.Date;
import org.apache.http.annotation.Obsolete;

public abstract interface SetCookie
  extends Cookie
{
  public abstract void setValue(String paramString);
  
  @Obsolete
  public abstract void setComment(String paramString);
  
  public abstract void setExpiryDate(Date paramDate);
  
  public abstract void setDomain(String paramString);
  
  public abstract void setPath(String paramString);
  
  public abstract void setSecure(boolean paramBoolean);
  
  @Obsolete
  public abstract void setVersion(int paramInt);
}
