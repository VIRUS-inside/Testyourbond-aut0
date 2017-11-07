package org.apache.bcel.verifier;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.TreeSet;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;























































public class VerifierFactoryListModel
  implements VerifierFactoryObserver, ListModel
{
  private ArrayList listeners = new ArrayList();
  
  private TreeSet cache = new TreeSet();
  
  public VerifierFactoryListModel() {
    VerifierFactory.attach(this);
    update(null);
  }
  
  public synchronized void update(String s) {
    int size = listeners.size();
    
    Verifier[] verifiers = VerifierFactory.getVerifiers();
    int num_of_verifiers = verifiers.length;
    cache.clear();
    for (int i = 0; i < num_of_verifiers; i++) {
      cache.add(verifiers[i].getClassName());
    }
    
    for (int i = 0; i < size; i++) {
      ListDataEvent e = new ListDataEvent(this, 0, 0, num_of_verifiers - 1);
      ((ListDataListener)listeners.get(i)).contentsChanged(e);
    }
  }
  
  public synchronized void addListDataListener(ListDataListener l) {
    listeners.add(l);
  }
  
  public synchronized void removeListDataListener(ListDataListener l) {
    listeners.remove(l);
  }
  
  public synchronized int getSize() {
    return cache.size();
  }
  
  public synchronized Object getElementAt(int index) {
    return cache.toArray()[index];
  }
}
