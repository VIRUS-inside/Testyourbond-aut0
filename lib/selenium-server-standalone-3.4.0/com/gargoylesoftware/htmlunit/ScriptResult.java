package com.gargoylesoftware.htmlunit;

import net.sourceforge.htmlunit.corejs.javascript.Undefined;





























public final class ScriptResult
{
  private final Object javaScriptResult_;
  private final Page newPage_;
  
  public ScriptResult(Object javaScriptResult, Page newPage)
  {
    javaScriptResult_ = javaScriptResult;
    newPage_ = newPage;
  }
  



  public Object getJavaScriptResult()
  {
    return javaScriptResult_;
  }
  



  public Page getNewPage()
  {
    return newPage_;
  }
  



  public String toString()
  {
    return "ScriptResult[result=" + javaScriptResult_ + " page=" + newPage_ + "]";
  }
  




  public static boolean isFalse(ScriptResult scriptResult)
  {
    return (scriptResult != null) && (Boolean.FALSE.equals(scriptResult.getJavaScriptResult()));
  }
  




  public static boolean isUndefined(ScriptResult scriptResult)
  {
    return (scriptResult != null) && ((scriptResult.getJavaScriptResult() instanceof Undefined));
  }
  





  public static ScriptResult combine(ScriptResult newResult, ScriptResult originalResult, boolean ie)
  {
    Object jsResult;
    




    Object jsResult;
    




    if (ie) { Object jsResult;
      if ((newResult != null) && (!isUndefined(newResult))) {
        jsResult = newResult.getJavaScriptResult();
      } else { Object jsResult;
        if (originalResult != null) {
          jsResult = originalResult.getJavaScriptResult();
        } else { Object jsResult;
          if (newResult != null) {
            jsResult = newResult.getJavaScriptResult();
          }
          else
            jsResult = null;
        }
      }
    } else { Object jsResult;
      if (isFalse(newResult)) {
        jsResult = newResult.getJavaScriptResult();
      } else { Object jsResult;
        if (originalResult != null) {
          jsResult = originalResult.getJavaScriptResult();
        } else { Object jsResult;
          if (newResult != null) {
            jsResult = newResult.getJavaScriptResult();
          }
          else
            jsResult = null;
        }
      } }
    Page page;
    Page page;
    if (newResult != null) {
      page = newResult.getNewPage();
    } else { Page page;
      if (originalResult != null) {
        page = originalResult.getNewPage();
      }
      else {
        page = null;
      }
    }
    
    if ((jsResult == null) && (page == null)) {
      return null;
    }
    return new ScriptResult(jsResult, page);
  }
}
