package org.apache.xml.utils;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;























































































public class FastStringBuffer
{
  static final int DEBUG_FORCE_INIT_BITS = 0;
  static final boolean DEBUG_FORCE_FIXED_CHUNKSIZE = true;
  public static final int SUPPRESS_LEADING_WS = 1;
  public static final int SUPPRESS_TRAILING_WS = 2;
  public static final int SUPPRESS_BOTH = 3;
  private static final int CARRY_WS = 4;
  int m_chunkBits = 15;
  





  int m_maxChunkBits = 15;
  








  int m_rebundleBits = 2;
  







  int m_chunkSize;
  







  int m_chunkMask;
  







  char[][] m_array;
  






  int m_lastChunk = 0;
  







  int m_firstFree = 0;
  







  FastStringBuffer m_innerFSB = null;
  
































  public FastStringBuffer(int initChunkBits, int maxChunkBits, int rebundleBits)
  {
    maxChunkBits = initChunkBits;
    

    m_array = new char[16][];
    

    if (initChunkBits > maxChunkBits) {
      initChunkBits = maxChunkBits;
    }
    m_chunkBits = initChunkBits;
    m_maxChunkBits = maxChunkBits;
    m_rebundleBits = rebundleBits;
    m_chunkSize = (1 << initChunkBits);
    m_chunkMask = (m_chunkSize - 1);
    m_array[0] = new char[m_chunkSize];
  }
  






  public FastStringBuffer(int initChunkBits, int maxChunkBits)
  {
    this(initChunkBits, maxChunkBits, 2);
  }
  









  public FastStringBuffer(int initChunkBits)
  {
    this(initChunkBits, 15, 2);
  }
  









  public FastStringBuffer()
  {
    this(10, 15, 2);
  }
  





  public final int size()
  {
    return (m_lastChunk << m_chunkBits) + m_firstFree;
  }
  





  public final int length()
  {
    return (m_lastChunk << m_chunkBits) + m_firstFree;
  }
  






  public final void reset()
  {
    m_lastChunk = 0;
    m_firstFree = 0;
    

    FastStringBuffer innermost = this;
    
    while (m_innerFSB != null)
    {
      innermost = m_innerFSB;
    }
    
    m_chunkBits = m_chunkBits;
    m_chunkSize = m_chunkSize;
    m_chunkMask = m_chunkMask;
    

    m_innerFSB = null;
    m_array = new char[16][0];
    m_array[0] = new char[m_chunkSize];
  }
  












  public final void setLength(int l)
  {
    m_lastChunk = (l >>> m_chunkBits);
    
    if ((m_lastChunk == 0) && (m_innerFSB != null))
    {

      m_innerFSB.setLength(l, this);
    }
    else
    {
      m_firstFree = (l & m_chunkMask);
      





      if ((m_firstFree == 0) && (m_lastChunk > 0))
      {
        m_lastChunk -= 1;
        m_firstFree = m_chunkSize;
      }
    }
  }
  








  private final void setLength(int l, FastStringBuffer rootFSB)
  {
    m_lastChunk = (l >>> m_chunkBits);
    
    if ((m_lastChunk == 0) && (m_innerFSB != null))
    {
      m_innerFSB.setLength(l, rootFSB);


    }
    else
    {

      m_chunkBits = m_chunkBits;
      m_maxChunkBits = m_maxChunkBits;
      m_rebundleBits = m_rebundleBits;
      m_chunkSize = m_chunkSize;
      m_chunkMask = m_chunkMask;
      m_array = m_array;
      m_innerFSB = m_innerFSB;
      m_lastChunk = m_lastChunk;
      

      m_firstFree = (l & m_chunkMask);
    }
  }
  














  public final String toString()
  {
    int length = (m_lastChunk << m_chunkBits) + m_firstFree;
    
    return getString(new StringBuffer(length), 0, 0, length).toString();
  }
  





  public final void append(char value)
  {
    char[] chunk;
    




    char[] chunk;
    



    if (m_firstFree < m_chunkSize) {
      chunk = m_array[m_lastChunk];

    }
    else
    {
      int i = m_array.length;
      
      if (m_lastChunk + 1 == i)
      {
        char[][] newarray = new char[i + 16][];
        
        System.arraycopy(m_array, 0, newarray, 0, i);
        
        m_array = newarray;
      }
      

      chunk = m_array[(++m_lastChunk)];
      
      if (chunk == null)
      {


        if ((m_lastChunk == 1 << m_rebundleBits) && (m_chunkBits < m_maxChunkBits))
        {




          m_innerFSB = new FastStringBuffer(this);
        }
        

        chunk = m_array[m_lastChunk] =  = new char[m_chunkSize];
      }
      
      m_firstFree = 0;
    }
    

    chunk[(m_firstFree++)] = value;
  }
  










  public final void append(String value)
  {
    if (value == null)
      return;
    int strlen = value.length();
    
    if (0 == strlen) {
      return;
    }
    int copyfrom = 0;
    char[] chunk = m_array[m_lastChunk];
    int available = m_chunkSize - m_firstFree;
    

    while (strlen > 0)
    {


      if (available > strlen) {
        available = strlen;
      }
      value.getChars(copyfrom, copyfrom + available, m_array[m_lastChunk], m_firstFree);
      

      strlen -= available;
      copyfrom += available;
      

      if (strlen > 0)
      {


        int i = m_array.length;
        
        if (m_lastChunk + 1 == i)
        {
          char[][] newarray = new char[i + 16][];
          
          System.arraycopy(m_array, 0, newarray, 0, i);
          
          m_array = newarray;
        }
        

        chunk = m_array[(++m_lastChunk)];
        
        if (chunk == null)
        {


          if ((m_lastChunk == 1 << m_rebundleBits) && (m_chunkBits < m_maxChunkBits))
          {




            m_innerFSB = new FastStringBuffer(this);
          }
          

          chunk = m_array[m_lastChunk] =  = new char[m_chunkSize];
        }
        
        available = m_chunkSize;
        m_firstFree = 0;
      }
    }
    

    m_firstFree += available;
  }
  










  public final void append(StringBuffer value)
  {
    if (value == null)
      return;
    int strlen = value.length();
    
    if (0 == strlen) {
      return;
    }
    int copyfrom = 0;
    char[] chunk = m_array[m_lastChunk];
    int available = m_chunkSize - m_firstFree;
    

    while (strlen > 0)
    {


      if (available > strlen) {
        available = strlen;
      }
      value.getChars(copyfrom, copyfrom + available, m_array[m_lastChunk], m_firstFree);
      

      strlen -= available;
      copyfrom += available;
      

      if (strlen > 0)
      {


        int i = m_array.length;
        
        if (m_lastChunk + 1 == i)
        {
          char[][] newarray = new char[i + 16][];
          
          System.arraycopy(m_array, 0, newarray, 0, i);
          
          m_array = newarray;
        }
        

        chunk = m_array[(++m_lastChunk)];
        
        if (chunk == null)
        {


          if ((m_lastChunk == 1 << m_rebundleBits) && (m_chunkBits < m_maxChunkBits))
          {




            m_innerFSB = new FastStringBuffer(this);
          }
          

          chunk = m_array[m_lastChunk] =  = new char[m_chunkSize];
        }
        
        available = m_chunkSize;
        m_firstFree = 0;
      }
    }
    

    m_firstFree += available;
  }
  













  public final void append(char[] chars, int start, int length)
  {
    int strlen = length;
    
    if (0 == strlen) {
      return;
    }
    int copyfrom = start;
    char[] chunk = m_array[m_lastChunk];
    int available = m_chunkSize - m_firstFree;
    

    while (strlen > 0)
    {


      if (available > strlen) {
        available = strlen;
      }
      System.arraycopy(chars, copyfrom, m_array[m_lastChunk], m_firstFree, available);
      

      strlen -= available;
      copyfrom += available;
      

      if (strlen > 0)
      {


        int i = m_array.length;
        
        if (m_lastChunk + 1 == i)
        {
          char[][] newarray = new char[i + 16][];
          
          System.arraycopy(m_array, 0, newarray, 0, i);
          
          m_array = newarray;
        }
        

        chunk = m_array[(++m_lastChunk)];
        
        if (chunk == null)
        {


          if ((m_lastChunk == 1 << m_rebundleBits) && (m_chunkBits < m_maxChunkBits))
          {




            m_innerFSB = new FastStringBuffer(this);
          }
          

          chunk = m_array[m_lastChunk] =  = new char[m_chunkSize];
        }
        
        available = m_chunkSize;
        m_firstFree = 0;
      }
    }
    

    m_firstFree += available;
  }
  















  public final void append(FastStringBuffer value)
  {
    if (value == null)
      return;
    int strlen = value.length();
    
    if (0 == strlen) {
      return;
    }
    int copyfrom = 0;
    char[] chunk = m_array[m_lastChunk];
    int available = m_chunkSize - m_firstFree;
    

    while (strlen > 0)
    {


      if (available > strlen) {
        available = strlen;
      }
      int sourcechunk = copyfrom + m_chunkSize - 1 >>> m_chunkBits;
      
      int sourcecolumn = copyfrom & m_chunkMask;
      int runlength = m_chunkSize - sourcecolumn;
      
      if (runlength > available) {
        runlength = available;
      }
      System.arraycopy(m_array[sourcechunk], sourcecolumn, m_array[m_lastChunk], m_firstFree, runlength);
      

      if (runlength != available) {
        System.arraycopy(m_array[(sourcechunk + 1)], 0, m_array[m_lastChunk], m_firstFree + runlength, available - runlength);
      }
      

      strlen -= available;
      copyfrom += available;
      

      if (strlen > 0)
      {


        int i = m_array.length;
        
        if (m_lastChunk + 1 == i)
        {
          char[][] newarray = new char[i + 16][];
          
          System.arraycopy(m_array, 0, newarray, 0, i);
          
          m_array = newarray;
        }
        

        chunk = m_array[(++m_lastChunk)];
        
        if (chunk == null)
        {


          if ((m_lastChunk == 1 << m_rebundleBits) && (m_chunkBits < m_maxChunkBits))
          {




            m_innerFSB = new FastStringBuffer(this);
          }
          

          chunk = m_array[m_lastChunk] =  = new char[m_chunkSize];
        }
        
        available = m_chunkSize;
        m_firstFree = 0;
      }
    }
    

    m_firstFree += available;
  }
  










  public boolean isWhitespace(int start, int length)
  {
    int sourcechunk = start >>> m_chunkBits;
    int sourcecolumn = start & m_chunkMask;
    int available = m_chunkSize - sourcecolumn;
    

    while (length > 0)
    {
      int runlength = length <= available ? length : available;
      boolean chunkOK;
      boolean chunkOK; if ((sourcechunk == 0) && (m_innerFSB != null)) {
        chunkOK = m_innerFSB.isWhitespace(sourcecolumn, runlength);
      } else {
        chunkOK = XMLCharacterRecognizer.isWhiteSpace(m_array[sourcechunk], sourcecolumn, runlength);
      }
      
      if (!chunkOK) {
        return false;
      }
      length -= runlength;
      
      sourcechunk++;
      
      sourcecolumn = 0;
      available = m_chunkSize;
    }
    
    return true;
  }
  






  public String getString(int start, int length)
  {
    int startColumn = start & m_chunkMask;
    int startChunk = start >>> m_chunkBits;
    if ((startColumn + length < m_chunkMask) && (m_innerFSB == null)) {
      return getOneChunkString(startChunk, startColumn, length);
    }
    return getString(new StringBuffer(length), startChunk, startColumn, length).toString();
  }
  

  protected String getOneChunkString(int startChunk, int startColumn, int length)
  {
    return new String(m_array[startChunk], startColumn, length);
  }
  






  StringBuffer getString(StringBuffer sb, int start, int length)
  {
    return getString(sb, start >>> m_chunkBits, start & m_chunkMask, length);
  }
  


























  StringBuffer getString(StringBuffer sb, int startChunk, int startColumn, int length)
  {
    int stop = (startChunk << m_chunkBits) + startColumn + length;
    int stopChunk = stop >>> m_chunkBits;
    int stopColumn = stop & m_chunkMask;
    


    for (int i = startChunk; i < stopChunk; i++)
    {
      if ((i == 0) && (m_innerFSB != null)) {
        m_innerFSB.getString(sb, startColumn, m_chunkSize - startColumn);
      } else {
        sb.append(m_array[i], startColumn, m_chunkSize - startColumn);
      }
      startColumn = 0;
    }
    
    if ((stopChunk == 0) && (m_innerFSB != null)) {
      m_innerFSB.getString(sb, startColumn, stopColumn - startColumn);
    } else if (stopColumn > startColumn) {
      sb.append(m_array[stopChunk], startColumn, stopColumn - startColumn);
    }
    return sb;
  }
  







  public char charAt(int pos)
  {
    int startChunk = pos >>> m_chunkBits;
    
    if ((startChunk == 0) && (m_innerFSB != null)) {
      return m_innerFSB.charAt(pos & m_chunkMask);
    }
    return m_array[startChunk][(pos & m_chunkMask)];
  }
  





















  public void sendSAXcharacters(ContentHandler ch, int start, int length)
    throws SAXException
  {
    int startChunk = start >>> m_chunkBits;
    int startColumn = start & m_chunkMask;
    if ((startColumn + length < m_chunkMask) && (m_innerFSB == null)) {
      ch.characters(m_array[startChunk], startColumn, length);
      return;
    }
    
    int stop = start + length;
    int stopChunk = stop >>> m_chunkBits;
    int stopColumn = stop & m_chunkMask;
    
    for (int i = startChunk; i < stopChunk; i++)
    {
      if ((i == 0) && (m_innerFSB != null)) {
        m_innerFSB.sendSAXcharacters(ch, startColumn, m_chunkSize - startColumn);
      }
      else {
        ch.characters(m_array[i], startColumn, m_chunkSize - startColumn);
      }
      startColumn = 0;
    }
    

    if ((stopChunk == 0) && (m_innerFSB != null)) {
      m_innerFSB.sendSAXcharacters(ch, startColumn, stopColumn - startColumn);
    } else if (stopColumn > startColumn)
    {
      ch.characters(m_array[stopChunk], startColumn, stopColumn - startColumn);
    }
  }
  





























  public int sendNormalizedSAXcharacters(ContentHandler ch, int start, int length)
    throws SAXException
  {
    int stateForNextChunk = 1;
    
    int stop = start + length;
    int startChunk = start >>> m_chunkBits;
    int startColumn = start & m_chunkMask;
    int stopChunk = stop >>> m_chunkBits;
    int stopColumn = stop & m_chunkMask;
    
    for (int i = startChunk; i < stopChunk; i++)
    {
      if ((i == 0) && (m_innerFSB != null)) {
        stateForNextChunk = m_innerFSB.sendNormalizedSAXcharacters(ch, startColumn, m_chunkSize - startColumn);
      }
      else
      {
        stateForNextChunk = sendNormalizedSAXcharacters(m_array[i], startColumn, m_chunkSize - startColumn, ch, stateForNextChunk);
      }
      


      startColumn = 0;
    }
    

    if ((stopChunk == 0) && (m_innerFSB != null)) {
      stateForNextChunk = m_innerFSB.sendNormalizedSAXcharacters(ch, startColumn, stopColumn - startColumn);
    }
    else if (stopColumn > startColumn)
    {
      stateForNextChunk = sendNormalizedSAXcharacters(m_array[stopChunk], startColumn, stopColumn - startColumn, ch, stateForNextChunk | 0x2);
    }
    


    return stateForNextChunk;
  }
  
  static final char[] SINGLE_SPACE = { ' ' };
  














































  static int sendNormalizedSAXcharacters(char[] ch, int start, int length, ContentHandler handler, int edgeTreatmentFlags)
    throws SAXException
  {
    boolean processingLeadingWhitespace = (edgeTreatmentFlags & 0x1) != 0;
    
    boolean seenWhitespace = (edgeTreatmentFlags & 0x4) != 0;
    int currPos = start;
    int limit = start + length;
    

    if (processingLeadingWhitespace)
    {
      while ((currPos < limit) && (XMLCharacterRecognizer.isWhiteSpace(ch[currPos]))) {
        currPos++;
      }
      

      if (currPos == limit) {
        return edgeTreatmentFlags;
      }
    }
    

    while (currPos < limit) {
      int startNonWhitespace = currPos;
      


      while ((currPos < limit) && (!XMLCharacterRecognizer.isWhiteSpace(ch[currPos]))) {
        currPos++;
      }
      

      if (startNonWhitespace != currPos) {
        if (seenWhitespace) {
          handler.characters(SINGLE_SPACE, 0, 1);
          seenWhitespace = false;
        }
        handler.characters(ch, startNonWhitespace, currPos - startNonWhitespace);
      }
      

      int startWhitespace = currPos;
      


      while ((currPos < limit) && (XMLCharacterRecognizer.isWhiteSpace(ch[currPos]))) {
        currPos++;
      }
      if (startWhitespace != currPos) {
        seenWhitespace = true;
      }
    }
    
    return (seenWhitespace ? 4 : 0) | edgeTreatmentFlags & 0x2;
  }
  













  public static void sendNormalizedSAXcharacters(char[] ch, int start, int length, ContentHandler handler)
    throws SAXException
  {
    sendNormalizedSAXcharacters(ch, start, length, handler, 3);
  }
  
















  public void sendSAXComment(LexicalHandler ch, int start, int length)
    throws SAXException
  {
    String comment = getString(start, length);
    ch.comment(comment.toCharArray(), 0, length);
  }
  

















  private void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {}
  

















  private FastStringBuffer(FastStringBuffer source)
  {
    m_chunkBits = m_chunkBits;
    m_maxChunkBits = m_maxChunkBits;
    m_rebundleBits = m_rebundleBits;
    m_chunkSize = m_chunkSize;
    m_chunkMask = m_chunkMask;
    m_array = m_array;
    m_innerFSB = m_innerFSB;
    


    m_lastChunk -= 1;
    m_firstFree = m_chunkSize;
    

    m_array = new char[16][];
    m_innerFSB = this;
    



    m_lastChunk = 1;
    m_firstFree = 0;
    m_chunkBits += m_rebundleBits;
    m_chunkSize = (1 << m_chunkBits);
    m_chunkMask = (m_chunkSize - 1);
  }
}
