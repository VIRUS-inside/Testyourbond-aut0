package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.PopStateEvent;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;





















public class History
  implements Serializable
{
  private final WebWindow window_;
  private transient ThreadLocal<Boolean> ignoreNewPages_;
  
  private static final class HistoryEntry
    implements Serializable
  {
    private transient SoftReference<Page> page_;
    private final WebRequest webRequest_;
    private Object state_;
    
    private HistoryEntry(Page page)
    {
      page_ = new SoftReference(page);
      
      WebRequest request = page.getWebResponse().getWebRequest();
      webRequest_ = new WebRequest(request.getUrl(), request.getHttpMethod());
      webRequest_.setRequestParameters(request.getRequestParameters());
    }
    
    private Page getPage() {
      if (page_ == null) {
        return null;
      }
      return (Page)page_.get();
    }
    
    private void clearPage() {
      page_ = null;
    }
    
    private WebRequest getWebRequest() {
      return webRequest_;
    }
    
    private URL getUrl() {
      return webRequest_.getUrl();
    }
    
    private void setUrl(URL url) {
      if (url != null) {
        webRequest_.setUrl(url);
        Page page = getPage();
        if (page != null) {
          page.getWebResponse().getWebRequest().setUrl(url);
        }
      }
    }
    


    private Object getState()
    {
      return state_;
    }
    



    private void setState(Object state)
    {
      state_ = state;
    }
  }
  













  private final List<HistoryEntry> entries_ = new ArrayList();
  

  private int index_ = -1;
  



  public History(WebWindow window)
  {
    window_ = window;
    initTransientFields();
  }
  


  private void initTransientFields()
  {
    ignoreNewPages_ = new ThreadLocal();
  }
  



  public int getLength()
  {
    return entries_.size();
  }
  



  public int getIndex()
  {
    return index_;
  }
  




  public URL getUrl(int index)
  {
    if ((index >= 0) && (index < entries_.size())) {
      return UrlUtils.toUrlSafe(((HistoryEntry)entries_.get(index)).getUrl().toExternalForm());
    }
    return null;
  }
  



  public History back()
    throws IOException
  {
    if (index_ > 0) {
      index_ -= 1;
      goToUrlAtCurrentIndex();
    }
    return this;
  }
  



  public History forward()
    throws IOException
  {
    if (index_ < entries_.size() - 1) {
      index_ += 1;
      goToUrlAtCurrentIndex();
    }
    return this;
  }
  





  public History go(int relativeIndex)
    throws IOException
  {
    int i = index_ + relativeIndex;
    if ((i < entries_.size()) && (i >= 0)) {
      index_ = i;
      goToUrlAtCurrentIndex();
    }
    return this;
  }
  



  public String toString()
  {
    return entries_.toString();
  }
  


  public void removeCurrent()
  {
    if ((index_ >= 0) && (index_ < entries_.size())) {
      entries_.remove(index_);
      if (index_ > 0) {
        index_ -= 1;
      }
    }
  }
  




  protected HistoryEntry addPage(Page page)
  {
    Boolean ignoreNewPages = (Boolean)ignoreNewPages_.get();
    if ((ignoreNewPages != null) && (ignoreNewPages.booleanValue())) {
      return null;
    }
    
    int sizeLimit = window_.getWebClient().getOptions().getHistorySizeLimit();
    if (sizeLimit <= 0) {
      entries_.clear();
      index_ = -1;
      return null;
    }
    
    index_ += 1;
    while (entries_.size() > index_) {
      entries_.remove(index_);
    }
    while (entries_.size() >= sizeLimit) {
      entries_.remove(0);
      index_ -= 1;
    }
    
    HistoryEntry entry = new HistoryEntry(page, null);
    entries_.add(entry);
    
    int cacheLimit = Math.max(window_.getWebClient().getOptions().getHistoryPageCacheLimit(), 0);
    if (entries_.size() > cacheLimit) {
      ((HistoryEntry)entries_.get(entries_.size() - cacheLimit - 1)).clearPage();
    }
    return entry;
  }
  


  private void goToUrlAtCurrentIndex()
    throws IOException
  {
    Boolean old = (Boolean)ignoreNewPages_.get();
    ignoreNewPages_.set(Boolean.TRUE);
    try
    {
      HistoryEntry entry = (HistoryEntry)entries_.get(index_);
      
      Page page = entry.getPage();
      if (page == null) {
        window_.getWebClient().getPage(window_, entry.getWebRequest(), false);
      }
      else {
        window_.setEnclosedPage(page);
        page.getWebResponse().getWebRequest().setUrl(entry.getUrl());
      }
      
      Window jsWindow = (Window)window_.getScriptableObject();
      if (jsWindow.hasEventHandlers("onpopstate")) {
        Event event = new PopStateEvent(jsWindow, "popstate", entry.getState());
        jsWindow.executeEventLocally(event);
      }
    }
    finally {
      ignoreNewPages_.set(old);
    }
  }
  





  public void replaceState(Object state, URL url)
  {
    if ((index_ >= 0) && (index_ < entries_.size())) {
      HistoryEntry entry = (HistoryEntry)entries_.get(index_);
      entry.setUrl(url);
      entry.setState(state);
    }
  }
  





  public void pushState(Object state, URL url)
  {
    Page page = window_.getEnclosedPage();
    HistoryEntry entry = addPage(page);
    
    if (entry != null) {
      entry.setUrl(url);
      entry.setState(state);
    }
  }
  




  public Object getCurrentState()
  {
    if ((index_ >= 0) && (index_ < entries_.size())) {
      return ((HistoryEntry)entries_.get(index_)).getState();
    }
    return null;
  }
  




  private void readObject(ObjectInputStream in)
    throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    initTransientFields();
  }
}
