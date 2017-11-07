package org.apache.regexp;

import java.io.IOException;
import java.io.Reader;



































































public final class ReaderCharacterIterator
  implements CharacterIterator
{
  private final Reader reader;
  private final StringBuffer buff;
  private boolean closed;
  
  public ReaderCharacterIterator(Reader paramReader)
  {
    reader = paramReader;
    buff = new StringBuffer(512);
    closed = false;
  }
  

  public String substring(int paramInt1, int paramInt2)
  {
    try
    {
      ensure(paramInt1 + paramInt2);
      return buff.toString().substring(paramInt1, paramInt2);
    }
    catch (IOException localIOException)
    {
      throw new StringIndexOutOfBoundsException(localIOException.getMessage());
    }
  }
  

  public String substring(int paramInt)
  {
    try
    {
      readAll();
      return buff.toString().substring(paramInt);
    }
    catch (IOException localIOException)
    {
      throw new StringIndexOutOfBoundsException(localIOException.getMessage());
    }
  }
  

  public char charAt(int paramInt)
  {
    try
    {
      ensure(paramInt);
      return buff.charAt(paramInt);
    }
    catch (IOException localIOException)
    {
      throw new StringIndexOutOfBoundsException(localIOException.getMessage());
    }
  }
  

  public boolean isEnd(int paramInt)
  {
    if (buff.length() > paramInt)
    {
      return false;
    }
    

    try
    {
      ensure(paramInt);
      return buff.length() <= paramInt;
    }
    catch (IOException localIOException)
    {
      throw new StringIndexOutOfBoundsException(localIOException.getMessage());
    }
  }
  

  private int read(int paramInt)
    throws IOException
  {
    if (closed)
    {
      return 0;
    }
    
    char[] arrayOfChar = new char[paramInt];
    int i = 0;
    int j = 0;
    
    do
    {
      j = reader.read(arrayOfChar);
      if (j < 0)
      {
        closed = true;
        break;
      }
      i += j;
      buff.append(arrayOfChar, 0, j);
    }
    while (i < paramInt);
    
    return i;
  }
  
  private void readAll()
    throws IOException
  {
    while (!closed)
    {
      read(1000);
    }
  }
  
  private void ensure(int paramInt)
    throws IOException
  {
    if (closed)
    {
      return;
    }
    
    if (paramInt < buff.length())
    {
      return;
    }
    read(paramInt + 1 - buff.length());
  }
}
