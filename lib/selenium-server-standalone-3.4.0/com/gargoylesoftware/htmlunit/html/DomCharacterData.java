package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import org.w3c.dom.CharacterData;






























public abstract class DomCharacterData
  extends DomNode
  implements CharacterData
{
  private String data_;
  
  public DomCharacterData(SgmlPage page, String data)
  {
    super(page);
    setData(data);
  }
  




  public String getData()
  {
    return data_;
  }
  




  public void setData(String data)
  {
    String oldData = data_;
    data_ = data;
    fireCharacterDataChanged(this, oldData);
  }
  




  public void setNodeValue(String newValue)
  {
    setData(newValue);
  }
  



  public void setTextContent(String textContent)
  {
    setData(textContent);
  }
  




  public int getLength()
  {
    return data_.length();
  }
  




  public void appendData(String newData)
  {
    data_ += newData;
  }
  







  public void deleteData(int offset, int count)
  {
    if (offset < 0) {
      throw new IllegalArgumentException("Provided offset: " + offset + " is less than zero.");
    }
    
    String data = data_.substring(0, offset);
    if (count >= 0) {
      int fromLeft = offset + count;
      if (fromLeft < data_.length()) {
        setData(data + data_.substring(fromLeft, data_.length()));
        return;
      }
    }
    setData(data);
  }
  





  public void insertData(int offset, String arg)
  {
    setData(new StringBuilder(data_).insert(offset, arg).toString());
  }
  






  public void replaceData(int offset, int count, String arg)
  {
    deleteData(offset, count);
    insertData(offset, arg);
  }
  







  public String substringData(int offset, int count)
  {
    int length = data_.length();
    if ((count < 0) || (offset < 0) || (offset > length - 1)) {
      throw new IllegalArgumentException("offset: " + offset + " count: " + count);
    }
    
    int tailIndex = Math.min(offset + count, length);
    return data_.substring(offset, tailIndex);
  }
  




  public String getNodeValue()
  {
    return data_;
  }
  



  public String getCanonicalXPath()
  {
    return getParentNode().getCanonicalXPath() + '/' + getXPathToken();
  }
  


  private String getXPathToken()
  {
    DomNode parent = getParentNode();
    


    int siblingsOfSameType = 0;
    int nodeIndex = 0;
    for (DomNode child : parent.getChildren()) {
      if (child == this) {
        siblingsOfSameType++;nodeIndex = siblingsOfSameType;
        if (nodeIndex > 1) {
          break;
        }
        

      }
      else if (child.getNodeType() == getNodeType()) {
        siblingsOfSameType++;
        if (nodeIndex > 0) {
          break;
        }
      }
    }
    


    String nodeName = getNodeName().substring(1) + "()";
    if (siblingsOfSameType == 1) {
      return nodeName;
    }
    return nodeName + '[' + nodeIndex + ']';
  }
}
