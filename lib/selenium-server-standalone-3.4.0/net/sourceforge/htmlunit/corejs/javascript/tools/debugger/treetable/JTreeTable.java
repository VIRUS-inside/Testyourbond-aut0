package net.sourceforge.htmlunit.corejs.javascript.tools.debugger.treetable;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;







































public class JTreeTable
  extends JTable
{
  private static final long serialVersionUID = -2103973006456695515L;
  protected TreeTableCellRenderer tree;
  
  public JTreeTable(TreeTableModel treeTableModel)
  {
    tree = new TreeTableCellRenderer(treeTableModel);
    

    super.setModel(new TreeTableModelAdapter(treeTableModel, tree));
    

    ListToTreeSelectionModelWrapper selectionWrapper = new ListToTreeSelectionModelWrapper();
    tree.setSelectionModel(selectionWrapper);
    setSelectionModel(selectionWrapper.getListSelectionModel());
    

    setDefaultRenderer(TreeTableModel.class, tree);
    setDefaultEditor(TreeTableModel.class, new TreeTableCellEditor());
    

    setShowGrid(false);
    

    setIntercellSpacing(new Dimension(0, 0));
    


    if (tree.getRowHeight() < 1)
    {
      setRowHeight(18);
    }
  }
  





  public void updateUI()
  {
    super.updateUI();
    if (tree != null) {
      tree.updateUI();
    }
    

    LookAndFeel.installColorsAndFont(this, "Tree.background", "Tree.foreground", "Tree.font");
  }
  








  public int getEditingRow()
  {
    return getColumnClass(editingColumn) == TreeTableModel.class ? -1 : editingRow;
  }
  




  public void setRowHeight(int rowHeight)
  {
    super.setRowHeight(rowHeight);
    if ((tree != null) && (tree.getRowHeight() != rowHeight)) {
      tree.setRowHeight(getRowHeight());
    }
  }
  


  public JTree getTree()
  {
    return tree;
  }
  

  public class TreeTableCellRenderer
    extends JTree
    implements TableCellRenderer
  {
    private static final long serialVersionUID = -193867880014600717L;
    protected int visibleRow;
    
    public TreeTableCellRenderer(TreeModel model)
    {
      super();
    }
    




    public void updateUI()
    {
      super.updateUI();
      

      TreeCellRenderer tcr = getCellRenderer();
      if ((tcr instanceof DefaultTreeCellRenderer)) {
        DefaultTreeCellRenderer dtcr = (DefaultTreeCellRenderer)tcr;
        



        dtcr.setTextSelectionColor(
          UIManager.getColor("Table.selectionForeground"));
        dtcr.setBackgroundSelectionColor(
          UIManager.getColor("Table.selectionBackground"));
      }
    }
    




    public void setRowHeight(int rowHeight)
    {
      if (rowHeight > 0) {
        super.setRowHeight(rowHeight);
        if ((JTreeTable.this != null) && 
          (getRowHeight() != rowHeight)) {
          JTreeTable.this.setRowHeight(getRowHeight());
        }
      }
    }
    



    public void setBounds(int x, int y, int w, int h)
    {
      super.setBounds(x, 0, w, getHeight());
    }
    




    public void paint(Graphics g)
    {
      g.translate(0, -visibleRow * getRowHeight());
      super.paint(g);
    }
    




    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
      if (isSelected) {
        setBackground(table.getSelectionBackground());
      } else {
        setBackground(table.getBackground());
      }
      visibleRow = row;
      return this;
    }
  }
  
  public class TreeTableCellEditor
    extends AbstractCellEditor implements TableCellEditor
  {
    public TreeTableCellEditor() {}
    
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int r, int c)
    {
      return tree;
    }
    



















    public boolean isCellEditable(EventObject e)
    {
      if ((e instanceof MouseEvent)) {
        for (int counter = getColumnCount() - 1; 
            counter >= 0; counter--) {
          if (getColumnClass(counter) == TreeTableModel.class) {
            MouseEvent me = (MouseEvent)e;
            



            MouseEvent newME = new MouseEvent(tree, me.getID(), me.getWhen(), me.getModifiers(), me.getX() - getCellRect(0, counter, true).x, me.getY(), me.getClickCount(), me.isPopupTrigger());
            tree.dispatchEvent(newME);
            break;
          }
        }
      }
      return false;
    }
  }
  



  public class ListToTreeSelectionModelWrapper
    extends DefaultTreeSelectionModel
  {
    private static final long serialVersionUID = 8168140829623071131L;
    


    protected boolean updatingListSelectionModel;
    


    public ListToTreeSelectionModelWrapper()
    {
      getListSelectionModel().addListSelectionListener(createListSelectionListener());
    }
    




    public ListSelectionModel getListSelectionModel()
    {
      return listSelectionModel;
    }
    





    public void resetRowSelection()
    {
      if (!updatingListSelectionModel) {
        updatingListSelectionModel = true;
        try {
          super.resetRowSelection();
          
          updatingListSelectionModel = false; } finally { updatingListSelectionModel = false;
        }
      }
    }
    







    protected ListSelectionListener createListSelectionListener()
    {
      return new ListSelectionHandler();
    }
    




    protected void updateSelectedPathsFromSelectedRows()
    {
      if (!updatingListSelectionModel) {
        updatingListSelectionModel = true;
        
        try
        {
          int min = listSelectionModel.getMinSelectionIndex();
          int max = listSelectionModel.getMaxSelectionIndex();
          
          clearSelection();
          if ((min != -1) && (max != -1)) {
            for (int counter = min; counter <= max; counter++) {
              if (listSelectionModel.isSelectedIndex(counter)) {
                TreePath selPath = tree.getPathForRow(counter);
                
                if (selPath != null) {
                  addSelectionPath(selPath);
                }
              }
            }
          }
        } finally {
          updatingListSelectionModel = false;
        }
      }
    }
    
    class ListSelectionHandler implements ListSelectionListener
    {
      ListSelectionHandler() {}
      
      public void valueChanged(ListSelectionEvent e)
      {
        updateSelectedPathsFromSelectedRows();
      }
    }
  }
}
