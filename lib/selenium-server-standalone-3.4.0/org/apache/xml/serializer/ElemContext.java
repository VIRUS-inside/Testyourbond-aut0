package org.apache.xml.serializer;






























final class ElemContext
{
  final int m_currentElemDepth;
  



























  ElemDesc m_elementDesc = null;
  



  String m_elementLocalName = null;
  



  String m_elementName = null;
  






  String m_elementURI = null;
  




  boolean m_isCdataSection;
  



  boolean m_isRaw = false;
  






  private ElemContext m_next;
  






  final ElemContext m_prev;
  





  boolean m_startTagOpen = false;
  





  ElemContext()
  {
    m_prev = this;
    
    m_currentElemDepth = 0;
  }
  










  private ElemContext(ElemContext previous)
  {
    m_prev = previous;
    m_currentElemDepth += 1;
  }
  








  final ElemContext pop()
  {
    return m_prev;
  }
  







  final ElemContext push()
  {
    ElemContext frame = m_next;
    if (frame == null)
    {



      frame = new ElemContext(this);
      m_next = frame;
    }
    





    m_startTagOpen = true;
    return frame;
  }
  













  final ElemContext push(String uri, String localName, String qName)
  {
    ElemContext frame = m_next;
    if (frame == null)
    {



      frame = new ElemContext(this);
      m_next = frame;
    }
    

    m_elementName = qName;
    m_elementLocalName = localName;
    m_elementURI = uri;
    m_isCdataSection = false;
    m_startTagOpen = true;
    


    return frame;
  }
}
