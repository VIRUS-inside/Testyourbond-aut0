package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Queue;
import java.util.function.Consumer;






























































@Beta
@GwtCompatible
public abstract class TreeTraverser<T>
{
  public TreeTraverser() {}
  
  public static <T> TreeTraverser<T> using(Function<T, ? extends Iterable<T>> nodeToChildrenFunction)
  {
    Preconditions.checkNotNull(nodeToChildrenFunction);
    new TreeTraverser()
    {
      public Iterable<T> children(T root) {
        return (Iterable)val$nodeToChildrenFunction.apply(root);
      }
    };
  }
  





  public abstract Iterable<T> children(T paramT);
  




  public final FluentIterable<T> preOrderTraversal(final T root)
  {
    Preconditions.checkNotNull(root);
    new FluentIterable()
    {
      public UnmodifiableIterator<T> iterator() {
        return preOrderIterator(root);
      }
      
      public void forEach(final Consumer<? super T> action)
      {
        Preconditions.checkNotNull(action);
        new Consumer()
        {
          public void accept(T t) {
            action.accept(t);
            children(t).forEach(this); } }
        
          .accept(root);
      }
    };
  }
  
  UnmodifiableIterator<T> preOrderIterator(T root)
  {
    return new PreOrderIterator(root);
  }
  
  private final class PreOrderIterator extends UnmodifiableIterator<T> {
    private final Deque<Iterator<T>> stack;
    
    PreOrderIterator() {
      stack = new ArrayDeque();
      stack.addLast(Iterators.singletonIterator(Preconditions.checkNotNull(root)));
    }
    
    public boolean hasNext()
    {
      return !stack.isEmpty();
    }
    
    public T next()
    {
      Iterator<T> itr = (Iterator)stack.getLast();
      T result = Preconditions.checkNotNull(itr.next());
      if (!itr.hasNext()) {
        stack.removeLast();
      }
      Iterator<T> childItr = children(result).iterator();
      if (childItr.hasNext()) {
        stack.addLast(childItr);
      }
      return result;
    }
  }
  






  public final FluentIterable<T> postOrderTraversal(final T root)
  {
    Preconditions.checkNotNull(root);
    new FluentIterable()
    {
      public UnmodifiableIterator<T> iterator() {
        return postOrderIterator(root);
      }
      
      public void forEach(final Consumer<? super T> action)
      {
        Preconditions.checkNotNull(action);
        new Consumer()
        {
          public void accept(T t) {
            children(t).forEach(this);
            action.accept(t); } }
        
          .accept(root);
      }
    };
  }
  
  UnmodifiableIterator<T> postOrderIterator(T root)
  {
    return new PostOrderIterator(root);
  }
  
  private static final class PostOrderNode<T> {
    final T root;
    final Iterator<T> childIterator;
    
    PostOrderNode(T root, Iterator<T> childIterator) {
      this.root = Preconditions.checkNotNull(root);
      this.childIterator = ((Iterator)Preconditions.checkNotNull(childIterator));
    }
  }
  
  private final class PostOrderIterator extends AbstractIterator<T> {
    private final ArrayDeque<TreeTraverser.PostOrderNode<T>> stack;
    
    PostOrderIterator() {
      stack = new ArrayDeque();
      stack.addLast(expand(root));
    }
    
    protected T computeNext()
    {
      while (!stack.isEmpty()) {
        TreeTraverser.PostOrderNode<T> top = (TreeTraverser.PostOrderNode)stack.getLast();
        if (childIterator.hasNext()) {
          T child = childIterator.next();
          stack.addLast(expand(child));
        } else {
          stack.removeLast();
          return root;
        }
      }
      return endOfData();
    }
    
    private TreeTraverser.PostOrderNode<T> expand(T t) {
      return new TreeTraverser.PostOrderNode(t, children(t).iterator());
    }
  }
  






  public final FluentIterable<T> breadthFirstTraversal(final T root)
  {
    Preconditions.checkNotNull(root);
    new FluentIterable()
    {
      public UnmodifiableIterator<T> iterator() {
        return new TreeTraverser.BreadthFirstIterator(TreeTraverser.this, root);
      }
    };
  }
  
  private final class BreadthFirstIterator extends UnmodifiableIterator<T> implements PeekingIterator<T>
  {
    private final Queue<T> queue;
    
    BreadthFirstIterator() {
      queue = new ArrayDeque();
      queue.add(root);
    }
    
    public boolean hasNext()
    {
      return !queue.isEmpty();
    }
    
    public T peek()
    {
      return queue.element();
    }
    
    public T next()
    {
      T result = queue.remove();
      Iterables.addAll(queue, children(result));
      return result;
    }
  }
}
