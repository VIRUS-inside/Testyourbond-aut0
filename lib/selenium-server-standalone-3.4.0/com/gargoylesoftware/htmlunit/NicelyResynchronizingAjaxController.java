package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.ref.WeakReference;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

























public class NicelyResynchronizingAjaxController
  extends AjaxController
{
  private static final Log LOG = LogFactory.getLog(NicelyResynchronizingAjaxController.class);
  

  private transient WeakReference<Thread> originatedThread_;
  

  public NicelyResynchronizingAjaxController()
  {
    init();
  }
  


  private void init()
  {
    originatedThread_ = new WeakReference(Thread.currentThread());
  }
  





  public boolean processSynchron(HtmlPage page, WebRequest settings, boolean async)
  {
    if ((async) && (isInOriginalThread())) {
      LOG.info("Re-synchronized call to " + settings.getUrl());
      return true;
    }
    return !async;
  }
  



  boolean isInOriginalThread()
  {
    return Thread.currentThread() == originatedThread_.get();
  }
  




  private void readObject(ObjectInputStream stream)
    throws IOException, ClassNotFoundException
  {
    stream.defaultReadObject();
    init();
  }
}
