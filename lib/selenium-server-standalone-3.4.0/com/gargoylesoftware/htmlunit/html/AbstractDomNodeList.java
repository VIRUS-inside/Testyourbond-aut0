package com.gargoylesoftware.htmlunit.html;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Node;































public abstract class AbstractDomNodeList<E extends DomNode>
  extends AbstractList<E>
  implements DomNodeList<E>, Serializable
{
  private DomNode node_;
  private List<E> cachedElements_;
  
  public AbstractDomNodeList(DomNode node)
  {
    if (node != null) {
      node_ = node;
      DomHtmlAttributeChangeListenerImpl listener = new DomHtmlAttributeChangeListenerImpl(this, null);
      node_.addDomChangeListener(listener);
      if ((node_ instanceof HtmlElement)) {
        ((HtmlElement)node_).addHtmlAttributeChangeListener(listener);
        cachedElements_ = null;
      }
    }
  }
  



  protected DomNode getDomNode()
  {
    return node_;
  }
  




  protected abstract List<E> provideElements();
  



  private List<E> getNodes()
  {
    if (cachedElements_ == null) {
      if (node_ == null) {
        cachedElements_ = new ArrayList();
      }
      else {
        cachedElements_ = provideElements();
      }
    }
    return cachedElements_;
  }
  



  public int size()
  {
    return getLength();
  }
  



  public int getLength()
  {
    return getNodes().size();
  }
  



  public Node item(int index)
  {
    return (Node)getNodes().get(index);
  }
  



  public E get(int index)
  {
    return (DomNode)getNodes().get(index);
  }
  


  private static final class DomHtmlAttributeChangeListenerImpl
    implements DomChangeListener, HtmlAttributeChangeListener
  {
    private transient WeakReference<AbstractDomNodeList<?>> nodeList_;
    


    private DomHtmlAttributeChangeListenerImpl(AbstractDomNodeList<?> nodeList)
    {
      nodeList_ = new WeakReference(nodeList);
    }
    



    public void nodeAdded(DomChangeEvent event)
    {
      clearCache();
    }
    



    public void nodeDeleted(DomChangeEvent event)
    {
      clearCache();
    }
    



    public void attributeAdded(HtmlAttributeChangeEvent event)
    {
      clearCache();
    }
    



    public void attributeRemoved(HtmlAttributeChangeEvent event)
    {
      clearCache();
    }
    



    public void attributeReplaced(HtmlAttributeChangeEvent event)
    {
      clearCache();
    }
    
    private void clearCache() {
      if (nodeList_ != null) {
        AbstractDomNodeList<?> nodes = (AbstractDomNodeList)nodeList_.get();
        if (nodes != null) {
          cachedElements_ = null;
        }
      }
    }
  }
}
