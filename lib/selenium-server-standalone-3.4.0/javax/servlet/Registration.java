package javax.servlet;

import java.util.Map;
import java.util.Set;

public abstract interface Registration
{
  public abstract String getName();
  
  public abstract String getClassName();
  
  public abstract boolean setInitParameter(String paramString1, String paramString2);
  
  public abstract String getInitParameter(String paramString);
  
  public abstract Set<String> setInitParameters(Map<String, String> paramMap);
  
  public abstract Map<String, String> getInitParameters();
  
  public static abstract interface Dynamic
    extends Registration
  {
    public abstract void setAsyncSupported(boolean paramBoolean);
  }
}
