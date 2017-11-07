package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
















@GwtCompatible(emulated=true)
abstract class CollectionFuture<V, C>
  extends AggregateFuture<V, C>
{
  CollectionFuture() {}
  
  abstract class CollectionFutureRunningState
    extends AggregateFuture<V, C>.RunningState
  {
    private List<Optional<V>> values;
    
    CollectionFutureRunningState(boolean futures)
    {
      super(futures, allMustSucceed, true);
      



      values = (futures.isEmpty() ? ImmutableList.of() : Lists.newArrayListWithCapacity(futures.size()));
      

      for (int i = 0; i < futures.size(); i++) {
        values.add(null);
      }
    }
    
    final void collectOneValue(boolean allMustSucceed, int index, @Nullable V returnValue)
    {
      List<Optional<V>> localValues = values;
      
      if (localValues != null) {
        localValues.set(index, Optional.fromNullable(returnValue));

      }
      else
      {
        Preconditions.checkState((allMustSucceed) || 
          (isCancelled()), "Future was done before all dependencies completed");
      }
    }
    
    final void handleAllCompleted()
    {
      List<Optional<V>> localValues = values;
      if (localValues != null) {
        set(combine(localValues));
      } else {
        Preconditions.checkState(isDone());
      }
    }
    
    void releaseResourcesAfterFailure()
    {
      super.releaseResourcesAfterFailure();
      values = null;
    }
    
    abstract C combine(List<Optional<V>> paramList);
  }
  
  static final class ListFuture<V>
    extends CollectionFuture<V, List<V>>
  {
    ListFuture(ImmutableCollection<? extends ListenableFuture<? extends V>> futures, boolean allMustSucceed)
    {
      init(new ListFutureRunningState(futures, allMustSucceed));
    }
    
    private final class ListFutureRunningState extends CollectionFuture<V, List<V>>.CollectionFutureRunningState
    {
      ListFutureRunningState(boolean futures)
      {
        super(futures, allMustSucceed);
      }
      
      public List<V> combine(List<Optional<V>> values)
      {
        List<V> result = Lists.newArrayListWithCapacity(values.size());
        for (Optional<V> element : values) {
          result.add(element != null ? element.orNull() : null);
        }
        return Collections.unmodifiableList(result);
      }
    }
  }
}
