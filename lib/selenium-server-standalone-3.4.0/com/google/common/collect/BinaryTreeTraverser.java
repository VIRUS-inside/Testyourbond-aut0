package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Deque;
import java.util.Iterator;
import java.util.function.Consumer;

































@Beta
@GwtCompatible
public abstract class BinaryTreeTraverser<T>
  extends TreeTraverser<T>
{
  public BinaryTreeTraverser() {}
  
  public abstract Optional<T> leftChild(T paramT);
  
  public abstract Optional<T> rightChild(T paramT);
  
  public final Iterable<T> children(final T root)
  {
    Preconditions.checkNotNull(root);
    new FluentIterable()
    {
      public Iterator<T> iterator() {
        new AbstractIterator()
        {
          boolean doneLeft;
          boolean doneRight;
          
          protected T computeNext() {
            if (!doneLeft) {
              doneLeft = true;
              Optional<T> left = leftChild(val$root);
              if (left.isPresent()) {
                return left.get();
              }
            }
            if (!doneRight) {
              doneRight = true;
              Optional<T> right = rightChild(val$root);
              if (right.isPresent()) {
                return right.get();
              }
            }
            return endOfData();
          }
        };
      }
      
      public void forEach(Consumer<? super T> action)
      {
        BinaryTreeTraverser.acceptIfPresent(action, leftChild(root));
        BinaryTreeTraverser.acceptIfPresent(action, rightChild(root));
      }
    };
  }
  
  UnmodifiableIterator<T> preOrderIterator(T root)
  {
    return new PreOrderIterator(root);
  }
  
  private final class PreOrderIterator
    extends UnmodifiableIterator<T>
    implements PeekingIterator<T>
  {
    private final Deque<T> stack;
    
    PreOrderIterator()
    {
      stack = new ArrayDeque(8);
      stack.addLast(root);
    }
    
    public boolean hasNext()
    {
      return !stack.isEmpty();
    }
    
    public T next()
    {
      T result = stack.removeLast();
      BinaryTreeTraverser.pushIfPresent(stack, rightChild(result));
      BinaryTreeTraverser.pushIfPresent(stack, leftChild(result));
      return result;
    }
    
    public T peek()
    {
      return stack.getLast();
    }
  }
  
  UnmodifiableIterator<T> postOrderIterator(T root)
  {
    return new PostOrderIterator(root);
  }
  
  private final class PostOrderIterator
    extends UnmodifiableIterator<T>
  {
    private final Deque<T> stack;
    private final BitSet hasExpanded;
    
    PostOrderIterator()
    {
      stack = new ArrayDeque(8);
      stack.addLast(root);
      hasExpanded = new BitSet();
    }
    
    public boolean hasNext()
    {
      return !stack.isEmpty();
    }
    
    public T next()
    {
      for (;;) {
        T node = stack.getLast();
        boolean expandedNode = hasExpanded.get(stack.size() - 1);
        if (expandedNode) {
          stack.removeLast();
          hasExpanded.clear(stack.size());
          return node;
        }
        hasExpanded.set(stack.size() - 1);
        BinaryTreeTraverser.pushIfPresent(stack, rightChild(node));
        BinaryTreeTraverser.pushIfPresent(stack, leftChild(node));
      }
    }
  }
  


  public final FluentIterable<T> inOrderTraversal(final T root)
  {
    Preconditions.checkNotNull(root);
    new FluentIterable()
    {
      public UnmodifiableIterator<T> iterator() {
        return new BinaryTreeTraverser.InOrderIterator(BinaryTreeTraverser.this, root);
      }
      
      public void forEach(final Consumer<? super T> action)
      {
        Preconditions.checkNotNull(action);
        new Consumer()
        {
          public void accept(T t) {
            BinaryTreeTraverser.acceptIfPresent(this, leftChild(t));
            action.accept(t);
            BinaryTreeTraverser.acceptIfPresent(this, rightChild(t)); } }
        
          .accept(root);
      }
    };
  }
  
  private final class InOrderIterator extends AbstractIterator<T> {
    private final Deque<T> stack;
    private final BitSet hasExpandedLeft;
    
    InOrderIterator() {
      stack = new ArrayDeque(8);
      hasExpandedLeft = new BitSet();
      stack.addLast(root);
    }
    
    protected T computeNext()
    {
      while (!stack.isEmpty()) {
        T node = stack.getLast();
        if (hasExpandedLeft.get(stack.size() - 1)) {
          stack.removeLast();
          hasExpandedLeft.clear(stack.size());
          BinaryTreeTraverser.pushIfPresent(stack, rightChild(node));
          return node;
        }
        hasExpandedLeft.set(stack.size() - 1);
        BinaryTreeTraverser.pushIfPresent(stack, leftChild(node));
      }
      
      return endOfData();
    }
  }
  
  private static <T> void pushIfPresent(Deque<T> stack, Optional<T> node) {
    if (node.isPresent()) {
      stack.addLast(node.get());
    }
  }
  
  private static <T> void acceptIfPresent(Consumer<? super T> action, Optional<T> node) {
    if (node.isPresent()) {
      action.accept(node.get());
    }
  }
}
