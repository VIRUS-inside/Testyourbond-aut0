package org.yaml.snakeyaml.serializer;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.Version;
import org.yaml.snakeyaml.emitter.Emitable;
import org.yaml.snakeyaml.events.AliasEvent;
import org.yaml.snakeyaml.events.DocumentEndEvent;
import org.yaml.snakeyaml.events.DocumentStartEvent;
import org.yaml.snakeyaml.events.ImplicitTuple;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;
import org.yaml.snakeyaml.events.SequenceStartEvent;
import org.yaml.snakeyaml.events.StreamEndEvent;
import org.yaml.snakeyaml.events.StreamStartEvent;
import org.yaml.snakeyaml.nodes.AnchorNode;
import org.yaml.snakeyaml.nodes.CollectionNode;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.resolver.Resolver;















public final class Serializer
{
  private final Emitable emitter;
  private final Resolver resolver;
  private boolean explicitStart;
  private boolean explicitEnd;
  private DumperOptions.Version useVersion;
  private Map<String, String> useTags;
  private Set<Node> serializedNodes;
  private Map<Node, String> anchors;
  private int lastAnchorId;
  private Boolean closed;
  private Tag explicitRoot;
  
  public Serializer(Emitable emitter, Resolver resolver, DumperOptions opts, Tag rootTag)
  {
    this.emitter = emitter;
    this.resolver = resolver;
    explicitStart = opts.isExplicitStart();
    explicitEnd = opts.isExplicitEnd();
    if (opts.getVersion() != null) {
      useVersion = opts.getVersion();
    }
    useTags = opts.getTags();
    serializedNodes = new HashSet();
    anchors = new HashMap();
    lastAnchorId = 0;
    closed = null;
    explicitRoot = rootTag;
  }
  
  public void open() throws IOException {
    if (closed == null) {
      emitter.emit(new StreamStartEvent(null, null));
      closed = Boolean.FALSE;
    } else { if (Boolean.TRUE.equals(closed)) {
        throw new SerializerException("serializer is closed");
      }
      throw new SerializerException("serializer is already opened");
    }
  }
  
  public void close() throws IOException {
    if (closed == null)
      throw new SerializerException("serializer is not opened");
    if (!Boolean.TRUE.equals(closed)) {
      emitter.emit(new StreamEndEvent(null, null));
      closed = Boolean.TRUE;
    }
  }
  
  public void serialize(Node node) throws IOException {
    if (closed == null)
      throw new SerializerException("serializer is not opened");
    if (closed.booleanValue()) {
      throw new SerializerException("serializer is closed");
    }
    emitter.emit(new DocumentStartEvent(null, null, explicitStart, useVersion, useTags));
    
    anchorNode(node);
    if (explicitRoot != null) {
      node.setTag(explicitRoot);
    }
    serializeNode(node, null);
    emitter.emit(new DocumentEndEvent(null, null, explicitEnd));
    serializedNodes.clear();
    anchors.clear();
    lastAnchorId = 0;
  }
  
  private void anchorNode(Node node) {
    if (node.getNodeId() == NodeId.anchor) {
      node = ((AnchorNode)node).getRealNode();
    }
    if (anchors.containsKey(node)) {
      String anchor = (String)anchors.get(node);
      if (null == anchor) {
        anchor = generateAnchor();
        anchors.put(node, anchor);
      }
    } else {
      anchors.put(node, null);
      switch (1.$SwitchMap$org$yaml$snakeyaml$nodes$NodeId[node.getNodeId().ordinal()]) {
      case 1: 
        SequenceNode seqNode = (SequenceNode)node;
        List<Node> list = seqNode.getValue();
        for (Node item : list) {
          anchorNode(item);
        }
        break;
      case 2: 
        MappingNode mnode = (MappingNode)node;
        List<NodeTuple> map = mnode.getValue();
        for (NodeTuple object : map) {
          Node key = object.getKeyNode();
          Node value = object.getValueNode();
          anchorNode(key);
          anchorNode(value);
        }
      }
    }
  }
  
  private String generateAnchor()
  {
    lastAnchorId += 1;
    NumberFormat format = NumberFormat.getNumberInstance();
    format.setMinimumIntegerDigits(3);
    format.setMaximumFractionDigits(0);
    format.setGroupingUsed(false);
    String anchorId = format.format(lastAnchorId);
    return "id" + anchorId;
  }
  
  private void serializeNode(Node node, Node parent) throws IOException {
    if (node.getNodeId() == NodeId.anchor) {
      node = ((AnchorNode)node).getRealNode();
    }
    String tAlias = (String)anchors.get(node);
    if (serializedNodes.contains(node)) {
      emitter.emit(new AliasEvent(tAlias, null, null));
    } else {
      serializedNodes.add(node);
      switch (1.$SwitchMap$org$yaml$snakeyaml$nodes$NodeId[node.getNodeId().ordinal()]) {
      case 3: 
        ScalarNode scalarNode = (ScalarNode)node;
        Tag detectedTag = resolver.resolve(NodeId.scalar, scalarNode.getValue(), true);
        Tag defaultTag = resolver.resolve(NodeId.scalar, scalarNode.getValue(), false);
        ImplicitTuple tuple = new ImplicitTuple(node.getTag().equals(detectedTag), node.getTag().equals(defaultTag));
        
        ScalarEvent event = new ScalarEvent(tAlias, node.getTag().getValue(), tuple, scalarNode.getValue(), null, null, scalarNode.getStyle());
        
        emitter.emit(event);
        break;
      case 1: 
        SequenceNode seqNode = (SequenceNode)node;
        boolean implicitS = node.getTag().equals(resolver.resolve(NodeId.sequence, null, true));
        
        emitter.emit(new SequenceStartEvent(tAlias, node.getTag().getValue(), implicitS, null, null, seqNode.getFlowStyle()));
        
        List<Node> list = seqNode.getValue();
        for (Node item : list) {
          serializeNode(item, node);
        }
        emitter.emit(new SequenceEndEvent(null, null));
        break;
      default: 
        Tag implicitTag = resolver.resolve(NodeId.mapping, null, true);
        boolean implicitM = node.getTag().equals(implicitTag);
        emitter.emit(new MappingStartEvent(tAlias, node.getTag().getValue(), implicitM, null, null, ((CollectionNode)node).getFlowStyle()));
        
        MappingNode mnode = (MappingNode)node;
        List<NodeTuple> map = mnode.getValue();
        for (NodeTuple row : map) {
          Node key = row.getKeyNode();
          Node value = row.getValueNode();
          serializeNode(key, mnode);
          serializeNode(value, mnode);
        }
        emitter.emit(new MappingEndEvent(null, null));
      }
    }
  }
}
