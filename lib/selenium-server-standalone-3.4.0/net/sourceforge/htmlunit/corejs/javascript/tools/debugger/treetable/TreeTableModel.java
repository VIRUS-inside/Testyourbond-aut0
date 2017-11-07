package net.sourceforge.htmlunit.corejs.javascript.tools.debugger.treetable;

import javax.swing.tree.TreeModel;

public abstract interface TreeTableModel
  extends TreeModel
{
  public abstract int getColumnCount();
  
  public abstract String getColumnName(int paramInt);
  
  public abstract Class<?> getColumnClass(int paramInt);
  
  public abstract Object getValueAt(Object paramObject, int paramInt);
  
  public abstract boolean isCellEditable(Object paramObject, int paramInt);
  
  public abstract void setValueAt(Object paramObject1, Object paramObject2, int paramInt);
}
