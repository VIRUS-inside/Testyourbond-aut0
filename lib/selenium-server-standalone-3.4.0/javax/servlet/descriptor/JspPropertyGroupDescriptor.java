package javax.servlet.descriptor;

import java.util.Collection;

public abstract interface JspPropertyGroupDescriptor
{
  public abstract Collection<String> getUrlPatterns();
  
  public abstract String getElIgnored();
  
  public abstract String getPageEncoding();
  
  public abstract String getScriptingInvalid();
  
  public abstract String getIsXml();
  
  public abstract Collection<String> getIncludePreludes();
  
  public abstract Collection<String> getIncludeCodas();
  
  public abstract String getDeferredSyntaxAllowedAsLiteral();
  
  public abstract String getTrimDirectiveWhitespaces();
  
  public abstract String getDefaultContentType();
  
  public abstract String getBuffer();
  
  public abstract String getErrorOnUndeclaredNamespace();
}
