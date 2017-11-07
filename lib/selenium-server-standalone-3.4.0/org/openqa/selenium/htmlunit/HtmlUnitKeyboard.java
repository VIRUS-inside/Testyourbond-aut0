package org.openqa.selenium.htmlunit;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlFileInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import java.io.IOException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebDriverException;





















public class HtmlUnitKeyboard
  implements org.openqa.selenium.interactions.Keyboard
{
  private KeyboardModifiersState modifiersState = new KeyboardModifiersState();
  private final HtmlUnitDriver parent;
  private HtmlElement lastElement;
  
  HtmlUnitKeyboard(HtmlUnitDriver parent) {
    this.parent = parent;
  }
  
  public void sendKeys(CharSequence... keysToSend)
  {
    HtmlUnitWebElement htmlElem = (HtmlUnitWebElement)parent.switchTo().activeElement();
    sendKeys(htmlElem, false, keysToSend);
  }
  
  void sendKeys(HtmlUnitWebElement htmlElem, boolean releaseAllAtEnd, CharSequence... value) {
    htmlElem.verifyCanInteractWithElement(false);
    
    HtmlElement element = (HtmlElement)element;
    boolean inputElementInsideForm = ((element instanceof HtmlInput)) && 
      (((HtmlInput)element).getEnclosingForm() != null);
    InputKeysContainer keysContainer = new InputKeysContainer(inputElementInsideForm, value);
    
    htmlElem.switchFocusToThisIfNeeded();
    
    sendKeys(element, keysContainer, releaseAllAtEnd);
    
    if ((inputElementInsideForm) && (keysContainer.wasSubmitKeyFound())) {
      htmlElem.submitImpl();
    }
  }
  
  private void sendKeys(HtmlElement element, InputKeysContainer keysToSend, boolean releaseAllAtEnd) {
    keysToSend.setCapitalization(modifiersState.isShiftPressed());
    String keysSequence = keysToSend.toString();
    

    if ((element instanceof HtmlFileInput)) {
      HtmlFileInput fileInput = (HtmlFileInput)element;
      fileInput.setValueAttribute(keysSequence);
      return;
    }
    try
    {
      com.gargoylesoftware.htmlunit.html.Keyboard keyboard = asHtmlUnitKeyboard(lastElement != element, keysSequence, true);
      if (releaseAllAtEnd) {
        if (isShiftPressed()) {
          addToKeyboard(keyboard, Keys.SHIFT.charAt(0), false);
        }
        if (isAltPressed()) {
          addToKeyboard(keyboard, Keys.ALT.charAt(0), false);
        }
        if (isCtrlPressed()) {
          addToKeyboard(keyboard, Keys.CONTROL.charAt(0), false);
        }
      }
      element.type(keyboard);
    } catch (IOException e) {
      throw new WebDriverException(e);
    }
    lastElement = element;
  }
  
  private com.gargoylesoftware.htmlunit.html.Keyboard asHtmlUnitKeyboard(boolean startAtEnd, CharSequence keysSequence, boolean isPress) {
    com.gargoylesoftware.htmlunit.html.Keyboard keyboard = new com.gargoylesoftware.htmlunit.html.Keyboard(startAtEnd);
    for (int i = 0; i < keysSequence.length(); i++) {
      char ch = keysSequence.charAt(i);
      addToKeyboard(keyboard, ch, isPress);
    }
    return keyboard;
  }
  
  private void addToKeyboard(com.gargoylesoftware.htmlunit.html.Keyboard keyboard, char ch, boolean isPress) {
    if (HtmlUnitKeyboardMapping.isSpecialKey(ch)) {
      int keyCode = HtmlUnitKeyboardMapping.getKeysMapping(ch);
      if (isPress) {
        keyboard.press(keyCode);
        modifiersState.storeKeyDown(ch);
      }
      else {
        keyboard.release(keyCode);
        modifiersState.storeKeyUp(ch);
      }
    }
    else {
      keyboard.type(ch);
    }
  }
  
  public void pressKey(CharSequence keyToPress)
  {
    HtmlUnitWebElement htmlElement = (HtmlUnitWebElement)parent.switchTo().activeElement();
    HtmlElement element = (HtmlElement)element;
    try {
      element.type(asHtmlUnitKeyboard(lastElement != element, keyToPress, true));
    } catch (IOException e) {
      throw new WebDriverException(e);
    }
    for (int i = 0; i < keyToPress.length(); i++) {
      char ch = keyToPress.charAt(i);
      modifiersState.storeKeyDown(ch);
    }
  }
  
  public void releaseKey(CharSequence keyToRelease)
  {
    HtmlUnitWebElement htmlElement = (HtmlUnitWebElement)parent.switchTo().activeElement();
    HtmlElement element = (HtmlElement)element;
    try {
      element.type(asHtmlUnitKeyboard(lastElement != element, keyToRelease, false));
    } catch (IOException e) {
      throw new WebDriverException(e);
    }
    for (int i = 0; i < keyToRelease.length(); i++) {
      char ch = keyToRelease.charAt(i);
      modifiersState.storeKeyUp(ch);
    }
  }
  
  public boolean isShiftPressed() {
    return modifiersState.isShiftPressed();
  }
  
  public boolean isCtrlPressed() {
    return modifiersState.isCtrlPressed();
  }
  
  public boolean isAltPressed() {
    return modifiersState.isAltPressed();
  }
  
  public boolean isPressed(char ch) {
    return modifiersState.isPressed(ch);
  }
  
  public boolean isPressed(Keys keys) {
    return modifiersState.isPressed(keys);
  }
}
