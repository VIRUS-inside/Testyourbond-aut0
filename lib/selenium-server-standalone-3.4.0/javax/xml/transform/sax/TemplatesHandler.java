package javax.xml.transform.sax;

import javax.xml.transform.Templates;
import org.xml.sax.ContentHandler;

public abstract interface TemplatesHandler
  extends ContentHandler
{
  public abstract Templates getTemplates();
  
  public abstract void setSystemId(String paramString);
  
  public abstract String getSystemId();
}
