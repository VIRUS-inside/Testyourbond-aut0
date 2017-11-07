package org.yaml.snakeyaml.representer;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;



















public class Representer
  extends SafeRepresenter
{
  public Representer() { representers.put(null, new RepresentJavaBean()); }
  
  protected class RepresentJavaBean implements Represent {
    protected RepresentJavaBean() {}
    
    public Node representData(Object data) {
      try { return representJavaBean(getProperties(data.getClass()), data);
      } catch (IntrospectionException e) {
        throw new YAMLException(e);
      }
    }
  }
  













  protected MappingNode representJavaBean(Set<Property> properties, Object javaBean)
  {
    List<NodeTuple> value = new ArrayList(properties.size());
    
    Tag customTag = (Tag)classTags.get(javaBean.getClass());
    Tag tag = customTag != null ? customTag : new Tag(javaBean.getClass());
    
    MappingNode node = new MappingNode(tag, value, null);
    representedObjects.put(javaBean, node);
    boolean bestStyle = true;
    for (Property property : properties) {
      Object memberValue = property.get(javaBean);
      Tag customPropertyTag = memberValue == null ? null : (Tag)classTags.get(memberValue.getClass());
      
      NodeTuple tuple = representJavaBeanProperty(javaBean, property, memberValue, customPropertyTag);
      
      if (tuple != null)
      {

        if (((ScalarNode)tuple.getKeyNode()).getStyle() != null) {
          bestStyle = false;
        }
        Node nodeValue = tuple.getValueNode();
        if ((!(nodeValue instanceof ScalarNode)) || (((ScalarNode)nodeValue).getStyle() != null)) {
          bestStyle = false;
        }
        value.add(tuple);
      } }
    if (defaultFlowStyle != DumperOptions.FlowStyle.AUTO) {
      node.setFlowStyle(defaultFlowStyle.getStyleBoolean());
    } else {
      node.setFlowStyle(Boolean.valueOf(bestStyle));
    }
    return node;
  }
  














  protected NodeTuple representJavaBeanProperty(Object javaBean, Property property, Object propertyValue, Tag customTag)
  {
    ScalarNode nodeKey = (ScalarNode)representData(property.getName());
    
    boolean hasAlias = representedObjects.containsKey(propertyValue);
    
    Node nodeValue = representData(propertyValue);
    
    if ((propertyValue != null) && (!hasAlias)) {
      NodeId nodeId = nodeValue.getNodeId();
      if (customTag == null) {
        if (nodeId == NodeId.scalar) {
          if ((propertyValue instanceof Enum)) {
            nodeValue.setTag(Tag.STR);
          }
        } else {
          if ((nodeId == NodeId.mapping) && 
            (property.getType() == propertyValue.getClass()) && 
            (!(propertyValue instanceof Map)) && 
            (!nodeValue.getTag().equals(Tag.SET))) {
            nodeValue.setTag(Tag.MAP);
          }
          


          checkGlobalTag(property, nodeValue, propertyValue);
        }
      }
    }
    
    return new NodeTuple(nodeKey, nodeValue);
  }
  












  protected void checkGlobalTag(Property property, Node node, Object object)
  {
    if ((object.getClass().isArray()) && (object.getClass().getComponentType().isPrimitive())) {
      return;
    }
    
    Class<?>[] arguments = property.getActualTypeArguments();
    Class<?> keyType; Class<?> valueType; if (arguments != null) { Class<? extends Object> t;
      Iterator<Object> iter; if (node.getNodeId() == NodeId.sequence)
      {
        t = arguments[0];
        SequenceNode snode = (SequenceNode)node;
        Iterable<Object> memberList;
        Iterable<Object> memberList; if (object.getClass().isArray()) {
          memberList = Arrays.asList((Object[])object);
        }
        else {
          memberList = (Iterable)object;
        }
        iter = memberList.iterator();
        for (Node childNode : snode.getValue()) {
          Object member = iter.next();
          if ((member != null) && 
            (t.equals(member.getClass())) && 
            (childNode.getNodeId() == NodeId.mapping))
            childNode.setTag(Tag.MAP);
        }
      } else { Class<?> t;
        Iterator<NodeTuple> iter;
        if ((object instanceof Set)) {
          t = arguments[0];
          MappingNode mnode = (MappingNode)node;
          iter = mnode.getValue().iterator();
          Set<?> set = (Set)object;
          for (Object member : set) {
            NodeTuple tuple = (NodeTuple)iter.next();
            Node keyNode = tuple.getKeyNode();
            if ((t.equals(member.getClass())) && 
              (keyNode.getNodeId() == NodeId.mapping)) {
              keyNode.setTag(Tag.MAP);
            }
          }
        }
        else if ((object instanceof Map)) {
          keyType = arguments[0];
          valueType = arguments[1];
          MappingNode mnode = (MappingNode)node;
          for (NodeTuple tuple : mnode.getValue()) {
            resetTag(keyType, tuple.getKeyNode());
            resetTag(valueType, tuple.getValueNode());
          }
        }
      }
    }
  }
  

  private void resetTag(Class<? extends Object> type, Node node)
  {
    Tag tag = node.getTag();
    if (tag.matches(type)) {
      if (Enum.class.isAssignableFrom(type)) {
        node.setTag(Tag.STR);
      } else {
        node.setTag(Tag.MAP);
      }
    }
  }
  







  protected Set<Property> getProperties(Class<? extends Object> type)
    throws IntrospectionException
  {
    return getPropertyUtils().getProperties(type);
  }
}
