package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSS2Properties;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import org.apache.commons.lang3.StringUtils;





















class HtmlSerializer
{
  private final StringBuilder builder_ = new StringBuilder();
  
  protected static final String AS_TEXT_BLOCK_SEPARATOR = "§bs§";
  private static final int AS_TEXT_BLOCK_SEPARATOR_LENGTH = "§bs§".length();
  
  protected static final String AS_TEXT_NEW_LINE = "§nl§";
  
  private static final int AS_TEXT_NEW_LINE_LENGTH = "§nl§".length();
  

  protected static final String AS_TEXT_BLANK = "§blank§";
  
  protected static final String AS_TEXT_TAB = "§tab§";
  
  private static final Pattern TEXT_AREA_PATTERN = Pattern.compile("\r?\n");
  
  private boolean appletEnabled_;
  private boolean ignoreMaskedElements_ = true;
  

  HtmlSerializer() {}
  

  public String asText(DomNode node)
  {
    appletEnabled_ = node.getPage().getWebClient().getOptions().isAppletEnabled();
    builder_.setLength(0);
    appendNode(node);
    String response = builder_.toString();
    builder_.setLength(0);
    return cleanUp(response);
  }
  
  protected String cleanUp(String text)
  {
    text = reduceWhitespace(text);
    text = StringUtils.replace(text, "§blank§", " ");
    String ls = System.lineSeparator();
    text = StringUtils.replace(text, "§nl§", ls);
    
    text = StringUtils.replace(text, "§bs§", ls);
    text = StringUtils.replace(text, "§tab§", "\t");
    
    return text;
  }
  
  private static String reduceWhitespace(String text) {
    text = trim(text);
    

    text = reduceWhiteSpaceAroundBlockSeparator(text);
    

    while (text.startsWith("§bs§")) {
      text = text.substring(AS_TEXT_BLOCK_SEPARATOR_LENGTH);
    }
    

    while (text.endsWith("§bs§")) {
      text = text.substring(0, text.length() - AS_TEXT_BLOCK_SEPARATOR_LENGTH);
    }
    text = trim(text);
    
    StringBuilder builder = new StringBuilder(text.length());
    
    boolean whitespace = false;
    for (char ch : text.toCharArray())
    {

      if (ch == ' ') {
        builder.append(' ');
        whitespace = false;

      }
      else if (whitespace) {
        if (!isSpace(ch)) {
          builder.append(ch);
          whitespace = false;
        }
        
      }
      else if (isSpace(ch)) {
        whitespace = true;
        builder.append(' ');
      }
      else {
        builder.append(ch);
      }
    }
    

    return builder.toString();
  }
  
  private static boolean isSpace(char ch) {
    return (ch == ' ') || (ch == '\t') || (ch == '\n') || (ch == '\f') || (ch == '\r');
  }
  
  private static String trim(String string) {
    int length = string.length();
    
    int start = 0;
    while ((start != length) && (isSpace(string.charAt(start)))) {
      start++;
    }
    if (start != 0) {
      string = string.substring(start);
      length = string.length();
    }
    
    if (length != 0) {
      int end = length;
      while ((end != 0) && (isSpace(string.charAt(end - 1)))) {
        end--;
      }
      if (end != length) {
        string = string.substring(0, end);
      }
    }
    
    return string;
  }
  
  private static String reduceWhiteSpaceAroundBlockSeparator(String text) {
    int p0 = text.indexOf("§bs§");
    if (p0 == -1) {
      return text;
    }
    
    int length = text.length();
    if (length <= AS_TEXT_BLOCK_SEPARATOR_LENGTH) {
      return text;
    }
    
    StringBuilder result = new StringBuilder(length);
    int start = 0;
    for (; p0 != -1; 
        















        (p0 != -1) && (p0 == start))
    {
      int p1 = p0 + AS_TEXT_BLOCK_SEPARATOR_LENGTH;
      while ((p0 != start) && (isSpace(text.charAt(p0 - 1)))) {
        p0--;
      }
      if ((p0 >= AS_TEXT_NEW_LINE_LENGTH) && (text.startsWith("§nl§", p0 - AS_TEXT_NEW_LINE_LENGTH))) {
        p0 -= AS_TEXT_NEW_LINE_LENGTH;
      }
      result.append(text.substring(start, p0)).append("§bs§");
      
      while ((p1 < length) && (isSpace(text.charAt(p1)))) {
        p1++;
      }
      start = p1;
      

      p0 = text.indexOf("§bs§", start);
      continue;
      start += AS_TEXT_BLOCK_SEPARATOR_LENGTH;
      p0 = text.indexOf("§bs§", start);
    }
    
    if (start < length) {
      result.append(text.substring(start));
    }
    return result.toString();
  }
  
  protected void appendNode(DomNode node) {
    if ((node instanceof DomText)) {
      appendText((DomText)node);
    }
    else if (!(node instanceof DomComment))
    {

      if ((!(node instanceof HtmlApplet)) || (!appletEnabled_))
      {

        if ((node instanceof HtmlBreak)) {
          doAppendNewLine();
        }
        else if ((!(node instanceof HtmlHiddenInput)) && 
          (!(node instanceof HtmlScript)) && 
          (!(node instanceof HtmlStyle)) && 
          (!(node instanceof HtmlNoFrames)))
        {

          if ((node instanceof HtmlTextArea)) {
            appendHtmlTextArea((HtmlTextArea)node);
          }
          else if ((node instanceof HtmlTitle)) {
            appendHtmlTitle((HtmlTitle)node);
          }
          else if ((node instanceof HtmlTableRow)) {
            appendHtmlTableRow((HtmlTableRow)node);
          }
          else if ((node instanceof HtmlSelect)) {
            appendHtmlSelect((HtmlSelect)node);
          }
          else if ((node instanceof HtmlSubmitInput)) {
            doAppend(((HtmlSubmitInput)node).asText());
          }
          else if ((node instanceof HtmlResetInput)) {
            doAppend(((HtmlResetInput)node).asText());
          }
          else if ((node instanceof HtmlCheckBoxInput)) { String str;
            String str;
            if (((HtmlCheckBoxInput)node).isChecked()) {
              str = "checked";
            }
            else {
              str = "unchecked";
            }
            doAppend(str);
          }
          else if ((node instanceof HtmlRadioButtonInput)) { String str;
            String str;
            if (((HtmlRadioButtonInput)node).isChecked()) {
              str = "checked";
            }
            else {
              str = "unchecked";
            }
            doAppend(str);
          }
          else if ((node instanceof HtmlInput)) {
            doAppend(((HtmlInput)node).getValueAttribute());
          }
          else if ((node instanceof HtmlTable)) {
            appendHtmlTable((HtmlTable)node);
          }
          else if ((node instanceof HtmlOrderedList)) {
            appendHtmlOrderedList((HtmlOrderedList)node);
          }
          else if ((node instanceof HtmlUnorderedList)) {
            appendHtmlUnorderedList((HtmlUnorderedList)node);
          }
          else if ((node instanceof HtmlPreformattedText)) {
            appendHtmlPreformattedText((HtmlPreformattedText)node);
          }
          else if ((node instanceof HtmlInlineFrame)) {
            appendHtmlInlineFrame((HtmlInlineFrame)node);
          } else {
            if (((node instanceof HtmlNoScript)) && (node.getPage().getWebClient().getOptions().isJavaScriptEnabled())) {
              return;
            }
            

            ScriptableObject scriptableObject = node.getScriptableObject();
            boolean block; boolean block; if (((scriptableObject instanceof Element)) && (!(node instanceof HtmlBody))) {
              Element element = (Element)scriptableObject;
              String display = element.getWindow().getComputedStyle(element, null).getDisplay(true);
              block = "block".equals(display);
            }
            else {
              block = false;
            }
            
            if (block) {
              doAppendBlockSeparator();
            }
            appendChildren(node);
            if (block)
              doAppendBlockSeparator();
          } } }
    }
  }
  
  private void doAppendBlockSeparator() {
    builder_.append("§bs§");
  }
  
  private void doAppend(String str) {
    builder_.append(str);
  }
  
  private void doAppendNewLine() {
    builder_.append("§nl§");
  }
  
  private void doAppendTab() {
    builder_.append("§tab§");
  }
  
  private void appendHtmlUnorderedList(HtmlUnorderedList htmlUnorderedList) {
    doAppendBlockSeparator();
    boolean first = true;
    for (DomNode item : htmlUnorderedList.getChildren()) {
      if (!first) {
        doAppendBlockSeparator();
      }
      first = false;
      appendNode(item);
    }
    doAppendBlockSeparator();
  }
  



  private void appendHtmlTitle(HtmlTitle htmlTitle)
  {
    DomNode child = htmlTitle.getFirstChild();
    if ((child instanceof DomText)) {
      doAppend(((DomText)child).getData());
      doAppendBlockSeparator();
      return;
    }
  }
  
  private void appendChildren(DomNode node) {
    for (DomNode child : node.getChildren()) {
      appendNode(child);
    }
  }
  
  private void appendHtmlTableRow(HtmlTableRow htmlTableRow) {
    boolean first = true;
    for (HtmlTableCell cell : htmlTableRow.getCells()) {
      if (!first) {
        doAppendTab();
      }
      else {
        first = false;
      }
      appendChildren(cell);
    }
  }
  
  private void appendHtmlTextArea(HtmlTextArea htmlTextArea) {
    if (isVisible(htmlTextArea)) {
      String text = htmlTextArea.getText();
      text = StringUtils.stripEnd(text, null);
      text = TEXT_AREA_PATTERN.matcher(text).replaceAll("§nl§");
      text = StringUtils.replace(text, "\r", "§nl§");
      text = StringUtils.replace(text, " ", "§blank§");
      doAppend(text);
    }
  }
  
  private void appendHtmlTable(HtmlTable htmlTable) {
    doAppendBlockSeparator();
    String caption = htmlTable.getCaptionText();
    if (caption != null) {
      doAppend(caption);
      doAppendBlockSeparator();
    }
    
    boolean first = true;
    

    HtmlTableHeader tableHeader = htmlTable.getHeader();
    if (tableHeader != null) {
      first = appendHtmlTableRows(tableHeader.getRows(), true, null, null);
    }
    HtmlTableFooter tableFooter = htmlTable.getFooter();
    
    List<HtmlTableRow> tableRows = htmlTable.getRows();
    first = appendHtmlTableRows(tableRows, first, tableHeader, tableFooter);
    
    if (tableFooter != null) {
      first = appendHtmlTableRows(tableFooter.getRows(), first, null, null);
    }
    else if (tableRows.isEmpty()) {
      DomNode firstChild = htmlTable.getFirstChild();
      if (firstChild != null) {
        appendNode(firstChild);
      }
    }
    
    doAppendBlockSeparator();
  }
  
  private boolean appendHtmlTableRows(List<HtmlTableRow> rows, boolean first, TableRowGroup skipParent1, TableRowGroup skipParent2)
  {
    for (HtmlTableRow row : rows)
      if ((row.getParentNode() != skipParent1) && (row.getParentNode() != skipParent2))
      {

        if (!first) {
          doAppendBlockSeparator();
        }
        first = false;
        appendHtmlTableRow(row);
      }
    return first;
  }
  

  private void appendHtmlSelect(HtmlSelect htmlSelect)
  {
    List<HtmlOption> options;
    List<HtmlOption> options;
    if (htmlSelect.isMultipleSelectEnabled()) {
      options = htmlSelect.getOptions();
    }
    else {
      options = htmlSelect.getSelectedOptions();
    }
    
    for (Iterator<HtmlOption> i = options.iterator(); i.hasNext();) {
      HtmlOption currentOption = (HtmlOption)i.next();
      appendNode(currentOption);
      if (i.hasNext()) {
        doAppendBlockSeparator();
      }
    }
  }
  



  private void appendHtmlOrderedList(HtmlOrderedList htmlOrderedList)
  {
    doAppendBlockSeparator();
    boolean first = true;
    int i = 1;
    for (DomNode item : htmlOrderedList.getChildren()) {
      if (!first) {
        doAppendBlockSeparator();
      }
      first = false;
      if ((item instanceof HtmlListItem)) {
        doAppend(Integer.toString(i++));
        doAppend(". ");
        appendChildren(item);
      }
      else {
        appendNode(item);
      }
    }
    doAppendBlockSeparator();
  }
  
  private void appendHtmlPreformattedText(HtmlPreformattedText htmlPreformattedText) {
    if (isVisible(htmlPreformattedText)) {
      doAppendBlockSeparator();
      String text = htmlPreformattedText.getTextContent();
      text = StringUtils.replace(text, "\t", "§tab§");
      text = StringUtils.replace(text, " ", "§blank§");
      text = TEXT_AREA_PATTERN.matcher(text).replaceAll("§nl§");
      text = StringUtils.replace(text, "\r", "§nl§");
      doAppend(text);
      doAppendBlockSeparator();
    }
  }
  
  private void appendHtmlInlineFrame(HtmlInlineFrame htmlInlineFrame) {
    if (isVisible(htmlInlineFrame)) {
      doAppendBlockSeparator();
      Page page = htmlInlineFrame.getEnclosedPage();
      if ((page instanceof SgmlPage)) {
        doAppend(((SgmlPage)page).asText());
      }
      doAppendBlockSeparator();
    }
  }
  
  private void appendText(DomText domText) {
    DomNode parent = domText.getParentNode();
    if ((parent == null) || ((parent instanceof HtmlTitle)) || (isVisible(parent))) {
      append(domText.getData());
    }
  }
  
  private boolean isVisible(DomNode node) {
    return (!ignoreMaskedElements_) || (node.isDisplayed());
  }
  




  public void setIgnoreMaskedElements(boolean ignore)
  {
    ignoreMaskedElements_ = ignore;
  }
  
  private void append(String text) {
    doAppend(text);
  }
}
