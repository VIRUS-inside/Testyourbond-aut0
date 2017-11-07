package org.eclipse.jetty.util;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;





































public class BlockingArrayQueue<E>
  extends AbstractList<E>
  implements BlockingQueue<E>
{
  private static final int HEAD_OFFSET = MemoryUtils.getIntegersPerCacheLine() - 1;
  


  private static final int TAIL_OFFSET = HEAD_OFFSET + MemoryUtils.getIntegersPerCacheLine();
  

  public static final int DEFAULT_CAPACITY = 128;
  

  public static final int DEFAULT_GROWTH = 64;
  

  private final int _maxCapacity;
  

  private final int _growCapacity;
  

  private final int[] _indexes = new int[TAIL_OFFSET + 1];
  private final Lock _tailLock = new ReentrantLock();
  private final AtomicInteger _size = new AtomicInteger();
  private final Lock _headLock = new ReentrantLock();
  private final Condition _notEmpty = _headLock.newCondition();
  


  private Object[] _elements;
  



  public BlockingArrayQueue()
  {
    _elements = new Object[''];
    _growCapacity = 64;
    _maxCapacity = Integer.MAX_VALUE;
  }
  






  public BlockingArrayQueue(int maxCapacity)
  {
    _elements = new Object[maxCapacity];
    _growCapacity = -1;
    _maxCapacity = maxCapacity;
  }
  








  public BlockingArrayQueue(int capacity, int growBy)
  {
    _elements = new Object[capacity];
    _growCapacity = growBy;
    _maxCapacity = Integer.MAX_VALUE;
  }
  










  public BlockingArrayQueue(int capacity, int growBy, int maxCapacity)
  {
    if (capacity > maxCapacity)
      throw new IllegalArgumentException();
    _elements = new Object[capacity];
    _growCapacity = growBy;
    _maxCapacity = maxCapacity;
  }
  






  public void clear()
  {
    _tailLock.lock();
    
    try
    {
      _headLock.lock();
      try
      {
        _indexes[HEAD_OFFSET] = 0;
        _indexes[TAIL_OFFSET] = 0;
        _size.set(0);
        


        _headLock.unlock(); } finally { _headLock.unlock();
      }
    }
    finally
    {
      _tailLock.unlock();
    }
  }
  

  public int size()
  {
    return _size.get();
  }
  

  public Iterator<E> iterator()
  {
    return listIterator();
  }
  






  public E poll()
  {
    if (_size.get() == 0) {
      return null;
    }
    E e = null;
    
    _headLock.lock();
    try
    {
      if (_size.get() > 0)
      {
        int head = _indexes[HEAD_OFFSET];
        e = _elements[head];
        _elements[head] = null;
        _indexes[HEAD_OFFSET] = ((head + 1) % _elements.length);
        if (_size.decrementAndGet() > 0) {
          _notEmpty.signal();
        }
      }
      

      _headLock.unlock(); } finally { _headLock.unlock();
    }
    return e;
  }
  


  public E peek()
  {
    if (_size.get() == 0) {
      return null;
    }
    E e = null;
    
    _headLock.lock();
    try
    {
      if (_size.get() > 0) {
        e = _elements[_indexes[HEAD_OFFSET]];
      }
      

      _headLock.unlock(); } finally { _headLock.unlock();
    }
    return e;
  }
  

  public E remove()
  {
    E e = poll();
    if (e == null)
      throw new NoSuchElementException();
    return e;
  }
  

  public E element()
  {
    E e = peek();
    if (e == null)
      throw new NoSuchElementException();
    return e;
  }
  





  public boolean offer(E e)
  {
    Objects.requireNonNull(e);
    
    boolean notEmpty = false;
    _tailLock.lock();
    try
    {
      int size = _size.get();
      boolean bool1; if (size >= _maxCapacity) {
        return false;
      }
      
      if (size == _elements.length)
      {
        _headLock.lock();
        try
        {
          if (!grow()) {
            bool1 = false;
            


            _headLock.unlock();return bool1;
          }
        }
        finally {
          _headLock.unlock();
        }
      }
      

      int tail = _indexes[TAIL_OFFSET];
      _elements[tail] = e;
      _indexes[TAIL_OFFSET] = ((tail + 1) % _elements.length);
      notEmpty = _size.getAndIncrement() == 0;
    }
    finally
    {
      _tailLock.unlock();
    }
    
    if (notEmpty)
    {
      _headLock.lock();
      try
      {
        _notEmpty.signal();
      }
      finally
      {
        _headLock.unlock();
      }
    }
    
    return true;
  }
  

  public boolean add(E e)
  {
    if (offer(e)) {
      return true;
    }
    throw new IllegalStateException();
  }
  

  public void put(E o)
    throws InterruptedException
  {
    throw new UnsupportedOperationException();
  }
  

  public boolean offer(E o, long timeout, TimeUnit unit)
    throws InterruptedException
  {
    throw new UnsupportedOperationException();
  }
  

  public E take()
    throws InterruptedException
  {
    E e = null;
    
    _headLock.lockInterruptibly();
    try
    {
      try
      {
        while (_size.get() == 0)
        {
          _notEmpty.await();
        }
      }
      catch (InterruptedException ie)
      {
        _notEmpty.signal();
        throw ie;
      }
      
      int head = _indexes[HEAD_OFFSET];
      e = _elements[head];
      _elements[head] = null;
      _indexes[HEAD_OFFSET] = ((head + 1) % _elements.length);
      
      if (_size.decrementAndGet() > 0) {
        _notEmpty.signal();
      }
      

      _headLock.unlock(); } finally { _headLock.unlock();
    }
    return e;
  }
  

  public E poll(long time, TimeUnit unit)
    throws InterruptedException
  {
    long nanos = unit.toNanos(time);
    E e = null;
    
    _headLock.lockInterruptibly();
    try
    {
      try
      {
        while (_size.get() == 0)
        {
          if (nanos <= 0L)
            return null;
          nanos = _notEmpty.awaitNanos(nanos);
        }
      }
      catch (InterruptedException x)
      {
        _notEmpty.signal();
        throw x;
      }
      
      int head = _indexes[HEAD_OFFSET];
      e = _elements[head];
      _elements[head] = null;
      _indexes[HEAD_OFFSET] = ((head + 1) % _elements.length);
      
      if (_size.decrementAndGet() > 0) {
        _notEmpty.signal();
      }
    }
    finally {
      _headLock.unlock();
    }
    return e;
  }
  
  /* Error */
  public boolean remove(Object o)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 6	org/eclipse/jetty/util/BlockingArrayQueue:_tailLock	Ljava/util/concurrent/locks/Lock;
    //   4: invokeinterface 22 1 0
    //   9: aload_0
    //   10: getfield 10	org/eclipse/jetty/util/BlockingArrayQueue:_headLock	Ljava/util/concurrent/locks/Lock;
    //   13: invokeinterface 22 1 0
    //   18: aload_0
    //   19: invokevirtual 47	org/eclipse/jetty/util/BlockingArrayQueue:isEmpty	()Z
    //   22: ifeq +25 -> 47
    //   25: iconst_0
    //   26: istore_2
    //   27: aload_0
    //   28: getfield 10	org/eclipse/jetty/util/BlockingArrayQueue:_headLock	Ljava/util/concurrent/locks/Lock;
    //   31: invokeinterface 25 1 0
    //   36: aload_0
    //   37: getfield 6	org/eclipse/jetty/util/BlockingArrayQueue:_tailLock	Ljava/util/concurrent/locks/Lock;
    //   40: invokeinterface 25 1 0
    //   45: iload_2
    //   46: ireturn
    //   47: aload_0
    //   48: getfield 3	org/eclipse/jetty/util/BlockingArrayQueue:_indexes	[I
    //   51: getstatic 23	org/eclipse/jetty/util/BlockingArrayQueue:HEAD_OFFSET	I
    //   54: iaload
    //   55: istore_2
    //   56: aload_0
    //   57: getfield 3	org/eclipse/jetty/util/BlockingArrayQueue:_indexes	[I
    //   60: getstatic 2	org/eclipse/jetty/util/BlockingArrayQueue:TAIL_OFFSET	I
    //   63: iaload
    //   64: istore_3
    //   65: aload_0
    //   66: getfield 15	org/eclipse/jetty/util/BlockingArrayQueue:_elements	[Ljava/lang/Object;
    //   69: arraylength
    //   70: istore 4
    //   72: iload_2
    //   73: istore 5
    //   75: aload_0
    //   76: getfield 15	org/eclipse/jetty/util/BlockingArrayQueue:_elements	[Ljava/lang/Object;
    //   79: iload 5
    //   81: aaload
    //   82: aload_1
    //   83: invokestatic 48	java/util/Objects:equals	(Ljava/lang/Object;Ljava/lang/Object;)Z
    //   86: ifeq +52 -> 138
    //   89: aload_0
    //   90: iload 5
    //   92: iload_2
    //   93: if_icmplt +10 -> 103
    //   96: iload 5
    //   98: iload_2
    //   99: isub
    //   100: goto +10 -> 110
    //   103: iload 4
    //   105: iload_2
    //   106: isub
    //   107: iload 5
    //   109: iadd
    //   110: invokevirtual 49	org/eclipse/jetty/util/BlockingArrayQueue:remove	(I)Ljava/lang/Object;
    //   113: pop
    //   114: iconst_1
    //   115: istore 6
    //   117: aload_0
    //   118: getfield 10	org/eclipse/jetty/util/BlockingArrayQueue:_headLock	Ljava/util/concurrent/locks/Lock;
    //   121: invokeinterface 25 1 0
    //   126: aload_0
    //   127: getfield 6	org/eclipse/jetty/util/BlockingArrayQueue:_tailLock	Ljava/util/concurrent/locks/Lock;
    //   130: invokeinterface 25 1 0
    //   135: iload 6
    //   137: ireturn
    //   138: iinc 5 1
    //   141: iload 5
    //   143: iload 4
    //   145: if_icmpne +6 -> 151
    //   148: iconst_0
    //   149: istore 5
    //   151: iload 5
    //   153: iload_3
    //   154: if_icmpne -79 -> 75
    //   157: iconst_0
    //   158: istore 6
    //   160: aload_0
    //   161: getfield 10	org/eclipse/jetty/util/BlockingArrayQueue:_headLock	Ljava/util/concurrent/locks/Lock;
    //   164: invokeinterface 25 1 0
    //   169: aload_0
    //   170: getfield 6	org/eclipse/jetty/util/BlockingArrayQueue:_tailLock	Ljava/util/concurrent/locks/Lock;
    //   173: invokeinterface 25 1 0
    //   178: iload 6
    //   180: ireturn
    //   181: astore 7
    //   183: aload_0
    //   184: getfield 10	org/eclipse/jetty/util/BlockingArrayQueue:_headLock	Ljava/util/concurrent/locks/Lock;
    //   187: invokeinterface 25 1 0
    //   192: aload 7
    //   194: athrow
    //   195: astore 8
    //   197: aload_0
    //   198: getfield 6	org/eclipse/jetty/util/BlockingArrayQueue:_tailLock	Ljava/util/concurrent/locks/Lock;
    //   201: invokeinterface 25 1 0
    //   206: aload 8
    //   208: athrow
    // Line number table:
    //   Java source line #420	-> byte code offset #0
    //   Java source line #424	-> byte code offset #9
    //   Java source line #427	-> byte code offset #18
    //   Java source line #428	-> byte code offset #25
    //   Java source line #451	-> byte code offset #27
    //   Java source line #456	-> byte code offset #36
    //   Java source line #428	-> byte code offset #45
    //   Java source line #430	-> byte code offset #47
    //   Java source line #431	-> byte code offset #56
    //   Java source line #432	-> byte code offset #65
    //   Java source line #434	-> byte code offset #72
    //   Java source line #437	-> byte code offset #75
    //   Java source line #439	-> byte code offset #89
    //   Java source line #440	-> byte code offset #114
    //   Java source line #451	-> byte code offset #117
    //   Java source line #456	-> byte code offset #126
    //   Java source line #440	-> byte code offset #135
    //   Java source line #442	-> byte code offset #138
    //   Java source line #443	-> byte code offset #141
    //   Java source line #444	-> byte code offset #148
    //   Java source line #445	-> byte code offset #151
    //   Java source line #446	-> byte code offset #157
    //   Java source line #451	-> byte code offset #160
    //   Java source line #456	-> byte code offset #169
    //   Java source line #446	-> byte code offset #178
    //   Java source line #451	-> byte code offset #181
    //   Java source line #456	-> byte code offset #195
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	209	0	this	BlockingArrayQueue<E>
    //   0	209	1	o	Object
    //   26	20	2	bool1	boolean
    //   55	52	2	head	int
    //   64	90	3	tail	int
    //   70	74	4	capacity	int
    //   73	79	5	i	int
    //   115	64	6	bool2	boolean
    //   181	12	7	localObject1	Object
    //   195	12	8	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   18	27	181	finally
    //   47	117	181	finally
    //   138	160	181	finally
    //   181	183	181	finally
    //   9	36	195	finally
    //   47	126	195	finally
    //   138	169	195	finally
    //   181	197	195	finally
  }
  
  /* Error */
  public int remainingCapacity()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 6	org/eclipse/jetty/util/BlockingArrayQueue:_tailLock	Ljava/util/concurrent/locks/Lock;
    //   4: invokeinterface 22 1 0
    //   9: aload_0
    //   10: getfield 10	org/eclipse/jetty/util/BlockingArrayQueue:_headLock	Ljava/util/concurrent/locks/Lock;
    //   13: invokeinterface 22 1 0
    //   18: aload_0
    //   19: invokevirtual 50	org/eclipse/jetty/util/BlockingArrayQueue:getCapacity	()I
    //   22: aload_0
    //   23: invokevirtual 51	org/eclipse/jetty/util/BlockingArrayQueue:size	()I
    //   26: isub
    //   27: istore_1
    //   28: aload_0
    //   29: getfield 10	org/eclipse/jetty/util/BlockingArrayQueue:_headLock	Ljava/util/concurrent/locks/Lock;
    //   32: invokeinterface 25 1 0
    //   37: aload_0
    //   38: getfield 6	org/eclipse/jetty/util/BlockingArrayQueue:_tailLock	Ljava/util/concurrent/locks/Lock;
    //   41: invokeinterface 25 1 0
    //   46: iload_1
    //   47: ireturn
    //   48: astore_2
    //   49: aload_0
    //   50: getfield 10	org/eclipse/jetty/util/BlockingArrayQueue:_headLock	Ljava/util/concurrent/locks/Lock;
    //   53: invokeinterface 25 1 0
    //   58: aload_2
    //   59: athrow
    //   60: astore_3
    //   61: aload_0
    //   62: getfield 6	org/eclipse/jetty/util/BlockingArrayQueue:_tailLock	Ljava/util/concurrent/locks/Lock;
    //   65: invokeinterface 25 1 0
    //   70: aload_3
    //   71: athrow
    // Line number table:
    //   Java source line #464	-> byte code offset #0
    //   Java source line #468	-> byte code offset #9
    //   Java source line #471	-> byte code offset #18
    //   Java source line #475	-> byte code offset #28
    //   Java source line #480	-> byte code offset #37
    //   Java source line #471	-> byte code offset #46
    //   Java source line #475	-> byte code offset #48
    //   Java source line #480	-> byte code offset #60
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	72	0	this	BlockingArrayQueue<E>
    //   27	20	1	i	int
    //   48	11	2	localObject1	Object
    //   60	11	3	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   18	28	48	finally
    //   9	37	60	finally
    //   48	61	60	finally
  }
  
  public int drainTo(Collection<? super E> c)
  {
    throw new UnsupportedOperationException();
  }
  

  public int drainTo(Collection<? super E> c, int maxElements)
  {
    throw new UnsupportedOperationException();
  }
  
  /* Error */
  public E get(int index)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 6	org/eclipse/jetty/util/BlockingArrayQueue:_tailLock	Ljava/util/concurrent/locks/Lock;
    //   4: invokeinterface 22 1 0
    //   9: aload_0
    //   10: getfield 10	org/eclipse/jetty/util/BlockingArrayQueue:_headLock	Ljava/util/concurrent/locks/Lock;
    //   13: invokeinterface 22 1 0
    //   18: iload_1
    //   19: iflt +14 -> 33
    //   22: iload_1
    //   23: aload_0
    //   24: getfield 9	org/eclipse/jetty/util/BlockingArrayQueue:_size	Ljava/util/concurrent/atomic/AtomicInteger;
    //   27: invokevirtual 26	java/util/concurrent/atomic/AtomicInteger:get	()I
    //   30: if_icmplt +47 -> 77
    //   33: new 52	java/lang/IndexOutOfBoundsException
    //   36: dup
    //   37: new 53	java/lang/StringBuilder
    //   40: dup
    //   41: invokespecial 54	java/lang/StringBuilder:<init>	()V
    //   44: ldc 55
    //   46: invokevirtual 56	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   49: iload_1
    //   50: invokevirtual 57	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   53: ldc 58
    //   55: invokevirtual 56	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   58: aload_0
    //   59: getfield 9	org/eclipse/jetty/util/BlockingArrayQueue:_size	Ljava/util/concurrent/atomic/AtomicInteger;
    //   62: invokevirtual 59	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   65: ldc 60
    //   67: invokevirtual 56	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   70: invokevirtual 61	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   73: invokespecial 62	java/lang/IndexOutOfBoundsException:<init>	(Ljava/lang/String;)V
    //   76: athrow
    //   77: aload_0
    //   78: getfield 3	org/eclipse/jetty/util/BlockingArrayQueue:_indexes	[I
    //   81: getstatic 23	org/eclipse/jetty/util/BlockingArrayQueue:HEAD_OFFSET	I
    //   84: iaload
    //   85: iload_1
    //   86: iadd
    //   87: istore_2
    //   88: aload_0
    //   89: getfield 15	org/eclipse/jetty/util/BlockingArrayQueue:_elements	[Ljava/lang/Object;
    //   92: arraylength
    //   93: istore_3
    //   94: iload_2
    //   95: iload_3
    //   96: if_icmplt +7 -> 103
    //   99: iload_2
    //   100: iload_3
    //   101: isub
    //   102: istore_2
    //   103: aload_0
    //   104: getfield 15	org/eclipse/jetty/util/BlockingArrayQueue:_elements	[Ljava/lang/Object;
    //   107: iload_2
    //   108: aaload
    //   109: astore 4
    //   111: aload_0
    //   112: getfield 10	org/eclipse/jetty/util/BlockingArrayQueue:_headLock	Ljava/util/concurrent/locks/Lock;
    //   115: invokeinterface 25 1 0
    //   120: aload_0
    //   121: getfield 6	org/eclipse/jetty/util/BlockingArrayQueue:_tailLock	Ljava/util/concurrent/locks/Lock;
    //   124: invokeinterface 25 1 0
    //   129: aload 4
    //   131: areturn
    //   132: astore 5
    //   134: aload_0
    //   135: getfield 10	org/eclipse/jetty/util/BlockingArrayQueue:_headLock	Ljava/util/concurrent/locks/Lock;
    //   138: invokeinterface 25 1 0
    //   143: aload 5
    //   145: athrow
    //   146: astore 6
    //   148: aload_0
    //   149: getfield 6	org/eclipse/jetty/util/BlockingArrayQueue:_tailLock	Ljava/util/concurrent/locks/Lock;
    //   152: invokeinterface 25 1 0
    //   157: aload 6
    //   159: athrow
    // Line number table:
    //   Java source line #505	-> byte code offset #0
    //   Java source line #509	-> byte code offset #9
    //   Java source line #512	-> byte code offset #18
    //   Java source line #513	-> byte code offset #33
    //   Java source line #514	-> byte code offset #77
    //   Java source line #515	-> byte code offset #88
    //   Java source line #516	-> byte code offset #94
    //   Java source line #517	-> byte code offset #99
    //   Java source line #518	-> byte code offset #103
    //   Java source line #522	-> byte code offset #111
    //   Java source line #527	-> byte code offset #120
    //   Java source line #518	-> byte code offset #129
    //   Java source line #522	-> byte code offset #132
    //   Java source line #527	-> byte code offset #146
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	160	0	this	BlockingArrayQueue<E>
    //   0	160	1	index	int
    //   87	21	2	i	int
    //   93	8	3	capacity	int
    //   109	21	4	localObject1	Object
    //   132	12	5	localObject2	Object
    //   146	12	6	localObject3	Object
    // Exception table:
    //   from	to	target	type
    //   18	111	132	finally
    //   132	134	132	finally
    //   9	120	146	finally
    //   132	148	146	finally
  }
  
  public void add(int index, E e)
  {
    if (e == null) {
      throw new NullPointerException();
    }
    _tailLock.lock();
    
    try
    {
      _headLock.lock();
      try
      {
        int size = _size.get();
        
        if ((index < 0) || (index > size)) {
          throw new IndexOutOfBoundsException("!(0<" + index + "<=" + _size + ")");
        }
        if (index == size)
        {
          add(e);
        }
        else
        {
          if ((_indexes[TAIL_OFFSET] == _indexes[HEAD_OFFSET]) && 
            (!grow())) {
            throw new IllegalStateException("full");
          }
          
          int i = _indexes[HEAD_OFFSET] + index;
          int capacity = _elements.length;
          
          if (i >= capacity) {
            i -= capacity;
          }
          _size.incrementAndGet();
          int tail = _indexes[TAIL_OFFSET]; int 
            tmp206_205 = ((tail + 1) % capacity);tail = tmp206_205;_indexes[TAIL_OFFSET] = tmp206_205;
          
          if (i < tail)
          {
            System.arraycopy(_elements, i, _elements, i + 1, tail - i);
            _elements[i] = e;
          }
          else
          {
            if (tail > 0)
            {
              System.arraycopy(_elements, 0, _elements, 1, tail);
              _elements[0] = _elements[(capacity - 1)];
            }
            
            System.arraycopy(_elements, i, _elements, i + 1, capacity - i - 1);
            _elements[i] = e;
          }
        }
      }
      finally
      {
        _headLock.unlock();
      }
    }
    finally
    {
      _tailLock.unlock();
    }
  }
  
  /* Error */
  public E set(int index, E e)
  {
    // Byte code:
    //   0: aload_2
    //   1: invokestatic 34	java/util/Objects:requireNonNull	(Ljava/lang/Object;)Ljava/lang/Object;
    //   4: pop
    //   5: aload_0
    //   6: getfield 6	org/eclipse/jetty/util/BlockingArrayQueue:_tailLock	Ljava/util/concurrent/locks/Lock;
    //   9: invokeinterface 22 1 0
    //   14: aload_0
    //   15: getfield 10	org/eclipse/jetty/util/BlockingArrayQueue:_headLock	Ljava/util/concurrent/locks/Lock;
    //   18: invokeinterface 22 1 0
    //   23: iload_1
    //   24: iflt +14 -> 38
    //   27: iload_1
    //   28: aload_0
    //   29: getfield 9	org/eclipse/jetty/util/BlockingArrayQueue:_size	Ljava/util/concurrent/atomic/AtomicInteger;
    //   32: invokevirtual 26	java/util/concurrent/atomic/AtomicInteger:get	()I
    //   35: if_icmplt +47 -> 82
    //   38: new 52	java/lang/IndexOutOfBoundsException
    //   41: dup
    //   42: new 53	java/lang/StringBuilder
    //   45: dup
    //   46: invokespecial 54	java/lang/StringBuilder:<init>	()V
    //   49: ldc 55
    //   51: invokevirtual 56	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   54: iload_1
    //   55: invokevirtual 57	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   58: ldc 58
    //   60: invokevirtual 56	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   63: aload_0
    //   64: getfield 9	org/eclipse/jetty/util/BlockingArrayQueue:_size	Ljava/util/concurrent/atomic/AtomicInteger;
    //   67: invokevirtual 59	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   70: ldc 60
    //   72: invokevirtual 56	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   75: invokevirtual 61	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   78: invokespecial 62	java/lang/IndexOutOfBoundsException:<init>	(Ljava/lang/String;)V
    //   81: athrow
    //   82: aload_0
    //   83: getfield 3	org/eclipse/jetty/util/BlockingArrayQueue:_indexes	[I
    //   86: getstatic 23	org/eclipse/jetty/util/BlockingArrayQueue:HEAD_OFFSET	I
    //   89: iaload
    //   90: iload_1
    //   91: iadd
    //   92: istore_3
    //   93: aload_0
    //   94: getfield 15	org/eclipse/jetty/util/BlockingArrayQueue:_elements	[Ljava/lang/Object;
    //   97: arraylength
    //   98: istore 4
    //   100: iload_3
    //   101: iload 4
    //   103: if_icmplt +8 -> 111
    //   106: iload_3
    //   107: iload 4
    //   109: isub
    //   110: istore_3
    //   111: aload_0
    //   112: getfield 15	org/eclipse/jetty/util/BlockingArrayQueue:_elements	[Ljava/lang/Object;
    //   115: iload_3
    //   116: aaload
    //   117: astore 5
    //   119: aload_0
    //   120: getfield 15	org/eclipse/jetty/util/BlockingArrayQueue:_elements	[Ljava/lang/Object;
    //   123: iload_3
    //   124: aload_2
    //   125: aastore
    //   126: aload 5
    //   128: astore 6
    //   130: aload_0
    //   131: getfield 10	org/eclipse/jetty/util/BlockingArrayQueue:_headLock	Ljava/util/concurrent/locks/Lock;
    //   134: invokeinterface 25 1 0
    //   139: aload_0
    //   140: getfield 6	org/eclipse/jetty/util/BlockingArrayQueue:_tailLock	Ljava/util/concurrent/locks/Lock;
    //   143: invokeinterface 25 1 0
    //   148: aload 6
    //   150: areturn
    //   151: astore 7
    //   153: aload_0
    //   154: getfield 10	org/eclipse/jetty/util/BlockingArrayQueue:_headLock	Ljava/util/concurrent/locks/Lock;
    //   157: invokeinterface 25 1 0
    //   162: aload 7
    //   164: athrow
    //   165: astore 8
    //   167: aload_0
    //   168: getfield 6	org/eclipse/jetty/util/BlockingArrayQueue:_tailLock	Ljava/util/concurrent/locks/Lock;
    //   171: invokeinterface 25 1 0
    //   176: aload 8
    //   178: athrow
    // Line number table:
    //   Java source line #603	-> byte code offset #0
    //   Java source line #605	-> byte code offset #5
    //   Java source line #609	-> byte code offset #14
    //   Java source line #612	-> byte code offset #23
    //   Java source line #613	-> byte code offset #38
    //   Java source line #615	-> byte code offset #82
    //   Java source line #616	-> byte code offset #93
    //   Java source line #617	-> byte code offset #100
    //   Java source line #618	-> byte code offset #106
    //   Java source line #619	-> byte code offset #111
    //   Java source line #620	-> byte code offset #119
    //   Java source line #621	-> byte code offset #126
    //   Java source line #625	-> byte code offset #130
    //   Java source line #630	-> byte code offset #139
    //   Java source line #621	-> byte code offset #148
    //   Java source line #625	-> byte code offset #151
    //   Java source line #630	-> byte code offset #165
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	179	0	this	BlockingArrayQueue<E>
    //   0	179	1	index	int
    //   0	179	2	e	E
    //   92	32	3	i	int
    //   98	10	4	capacity	int
    //   117	10	5	old	E
    //   128	21	6	?	E
    //   151	12	7	localObject1	Object
    //   165	12	8	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   23	130	151	finally
    //   151	153	151	finally
    //   14	139	165	finally
    //   151	167	165	finally
  }
  
  /* Error */
  public E remove(int index)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 6	org/eclipse/jetty/util/BlockingArrayQueue:_tailLock	Ljava/util/concurrent/locks/Lock;
    //   4: invokeinterface 22 1 0
    //   9: aload_0
    //   10: getfield 10	org/eclipse/jetty/util/BlockingArrayQueue:_headLock	Ljava/util/concurrent/locks/Lock;
    //   13: invokeinterface 22 1 0
    //   18: iload_1
    //   19: iflt +14 -> 33
    //   22: iload_1
    //   23: aload_0
    //   24: getfield 9	org/eclipse/jetty/util/BlockingArrayQueue:_size	Ljava/util/concurrent/atomic/AtomicInteger;
    //   27: invokevirtual 26	java/util/concurrent/atomic/AtomicInteger:get	()I
    //   30: if_icmplt +47 -> 77
    //   33: new 52	java/lang/IndexOutOfBoundsException
    //   36: dup
    //   37: new 53	java/lang/StringBuilder
    //   40: dup
    //   41: invokespecial 54	java/lang/StringBuilder:<init>	()V
    //   44: ldc 55
    //   46: invokevirtual 56	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   49: iload_1
    //   50: invokevirtual 57	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   53: ldc 58
    //   55: invokevirtual 56	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   58: aload_0
    //   59: getfield 9	org/eclipse/jetty/util/BlockingArrayQueue:_size	Ljava/util/concurrent/atomic/AtomicInteger;
    //   62: invokevirtual 59	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   65: ldc 60
    //   67: invokevirtual 56	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   70: invokevirtual 61	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   73: invokespecial 62	java/lang/IndexOutOfBoundsException:<init>	(Ljava/lang/String;)V
    //   76: athrow
    //   77: aload_0
    //   78: getfield 3	org/eclipse/jetty/util/BlockingArrayQueue:_indexes	[I
    //   81: getstatic 23	org/eclipse/jetty/util/BlockingArrayQueue:HEAD_OFFSET	I
    //   84: iaload
    //   85: iload_1
    //   86: iadd
    //   87: istore_2
    //   88: aload_0
    //   89: getfield 15	org/eclipse/jetty/util/BlockingArrayQueue:_elements	[Ljava/lang/Object;
    //   92: arraylength
    //   93: istore_3
    //   94: iload_2
    //   95: iload_3
    //   96: if_icmplt +7 -> 103
    //   99: iload_2
    //   100: iload_3
    //   101: isub
    //   102: istore_2
    //   103: aload_0
    //   104: getfield 15	org/eclipse/jetty/util/BlockingArrayQueue:_elements	[Ljava/lang/Object;
    //   107: iload_2
    //   108: aaload
    //   109: astore 4
    //   111: aload_0
    //   112: getfield 3	org/eclipse/jetty/util/BlockingArrayQueue:_indexes	[I
    //   115: getstatic 2	org/eclipse/jetty/util/BlockingArrayQueue:TAIL_OFFSET	I
    //   118: iaload
    //   119: istore 5
    //   121: iload_2
    //   122: iload 5
    //   124: if_icmpge +37 -> 161
    //   127: aload_0
    //   128: getfield 15	org/eclipse/jetty/util/BlockingArrayQueue:_elements	[Ljava/lang/Object;
    //   131: iload_2
    //   132: iconst_1
    //   133: iadd
    //   134: aload_0
    //   135: getfield 15	org/eclipse/jetty/util/BlockingArrayQueue:_elements	[Ljava/lang/Object;
    //   138: iload_2
    //   139: iload 5
    //   141: iload_2
    //   142: isub
    //   143: invokestatic 69	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
    //   146: aload_0
    //   147: getfield 3	org/eclipse/jetty/util/BlockingArrayQueue:_indexes	[I
    //   150: getstatic 2	org/eclipse/jetty/util/BlockingArrayQueue:TAIL_OFFSET	I
    //   153: dup2
    //   154: iaload
    //   155: iconst_1
    //   156: isub
    //   157: iastore
    //   158: goto +97 -> 255
    //   161: aload_0
    //   162: getfield 15	org/eclipse/jetty/util/BlockingArrayQueue:_elements	[Ljava/lang/Object;
    //   165: iload_2
    //   166: iconst_1
    //   167: iadd
    //   168: aload_0
    //   169: getfield 15	org/eclipse/jetty/util/BlockingArrayQueue:_elements	[Ljava/lang/Object;
    //   172: iload_2
    //   173: iload_3
    //   174: iload_2
    //   175: isub
    //   176: iconst_1
    //   177: isub
    //   178: invokestatic 69	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
    //   181: aload_0
    //   182: getfield 15	org/eclipse/jetty/util/BlockingArrayQueue:_elements	[Ljava/lang/Object;
    //   185: iload_3
    //   186: iconst_1
    //   187: isub
    //   188: aload_0
    //   189: getfield 15	org/eclipse/jetty/util/BlockingArrayQueue:_elements	[Ljava/lang/Object;
    //   192: iconst_0
    //   193: aaload
    //   194: aastore
    //   195: iload 5
    //   197: ifle +33 -> 230
    //   200: aload_0
    //   201: getfield 15	org/eclipse/jetty/util/BlockingArrayQueue:_elements	[Ljava/lang/Object;
    //   204: iconst_1
    //   205: aload_0
    //   206: getfield 15	org/eclipse/jetty/util/BlockingArrayQueue:_elements	[Ljava/lang/Object;
    //   209: iconst_0
    //   210: iload 5
    //   212: invokestatic 69	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
    //   215: aload_0
    //   216: getfield 3	org/eclipse/jetty/util/BlockingArrayQueue:_indexes	[I
    //   219: getstatic 2	org/eclipse/jetty/util/BlockingArrayQueue:TAIL_OFFSET	I
    //   222: dup2
    //   223: iaload
    //   224: iconst_1
    //   225: isub
    //   226: iastore
    //   227: goto +14 -> 241
    //   230: aload_0
    //   231: getfield 3	org/eclipse/jetty/util/BlockingArrayQueue:_indexes	[I
    //   234: getstatic 2	org/eclipse/jetty/util/BlockingArrayQueue:TAIL_OFFSET	I
    //   237: iload_3
    //   238: iconst_1
    //   239: isub
    //   240: iastore
    //   241: aload_0
    //   242: getfield 15	org/eclipse/jetty/util/BlockingArrayQueue:_elements	[Ljava/lang/Object;
    //   245: aload_0
    //   246: getfield 3	org/eclipse/jetty/util/BlockingArrayQueue:_indexes	[I
    //   249: getstatic 2	org/eclipse/jetty/util/BlockingArrayQueue:TAIL_OFFSET	I
    //   252: iaload
    //   253: aconst_null
    //   254: aastore
    //   255: aload_0
    //   256: getfield 9	org/eclipse/jetty/util/BlockingArrayQueue:_size	Ljava/util/concurrent/atomic/AtomicInteger;
    //   259: invokevirtual 28	java/util/concurrent/atomic/AtomicInteger:decrementAndGet	()I
    //   262: pop
    //   263: aload 4
    //   265: astore 6
    //   267: aload_0
    //   268: getfield 10	org/eclipse/jetty/util/BlockingArrayQueue:_headLock	Ljava/util/concurrent/locks/Lock;
    //   271: invokeinterface 25 1 0
    //   276: aload_0
    //   277: getfield 6	org/eclipse/jetty/util/BlockingArrayQueue:_tailLock	Ljava/util/concurrent/locks/Lock;
    //   280: invokeinterface 25 1 0
    //   285: aload 6
    //   287: areturn
    //   288: astore 7
    //   290: aload_0
    //   291: getfield 10	org/eclipse/jetty/util/BlockingArrayQueue:_headLock	Ljava/util/concurrent/locks/Lock;
    //   294: invokeinterface 25 1 0
    //   299: aload 7
    //   301: athrow
    //   302: astore 8
    //   304: aload_0
    //   305: getfield 6	org/eclipse/jetty/util/BlockingArrayQueue:_tailLock	Ljava/util/concurrent/locks/Lock;
    //   308: invokeinterface 25 1 0
    //   313: aload 8
    //   315: athrow
    // Line number table:
    //   Java source line #639	-> byte code offset #0
    //   Java source line #643	-> byte code offset #9
    //   Java source line #646	-> byte code offset #18
    //   Java source line #647	-> byte code offset #33
    //   Java source line #649	-> byte code offset #77
    //   Java source line #650	-> byte code offset #88
    //   Java source line #651	-> byte code offset #94
    //   Java source line #652	-> byte code offset #99
    //   Java source line #653	-> byte code offset #103
    //   Java source line #655	-> byte code offset #111
    //   Java source line #656	-> byte code offset #121
    //   Java source line #658	-> byte code offset #127
    //   Java source line #659	-> byte code offset #146
    //   Java source line #663	-> byte code offset #161
    //   Java source line #664	-> byte code offset #181
    //   Java source line #665	-> byte code offset #195
    //   Java source line #667	-> byte code offset #200
    //   Java source line #668	-> byte code offset #215
    //   Java source line #672	-> byte code offset #230
    //   Java source line #674	-> byte code offset #241
    //   Java source line #677	-> byte code offset #255
    //   Java source line #679	-> byte code offset #263
    //   Java source line #683	-> byte code offset #267
    //   Java source line #688	-> byte code offset #276
    //   Java source line #679	-> byte code offset #285
    //   Java source line #683	-> byte code offset #288
    //   Java source line #688	-> byte code offset #302
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	316	0	this	BlockingArrayQueue<E>
    //   0	316	1	index	int
    //   87	88	2	i	int
    //   93	145	3	capacity	int
    //   109	155	4	old	E
    //   119	92	5	tail	int
    //   265	21	6	?	E
    //   288	12	7	localObject1	Object
    //   302	12	8	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   18	267	288	finally
    //   288	290	288	finally
    //   9	276	302	finally
    //   288	304	302	finally
  }
  
  /* Error */
  public ListIterator<E> listIterator(int index)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 6	org/eclipse/jetty/util/BlockingArrayQueue:_tailLock	Ljava/util/concurrent/locks/Lock;
    //   4: invokeinterface 22 1 0
    //   9: aload_0
    //   10: getfield 10	org/eclipse/jetty/util/BlockingArrayQueue:_headLock	Ljava/util/concurrent/locks/Lock;
    //   13: invokeinterface 22 1 0
    //   18: aload_0
    //   19: invokevirtual 51	org/eclipse/jetty/util/BlockingArrayQueue:size	()I
    //   22: anewarray 14	java/lang/Object
    //   25: astore_2
    //   26: aload_0
    //   27: invokevirtual 51	org/eclipse/jetty/util/BlockingArrayQueue:size	()I
    //   30: ifle +79 -> 109
    //   33: aload_0
    //   34: getfield 3	org/eclipse/jetty/util/BlockingArrayQueue:_indexes	[I
    //   37: getstatic 23	org/eclipse/jetty/util/BlockingArrayQueue:HEAD_OFFSET	I
    //   40: iaload
    //   41: istore_3
    //   42: aload_0
    //   43: getfield 3	org/eclipse/jetty/util/BlockingArrayQueue:_indexes	[I
    //   46: getstatic 2	org/eclipse/jetty/util/BlockingArrayQueue:TAIL_OFFSET	I
    //   49: iaload
    //   50: istore 4
    //   52: iload_3
    //   53: iload 4
    //   55: if_icmpge +20 -> 75
    //   58: aload_0
    //   59: getfield 15	org/eclipse/jetty/util/BlockingArrayQueue:_elements	[Ljava/lang/Object;
    //   62: iload_3
    //   63: aload_2
    //   64: iconst_0
    //   65: iload 4
    //   67: iload_3
    //   68: isub
    //   69: invokestatic 69	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
    //   72: goto +37 -> 109
    //   75: aload_0
    //   76: getfield 15	org/eclipse/jetty/util/BlockingArrayQueue:_elements	[Ljava/lang/Object;
    //   79: arraylength
    //   80: iload_3
    //   81: isub
    //   82: istore 5
    //   84: aload_0
    //   85: getfield 15	org/eclipse/jetty/util/BlockingArrayQueue:_elements	[Ljava/lang/Object;
    //   88: iload_3
    //   89: aload_2
    //   90: iconst_0
    //   91: iload 5
    //   93: invokestatic 69	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
    //   96: aload_0
    //   97: getfield 15	org/eclipse/jetty/util/BlockingArrayQueue:_elements	[Ljava/lang/Object;
    //   100: iconst_0
    //   101: aload_2
    //   102: iload 5
    //   104: iload 4
    //   106: invokestatic 69	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
    //   109: new 70	org/eclipse/jetty/util/BlockingArrayQueue$Itr
    //   112: dup
    //   113: aload_0
    //   114: aload_2
    //   115: iload_1
    //   116: invokespecial 71	org/eclipse/jetty/util/BlockingArrayQueue$Itr:<init>	(Lorg/eclipse/jetty/util/BlockingArrayQueue;[Ljava/lang/Object;I)V
    //   119: astore_3
    //   120: aload_0
    //   121: getfield 10	org/eclipse/jetty/util/BlockingArrayQueue:_headLock	Ljava/util/concurrent/locks/Lock;
    //   124: invokeinterface 25 1 0
    //   129: aload_0
    //   130: getfield 6	org/eclipse/jetty/util/BlockingArrayQueue:_tailLock	Ljava/util/concurrent/locks/Lock;
    //   133: invokeinterface 25 1 0
    //   138: aload_3
    //   139: areturn
    //   140: astore 6
    //   142: aload_0
    //   143: getfield 10	org/eclipse/jetty/util/BlockingArrayQueue:_headLock	Ljava/util/concurrent/locks/Lock;
    //   146: invokeinterface 25 1 0
    //   151: aload 6
    //   153: athrow
    //   154: astore 7
    //   156: aload_0
    //   157: getfield 6	org/eclipse/jetty/util/BlockingArrayQueue:_tailLock	Ljava/util/concurrent/locks/Lock;
    //   160: invokeinterface 25 1 0
    //   165: aload 7
    //   167: athrow
    // Line number table:
    //   Java source line #696	-> byte code offset #0
    //   Java source line #700	-> byte code offset #9
    //   Java source line #703	-> byte code offset #18
    //   Java source line #704	-> byte code offset #26
    //   Java source line #706	-> byte code offset #33
    //   Java source line #707	-> byte code offset #42
    //   Java source line #708	-> byte code offset #52
    //   Java source line #710	-> byte code offset #58
    //   Java source line #714	-> byte code offset #75
    //   Java source line #715	-> byte code offset #84
    //   Java source line #716	-> byte code offset #96
    //   Java source line #719	-> byte code offset #109
    //   Java source line #723	-> byte code offset #120
    //   Java source line #728	-> byte code offset #129
    //   Java source line #719	-> byte code offset #138
    //   Java source line #723	-> byte code offset #140
    //   Java source line #728	-> byte code offset #154
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	168	0	this	BlockingArrayQueue<E>
    //   0	168	1	index	int
    //   25	90	2	elements	Object[]
    //   41	98	3	head	int
    //   50	55	4	tail	int
    //   82	21	5	chunk	int
    //   140	12	6	localObject1	Object
    //   154	12	7	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   18	120	140	finally
    //   140	142	140	finally
    //   9	129	154	finally
    //   140	156	154	finally
  }
  
  public int getCapacity()
  {
    _tailLock.lock();
    try
    {
      return _elements.length;
    }
    finally
    {
      _tailLock.unlock();
    }
  }
  



  public int getMaxCapacity()
  {
    return _maxCapacity;
  }
  
  /* Error */
  private boolean grow()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 16	org/eclipse/jetty/util/BlockingArrayQueue:_growCapacity	I
    //   4: ifgt +5 -> 9
    //   7: iconst_0
    //   8: ireturn
    //   9: aload_0
    //   10: getfield 6	org/eclipse/jetty/util/BlockingArrayQueue:_tailLock	Ljava/util/concurrent/locks/Lock;
    //   13: invokeinterface 22 1 0
    //   18: aload_0
    //   19: getfield 10	org/eclipse/jetty/util/BlockingArrayQueue:_headLock	Ljava/util/concurrent/locks/Lock;
    //   22: invokeinterface 22 1 0
    //   27: aload_0
    //   28: getfield 3	org/eclipse/jetty/util/BlockingArrayQueue:_indexes	[I
    //   31: getstatic 23	org/eclipse/jetty/util/BlockingArrayQueue:HEAD_OFFSET	I
    //   34: iaload
    //   35: istore_1
    //   36: aload_0
    //   37: getfield 3	org/eclipse/jetty/util/BlockingArrayQueue:_indexes	[I
    //   40: getstatic 2	org/eclipse/jetty/util/BlockingArrayQueue:TAIL_OFFSET	I
    //   43: iaload
    //   44: istore_2
    //   45: aload_0
    //   46: getfield 15	org/eclipse/jetty/util/BlockingArrayQueue:_elements	[Ljava/lang/Object;
    //   49: arraylength
    //   50: istore 4
    //   52: iload 4
    //   54: aload_0
    //   55: getfield 16	org/eclipse/jetty/util/BlockingArrayQueue:_growCapacity	I
    //   58: iadd
    //   59: anewarray 14	java/lang/Object
    //   62: astore 5
    //   64: iload_1
    //   65: iload_2
    //   66: if_icmpge +22 -> 88
    //   69: iload_2
    //   70: iload_1
    //   71: isub
    //   72: istore_3
    //   73: aload_0
    //   74: getfield 15	org/eclipse/jetty/util/BlockingArrayQueue:_elements	[Ljava/lang/Object;
    //   77: iload_1
    //   78: aload 5
    //   80: iconst_0
    //   81: iload_3
    //   82: invokestatic 69	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
    //   85: goto +62 -> 147
    //   88: iload_1
    //   89: iload_2
    //   90: if_icmpgt +13 -> 103
    //   93: aload_0
    //   94: getfield 9	org/eclipse/jetty/util/BlockingArrayQueue:_size	Ljava/util/concurrent/atomic/AtomicInteger;
    //   97: invokevirtual 26	java/util/concurrent/atomic/AtomicInteger:get	()I
    //   100: ifle +45 -> 145
    //   103: iload 4
    //   105: iload_2
    //   106: iadd
    //   107: iload_1
    //   108: isub
    //   109: istore_3
    //   110: iload 4
    //   112: iload_1
    //   113: isub
    //   114: istore 6
    //   116: aload_0
    //   117: getfield 15	org/eclipse/jetty/util/BlockingArrayQueue:_elements	[Ljava/lang/Object;
    //   120: iload_1
    //   121: aload 5
    //   123: iconst_0
    //   124: iload 6
    //   126: invokestatic 69	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
    //   129: aload_0
    //   130: getfield 15	org/eclipse/jetty/util/BlockingArrayQueue:_elements	[Ljava/lang/Object;
    //   133: iconst_0
    //   134: aload 5
    //   136: iload 6
    //   138: iload_2
    //   139: invokestatic 69	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
    //   142: goto +5 -> 147
    //   145: iconst_0
    //   146: istore_3
    //   147: aload_0
    //   148: aload 5
    //   150: putfield 15	org/eclipse/jetty/util/BlockingArrayQueue:_elements	[Ljava/lang/Object;
    //   153: aload_0
    //   154: getfield 3	org/eclipse/jetty/util/BlockingArrayQueue:_indexes	[I
    //   157: getstatic 23	org/eclipse/jetty/util/BlockingArrayQueue:HEAD_OFFSET	I
    //   160: iconst_0
    //   161: iastore
    //   162: aload_0
    //   163: getfield 3	org/eclipse/jetty/util/BlockingArrayQueue:_indexes	[I
    //   166: getstatic 2	org/eclipse/jetty/util/BlockingArrayQueue:TAIL_OFFSET	I
    //   169: iload_3
    //   170: iastore
    //   171: iconst_1
    //   172: istore 6
    //   174: aload_0
    //   175: getfield 10	org/eclipse/jetty/util/BlockingArrayQueue:_headLock	Ljava/util/concurrent/locks/Lock;
    //   178: invokeinterface 25 1 0
    //   183: aload_0
    //   184: getfield 6	org/eclipse/jetty/util/BlockingArrayQueue:_tailLock	Ljava/util/concurrent/locks/Lock;
    //   187: invokeinterface 25 1 0
    //   192: iload 6
    //   194: ireturn
    //   195: astore 7
    //   197: aload_0
    //   198: getfield 10	org/eclipse/jetty/util/BlockingArrayQueue:_headLock	Ljava/util/concurrent/locks/Lock;
    //   201: invokeinterface 25 1 0
    //   206: aload 7
    //   208: athrow
    //   209: astore 8
    //   211: aload_0
    //   212: getfield 6	org/eclipse/jetty/util/BlockingArrayQueue:_tailLock	Ljava/util/concurrent/locks/Lock;
    //   215: invokeinterface 25 1 0
    //   220: aload 8
    //   222: athrow
    // Line number table:
    //   Java source line #766	-> byte code offset #0
    //   Java source line #767	-> byte code offset #7
    //   Java source line #769	-> byte code offset #9
    //   Java source line #773	-> byte code offset #18
    //   Java source line #776	-> byte code offset #27
    //   Java source line #777	-> byte code offset #36
    //   Java source line #779	-> byte code offset #45
    //   Java source line #781	-> byte code offset #52
    //   Java source line #783	-> byte code offset #64
    //   Java source line #785	-> byte code offset #69
    //   Java source line #786	-> byte code offset #73
    //   Java source line #788	-> byte code offset #88
    //   Java source line #790	-> byte code offset #103
    //   Java source line #791	-> byte code offset #110
    //   Java source line #792	-> byte code offset #116
    //   Java source line #793	-> byte code offset #129
    //   Java source line #794	-> byte code offset #142
    //   Java source line #797	-> byte code offset #145
    //   Java source line #800	-> byte code offset #147
    //   Java source line #801	-> byte code offset #153
    //   Java source line #802	-> byte code offset #162
    //   Java source line #803	-> byte code offset #171
    //   Java source line #807	-> byte code offset #174
    //   Java source line #812	-> byte code offset #183
    //   Java source line #803	-> byte code offset #192
    //   Java source line #807	-> byte code offset #195
    //   Java source line #812	-> byte code offset #209
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	223	0	this	BlockingArrayQueue<E>
    //   35	86	1	head	int
    //   44	95	2	tail	int
    //   72	10	3	newTail	int
    //   109	2	3	newTail	int
    //   146	24	3	newTail	int
    //   50	61	4	capacity	int
    //   62	87	5	elements	Object[]
    //   114	79	6	cut	int
    //   195	12	7	localObject1	Object
    //   209	12	8	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   27	174	195	finally
    //   195	197	195	finally
    //   18	183	209	finally
    //   195	211	209	finally
  }
  
  private class Itr
    implements ListIterator<E>
  {
    private final Object[] _elements;
    private int _cursor;
    
    public Itr(Object[] elements, int offset)
    {
      _elements = elements;
      _cursor = offset;
    }
    

    public boolean hasNext()
    {
      return _cursor < _elements.length;
    }
    


    public E next()
    {
      return _elements[(_cursor++)];
    }
    

    public boolean hasPrevious()
    {
      return _cursor > 0;
    }
    


    public E previous()
    {
      return _elements[(--_cursor)];
    }
    

    public int nextIndex()
    {
      return _cursor + 1;
    }
    

    public int previousIndex()
    {
      return _cursor - 1;
    }
    

    public void remove()
    {
      throw new UnsupportedOperationException();
    }
    

    public void set(E e)
    {
      throw new UnsupportedOperationException();
    }
    

    public void add(E e)
    {
      throw new UnsupportedOperationException();
    }
  }
}
