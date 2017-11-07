package com.gargoylesoftware.htmlunit.util;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitContextFactory;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import java.net.URL;
import javax.swing.JFrame;
import net.sourceforge.htmlunit.corejs.javascript.debug.DebuggableScript;
import net.sourceforge.htmlunit.corejs.javascript.tools.debugger.Main;
import net.sourceforge.htmlunit.corejs.javascript.tools.debugger.ScopeProvider;
import net.sourceforge.htmlunit.corejs.javascript.tools.debugger.SourceProvider;
import org.apache.commons.lang3.StringUtils;




























public final class WebClientUtils
{
  private WebClientUtils() {}
  
  public static void attachVisualDebugger(WebClient client)
  {
    ScopeProvider sp = null;
    HtmlUnitContextFactory cf = client.getJavaScriptEngine().getContextFactory();
    Main main = Main.mainEmbedded(cf, sp, "HtmlUnit JavaScript Debugger");
    main.getDebugFrame().setExtendedState(6);
    
    SourceProvider sourceProvider = new SourceProvider()
    {
      public String getSource(DebuggableScript script) {
        String sourceName = script.getSourceName();
        if ((sourceName.endsWith("(eval)")) || (sourceName.endsWith("(Function)"))) {
          return null;
        }
        if (sourceName.startsWith("script in ")) {
          sourceName = StringUtils.substringBetween(sourceName, "script in ", " from");
          for (WebWindow ww : getWebWindows()) {
            WebResponse wr = ww.getEnclosedPage().getWebResponse();
            if (sourceName.equals(wr.getWebRequest().getUrl().toString())) {
              return wr.getContentAsString();
            }
          }
        }
        return null;
      }
    };
    main.setSourceProvider(sourceProvider);
  }
}
