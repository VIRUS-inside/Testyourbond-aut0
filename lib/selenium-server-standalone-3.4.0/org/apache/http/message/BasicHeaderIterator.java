package org.apache.http.message;

import java.util.NoSuchElementException;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.util.Args;




























































public class BasicHeaderIterator
  implements HeaderIterator
{
  protected final Header[] allHeaders;
  protected int currentIndex;
  protected String headerName;
  
  public BasicHeaderIterator(Header[] headers, String name)
  {
    allHeaders = ((Header[])Args.notNull(headers, "Header array"));
    headerName = name;
    currentIndex = findNext(-1);
  }
  









  protected int findNext(int pos)
  {
    int from = pos;
    if (from < -1) {
      return -1;
    }
    
    int to = allHeaders.length - 1;
    boolean found = false;
    while ((!found) && (from < to)) {
      from++;
      found = filterHeader(from);
    }
    return found ? from : -1;
  }
  








  protected boolean filterHeader(int index)
  {
    return (headerName == null) || (headerName.equalsIgnoreCase(allHeaders[index].getName()));
  }
  



  public boolean hasNext()
  {
    return currentIndex >= 0;
  }
  









  public Header nextHeader()
    throws NoSuchElementException
  {
    int current = currentIndex;
    if (current < 0) {
      throw new NoSuchElementException("Iteration already finished.");
    }
    
    currentIndex = findNext(current);
    
    return allHeaders[current];
  }
  









  public final Object next()
    throws NoSuchElementException
  {
    return nextHeader();
  }
  







  public void remove()
    throws UnsupportedOperationException
  {
    throw new UnsupportedOperationException("Removing headers is not supported.");
  }
}
