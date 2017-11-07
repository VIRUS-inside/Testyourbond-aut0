package org.apache.http.message;

import java.util.List;
import java.util.NoSuchElementException;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

































































public class BasicListHeaderIterator
  implements HeaderIterator
{
  protected final List<Header> allHeaders;
  protected int currentIndex;
  protected int lastIndex;
  protected String headerName;
  
  public BasicListHeaderIterator(List<Header> headers, String name)
  {
    allHeaders = ((List)Args.notNull(headers, "Header list"));
    headerName = name;
    currentIndex = findNext(-1);
    lastIndex = -1;
  }
  









  protected int findNext(int pos)
  {
    int from = pos;
    if (from < -1) {
      return -1;
    }
    
    int to = allHeaders.size() - 1;
    boolean found = false;
    while ((!found) && (from < to)) {
      from++;
      found = filterHeader(from);
    }
    return found ? from : -1;
  }
  








  protected boolean filterHeader(int index)
  {
    if (headerName == null) {
      return true;
    }
    

    String name = ((Header)allHeaders.get(index)).getName();
    
    return headerName.equalsIgnoreCase(name);
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
    
    lastIndex = current;
    currentIndex = findNext(current);
    
    return (Header)allHeaders.get(current);
  }
  









  public final Object next()
    throws NoSuchElementException
  {
    return nextHeader();
  }
  




  public void remove()
    throws UnsupportedOperationException
  {
    Asserts.check(lastIndex >= 0, "No header to remove");
    allHeaders.remove(lastIndex);
    lastIndex = -1;
    currentIndex -= 1;
  }
}
