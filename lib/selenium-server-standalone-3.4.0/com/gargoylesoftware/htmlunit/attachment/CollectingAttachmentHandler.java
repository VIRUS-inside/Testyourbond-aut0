package com.gargoylesoftware.htmlunit.attachment;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebAssert;
import java.util.ArrayList;
import java.util.List;

























public class CollectingAttachmentHandler
  implements AttachmentHandler
{
  private final List<Attachment> collectedAttachments_;
  
  public CollectingAttachmentHandler()
  {
    this(new ArrayList());
  }
  



  public CollectingAttachmentHandler(List<Attachment> list)
  {
    WebAssert.notNull("list", list);
    collectedAttachments_ = list;
  }
  



  public void handleAttachment(Page page)
  {
    collectedAttachments_.add(new Attachment(page));
  }
  




  public List<Attachment> getCollectedAttachments()
  {
    return collectedAttachments_;
  }
}
