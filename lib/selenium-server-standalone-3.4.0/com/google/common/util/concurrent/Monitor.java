package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.j2objc.annotations.Weak;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BooleanSupplier;
import javax.annotation.concurrent.GuardedBy;
























































































































































































































































































@Beta
@GwtIncompatible
public final class Monitor
{
  private final boolean fair;
  private final ReentrantLock lock;
  
  @Beta
  public static abstract class Guard
  {
    @Weak
    final Monitor monitor;
    final Condition condition;
    @GuardedBy("monitor.lock")
    int waiterCount = 0;
    
    @GuardedBy("monitor.lock")
    Guard next;
    

    protected Guard(Monitor monitor)
    {
      this.monitor = ((Monitor)Preconditions.checkNotNull(monitor, "monitor"));
      condition = lock.newCondition();
    }
    









    public abstract boolean isSatisfied();
  }
  









  @GuardedBy("lock")
  private Guard activeGuards = null;
  




  public Monitor()
  {
    this(false);
  }
  





  public Monitor(boolean fair)
  {
    this.fair = fair;
    lock = new ReentrantLock(fair);
  }
  




  public Guard newGuard(final BooleanSupplier isSatisfied)
  {
    Preconditions.checkNotNull(isSatisfied, "isSatisfied");
    new Guard(this)
    {
      public boolean isSatisfied() {
        return isSatisfied.getAsBoolean();
      }
    };
  }
  


  public void enter()
  {
    lock.lock();
  }
  



  public void enterInterruptibly()
    throws InterruptedException
  {
    lock.lockInterruptibly();
  }
  
  /* Error */
  public boolean enter(long time, TimeUnit unit)
  {
    // Byte code:
    //   0: lload_1
    //   1: aload_3
    //   2: invokestatic 14	com/google/common/util/concurrent/Monitor:toSafeNanos	(JLjava/util/concurrent/TimeUnit;)J
    //   5: lstore 4
    //   7: aload_0
    //   8: getfield 1	com/google/common/util/concurrent/Monitor:lock	Ljava/util/concurrent/locks/ReentrantLock;
    //   11: astore 6
    //   13: aload_0
    //   14: getfield 5	com/google/common/util/concurrent/Monitor:fair	Z
    //   17: ifne +13 -> 30
    //   20: aload 6
    //   22: invokevirtual 15	java/util/concurrent/locks/ReentrantLock:tryLock	()Z
    //   25: ifeq +5 -> 30
    //   28: iconst_1
    //   29: ireturn
    //   30: invokestatic 16	java/lang/Thread:interrupted	()Z
    //   33: istore 7
    //   35: invokestatic 17	java/lang/System:nanoTime	()J
    //   38: lstore 8
    //   40: lload 4
    //   42: lstore 10
    //   44: aload 6
    //   46: lload 10
    //   48: getstatic 18	java/util/concurrent/TimeUnit:NANOSECONDS	Ljava/util/concurrent/TimeUnit;
    //   51: invokevirtual 19	java/util/concurrent/locks/ReentrantLock:tryLock	(JLjava/util/concurrent/TimeUnit;)Z
    //   54: istore 12
    //   56: iload 7
    //   58: ifeq +9 -> 67
    //   61: invokestatic 20	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   64: invokevirtual 21	java/lang/Thread:interrupt	()V
    //   67: iload 12
    //   69: ireturn
    //   70: astore 12
    //   72: iconst_1
    //   73: istore 7
    //   75: lload 8
    //   77: lload 4
    //   79: invokestatic 23	com/google/common/util/concurrent/Monitor:remainingNanos	(JJ)J
    //   82: lstore 10
    //   84: goto -40 -> 44
    //   87: astore 13
    //   89: iload 7
    //   91: ifeq +9 -> 100
    //   94: invokestatic 20	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   97: invokevirtual 21	java/lang/Thread:interrupt	()V
    //   100: aload 13
    //   102: athrow
    // Line number table:
    //   Java source line #400	-> byte code offset #0
    //   Java source line #401	-> byte code offset #7
    //   Java source line #402	-> byte code offset #13
    //   Java source line #403	-> byte code offset #28
    //   Java source line #405	-> byte code offset #30
    //   Java source line #407	-> byte code offset #35
    //   Java source line #408	-> byte code offset #40
    //   Java source line #410	-> byte code offset #44
    //   Java source line #417	-> byte code offset #56
    //   Java source line #418	-> byte code offset #61
    //   Java source line #410	-> byte code offset #67
    //   Java source line #411	-> byte code offset #70
    //   Java source line #412	-> byte code offset #72
    //   Java source line #413	-> byte code offset #75
    //   Java source line #414	-> byte code offset #84
    //   Java source line #417	-> byte code offset #87
    //   Java source line #418	-> byte code offset #94
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	103	0	this	Monitor
    //   0	103	1	time	long
    //   0	103	3	unit	TimeUnit
    //   5	73	4	timeoutNanos	long
    //   11	34	6	lock	ReentrantLock
    //   33	57	7	interrupted	boolean
    //   38	38	8	startTime	long
    //   42	41	10	remainingNanos	long
    //   54	14	12	bool1	boolean
    //   70	3	12	interrupt	InterruptedException
    //   87	14	13	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   44	56	70	java/lang/InterruptedException
    //   35	56	87	finally
    //   70	89	87	finally
  }
  
  public boolean enterInterruptibly(long time, TimeUnit unit)
    throws InterruptedException
  {
    return lock.tryLock(time, unit);
  }
  






  public boolean tryEnter()
  {
    return lock.tryLock();
  }
  



  public void enterWhen(Guard guard)
    throws InterruptedException
  {
    if (monitor != this) {
      throw new IllegalMonitorStateException();
    }
    ReentrantLock lock = this.lock;
    boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
    lock.lockInterruptibly();
    
    boolean satisfied = false;
    try {
      if (!guard.isSatisfied()) {
        await(guard, signalBeforeWaiting);
      }
      satisfied = true;
    } finally {
      if (!satisfied) {
        leave();
      }
    }
  }
  


  public void enterWhenUninterruptibly(Guard guard)
  {
    if (monitor != this) {
      throw new IllegalMonitorStateException();
    }
    ReentrantLock lock = this.lock;
    boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
    lock.lock();
    
    boolean satisfied = false;
    try {
      if (!guard.isSatisfied()) {
        awaitUninterruptibly(guard, signalBeforeWaiting);
      }
      satisfied = true;
    } finally {
      if (!satisfied) {
        leave();
      }
    }
  }
  






  public boolean enterWhen(Guard guard, long time, TimeUnit unit)
    throws InterruptedException
  {
    long timeoutNanos = toSafeNanos(time, unit);
    if (monitor != this) {
      throw new IllegalMonitorStateException();
    }
    ReentrantLock lock = this.lock;
    boolean reentrant = lock.isHeldByCurrentThread();
    long startTime = 0L;
    


    if (!fair)
    {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      if (lock.tryLock()) {}
    }
    else
    {
      startTime = initNanoTime(timeoutNanos);
      if (!lock.tryLock(time, unit)) {
        return false;
      }
    }
    
    boolean satisfied = false;
    boolean threw = true;
    try
    {
      if (!guard.isSatisfied()) {}
      satisfied = awaitNanos(guard, startTime == 0L ? timeoutNanos : 
      
        remainingNanos(startTime, timeoutNanos), reentrant);
      
      threw = false;
      return satisfied;
    } finally {
      if (!satisfied) {
        try
        {
          if ((threw) && (!reentrant)) {
            signalNextWaiter();
          }
        } finally {
          lock.unlock();
        }
      }
    }
  }
  
  /* Error */
  public boolean enterWhenUninterruptibly(Guard guard, long time, TimeUnit unit)
  {
    // Byte code:
    //   0: lload_2
    //   1: aload 4
    //   3: invokestatic 14	com/google/common/util/concurrent/Monitor:toSafeNanos	(JLjava/util/concurrent/TimeUnit;)J
    //   6: lstore 5
    //   8: aload_1
    //   9: getfield 24	com/google/common/util/concurrent/Monitor$Guard:monitor	Lcom/google/common/util/concurrent/Monitor;
    //   12: aload_0
    //   13: if_acmpeq +11 -> 24
    //   16: new 25	java/lang/IllegalMonitorStateException
    //   19: dup
    //   20: invokespecial 26	java/lang/IllegalMonitorStateException:<init>	()V
    //   23: athrow
    //   24: aload_0
    //   25: getfield 1	com/google/common/util/concurrent/Monitor:lock	Ljava/util/concurrent/locks/ReentrantLock;
    //   28: astore 7
    //   30: lconst_0
    //   31: lstore 8
    //   33: aload 7
    //   35: invokevirtual 27	java/util/concurrent/locks/ReentrantLock:isHeldByCurrentThread	()Z
    //   38: istore 10
    //   40: invokestatic 16	java/lang/Thread:interrupted	()Z
    //   43: istore 11
    //   45: aload_0
    //   46: getfield 5	com/google/common/util/concurrent/Monitor:fair	Z
    //   49: ifne +11 -> 60
    //   52: aload 7
    //   54: invokevirtual 15	java/util/concurrent/locks/ReentrantLock:tryLock	()Z
    //   57: ifne +64 -> 121
    //   60: lload 5
    //   62: invokestatic 33	com/google/common/util/concurrent/Monitor:initNanoTime	(J)J
    //   65: lstore 8
    //   67: lload 5
    //   69: lstore 12
    //   71: aload 7
    //   73: lload 12
    //   75: getstatic 18	java/util/concurrent/TimeUnit:NANOSECONDS	Ljava/util/concurrent/TimeUnit;
    //   78: invokevirtual 19	java/util/concurrent/locks/ReentrantLock:tryLock	(JLjava/util/concurrent/TimeUnit;)Z
    //   81: ifeq +6 -> 87
    //   84: goto +37 -> 121
    //   87: iconst_0
    //   88: istore 14
    //   90: iload 11
    //   92: ifeq +9 -> 101
    //   95: invokestatic 20	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   98: invokevirtual 21	java/lang/Thread:interrupt	()V
    //   101: iload 14
    //   103: ireturn
    //   104: astore 14
    //   106: iconst_1
    //   107: istore 11
    //   109: lload 8
    //   111: lload 5
    //   113: invokestatic 23	com/google/common/util/concurrent/Monitor:remainingNanos	(JJ)J
    //   116: lstore 12
    //   118: goto -47 -> 71
    //   121: iconst_0
    //   122: istore 12
    //   124: aload_1
    //   125: invokevirtual 28	com/google/common/util/concurrent/Monitor$Guard:isSatisfied	()Z
    //   128: ifeq +9 -> 137
    //   131: iconst_1
    //   132: istore 12
    //   134: goto +44 -> 178
    //   137: lload 8
    //   139: lconst_0
    //   140: lcmp
    //   141: ifne +17 -> 158
    //   144: lload 5
    //   146: invokestatic 33	com/google/common/util/concurrent/Monitor:initNanoTime	(J)J
    //   149: lstore 8
    //   151: lload 5
    //   153: lstore 13
    //   155: goto +12 -> 167
    //   158: lload 8
    //   160: lload 5
    //   162: invokestatic 23	com/google/common/util/concurrent/Monitor:remainingNanos	(JJ)J
    //   165: lstore 13
    //   167: aload_0
    //   168: aload_1
    //   169: lload 13
    //   171: iload 10
    //   173: invokespecial 34	com/google/common/util/concurrent/Monitor:awaitNanos	(Lcom/google/common/util/concurrent/Monitor$Guard;JZ)Z
    //   176: istore 12
    //   178: iload 12
    //   180: istore 13
    //   182: iload 12
    //   184: ifne +8 -> 192
    //   187: aload 7
    //   189: invokevirtual 36	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   192: iload 11
    //   194: ifeq +9 -> 203
    //   197: invokestatic 20	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   200: invokevirtual 21	java/lang/Thread:interrupt	()V
    //   203: iload 13
    //   205: ireturn
    //   206: astore 13
    //   208: iconst_1
    //   209: istore 11
    //   211: iconst_0
    //   212: istore 10
    //   214: goto -90 -> 124
    //   217: astore 15
    //   219: iload 12
    //   221: ifne +8 -> 229
    //   224: aload 7
    //   226: invokevirtual 36	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   229: aload 15
    //   231: athrow
    //   232: astore 16
    //   234: iload 11
    //   236: ifeq +9 -> 245
    //   239: invokestatic 20	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   242: invokevirtual 21	java/lang/Thread:interrupt	()V
    //   245: aload 16
    //   247: athrow
    // Line number table:
    //   Java source line #560	-> byte code offset #0
    //   Java source line #561	-> byte code offset #8
    //   Java source line #562	-> byte code offset #16
    //   Java source line #564	-> byte code offset #24
    //   Java source line #565	-> byte code offset #30
    //   Java source line #566	-> byte code offset #33
    //   Java source line #567	-> byte code offset #40
    //   Java source line #569	-> byte code offset #45
    //   Java source line #570	-> byte code offset #60
    //   Java source line #571	-> byte code offset #67
    //   Java source line #573	-> byte code offset #71
    //   Java source line #574	-> byte code offset #84
    //   Java source line #576	-> byte code offset #87
    //   Java source line #613	-> byte code offset #90
    //   Java source line #614	-> byte code offset #95
    //   Java source line #576	-> byte code offset #101
    //   Java source line #578	-> byte code offset #104
    //   Java source line #579	-> byte code offset #106
    //   Java source line #580	-> byte code offset #109
    //   Java source line #581	-> byte code offset #118
    //   Java source line #585	-> byte code offset #121
    //   Java source line #589	-> byte code offset #124
    //   Java source line #590	-> byte code offset #131
    //   Java source line #593	-> byte code offset #137
    //   Java source line #594	-> byte code offset #144
    //   Java source line #595	-> byte code offset #151
    //   Java source line #597	-> byte code offset #158
    //   Java source line #599	-> byte code offset #167
    //   Java source line #601	-> byte code offset #178
    //   Java source line #608	-> byte code offset #182
    //   Java source line #609	-> byte code offset #187
    //   Java source line #613	-> byte code offset #192
    //   Java source line #614	-> byte code offset #197
    //   Java source line #601	-> byte code offset #203
    //   Java source line #602	-> byte code offset #206
    //   Java source line #603	-> byte code offset #208
    //   Java source line #604	-> byte code offset #211
    //   Java source line #605	-> byte code offset #214
    //   Java source line #608	-> byte code offset #217
    //   Java source line #609	-> byte code offset #224
    //   Java source line #613	-> byte code offset #232
    //   Java source line #614	-> byte code offset #239
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	248	0	this	Monitor
    //   0	248	1	guard	Guard
    //   0	248	2	time	long
    //   0	248	4	unit	TimeUnit
    //   6	155	5	timeoutNanos	long
    //   28	197	7	lock	ReentrantLock
    //   31	128	8	startTime	long
    //   38	175	10	signalBeforeWaiting	boolean
    //   43	192	11	interrupted	boolean
    //   69	48	12	remainingNanos	long
    //   122	98	12	satisfied	boolean
    //   153	3	13	remainingNanos	long
    //   165	39	13	remainingNanos	long
    //   206	3	13	interrupt	InterruptedException
    //   88	14	14	bool1	boolean
    //   104	3	14	interrupt	InterruptedException
    //   217	13	15	localObject1	Object
    //   232	14	16	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   71	84	104	java/lang/InterruptedException
    //   87	90	104	java/lang/InterruptedException
    //   124	182	206	java/lang/InterruptedException
    //   124	182	217	finally
    //   206	219	217	finally
    //   45	90	232	finally
    //   104	192	232	finally
    //   206	234	232	finally
  }
  
  public boolean enterIf(Guard guard)
  {
    if (monitor != this) {
      throw new IllegalMonitorStateException();
    }
    ReentrantLock lock = this.lock;
    lock.lock();
    
    boolean satisfied = false;
    try {
      return satisfied = guard.isSatisfied();
    } finally {
      if (!satisfied) {
        lock.unlock();
      }
    }
  }
  





  public boolean enterIfInterruptibly(Guard guard)
    throws InterruptedException
  {
    if (monitor != this) {
      throw new IllegalMonitorStateException();
    }
    ReentrantLock lock = this.lock;
    lock.lockInterruptibly();
    
    boolean satisfied = false;
    try {
      return satisfied = guard.isSatisfied();
    } finally {
      if (!satisfied) {
        lock.unlock();
      }
    }
  }
  





  public boolean enterIf(Guard guard, long time, TimeUnit unit)
  {
    if (monitor != this) {
      throw new IllegalMonitorStateException();
    }
    if (!enter(time, unit)) {
      return false;
    }
    
    boolean satisfied = false;
    try {
      return satisfied = guard.isSatisfied();
    } finally {
      if (!satisfied) {
        lock.unlock();
      }
    }
  }
  





  public boolean enterIfInterruptibly(Guard guard, long time, TimeUnit unit)
    throws InterruptedException
  {
    if (monitor != this) {
      throw new IllegalMonitorStateException();
    }
    ReentrantLock lock = this.lock;
    if (!lock.tryLock(time, unit)) {
      return false;
    }
    
    boolean satisfied = false;
    try {
      return satisfied = guard.isSatisfied();
    } finally {
      if (!satisfied) {
        lock.unlock();
      }
    }
  }
  







  public boolean tryEnterIf(Guard guard)
  {
    if (monitor != this) {
      throw new IllegalMonitorStateException();
    }
    ReentrantLock lock = this.lock;
    if (!lock.tryLock()) {
      return false;
    }
    
    boolean satisfied = false;
    try {
      return satisfied = guard.isSatisfied();
    } finally {
      if (!satisfied) {
        lock.unlock();
      }
    }
  }
  




  public void waitFor(Guard guard)
    throws InterruptedException
  {
    if (!(monitor == this & lock.isHeldByCurrentThread())) {
      throw new IllegalMonitorStateException();
    }
    if (!guard.isSatisfied()) {
      await(guard, true);
    }
  }
  



  public void waitForUninterruptibly(Guard guard)
  {
    if (!(monitor == this & lock.isHeldByCurrentThread())) {
      throw new IllegalMonitorStateException();
    }
    if (!guard.isSatisfied()) {
      awaitUninterruptibly(guard, true);
    }
  }
  





  public boolean waitFor(Guard guard, long time, TimeUnit unit)
    throws InterruptedException
  {
    long timeoutNanos = toSafeNanos(time, unit);
    if (!(monitor == this & lock.isHeldByCurrentThread())) {
      throw new IllegalMonitorStateException();
    }
    if (guard.isSatisfied()) {
      return true;
    }
    if (Thread.interrupted()) {
      throw new InterruptedException();
    }
    return awaitNanos(guard, timeoutNanos, true);
  }
  
  /* Error */
  public boolean waitForUninterruptibly(Guard guard, long time, TimeUnit unit)
  {
    // Byte code:
    //   0: lload_2
    //   1: aload 4
    //   3: invokestatic 14	com/google/common/util/concurrent/Monitor:toSafeNanos	(JLjava/util/concurrent/TimeUnit;)J
    //   6: lstore 5
    //   8: aload_1
    //   9: getfield 24	com/google/common/util/concurrent/Monitor$Guard:monitor	Lcom/google/common/util/concurrent/Monitor;
    //   12: aload_0
    //   13: if_acmpne +7 -> 20
    //   16: iconst_1
    //   17: goto +4 -> 21
    //   20: iconst_0
    //   21: aload_0
    //   22: getfield 1	com/google/common/util/concurrent/Monitor:lock	Ljava/util/concurrent/locks/ReentrantLock;
    //   25: invokevirtual 27	java/util/concurrent/locks/ReentrantLock:isHeldByCurrentThread	()Z
    //   28: iand
    //   29: ifne +11 -> 40
    //   32: new 25	java/lang/IllegalMonitorStateException
    //   35: dup
    //   36: invokespecial 26	java/lang/IllegalMonitorStateException:<init>	()V
    //   39: athrow
    //   40: aload_1
    //   41: invokevirtual 28	com/google/common/util/concurrent/Monitor$Guard:isSatisfied	()Z
    //   44: ifeq +5 -> 49
    //   47: iconst_1
    //   48: ireturn
    //   49: iconst_1
    //   50: istore 7
    //   52: lload 5
    //   54: invokestatic 33	com/google/common/util/concurrent/Monitor:initNanoTime	(J)J
    //   57: lstore 8
    //   59: invokestatic 16	java/lang/Thread:interrupted	()Z
    //   62: istore 10
    //   64: lload 5
    //   66: lstore 11
    //   68: aload_0
    //   69: aload_1
    //   70: lload 11
    //   72: iload 7
    //   74: invokespecial 34	com/google/common/util/concurrent/Monitor:awaitNanos	(Lcom/google/common/util/concurrent/Monitor$Guard;JZ)Z
    //   77: istore 13
    //   79: iload 10
    //   81: ifeq +9 -> 90
    //   84: invokestatic 20	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   87: invokevirtual 21	java/lang/Thread:interrupt	()V
    //   90: iload 13
    //   92: ireturn
    //   93: astore 13
    //   95: iconst_1
    //   96: istore 10
    //   98: aload_1
    //   99: invokevirtual 28	com/google/common/util/concurrent/Monitor$Guard:isSatisfied	()Z
    //   102: ifeq +20 -> 122
    //   105: iconst_1
    //   106: istore 14
    //   108: iload 10
    //   110: ifeq +9 -> 119
    //   113: invokestatic 20	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   116: invokevirtual 21	java/lang/Thread:interrupt	()V
    //   119: iload 14
    //   121: ireturn
    //   122: iconst_0
    //   123: istore 7
    //   125: lload 8
    //   127: lload 5
    //   129: invokestatic 23	com/google/common/util/concurrent/Monitor:remainingNanos	(JJ)J
    //   132: lstore 11
    //   134: goto -66 -> 68
    //   137: astore 15
    //   139: iload 10
    //   141: ifeq +9 -> 150
    //   144: invokestatic 20	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   147: invokevirtual 21	java/lang/Thread:interrupt	()V
    //   150: aload 15
    //   152: athrow
    // Line number table:
    //   Java source line #799	-> byte code offset #0
    //   Java source line #800	-> byte code offset #8
    //   Java source line #801	-> byte code offset #32
    //   Java source line #803	-> byte code offset #40
    //   Java source line #804	-> byte code offset #47
    //   Java source line #806	-> byte code offset #49
    //   Java source line #807	-> byte code offset #52
    //   Java source line #808	-> byte code offset #59
    //   Java source line #810	-> byte code offset #64
    //   Java source line #812	-> byte code offset #68
    //   Java source line #823	-> byte code offset #79
    //   Java source line #824	-> byte code offset #84
    //   Java source line #812	-> byte code offset #90
    //   Java source line #813	-> byte code offset #93
    //   Java source line #814	-> byte code offset #95
    //   Java source line #815	-> byte code offset #98
    //   Java source line #816	-> byte code offset #105
    //   Java source line #823	-> byte code offset #108
    //   Java source line #824	-> byte code offset #113
    //   Java source line #816	-> byte code offset #119
    //   Java source line #818	-> byte code offset #122
    //   Java source line #819	-> byte code offset #125
    //   Java source line #820	-> byte code offset #134
    //   Java source line #823	-> byte code offset #137
    //   Java source line #824	-> byte code offset #144
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	153	0	this	Monitor
    //   0	153	1	guard	Guard
    //   0	153	2	time	long
    //   0	153	4	unit	TimeUnit
    //   6	122	5	timeoutNanos	long
    //   50	74	7	signalBeforeWaiting	boolean
    //   57	69	8	startTime	long
    //   62	78	10	interrupted	boolean
    //   66	67	11	remainingNanos	long
    //   77	14	13	bool1	boolean
    //   93	3	13	interrupt	InterruptedException
    //   106	14	14	bool2	boolean
    //   137	14	15	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   68	79	93	java/lang/InterruptedException
    //   64	79	137	finally
    //   93	108	137	finally
    //   122	139	137	finally
  }
  
  public void leave()
  {
    ReentrantLock lock = this.lock;
    try
    {
      if (lock.getHoldCount() == 1) {
        signalNextWaiter();
      }
      
      lock.unlock(); } finally { lock.unlock();
    }
  }
  


  public boolean isFair()
  {
    return fair;
  }
  



  public boolean isOccupied()
  {
    return lock.isLocked();
  }
  



  public boolean isOccupiedByCurrentThread()
  {
    return lock.isHeldByCurrentThread();
  }
  



  public int getOccupiedDepth()
  {
    return lock.getHoldCount();
  }
  





  public int getQueueLength()
  {
    return lock.getQueueLength();
  }
  





  public boolean hasQueuedThreads()
  {
    return lock.hasQueuedThreads();
  }
  





  public boolean hasQueuedThread(Thread thread)
  {
    return lock.hasQueuedThread(thread);
  }
  





  public boolean hasWaiters(Guard guard)
  {
    return getWaitQueueLength(guard) > 0;
  }
  





  public int getWaitQueueLength(Guard guard)
  {
    if (monitor != this) {
      throw new IllegalMonitorStateException();
    }
    lock.lock();
    try {
      return waiterCount;
    } finally {
      lock.unlock();
    }
  }
  




  private static long toSafeNanos(long time, TimeUnit unit)
  {
    long timeoutNanos = unit.toNanos(time);
    return timeoutNanos > 6917529027641081853L ? 6917529027641081853L : timeoutNanos <= 0L ? 0L : timeoutNanos;
  }
  





  private static long initNanoTime(long timeoutNanos)
  {
    if (timeoutNanos <= 0L) {
      return 0L;
    }
    long startTime = System.nanoTime();
    return startTime == 0L ? 1L : startTime;
  }
  











  private static long remainingNanos(long startTime, long timeoutNanos)
  {
    return timeoutNanos <= 0L ? 0L : timeoutNanos - (System.nanoTime() - startTime);
  }
  























  @GuardedBy("lock")
  private void signalNextWaiter()
  {
    for (Guard guard = activeGuards; guard != null; guard = next) {
      if (isSatisfied(guard)) {
        condition.signal();
        break;
      }
    }
  }
  




















  @GuardedBy("lock")
  private boolean isSatisfied(Guard guard)
  {
    try
    {
      return guard.isSatisfied();
    } catch (Throwable throwable) {
      signalAllWaiters();
      throw Throwables.propagate(throwable);
    }
  }
  


  @GuardedBy("lock")
  private void signalAllWaiters()
  {
    for (Guard guard = activeGuards; guard != null; guard = next) {
      condition.signalAll();
    }
  }
  


  @GuardedBy("lock")
  private void beginWaitingFor(Guard guard)
  {
    int waiters = waiterCount++;
    if (waiters == 0)
    {
      next = activeGuards;
      activeGuards = guard;
    }
  }
  


  @GuardedBy("lock")
  private void endWaitingFor(Guard guard)
  {
    int waiters = --waiterCount;
    if (waiters == 0)
    {
      Guard p = activeGuards; for (Guard pred = null;; p = next) {
        if (p == guard) {
          if (pred == null) {
            activeGuards = next;
          } else {
            next = next;
          }
          next = null;
          break;
        }
        pred = p;
      }
    }
  }
  













  @GuardedBy("lock")
  private void await(Guard guard, boolean signalBeforeWaiting)
    throws InterruptedException
  {
    if (signalBeforeWaiting) {
      signalNextWaiter();
    }
    beginWaitingFor(guard);
    try {
      do {
        condition.await();
      } while (!guard.isSatisfied());
      
      endWaitingFor(guard); } finally { endWaitingFor(guard);
    }
  }
  
  @GuardedBy("lock")
  private void awaitUninterruptibly(Guard guard, boolean signalBeforeWaiting) {
    if (signalBeforeWaiting) {
      signalNextWaiter();
    }
    beginWaitingFor(guard);
    try {
      do {
        condition.awaitUninterruptibly();
      } while (!guard.isSatisfied());
      
      endWaitingFor(guard); } finally { endWaitingFor(guard);
    }
  }
  


  @GuardedBy("lock")
  private boolean awaitNanos(Guard guard, long nanos, boolean signalBeforeWaiting)
    throws InterruptedException
  {
    boolean firstTime = true;
    try {
      boolean bool1;
      do { if (nanos <= 0L) {
          return false;
        }
        if (firstTime) {
          if (signalBeforeWaiting) {
            signalNextWaiter();
          }
          beginWaitingFor(guard);
          firstTime = false;
        }
        nanos = condition.awaitNanos(nanos);
      } while (!guard.isSatisfied());
      return true;
    } finally {
      if (!firstTime) {
        endWaitingFor(guard);
      }
    }
  }
}
