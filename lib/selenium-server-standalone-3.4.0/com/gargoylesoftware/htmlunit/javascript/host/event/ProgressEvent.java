package com.gargoylesoftware.htmlunit.javascript.host.event;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

































@JsxClass
public class ProgressEvent
  extends Event
{
  private boolean lengthComputable_;
  private long loaded_;
  private long total_;
  
  public ProgressEvent() {}
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public void jsConstructor(String type, ScriptableObject details)
  {
    super.jsConstructor(type, details);
    
    if ((details != null) && (!Undefined.instance.equals(details))) {
      Object lengthComputable = details.get("lengthComputable");
      if ((lengthComputable instanceof Boolean)) {
        lengthComputable_ = ((Boolean)lengthComputable).booleanValue();
      }
      else {
        lengthComputable_ = Boolean.parseBoolean(lengthComputable.toString());
      }
      
      Object loaded = details.get("loaded");
      if ((loaded instanceof Long)) {
        loaded_ = ((Long)loaded).longValue();
      }
      else if ((loaded instanceof Double)) {
        loaded_ = ((Double)loaded).longValue();
      } else {
        try
        {
          loaded_ = Long.parseLong(loaded.toString());
        }
        catch (NumberFormatException localNumberFormatException) {}
      }
      


      Object total = details.get("total");
      if ((total instanceof Long)) {
        total_ = ((Long)total).longValue();
      }
      else if ((total instanceof Double)) {
        total_ = ((Double)total).longValue();
      } else {
        try
        {
          total_ = Long.parseLong(details.get("total").toString());
        }
        catch (NumberFormatException localNumberFormatException1) {}
      }
    }
  }
  






  public ProgressEvent(EventTarget target, String type)
  {
    super(target, type);
  }
  



  @JsxGetter
  public boolean getLengthComputable()
  {
    return lengthComputable_;
  }
  




  public void setLengthComputable(boolean lengthComputable)
  {
    lengthComputable_ = lengthComputable;
  }
  



  @JsxGetter
  public long getLoaded()
  {
    return loaded_;
  }
  




  public void setLoaded(long loaded)
  {
    loaded_ = loaded;
  }
  



  @JsxGetter
  public long getTotal()
  {
    return total_;
  }
  




  public void setTotal(long total)
  {
    total_ = total;
  }
}
