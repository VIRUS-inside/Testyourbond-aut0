package net.sf.cglib.util;

import java.util.Comparator;
import net.sf.cglib.asm..ClassVisitor;
import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.AbstractClassGenerator.Source;
import net.sf.cglib.core.ClassesKey;
import net.sf.cglib.core.ReflectUtils;













































public abstract class ParallelSorter
  extends SorterTemplate
{
  protected Object[] a;
  private Comparer comparer;
  
  protected ParallelSorter() {}
  
  public abstract ParallelSorter newInstance(Object[] paramArrayOfObject);
  
  public static ParallelSorter create(Object[] arrays)
  {
    Generator gen = new Generator();
    gen.setArrays(arrays);
    return gen.create();
  }
  
  private int len() {
    return ((Object[])a[0]).length;
  }
  



  public void quickSort(int index)
  {
    quickSort(index, 0, len(), null);
  }
  





  public void quickSort(int index, int lo, int hi)
  {
    quickSort(index, lo, hi, null);
  }
  




  public void quickSort(int index, Comparator cmp)
  {
    quickSort(index, 0, len(), cmp);
  }
  






  public void quickSort(int index, int lo, int hi, Comparator cmp)
  {
    chooseComparer(index, cmp);
    super.quickSort(lo, hi - 1);
  }
  


  public void mergeSort(int index)
  {
    mergeSort(index, 0, len(), null);
  }
  





  public void mergeSort(int index, int lo, int hi)
  {
    mergeSort(index, lo, hi, null);
  }
  





  public void mergeSort(int index, Comparator cmp)
  {
    mergeSort(index, 0, len(), cmp);
  }
  






  public void mergeSort(int index, int lo, int hi, Comparator cmp)
  {
    chooseComparer(index, cmp);
    super.mergeSort(lo, hi - 1);
  }
  
  private void chooseComparer(int index, Comparator cmp) {
    Object array = a[index];
    Class type = array.getClass().getComponentType();
    if (type.equals(Integer.TYPE)) {
      comparer = new IntComparer((int[])array);
    } else if (type.equals(Long.TYPE)) {
      comparer = new LongComparer((long[])array);
    } else if (type.equals(Double.TYPE)) {
      comparer = new DoubleComparer((double[])array);
    } else if (type.equals(Float.TYPE)) {
      comparer = new FloatComparer((float[])array);
    } else if (type.equals(Short.TYPE)) {
      comparer = new ShortComparer((short[])array);
    } else if (type.equals(Byte.TYPE)) {
      comparer = new ByteComparer((byte[])array);
    } else if (cmp != null) {
      comparer = new ComparatorComparer((Object[])array, cmp);
    } else {
      comparer = new ObjectComparer((Object[])array);
    }
  }
  
  protected int compare(int i, int j) {
    return comparer.compare(i, j);
  }
  
  static abstract interface Comparer {
    public abstract int compare(int paramInt1, int paramInt2);
  }
  
  static class ComparatorComparer implements ParallelSorter.Comparer {
    private Object[] a;
    private Comparator cmp;
    
    public ComparatorComparer(Object[] a, Comparator cmp) {
      this.a = a;
      this.cmp = cmp;
    }
    

    public int compare(int i, int j) { return cmp.compare(a[i], a[j]); }
  }
  
  static class ObjectComparer implements ParallelSorter.Comparer {
    private Object[] a;
    
    public ObjectComparer(Object[] a) { this.a = a; }
    
    public int compare(int i, int j) { return ((Comparable)a[i]).compareTo(a[j]); }
  }
  
  static class IntComparer implements ParallelSorter.Comparer {
    private int[] a;
    
    public IntComparer(int[] a) { this.a = a; }
    public int compare(int i, int j) { return a[i] - a[j]; }
  }
  
  static class LongComparer implements ParallelSorter.Comparer { private long[] a;
    
    public LongComparer(long[] a) { this.a = a; }
    
    public int compare(int i, int j) { long vi = a[i];
      long vj = a[j];
      return vi > vj ? 1 : vi == vj ? 0 : -1;
    }
  }
  
  static class FloatComparer implements ParallelSorter.Comparer { private float[] a;
    
    public FloatComparer(float[] a) { this.a = a; }
    
    public int compare(int i, int j) { float vi = a[i];
      float vj = a[j];
      return vi > vj ? 1 : vi == vj ? 0 : -1;
    }
  }
  
  static class DoubleComparer implements ParallelSorter.Comparer { private double[] a;
    
    public DoubleComparer(double[] a) { this.a = a; }
    
    public int compare(int i, int j) { double vi = a[i];
      double vj = a[j];
      return vi > vj ? 1 : vi == vj ? 0 : -1;
    }
  }
  
  static class ShortComparer implements ParallelSorter.Comparer { private short[] a;
    
    public ShortComparer(short[] a) { this.a = a; }
    public int compare(int i, int j) { return a[i] - a[j]; }
  }
  
  static class ByteComparer implements ParallelSorter.Comparer { private byte[] a;
    
    public ByteComparer(byte[] a) { this.a = a; }
    public int compare(int i, int j) { return a[i] - a[j]; }
  }
  
  public static class Generator extends AbstractClassGenerator {
    private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(ParallelSorter.class.getName());
    private Object[] arrays;
    
    public Generator()
    {
      super();
    }
    
    protected ClassLoader getDefaultClassLoader() {
      return null;
    }
    
    public void setArrays(Object[] arrays) {
      this.arrays = arrays;
    }
    
    public ParallelSorter create() {
      return (ParallelSorter)super.create(ClassesKey.create(arrays));
    }
    
    public void generateClass(.ClassVisitor v) throws Exception {
      if (arrays.length == 0) {
        throw new IllegalArgumentException("No arrays specified to sort");
      }
      for (int i = 0; i < arrays.length; i++) {
        if (!arrays[i].getClass().isArray()) {
          throw new IllegalArgumentException(arrays[i].getClass() + " is not an array");
        }
      }
      new ParallelSorterEmitter(v, getClassName(), arrays);
    }
    
    protected Object firstInstance(Class type) {
      return ((ParallelSorter)ReflectUtils.newInstance(type)).newInstance(arrays);
    }
    
    protected Object nextInstance(Object instance) {
      return ((ParallelSorter)instance).newInstance(arrays);
    }
  }
}
