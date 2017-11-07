package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;





















@GwtCompatible
abstract class AggregateFuture<InputT, OutputT>
  extends AbstractFuture.TrustedFuture<OutputT>
{
  private static final Logger logger = Logger.getLogger(AggregateFuture.class.getName());
  
  private AggregateFuture<InputT, OutputT>.RunningState runningState;
  

  AggregateFuture() {}
  

  protected final void afterDone()
  {
    super.afterDone();
    AggregateFuture<InputT, OutputT>.RunningState localRunningState = runningState;
    boolean wasInterrupted; UnmodifiableIterator localUnmodifiableIterator; if (localRunningState != null)
    {
      runningState = null;
      
      ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures = futures;
      wasInterrupted = wasInterrupted();
      
      if (wasInterrupted()) {
        localRunningState.interruptTask();
      }
      
      if ((isCancelled() & futures != null)) {
        for (localUnmodifiableIterator = futures.iterator(); localUnmodifiableIterator.hasNext();) { ListenableFuture<?> future = (ListenableFuture)localUnmodifiableIterator.next();
          future.cancel(wasInterrupted);
        }
      }
    }
  }
  


  final void init(AggregateFuture<InputT, OutputT>.RunningState runningState)
  {
    this.runningState = runningState;
    runningState.init();
  }
  
  abstract class RunningState
    extends AggregateFutureState implements Runnable
  {
    private ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures;
    private final boolean allMustSucceed;
    private final boolean collectsValues;
    
    RunningState(boolean futures, boolean allMustSucceed)
    {
      super();
      this.futures = ((ImmutableCollection)Preconditions.checkNotNull(futures));
      this.allMustSucceed = allMustSucceed;
      this.collectsValues = collectsValues;
    }
    

    public final void run()
    {
      decrementCountAndMaybeComplete();
    }
    






    private void init()
    {
      if (futures.isEmpty()) {
        handleAllCompleted(); return;
      }
      
      int i;
      
      UnmodifiableIterator localUnmodifiableIterator;
      
      if (allMustSucceed)
      {








        i = 0;
        for (localUnmodifiableIterator = futures.iterator(); localUnmodifiableIterator.hasNext();) { final ListenableFuture<? extends InputT> listenable = (ListenableFuture)localUnmodifiableIterator.next();
          final int index = i++;
          listenable.addListener(new Runnable()
          {
            public void run()
            {
              try {
                AggregateFuture.RunningState.this.handleOneInputDone(index, listenable);
                
                AggregateFuture.RunningState.this.decrementCountAndMaybeComplete(); } finally { AggregateFuture.RunningState.this.decrementCountAndMaybeComplete();
              }
              
            }
          }, MoreExecutors.directExecutor());
        }
      }
      else
      {
        for (i = futures.iterator(); i.hasNext();) { Object listenable = (ListenableFuture)i.next();
          ((ListenableFuture)listenable).addListener(this, MoreExecutors.directExecutor());
        }
      }
    }
    





    private void handleException(Throwable throwable)
    {
      Preconditions.checkNotNull(throwable);
      
      boolean completedWithFailure = false;
      boolean firstTimeSeeingThisException = true;
      if (allMustSucceed)
      {

        completedWithFailure = setException(throwable);
        if (completedWithFailure) {
          releaseResourcesAfterFailure();
        }
        else
        {
          firstTimeSeeingThisException = AggregateFuture.addCausalChain(getOrInitSeenExceptions(), throwable);
        }
      }
      

      if ((throwable instanceof Error | allMustSucceed & !completedWithFailure & firstTimeSeeingThisException))
      {
        String message = (throwable instanceof Error) ? "Input Future failed with Error" : "Got more than one input Future failure. Logging failures after the first";
        


        AggregateFuture.logger.log(Level.SEVERE, message, throwable);
      }
    }
    
    final void addInitialException(Set<Throwable> seen)
    {
      if (!isCancelled())
      {
        boolean bool = AggregateFuture.addCausalChain(seen, trustedGetException());
      }
    }
    




    private void handleOneInputDone(int index, Future<? extends InputT> future)
    {
      Preconditions.checkState((allMustSucceed) || 
        (!isDone()) || (isCancelled()), "Future was done before all dependencies completed");
      
      try
      {
        Preconditions.checkState(future.isDone(), "Tried to set value from future which is not done");
        if (allMustSucceed) {
          if (future.isCancelled())
          {

            runningState = null;
            cancel(false);
          }
          else {
            InputT result = Futures.getDone(future);
            if (collectsValues) {
              collectOneValue(allMustSucceed, index, result);
            }
          }
        } else if ((collectsValues) && (!future.isCancelled())) {
          collectOneValue(allMustSucceed, index, Futures.getDone(future));
        }
      } catch (ExecutionException e) {
        handleException(e.getCause());
      } catch (Throwable t) {
        handleException(t);
      }
    }
    
    private void decrementCountAndMaybeComplete() {
      int newRemaining = decrementRemainingAndGet();
      Preconditions.checkState(newRemaining >= 0, "Less than 0 remaining futures");
      if (newRemaining == 0) {
        processCompleted();
      }
    }
    
    private void processCompleted() {
      int i;
      UnmodifiableIterator localUnmodifiableIterator;
      if ((collectsValues & !allMustSucceed)) {
        i = 0;
        for (localUnmodifiableIterator = futures.iterator(); localUnmodifiableIterator.hasNext();) { ListenableFuture<? extends InputT> listenable = (ListenableFuture)localUnmodifiableIterator.next();
          handleOneInputDone(i++, listenable);
        }
      }
      handleAllCompleted();
    }
    








    void releaseResourcesAfterFailure()
    {
      futures = null;
    }
    
    abstract void collectOneValue(boolean paramBoolean, int paramInt, @Nullable InputT paramInputT);
    
    abstract void handleAllCompleted();
    
    void interruptTask() {}
  }
  
  private static boolean addCausalChain(Set<Throwable> seen, Throwable t)
  {
    for (; 
        




        t != null; t = t.getCause()) {
      boolean firstTimeSeen = seen.add(t);
      if (!firstTimeSeen)
      {





        return false;
      }
    }
    return true;
  }
}
