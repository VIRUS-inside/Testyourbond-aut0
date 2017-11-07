package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.html.impl.SelectionDelegate;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;







































class DoTypeProcessor
  implements Serializable, ClipboardOwner
{
  private static Map<Integer, Character> SPECIAL_KEYS_MAP_ = new HashMap();
  

  private DomNode domNode_;
  

  static
  {
    SPECIAL_KEYS_MAP_.put(Integer.valueOf(107), Character.valueOf('+'));
    SPECIAL_KEYS_MAP_.put(Integer.valueOf(110), Character.valueOf('.'));
    SPECIAL_KEYS_MAP_.put(Integer.valueOf(111), Character.valueOf('/'));
    SPECIAL_KEYS_MAP_.put(Integer.valueOf(61), Character.valueOf('='));
    SPECIAL_KEYS_MAP_.put(Integer.valueOf(106), Character.valueOf('*'));
    SPECIAL_KEYS_MAP_.put(Integer.valueOf(59), Character.valueOf(';'));
    SPECIAL_KEYS_MAP_.put(Integer.valueOf(108), Character.valueOf(','));
    SPECIAL_KEYS_MAP_.put(Integer.valueOf(32), Character.valueOf(' '));
    SPECIAL_KEYS_MAP_.put(Integer.valueOf(109), Character.valueOf('-'));
    
    for (int i = 96; i <= 105; i++) {
      SPECIAL_KEYS_MAP_.put(Integer.valueOf(i), Character.valueOf((char)(48 + (i - 96))));
    }
  }
  
  DoTypeProcessor(DomNode domNode) {
    domNode_ = domNode;
  }
  

  void doType(String currentValue, SelectionDelegate selectionDelegate, char c, HtmlElement element, boolean lastType)
  {
    int selectionStart = selectionDelegate.getSelectionStart();
    int selectionEnd = selectionDelegate.getSelectionEnd();
    
    StringBuilder newValue = new StringBuilder(currentValue);
    if (c == '\b') {
      if (selectionStart > 0) {
        newValue.deleteCharAt(selectionStart - 1);
        selectionStart--;
        selectionEnd--;
      }
    }
    else if (acceptChar(c)) {
      boolean ctrlKey = element.isCtrlPressed();
      if ((ctrlKey) && ((c == 'C') || (c == 'c'))) {
        String content = newValue.substring(selectionStart, selectionEnd);
        setClipboardContent(content);
      }
      else if ((ctrlKey) && ((c == 'V') || (c == 'v'))) {
        String content = getClipboardContent();
        add(newValue, content, selectionStart, selectionEnd);
        selectionStart += content.length();
        selectionEnd = selectionStart;
      }
      else if ((ctrlKey) && ((c == 'X') || (c == 'x'))) {
        String content = newValue.substring(selectionStart, selectionEnd);
        setClipboardContent(content);
        newValue.delete(selectionStart, selectionEnd);
        selectionEnd = selectionStart;
      }
      else if ((ctrlKey) && ((c == 'A') || (c == 'a'))) {
        selectionStart = 0;
        selectionEnd = newValue.length();
      }
      else {
        add(newValue, c, selectionStart, selectionEnd);
        selectionStart++;
        selectionEnd = selectionStart;
      }
    }
    
    typeDone(newValue.toString(), lastType);
    
    selectionDelegate.setSelectionStart(selectionStart);
    selectionDelegate.setSelectionEnd(selectionEnd);
  }
  
  private static void add(StringBuilder newValue, char c, int selectionStart, int selectionEnd)
  {
    if (selectionStart != newValue.length()) {
      newValue.replace(selectionStart, selectionEnd, Character.toString(c));
    }
    else {
      newValue.append(c);
    }
  }
  
  private static void add(StringBuilder newValue, String string, int selectionStart, int selectionEnd)
  {
    if (selectionStart != newValue.length()) {
      newValue.replace(selectionStart, selectionEnd, string);
    }
    else {
      newValue.append(string);
    }
  }
  
  private static String getClipboardContent() {
    String result = "";
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    Transferable contents = clipboard.getContents(null);
    if ((contents != null) && (contents.isDataFlavorSupported(DataFlavor.stringFlavor))) {
      try {
        result = (String)contents.getTransferData(DataFlavor.stringFlavor);
      }
      catch (UnsupportedFlavorException|IOException localUnsupportedFlavorException) {}
    }
    
    return result;
  }
  
  private void setClipboardContent(String string) {
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    StringSelection stringSelection = new StringSelection(string);
    clipboard.setContents(stringSelection, this);
  }
  
  private void typeDone(String newValue, boolean notifyAttributeChangeListeners) {
    if ((domNode_ instanceof DomText)) {
      ((DomText)domNode_).setData(newValue);
    }
    else {
      ((HtmlElement)domNode_).typeDone(newValue, notifyAttributeChangeListeners);
    }
  }
  
  private boolean acceptChar(char ch) {
    if ((domNode_ instanceof DomText)) {
      return ((DomText)domNode_).acceptChar(ch);
    }
    return ((HtmlElement)domNode_).acceptChar(ch);
  }
  

  void doType(String currentValue, SelectionDelegate selectionDelegate, int keyCode, HtmlElement element, boolean lastType)
  {
    StringBuilder newValue = new StringBuilder(currentValue);
    int selectionStart = selectionDelegate.getSelectionStart();
    int selectionEnd = selectionDelegate.getSelectionEnd();
    
    Character ch = (Character)SPECIAL_KEYS_MAP_.get(Integer.valueOf(keyCode));
    if (ch != null) {
      doType(currentValue, selectionDelegate, ch.charValue(), element, lastType);
      return;
    }
    switch (keyCode) {
    case 8: 
      if (selectionStart > 0) {
        newValue.deleteCharAt(selectionStart - 1);
        selectionStart--;
      }
      break;
    
    case 37: 
      if (element.isCtrlPressed()) {
        do {
          selectionStart--;
          if (selectionStart <= 0) break; } while (newValue.charAt(selectionStart - 1) != ' ');


      }
      else if (selectionStart > 0) {
        selectionStart--;
      }
      break;
    
    case 39: 
      if (element.isCtrlPressed()) {
        if (selectionStart < newValue.length()) {
          selectionStart++;
        }
        do {
          selectionStart++;
          if (selectionStart >= newValue.length()) break; } while (newValue.charAt(selectionStart - 1) != ' ');


      }
      else if (element.isShiftPressed()) {
        selectionEnd++;
      }
      else if (selectionStart > 0) {
        selectionStart++;
      }
      break;
    
    case 36: 
      selectionStart = 0;
      break;
    
    case 35: 
      if (element.isShiftPressed()) {
        selectionEnd = newValue.length();
      }
      else {
        selectionStart = newValue.length();
      }
      break;
    
    case 46: 
      if (selectionEnd == selectionStart) {
        selectionEnd++;
      }
      newValue.delete(selectionStart, selectionEnd);
      selectionEnd = selectionStart;
      break;
    
    default: 
      return;
    }
    
    if (!element.isShiftPressed()) {
      selectionEnd = selectionStart;
    }
    
    typeDone(newValue.toString(), lastType);
    
    selectionDelegate.setSelectionStart(selectionStart);
    selectionDelegate.setSelectionEnd(selectionEnd);
  }
  
  public void lostOwnership(Clipboard clipboard, Transferable contents) {}
}
