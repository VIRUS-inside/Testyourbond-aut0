package com.steadystate.css.parser;

import java.io.IOException;
import java.io.Reader;










public final class CssCharStream
  implements CharStream
{
  public static final boolean staticFlag = false;
  private static final int BUFFER_SIZE = 2048;
  int bufsize;
  int available;
  int tokenBegin;
  public int bufpos = -1;
  
  private int[] bufline;
  private int[] bufcolumn;
  private int column = 0;
  private int line = 1;
  
  private boolean prevCharIsCR = false;
  private boolean prevCharIsLF = false;
  
  private Reader inputStream;
  
  private char[] buffer;
  private int maxNextCharInd = 0;
  private int inBuf = 0;
  
  private int tabSize = 1;
  private boolean trackLineColumn = true;
  
  private void ExpandBuff(boolean wrapAround)
  {
    char[] newbuffer = new char[bufsize + 2048];
    int[] newbufline = new int[bufsize + 2048];
    int[] newbufcolumn = new int[bufsize + 2048];
    
    try
    {
      if (wrapAround)
      {
        System.arraycopy(buffer, tokenBegin, newbuffer, 0, bufsize - tokenBegin);
        System.arraycopy(buffer, 0, newbuffer, bufsize - tokenBegin, bufpos);
        buffer = newbuffer;
        
        System.arraycopy(bufline, tokenBegin, newbufline, 0, bufsize - tokenBegin);
        System.arraycopy(bufline, 0, newbufline, bufsize - tokenBegin, bufpos);
        bufline = newbufline;
        
        System.arraycopy(bufcolumn, tokenBegin, newbufcolumn, 0, bufsize - tokenBegin);
        System.arraycopy(bufcolumn, 0, newbufcolumn, bufsize - tokenBegin, bufpos);
        bufcolumn = newbufcolumn;
        
        maxNextCharInd = (this.bufpos += bufsize - tokenBegin);
      }
      else
      {
        System.arraycopy(buffer, tokenBegin, newbuffer, 0, bufsize - tokenBegin);
        buffer = newbuffer;
        
        System.arraycopy(bufline, tokenBegin, newbufline, 0, bufsize - tokenBegin);
        bufline = newbufline;
        
        System.arraycopy(bufcolumn, tokenBegin, newbufcolumn, 0, bufsize - tokenBegin);
        bufcolumn = newbufcolumn;
        
        maxNextCharInd = (this.bufpos -= tokenBegin);
      }
    }
    catch (Throwable t)
    {
      throw new Error(t.getMessage());
    }
    
    bufsize += 2048;
    available = bufsize;
    tokenBegin = 0;
  }
  
  private final void FillBuff() throws IOException
  {
    if (maxNextCharInd == available)
    {
      if (available == bufsize)
      {
        if (tokenBegin > 2048)
        {
          bufpos = (this.maxNextCharInd = 0);
          available = tokenBegin;
        }
        else if (tokenBegin < 0) {
          bufpos = (this.maxNextCharInd = 0);
        } else {
          ExpandBuff(false);
        }
      } else if (available > tokenBegin) {
        available = bufsize;
      } else if (tokenBegin - available < 2048) {
        ExpandBuff(true);
      } else {
        available = tokenBegin;
      }
    }
    try {
      int i;
      if ((i = inputStream.read(buffer, maxNextCharInd, available - maxNextCharInd)) == -1)
      {
        inputStream.close();
        throw new IOException();
      }
      
      maxNextCharInd += i;
      return;
    }
    catch (IOException e) {
      bufpos -= 1;
      backup(0);
      if (tokenBegin == -1)
        tokenBegin = bufpos;
      throw e;
    }
  }
  
  public final char BeginToken()
    throws IOException
  {
    tokenBegin = -1;
    char c = readChar();
    tokenBegin = bufpos;
    
    return c;
  }
  
  private final void UpdateLineColumn(char c)
  {
    column += 1;
    
    if (prevCharIsLF)
    {
      prevCharIsLF = false;
      line += (this.column = 1);
    }
    else if (prevCharIsCR)
    {
      prevCharIsCR = false;
      if (c == '\n')
      {
        prevCharIsLF = true;
      }
      else {
        line += (this.column = 1);
      }
    }
    switch (c)
    {
    case '\r': 
      prevCharIsCR = true;
      break;
    case '\n': 
      prevCharIsLF = true;
      break;
    }
    
    





    bufline[bufpos] = line;
    bufcolumn[bufpos] = column;
  }
  
  public final char readChar()
    throws IOException
  {
    if (inBuf > 0)
    {
      inBuf -= 1;
      
      if (++bufpos == bufsize) {
        bufpos = 0;
      }
      return buffer[bufpos];
    }
    
    if (++bufpos >= maxNextCharInd) {
      FillBuff();
    }
    char c = buffer[bufpos];
    
    UpdateLineColumn(c);
    return c;
  }
  



  @Deprecated
  public final int getColumn()
  {
    return bufcolumn[bufpos];
  }
  



  @Deprecated
  public final int getLine()
  {
    return bufline[bufpos];
  }
  
  public final int getEndColumn()
  {
    return bufcolumn[bufpos];
  }
  
  public final int getEndLine()
  {
    return bufline[bufpos];
  }
  
  public final int getBeginColumn()
  {
    return bufcolumn[tokenBegin];
  }
  
  public final int getBeginLine()
  {
    return bufline[tokenBegin];
  }
  
  public final void backup(int amount)
  {
    inBuf += amount;
    if (this.bufpos -= amount < 0) {
      bufpos += bufsize;
    }
  }
  
  public CssCharStream(Reader dstream, int startline, int startcolumn, int buffersize)
  {
    inputStream = dstream;
    line = startline;
    column = (startcolumn - 1);
    
    available = (this.bufsize = buffersize);
    buffer = new char[buffersize];
    bufline = new int[buffersize];
    bufcolumn = new int[buffersize];
  }
  


  public CssCharStream(Reader dstream, int startline, int startcolumn)
  {
    this(dstream, startline, startcolumn, 4096);
  }
  

  public final String GetImage()
  {
    if (bufpos >= tokenBegin)
      return new String(buffer, tokenBegin, bufpos - tokenBegin + 1);
    return new String(buffer, tokenBegin, bufsize - tokenBegin) + new String(buffer, 0, bufpos + 1);
  }
  

  public final char[] GetSuffix(int len)
  {
    char[] ret = new char[len];
    
    if (bufpos + 1 >= len) {
      System.arraycopy(buffer, bufpos - len + 1, ret, 0, len);
    }
    else {
      System.arraycopy(buffer, bufsize - (len - bufpos - 1), ret, 0, len - bufpos - 1);
      System.arraycopy(buffer, 0, ret, len - bufpos - 1, bufpos + 1);
    }
    
    return ret;
  }
  

  public void Done()
  {
    buffer = null;
    bufline = null;
    bufcolumn = null;
  }
  



  public void adjustBeginLineColumn(int newLine, int newCol)
  {
    int start = tokenBegin;
    int len;
    int len;
    if (bufpos >= tokenBegin)
    {
      len = bufpos - tokenBegin + inBuf + 1;
    }
    else
    {
      len = bufsize - tokenBegin + bufpos + 1 + inBuf;
    }
    
    int i = 0;int j = 0;int k = 0;
    int nextColDiff = 0;int columnDiff = 0;
    
    while ((i < len) && (bufline[(j = start % bufsize)] == bufline[(k = ++start % bufsize)]))
    {
      bufline[j] = newLine;
      nextColDiff = columnDiff + bufcolumn[k] - bufcolumn[j];
      bufcolumn[j] = (newCol + columnDiff);
      columnDiff = nextColDiff;
      i++;
    }
    
    if (i < len)
    {
      bufline[j] = (newLine++);
      bufcolumn[j] = (newCol + columnDiff);
      
      while (i++ < len)
      {
        if (bufline[(j = start % bufsize)] != bufline[(++start % bufsize)]) {
          bufline[j] = (newLine++);
        } else {
          bufline[j] = newLine;
        }
      }
    }
    line = bufline[j];
    column = bufcolumn[j];
  }
  
  public void setTabSize(int i) {
    tabSize = i;
  }
  
  public int getTabSize() {
    return tabSize;
  }
  
  public boolean getTrackLineColumn() {
    return trackLineColumn;
  }
  
  public void setTrackLineColumn(boolean tlc) {
    trackLineColumn = tlc;
  }
}
