package netscape.javascript;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import java.applet.Applet;
import net.sourceforge.htmlunit.corejs.javascript.ConsString;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;




























public class JSObject
{
  private static Window Window_;
  private ScriptableObject scriptableObject_;
  
  public JSObject(ScriptableObject scriptableObject)
  {
    scriptableObject_ = scriptableObject;
  }
  






  public Object call(String paramString, Object[] paramArrayOfObject)
    throws JSException
  {
    throw new RuntimeException("Not yet implemented (netscape.javascript.JSObject.call(String, Object[])).");
  }
  





  public Object eval(String paramString)
    throws JSException
  {
    Page page = Window_.getWebWindow().getEnclosedPage();
    if ((page instanceof HtmlPage)) {
      HtmlPage htmlPage = (HtmlPage)page;
      ScriptResult result = htmlPage.executeJavaScript(paramString);
      if ((result.getJavaScriptResult() instanceof ScriptableObject)) {
        return new JSObject((ScriptableObject)result.getJavaScriptResult());
      }
      if ((result.getJavaScriptResult() instanceof ConsString)) {
        return ((ConsString)result.getJavaScriptResult()).toString();
      }
      return result.getJavaScriptResult();
    }
    return null;
  }
  





  public Object getMember(String paramString)
    throws JSException
  {
    if ((scriptableObject_ instanceof Element)) {
      return ((Element)scriptableObject_).getAttribute(paramString, null);
    }
    return scriptableObject_.get(paramString, scriptableObject_);
  }
  





  public void setMember(String paramString, Object paramObject)
    throws JSException
  {
    if ((scriptableObject_ instanceof Element)) {
      ((Element)scriptableObject_).setAttribute(paramString, paramObject.toString());
      return;
    }
    scriptableObject_.put(paramString, scriptableObject_, paramObject);
  }
  




  public void removeMember(String paramString)
    throws JSException
  {
    throw new RuntimeException("Not yet implemented (netscape.javascript.JSObject.removeMember(String)).");
  }
  





  public Object getSlot(int paramInt)
    throws JSException
  {
    throw new RuntimeException("Not yet implemented (netscape.javascript.JSObject.getSlot(int)).");
  }
  





  public void setSlot(int paramInt, Object paramObject)
    throws JSException
  {
    throw new RuntimeException("Not yet implemented (netscape.javascript.JSObject.setSlot(int, Object)).");
  }
  



  public static JSObject getWindow(Applet paramApplet)
    throws JSException
  {
    return new JSObject(Window_);
  }
  






  public static void setWindow(Window window)
  {
    Window_ = window;
  }
}
