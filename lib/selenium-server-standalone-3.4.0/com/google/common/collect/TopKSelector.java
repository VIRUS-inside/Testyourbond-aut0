package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;







































@GwtCompatible
final class TopKSelector<T>
{
  private final int k;
  private final Comparator<? super T> comparator;
  private final T[] buffer;
  private int bufferSize;
  private T threshold;
  
  public static <T extends Comparable<? super T>> TopKSelector<T> least(int k)
  {
    return least(k, Ordering.natural());
  }
  






  public static <T extends Comparable<? super T>> TopKSelector<T> greatest(int k)
  {
    return greatest(k, Ordering.natural());
  }
  





  public static <T> TopKSelector<T> least(int k, Comparator<? super T> comparator)
  {
    return new TopKSelector(comparator, k);
  }
  





  public static <T> TopKSelector<T> greatest(int k, Comparator<? super T> comparator)
  {
    return new TopKSelector(Ordering.from(comparator).reverse(), k);
  }
  
















  private TopKSelector(Comparator<? super T> comparator, int k)
  {
    this.comparator = ((Comparator)Preconditions.checkNotNull(comparator, "comparator"));
    this.k = k;
    Preconditions.checkArgument(k >= 0, "k must be nonnegative, was %s", k);
    buffer = ((Object[])new Object[k * 2]);
    bufferSize = 0;
    threshold = null;
  }
  



  public void offer(@Nullable T elem)
  {
    if (k == 0)
      return;
    if (bufferSize == 0) {
      buffer[0] = elem;
      threshold = elem;
      bufferSize = 1;
    } else if (bufferSize < k) {
      buffer[(bufferSize++)] = elem;
      if (comparator.compare(elem, threshold) > 0) {
        threshold = elem;
      }
    } else if (comparator.compare(elem, threshold) < 0)
    {
      buffer[(bufferSize++)] = elem;
      if (bufferSize == 2 * k) {
        trim();
      }
    }
  }
  



  private void trim()
  {
    int left = 0;
    int right = 2 * k - 1;
    
    int minThresholdPosition = 0;
    


    int iterations = 0;
    int maxIterations = IntMath.log2(right - left, RoundingMode.CEILING) * 3;
    while (left < right) {
      int pivotIndex = left + right + 1 >>> 1;
      
      int pivotNewIndex = partition(left, right, pivotIndex);
      
      if (pivotNewIndex > k) {
        right = pivotNewIndex - 1;
      } else { if (pivotNewIndex >= k) break;
        left = Math.max(pivotNewIndex, left + 1);
        minThresholdPosition = pivotNewIndex;
      }
      

      iterations++;
      if (iterations >= maxIterations)
      {
        Arrays.sort(buffer, left, right, comparator);
        break;
      }
    }
    bufferSize = k;
    
    threshold = buffer[minThresholdPosition];
    for (int i = minThresholdPosition + 1; i < k; i++) {
      if (comparator.compare(buffer[i], threshold) > 0) {
        threshold = buffer[i];
      }
    }
  }
  





  private int partition(int left, int right, int pivotIndex)
  {
    T pivotValue = buffer[pivotIndex];
    buffer[pivotIndex] = buffer[right];
    
    int pivotNewIndex = left;
    for (int i = left; i < right; i++) {
      if (comparator.compare(buffer[i], pivotValue) < 0) {
        swap(pivotNewIndex, i);
        pivotNewIndex++;
      }
    }
    buffer[right] = buffer[pivotNewIndex];
    buffer[pivotNewIndex] = pivotValue;
    return pivotNewIndex;
  }
  
  private void swap(int i, int j) {
    T tmp = buffer[i];
    buffer[i] = buffer[j];
    buffer[j] = tmp;
  }
  
  TopKSelector<T> combine(TopKSelector<T> other) {
    for (int i = 0; i < bufferSize; i++) {
      offer(buffer[i]);
    }
    return this;
  }
  







  public void offerAll(Iterable<? extends T> elements)
  {
    offerAll(elements.iterator());
  }
  








  public void offerAll(Iterator<? extends T> elements)
  {
    while (elements.hasNext()) {
      offer(elements.next());
    }
  }
  







  public List<T> topK()
  {
    Arrays.sort(buffer, 0, bufferSize, comparator);
    if (bufferSize > k) {
      Arrays.fill(buffer, k, buffer.length, null);
      bufferSize = k;
      threshold = buffer[(k - 1)];
    }
    
    return Collections.unmodifiableList(Arrays.asList(Arrays.copyOf(buffer, bufferSize)));
  }
}
