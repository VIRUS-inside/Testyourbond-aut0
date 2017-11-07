package org.apache.http.message;

import java.util.NoSuchElementException;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HeaderIterator;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;


































public class BasicHeaderElementIterator
  implements HeaderElementIterator
{
  private final HeaderIterator headerIt;
  private final HeaderValueParser parser;
  private HeaderElement currentElement = null;
  private CharArrayBuffer buffer = null;
  private ParserCursor cursor = null;
  




  public BasicHeaderElementIterator(HeaderIterator headerIterator, HeaderValueParser parser)
  {
    headerIt = ((HeaderIterator)Args.notNull(headerIterator, "Header iterator"));
    this.parser = ((HeaderValueParser)Args.notNull(parser, "Parser"));
  }
  
  public BasicHeaderElementIterator(HeaderIterator headerIterator)
  {
    this(headerIterator, BasicHeaderValueParser.INSTANCE);
  }
  
  private void bufferHeaderValue()
  {
    cursor = null;
    buffer = null;
    while (headerIt.hasNext()) {
      Header h = headerIt.nextHeader();
      if ((h instanceof FormattedHeader)) {
        buffer = ((FormattedHeader)h).getBuffer();
        cursor = new ParserCursor(0, buffer.length());
        cursor.updatePos(((FormattedHeader)h).getValuePos());
        break;
      }
      String value = h.getValue();
      if (value != null) {
        buffer = new CharArrayBuffer(value.length());
        buffer.append(value);
        cursor = new ParserCursor(0, buffer.length());
        break;
      }
    }
  }
  

  private void parseNextElement()
  {
    while ((headerIt.hasNext()) || (cursor != null)) {
      if ((cursor == null) || (cursor.atEnd()))
      {
        bufferHeaderValue();
      }
      
      if (cursor != null)
      {
        while (!cursor.atEnd()) {
          HeaderElement e = parser.parseHeaderElement(buffer, cursor);
          if ((e.getName().length() != 0) || (e.getValue() != null))
          {
            currentElement = e;
            return;
          }
        }
        
        if (cursor.atEnd())
        {
          cursor = null;
          buffer = null;
        }
      }
    }
  }
  
  public boolean hasNext()
  {
    if (currentElement == null) {
      parseNextElement();
    }
    return currentElement != null;
  }
  
  public HeaderElement nextElement() throws NoSuchElementException
  {
    if (currentElement == null) {
      parseNextElement();
    }
    
    if (currentElement == null) {
      throw new NoSuchElementException("No more header elements available");
    }
    
    HeaderElement element = currentElement;
    currentElement = null;
    return element;
  }
  
  public final Object next() throws NoSuchElementException
  {
    return nextElement();
  }
  
  public void remove() throws UnsupportedOperationException
  {
    throw new UnsupportedOperationException("Remove not supported");
  }
}
