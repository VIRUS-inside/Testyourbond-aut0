package org.apache.http.cookie;

import org.apache.http.annotation.Obsolete;

public abstract interface SetCookie2
  extends SetCookie
{
  @Obsolete
  public abstract void setCommentURL(String paramString);
  
  @Obsolete
  public abstract void setPorts(int[] paramArrayOfInt);
  
  @Obsolete
  public abstract void setDiscard(boolean paramBoolean);
}
