package org.apache.xml.dtm.ref;

import java.io.PrintStream;
import org.apache.xml.res.XMLMessages;






































final class ChunkedIntArray
{
  static final int slotsize = 4;
  static final int lowbits = 10;
  static final int chunkalloc = 1024;
  static final int lowmask = 1023;
  ChunksVector chunks = new ChunksVector();
  final int[] fastArray = new int['Ѐ'];
  int lastUsed = 0;
  




  ChunkedIntArray(int slotsize)
  {
    if (4 < slotsize)
      throw new ArrayIndexOutOfBoundsException(XMLMessages.createXMLMessage("ER_CHUNKEDINTARRAY_NOT_SUPPORTED", new Object[] { Integer.toString(slotsize) }));
    if (4 > slotsize)
      System.out.println("*****WARNING: ChunkedIntArray(" + slotsize + ") wasting " + (4 - slotsize) + " words per slot");
    chunks.addElement(fastArray);
  }
  


















  int appendSlot(int w0, int w1, int w2, int w3)
  {
    int slotsize = 4;
    int newoffset = (lastUsed + 1) * 4;
    int chunkpos = newoffset >> 10;
    int slotpos = newoffset & 0x3FF;
    

    if (chunkpos > chunks.size() - 1)
      chunks.addElement(new int['Ѐ']);
    int[] chunk = chunks.elementAt(chunkpos);
    chunk[slotpos] = w0;
    chunk[(slotpos + 1)] = w1;
    chunk[(slotpos + 2)] = w2;
    chunk[(slotpos + 3)] = w3;
    
    return ++lastUsed;
  }
  















  int readEntry(int position, int offset)
    throws ArrayIndexOutOfBoundsException
  {
    if (offset >= 4)
      throw new ArrayIndexOutOfBoundsException(XMLMessages.createXMLMessage("ER_OFFSET_BIGGER_THAN_SLOT", null));
    position *= 4;
    int chunkpos = position >> 10;
    int slotpos = position & 0x3FF;
    int[] chunk = chunks.elementAt(chunkpos);
    return chunk[(slotpos + offset)];
  }
  









  int specialFind(int startPos, int position)
  {
    int ancestor = startPos;
    while (ancestor > 0)
    {

      ancestor *= 4;
      int chunkpos = ancestor >> 10;
      int slotpos = ancestor & 0x3FF;
      int[] chunk = chunks.elementAt(chunkpos);
      



      ancestor = chunk[(slotpos + 1)];
      
      if (ancestor == position) {
        break;
      }
    }
    if (ancestor <= 0)
    {
      return position;
    }
    return -1;
  }
  



  int slotsUsed()
  {
    return lastUsed;
  }
  





  void discardLast()
  {
    lastUsed -= 1;
  }
  















  void writeEntry(int position, int offset, int value)
    throws ArrayIndexOutOfBoundsException
  {
    if (offset >= 4)
      throw new ArrayIndexOutOfBoundsException(XMLMessages.createXMLMessage("ER_OFFSET_BIGGER_THAN_SLOT", null));
    position *= 4;
    int chunkpos = position >> 10;
    int slotpos = position & 0x3FF;
    int[] chunk = chunks.elementAt(chunkpos);
    chunk[(slotpos + offset)] = value;
  }
  










  void writeSlot(int position, int w0, int w1, int w2, int w3)
  {
    position *= 4;
    int chunkpos = position >> 10;
    int slotpos = position & 0x3FF;
    

    if (chunkpos > chunks.size() - 1)
      chunks.addElement(new int['Ѐ']);
    int[] chunk = chunks.elementAt(chunkpos);
    chunk[slotpos] = w0;
    chunk[(slotpos + 1)] = w1;
    chunk[(slotpos + 2)] = w2;
    chunk[(slotpos + 3)] = w3;
  }
  

















  void readSlot(int position, int[] buffer)
  {
    position *= 4;
    int chunkpos = position >> 10;
    int slotpos = position & 0x3FF;
    

    if (chunkpos > chunks.size() - 1)
      chunks.addElement(new int['Ѐ']);
    int[] chunk = chunks.elementAt(chunkpos);
    System.arraycopy(chunk, slotpos, buffer, 0, 4);
  }
  

  static class ChunksVector
  {
    static final int BLOCKSIZE = 64;
    int[][] m_map = new int[64][];
    int m_mapSize = 64;
    int pos = 0;
    

    ChunksVector() {}
    

    final int size()
    {
      return pos;
    }
    
    void addElement(int[] value)
    {
      if (pos >= m_mapSize)
      {
        int orgMapSize = m_mapSize;
        while (pos >= m_mapSize)
          m_mapSize += 64;
        int[][] newMap = new int[m_mapSize][];
        System.arraycopy(m_map, 0, newMap, 0, orgMapSize);
        m_map = newMap;
      }
      

      m_map[pos] = value;
      pos += 1;
    }
    
    final int[] elementAt(int pos)
    {
      return m_map[pos];
    }
  }
}
