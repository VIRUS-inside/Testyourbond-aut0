package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

































public class HtmlTableRow
  extends HtmlElement
{
  public static final String TAG_NAME = "tr";
  
  HtmlTableRow(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  


  public CellIterator getCellIterator()
  {
    return new CellIterator();
  }
  



  public List<HtmlTableCell> getCells()
  {
    List<HtmlTableCell> result = new ArrayList();
    for (HtmlTableCell cell : getCellIterator()) {
      result.add(cell);
    }
    return Collections.unmodifiableList(result);
  }
  



  public HtmlTableCell getCell(int index)
    throws IndexOutOfBoundsException
  {
    int count = 0;
    for (HtmlTableCell cell : getCellIterator()) {
      if (count == index) {
        return cell;
      }
      count++;
    }
    throw new IndexOutOfBoundsException();
  }
  







  public final String getAlignAttribute()
  {
    return getAttribute("align");
  }
  







  public final String getCharAttribute()
  {
    return getAttribute("char");
  }
  







  public final String getCharoffAttribute()
  {
    return getAttribute("charoff");
  }
  







  public final String getValignAttribute()
  {
    return getAttribute("valign");
  }
  



  public HtmlTable getEnclosingTable()
  {
    return (HtmlTable)getEnclosingElement("table");
  }
  







  public final String getBgcolorAttribute()
  {
    return getAttribute("bgcolor");
  }
  

  public class CellIterator
    implements Iterator<HtmlTableCell>, Iterable<HtmlTableCell>
  {
    private HtmlTableCell nextCell_;
    
    private HtmlForm currentForm_;
    
    public CellIterator()
    {
      setNextCell(getFirstChild());
    }
    

    public boolean hasNext()
    {
      return nextCell_ != null;
    }
    



    public HtmlTableCell next()
      throws NoSuchElementException
    {
      return nextCell();
    }
    



    public void remove()
    {
      if (nextCell_ == null) {
        throw new IllegalStateException();
      }
      DomNode sibling = nextCell_.getPreviousSibling();
      if (sibling != null) {
        sibling.remove();
      }
    }
    


    public HtmlTableCell nextCell()
      throws NoSuchElementException
    {
      if (nextCell_ != null) {
        HtmlTableCell result = nextCell_;
        setNextCell(nextCell_.getNextSibling());
        return result;
      }
      throw new NoSuchElementException();
    }
    




    private void setNextCell(DomNode node)
    {
      nextCell_ = null;
      for (DomNode next = node; next != null; next = next.getNextSibling()) {
        if ((next instanceof HtmlTableCell)) {
          nextCell_ = ((HtmlTableCell)next);
          return;
        }
        if ((currentForm_ == null) && ((next instanceof HtmlForm)))
        {
          currentForm_ = ((HtmlForm)next);
          setNextCell(next.getFirstChild());
          return;
        }
      }
      if (currentForm_ != null) {
        DomNode form = currentForm_;
        currentForm_ = null;
        setNextCell(form.getNextSibling());
      }
    }
    





    public Iterator<HtmlTableCell> iterator()
    {
      return this;
    }
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    return HtmlElement.DisplayStyle.TABLE_ROW;
  }
}
