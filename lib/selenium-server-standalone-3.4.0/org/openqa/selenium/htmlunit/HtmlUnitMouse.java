package org.openqa.selenium.htmlunit;

import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import java.io.IOException;
import java.io.PrintStream;
import java.net.SocketTimeoutException;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.interactions.InvalidCoordinatesException;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.internal.Coordinates;






















public class HtmlUnitMouse
  implements Mouse
{
  private final HtmlUnitDriver parent;
  private final HtmlUnitKeyboard keyboard;
  private DomElement currentActiveElement;
  
  public HtmlUnitMouse(HtmlUnitDriver parent, HtmlUnitKeyboard keyboard)
  {
    this.parent = parent;
    this.keyboard = keyboard;
  }
  
  private DomElement getElementForOperation(Coordinates potentialCoordinates) {
    if (potentialCoordinates != null) {
      return (DomElement)potentialCoordinates.getAuxiliary();
    }
    
    if (currentActiveElement == null) {
      throw new InvalidCoordinatesException("About to perform an interaction that relies on the active element, but there isn't one.");
    }
    

    return currentActiveElement;
  }
  
  public void click(Coordinates elementCoordinates)
  {
    DomElement element = getElementForOperation(elementCoordinates);
    parent.click(element, false);
  }
  



  void click(DomElement element, boolean directClick)
  {
    if (!element.isDisplayed()) {
      throw new ElementNotVisibleException("You may only interact with visible elements");
    }
    
    moveOutIfNeeded(element);
    try
    {
      element.mouseOver();
      element.mouseMove();
      
      element.click(keyboard.isShiftPressed(), 
        (keyboard.isCtrlPressed()) || ((directClick) && ((element instanceof HtmlOption))), 
        keyboard.isAltPressed());
      updateActiveElement(element);
    } catch (IOException e) {
      throw new WebDriverException(e);
    }
    catch (ScriptException e) {
      System.out.println(e.getMessage());
    }
    catch (RuntimeException e) {
      Throwable cause = e.getCause();
      if ((cause instanceof SocketTimeoutException)) {
        throw new TimeoutException(cause);
      }
      throw e;
    }
  }
  
  private void moveOutIfNeeded(DomElement element) {
    try {
      if (currentActiveElement != element) {
        if (currentActiveElement != null) {
          currentActiveElement.mouseOver(keyboard.isShiftPressed(), 
            keyboard.isCtrlPressed(), keyboard.isAltPressed(), 0);
          
          currentActiveElement.mouseOut(keyboard.isShiftPressed(), 
            keyboard.isCtrlPressed(), keyboard.isAltPressed(), 0);
          
          currentActiveElement.blur();
        }
        
        if (element != null) {
          element.mouseMove(keyboard.isShiftPressed(), 
            keyboard.isCtrlPressed(), keyboard.isAltPressed(), 
            0);
          element.mouseOver(keyboard.isShiftPressed(), 
            keyboard.isCtrlPressed(), keyboard.isAltPressed(), 
            0);
        }
      }
    } catch (ScriptException ignored) {
      System.out.println(ignored.getMessage());
    }
  }
  
  private void updateActiveElement(DomElement element) {
    if (element != null) {
      currentActiveElement = element;
    }
  }
  
  public void doubleClick(Coordinates elementCoordinates)
  {
    DomElement element = getElementForOperation(elementCoordinates);
    parent.doubleClick(element);
  }
  
  void doubleClick(DomElement element)
  {
    moveOutIfNeeded(element);
    
    try
    {
      element.dblClick(keyboard.isShiftPressed(), 
        keyboard.isCtrlPressed(), keyboard.isAltPressed());
      updateActiveElement(element);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public void contextClick(Coordinates elementCoordinates)
  {
    DomElement element = getElementForOperation(elementCoordinates);
    
    moveOutIfNeeded(element);
    
    element.rightClick(keyboard.isShiftPressed(), 
      keyboard.isCtrlPressed(), keyboard.isAltPressed());
    
    updateActiveElement(element);
  }
  
  public void mouseDown(Coordinates elementCoordinates)
  {
    DomElement element = getElementForOperation(elementCoordinates);
    parent.mouseDown(element);
  }
  
  void mouseDown(DomElement element) {
    moveOutIfNeeded(element);
    
    element.mouseDown(keyboard.isShiftPressed(), 
      keyboard.isCtrlPressed(), keyboard.isAltPressed(), 
      0);
    
    updateActiveElement(element);
  }
  
  public void mouseUp(Coordinates elementCoordinates)
  {
    DomElement element = getElementForOperation(elementCoordinates);
    parent.mouseUp(element);
  }
  
  void mouseUp(DomElement element) {
    moveOutIfNeeded(element);
    
    element.mouseUp(keyboard.isShiftPressed(), 
      keyboard.isCtrlPressed(), keyboard.isAltPressed(), 
      0);
    
    updateActiveElement(element);
  }
  
  public void mouseMove(Coordinates elementCoordinates)
  {
    DomElement element = (DomElement)elementCoordinates.getAuxiliary();
    parent.mouseMove(element);
  }
  
  void mouseMove(DomElement element) {
    moveOutIfNeeded(element);
    
    updateActiveElement(element);
  }
  
  public void mouseMove(Coordinates where, long xOffset, long yOffset)
  {
    throw new UnsupportedOperationException("Moving to arbitrary X,Y coordinates not supported.");
  }
}
