package net.sourceforge.htmlunit.corejs.javascript.tools.debugger;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import net.sourceforge.htmlunit.corejs.javascript.tools.debugger.treetable.JTreeTable;
import net.sourceforge.htmlunit.corejs.javascript.tools.debugger.treetable.JTreeTable.ListToTreeSelectionModelWrapper;
import net.sourceforge.htmlunit.corejs.javascript.tools.debugger.treetable.JTreeTable.TreeTableCellEditor;
import net.sourceforge.htmlunit.corejs.javascript.tools.debugger.treetable.JTreeTable.TreeTableCellRenderer;
import net.sourceforge.htmlunit.corejs.javascript.tools.debugger.treetable.TreeTableModel;
import net.sourceforge.htmlunit.corejs.javascript.tools.debugger.treetable.TreeTableModelAdapter;






































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































class MyTreeTable
  extends JTreeTable
{
  private static final long serialVersionUID = 3457265548184453049L;
  
  public MyTreeTable(VariableModel model)
  {
    super(model);
  }
  


  public JTree resetTree(TreeTableModel treeTableModel)
  {
    tree = new JTreeTable.TreeTableCellRenderer(this, treeTableModel);
    

    super.setModel(new TreeTableModelAdapter(treeTableModel, tree));
    

    JTreeTable.ListToTreeSelectionModelWrapper selectionWrapper = new JTreeTable.ListToTreeSelectionModelWrapper(this);
    tree.setSelectionModel(selectionWrapper);
    setSelectionModel(selectionWrapper.getListSelectionModel());
    

    if (tree.getRowHeight() < 1)
    {
      setRowHeight(18);
    }
    

    setDefaultRenderer(TreeTableModel.class, tree);
    setDefaultEditor(TreeTableModel.class, new JTreeTable.TreeTableCellEditor(this));
    setShowGrid(true);
    setIntercellSpacing(new Dimension(1, 1));
    tree.setRootVisible(false);
    tree.setShowsRootHandles(true);
    
    DefaultTreeCellRenderer r = (DefaultTreeCellRenderer)tree.getCellRenderer();
    r.setOpenIcon(null);
    r.setClosedIcon(null);
    r.setLeafIcon(null);
    return tree;
  }
  



  public boolean isCellEditable(EventObject e)
  {
    if ((e instanceof MouseEvent)) {
      MouseEvent me = (MouseEvent)e;
      





      if (me.getModifiers() != 0) { if ((me.getModifiers() & 0x410) != 0)
        {
          if ((me.getModifiers() & 0x1ACF) != 0) {}

        }
        

      }
      else
      {

        int row = rowAtPoint(me.getPoint());
        for (int counter = getColumnCount() - 1; 
            counter >= 0; counter--) {
          if (TreeTableModel.class == getColumnClass(counter))
          {



            MouseEvent newME = new MouseEvent(tree, me.getID(), me.getWhen(), me.getModifiers(), me.getX() - getCellRectx, me.getY(), me.getClickCount(), me.isPopupTrigger());
            tree.dispatchEvent(newME);
            break;
          }
        }
      }
      if (me.getClickCount() >= 3) {
        return true;
      }
      return false;
    }
    if (e == null) {
      return true;
    }
    return false;
  }
}
