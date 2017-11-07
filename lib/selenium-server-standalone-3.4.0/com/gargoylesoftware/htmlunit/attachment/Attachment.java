package com.gargoylesoftware.htmlunit.attachment;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebResponse;



























public class Attachment
{
  private final Page page_;
  
  public Attachment(Page page)
  {
    page_ = page;
  }
  



  public Page getPage()
  {
    return page_;
  }
  




  public String getSuggestedFilename()
  {
    WebResponse response = page_.getWebResponse();
    String disp = response.getResponseHeaderValue("Content-Disposition");
    int start = disp.indexOf("filename=");
    if (start == -1) {
      return null;
    }
    start += "filename=".length();
    if (start >= disp.length()) {
      return null;
    }
    
    int end = disp.indexOf(';', start);
    if (end == -1) {
      end = disp.length();
    }
    if ((disp.charAt(start) == '"') && (disp.charAt(end - 1) == '"')) {
      start++;
      end--;
    }
    return disp.substring(start, end);
  }
  





  public static boolean isAttachment(WebResponse response)
  {
    String disp = response.getResponseHeaderValue("Content-Disposition");
    if (disp == null) {
      return false;
    }
    return disp.startsWith("attachment");
  }
}
