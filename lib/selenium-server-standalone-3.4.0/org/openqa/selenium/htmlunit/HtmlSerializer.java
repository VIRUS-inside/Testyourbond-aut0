package org.openqa.selenium.htmlunit;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlBreak;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlLabel;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;


















class HtmlSerializer
{
  HtmlSerializer() {}
  
  static String getText(DomElement element)
  {
    if ((element instanceof HtmlInput)) {
      return "";
    }
    if ((element instanceof HtmlLabel)) {
      return getDirectChildren(element);
    }
    String text = element.asText();
    if (((element instanceof HtmlTextArea)) && (element.isDisplayed())) {
      text = ((HtmlTextArea)element).getDefaultValue();
      if (text.endsWith("\n")) {
        text = text.substring(0, text.length() - 1);
      }
    }
    text = text.replace('\t', ' ');
    text = text.replace("\r", "");
    if (!(element instanceof HtmlElement)) {
      text = text.replaceAll("\\p{javaWhitespace}+", " ").trim();
    }
    return text;
  }
  
  private static String getDirectChildren(DomElement element) {
    StringBuilder builder = new StringBuilder();
    for (DomNode e : element.getChildNodes()) {
      if ((e instanceof DomText)) {
        builder.append(e.asText());
      }
      else if ((e instanceof HtmlBreak)) {
        builder.append('\n');
      }
    }
    return builder.toString();
  }
}
