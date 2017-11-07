package com.gargoylesoftware.htmlunit.javascript.background;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import java.io.IOException;
import java.net.URL;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;























final class DownloadBehaviorJob
  extends BasicJavaScriptJob
{
  private static final Log LOG = LogFactory.getLog(DownloadBehaviorJob.class);
  

  private final URL url_;
  

  private final Function callback_;
  
  private final WebClient client_;
  

  DownloadBehaviorJob(URL url, Function callback, WebClient client)
  {
    url_ = url;
    callback_ = callback;
    client_ = client;
  }
  



  public void run()
  {
    final Scriptable scope = callback_.getParentScope();
    WebRequest request = new WebRequest(url_);
    try {
      WebResponse webResponse = client_.loadWebResponse(request);
      String content = webResponse.getContentAsString();
      if (LOG.isDebugEnabled()) {
        LOG.debug("Downloaded content: " + StringUtils.abbreviate(content, 512));
      }
      final Object[] args = { content };
      ContextAction action = new ContextAction()
      {
        public Object run(Context cx) {
          callback_.call(cx, scope, scope, args);
          return null;
        }
      };
      ContextFactory cf = client_.getJavaScriptEngine().getContextFactory();
      cf.call(action);
    }
    catch (IOException e) {
      LOG.error("Behavior #default#download: Cannot download " + url_, e);
    }
  }
}
