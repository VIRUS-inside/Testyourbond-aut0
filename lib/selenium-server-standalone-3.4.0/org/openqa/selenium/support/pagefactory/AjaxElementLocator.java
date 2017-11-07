package org.openqa.selenium.support.pagefactory;

import com.google.common.collect.Lists;
import java.lang.reflect.Field;
import java.util.List;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Clock;
import org.openqa.selenium.support.ui.SlowLoadableComponent;
import org.openqa.selenium.support.ui.SystemClock;

































public class AjaxElementLocator
  extends DefaultElementLocator
{
  protected final int timeOutInSeconds;
  private final Clock clock;
  
  public AjaxElementLocator(SearchContext context, int timeOutInSeconds, AbstractAnnotations annotations)
  {
    this(new SystemClock(), context, timeOutInSeconds, annotations);
  }
  
  public AjaxElementLocator(Clock clock, SearchContext context, int timeOutInSeconds, AbstractAnnotations annotations)
  {
    super(context, annotations);
    this.timeOutInSeconds = timeOutInSeconds;
    this.clock = clock;
  }
  






  public AjaxElementLocator(SearchContext searchContext, Field field, int timeOutInSeconds)
  {
    this(new SystemClock(), searchContext, field, timeOutInSeconds);
  }
  
  public AjaxElementLocator(Clock clock, SearchContext searchContext, Field field, int timeOutInSeconds) {
    this(clock, searchContext, timeOutInSeconds, new Annotations(field));
  }
  





  public WebElement findElement()
  {
    SlowLoadingElement loadingElement = new SlowLoadingElement(clock, timeOutInSeconds);
    try {
      return ((SlowLoadingElement)loadingElement.get()).getElement();
    }
    catch (NoSuchElementError e)
    {
      throw new NoSuchElementException(String.format("Timed out after %d seconds. %s", new Object[] {Integer.valueOf(timeOutInSeconds), e.getMessage() }), e.getCause());
    }
  }
  





  public List<WebElement> findElements()
  {
    SlowLoadingElementList list = new SlowLoadingElementList(clock, timeOutInSeconds);
    try {
      return ((SlowLoadingElementList)list.get()).getElements();
    } catch (NoSuchElementError e) {}
    return Lists.newArrayList();
  }
  






  protected long sleepFor()
  {
    return 250L;
  }
  











  protected boolean isElementUsable(WebElement element)
  {
    return true;
  }
  
  private class SlowLoadingElement extends SlowLoadableComponent<SlowLoadingElement> {
    private NoSuchElementException lastException;
    private WebElement element;
    
    public SlowLoadingElement(Clock clock, int timeOutInSeconds) {
      super(timeOutInSeconds);
    }
    


    protected void load() {}
    

    protected long sleepFor()
    {
      return AjaxElementLocator.this.sleepFor();
    }
    
    protected void isLoaded() throws Error
    {
      try {
        element = AjaxElementLocator.this.findElement();
        if (!isElementUsable(element)) {
          throw new NoSuchElementException("Element is not usable");
        }
      } catch (NoSuchElementException e) {
        lastException = e;
        
        throw new AjaxElementLocator.NoSuchElementError("Unable to locate the element", e, null);
      }
    }
    
    public NoSuchElementException getLastException() {
      return lastException;
    }
    
    public WebElement getElement() {
      return element;
    }
  }
  
  private class SlowLoadingElementList extends SlowLoadableComponent<SlowLoadingElementList> {
    private NoSuchElementException lastException;
    private List<WebElement> elements;
    
    public SlowLoadingElementList(Clock clock, int timeOutInSeconds) {
      super(timeOutInSeconds);
    }
    


    protected void load() {}
    

    protected long sleepFor()
    {
      return AjaxElementLocator.this.sleepFor();
    }
    
    protected void isLoaded() throws Error
    {
      try {
        elements = AjaxElementLocator.this.findElements();
        if (elements.size() == 0) {
          throw new NoSuchElementException("Unable to locate the element");
        }
        for (WebElement element : elements) {
          if (!isElementUsable(element)) {
            throw new NoSuchElementException("Element is not usable");
          }
        }
      } catch (NoSuchElementException e) {
        lastException = e;
        
        throw new AjaxElementLocator.NoSuchElementError("Unable to locate the element", e, null);
      }
    }
    
    public NoSuchElementException getLastException() {
      return lastException;
    }
    
    public List<WebElement> getElements() {
      return elements;
    }
  }
  
  private static class NoSuchElementError extends Error {
    private NoSuchElementError(String message, Throwable throwable) {
      super(throwable);
    }
  }
}
