package com.gargoylesoftware.htmlunit.javascript.host.file;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

































@JsxClass
public class FileList
  extends SimpleScriptable
{
  private File[] files_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public FileList() {}
  
  public FileList(java.io.File[] array)
  {
    files_ = new File[array.length];
    
    for (int i = 0; i < array.length; i++) {
      files_[i] = new File(array[i].getAbsolutePath());
    }
  }
  



  public void setParentScope(Scriptable m)
  {
    super.setParentScope(m);
    if (files_ != null) {
      for (File file : files_) {
        file.setParentScope(m);
        file.setPrototype(getPrototype(file.getClass()));
      }
    }
  }
  



  @JsxGetter
  public int getLength()
  {
    return files_.length;
  }
  




  @JsxFunction
  public File item(int index)
  {
    return files_[index];
  }
  



  public Object get(int index, Scriptable start)
  {
    return item(index);
  }
}
