package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.SgmlPage;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

































public class HtmlTable
  extends HtmlElement
{
  public static final String TAG_NAME = "table";
  
  HtmlTable(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  




















  public final HtmlTableCell getCellAt(int rowIndex, int columnIndex)
  {
    RowIterator rowIterator = getRowIterator();
    HashSet<Point> occupied = new HashSet();
    int row = 0;
    for (HtmlTableRow htmlTableRow : rowIterator) {
      HtmlTableRow.CellIterator cellIterator = htmlTableRow.getCellIterator();
      int col = 0;
      for (HtmlTableCell cell : cellIterator) {
        while (occupied.contains(new Point(row, col))) {
          col++;
        }
        int nextRow = row + cell.getRowSpan();
        if ((row <= rowIndex) && (nextRow > rowIndex)) {
          int nextCol = col + cell.getColumnSpan();
          if ((col <= columnIndex) && (nextCol > columnIndex)) {
            return cell;
          }
        }
        if ((cell.getRowSpan() > 1) || (cell.getColumnSpan() > 1)) {
          for (int i = 0; i < cell.getRowSpan(); i++) {
            for (int j = 0; j < cell.getColumnSpan(); j++) {
              occupied.add(new Point(row + i, col + j));
            }
          }
        }
        col++;
      }
      row++;
    }
    return null;
  }
  


  private RowIterator getRowIterator()
  {
    return new RowIterator();
  }
  



  public List<HtmlTableRow> getRows()
  {
    List<HtmlTableRow> result = new ArrayList();
    for (HtmlTableRow row : getRowIterator()) {
      result.add(row);
    }
    return Collections.unmodifiableList(result);
  }
  




  public HtmlTableRow getRow(int index)
    throws IndexOutOfBoundsException
  {
    int count = 0;
    for (HtmlTableRow row : getRowIterator()) {
      if (count == index) {
        return row;
      }
      count++;
    }
    throw new IndexOutOfBoundsException();
  }
  





  public final int getRowCount()
  {
    int count = 0;
    for (RowIterator iterator = getRowIterator(); iterator.hasNext(); iterator.next()) {
      count++;
    }
    return count;
  }
  





  public final HtmlTableRow getRowById(String id)
    throws ElementNotFoundException
  {
    for (HtmlTableRow row : getRowIterator()) {
      if (row.getId().equals(id)) {
        return row;
      }
    }
    throw new ElementNotFoundException("tr", "id", id);
  }
  




  public String getCaptionText()
  {
    for (DomElement element : getChildElements()) {
      if ((element instanceof HtmlCaption)) {
        return element.asText();
      }
    }
    return null;
  }
  




  public HtmlTableHeader getHeader()
  {
    for (DomElement element : getChildElements()) {
      if ((element instanceof HtmlTableHeader)) {
        return (HtmlTableHeader)element;
      }
    }
    return null;
  }
  




  public HtmlTableFooter getFooter()
  {
    for (DomElement element : getChildElements()) {
      if ((element instanceof HtmlTableFooter)) {
        return (HtmlTableFooter)element;
      }
    }
    return null;
  }
  





  public List<HtmlTableBody> getBodies()
  {
    List<HtmlTableBody> bodies = new ArrayList();
    for (DomElement element : getChildElements()) {
      if ((element instanceof HtmlTableBody)) {
        bodies.add((HtmlTableBody)element);
      }
    }
    return bodies;
  }
  







  public final String getSummaryAttribute()
  {
    return getAttribute("summary");
  }
  







  public final String getWidthAttribute()
  {
    return getAttribute("width");
  }
  







  public final String getBorderAttribute()
  {
    return getAttribute("border");
  }
  







  public final String getFrameAttribute()
  {
    return getAttribute("frame");
  }
  







  public final String getRulesAttribute()
  {
    return getAttribute("rules");
  }
  







  public final String getCellSpacingAttribute()
  {
    return getAttribute("cellspacing");
  }
  







  public final String getCellPaddingAttribute()
  {
    return getAttribute("cellpadding");
  }
  







  public final String getAlignAttribute()
  {
    return getAttribute("align");
  }
  







  public final String getBgcolorAttribute()
  {
    return getAttribute("bgcolor");
  }
  

  private class RowIterator
    implements Iterator<HtmlTableRow>, Iterable<HtmlTableRow>
  {
    private HtmlTableRow nextRow_;
    
    private TableRowGroup currentGroup_;
    
    RowIterator()
    {
      setNextRow(getFirstChild());
    }
    



    public boolean hasNext()
    {
      return nextRow_ != null;
    }
    



    public HtmlTableRow next()
      throws NoSuchElementException
    {
      return nextRow();
    }
    



    public void remove()
    {
      if (nextRow_ == null) {
        throw new IllegalStateException();
      }
      DomNode sibling = nextRow_.getPreviousSibling();
      if (sibling != null) {
        sibling.remove();
      }
    }
    


    public HtmlTableRow nextRow()
      throws NoSuchElementException
    {
      if (nextRow_ != null) {
        HtmlTableRow result = nextRow_;
        setNextRow(nextRow_.getNextSibling());
        return result;
      }
      throw new NoSuchElementException();
    }
    




    private void setNextRow(DomNode node)
    {
      nextRow_ = null;
      for (DomNode next = node; next != null; next = next.getNextSibling()) {
        if ((next instanceof HtmlTableRow)) {
          nextRow_ = ((HtmlTableRow)next);
          return;
        }
        if ((currentGroup_ == null) && ((next instanceof TableRowGroup))) {
          currentGroup_ = ((TableRowGroup)next);
          setNextRow(next.getFirstChild());
          return;
        }
      }
      if (currentGroup_ != null) {
        DomNode group = currentGroup_;
        currentGroup_ = null;
        setNextRow(group.getNextSibling());
      }
    }
    
    public Iterator<HtmlTableRow> iterator()
    {
      return this;
    }
  }
  




  protected boolean isEmptyXmlTagExpanded()
  {
    return true;
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    return HtmlElement.DisplayStyle.TABLE;
  }
}
