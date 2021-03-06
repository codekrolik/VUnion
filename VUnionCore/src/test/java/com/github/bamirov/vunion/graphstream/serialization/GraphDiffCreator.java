package com.github.bamirov.vunion.graphstream.serialization;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.github.bamirov.vunion.graph.VEdge;
import com.github.bamirov.vunion.graph.VEdgeType;
import com.github.bamirov.vunion.graph.VGraphElement;
import com.github.bamirov.vunion.graph.VSubgraphElement;
import com.github.bamirov.vunion.graph.VVertex;
import com.github.bamirov.vunion.graph.VVertexType;
import com.github.bamirov.vunion.graphstream.VElementSyncRecord;
import com.github.bamirov.vunion.graphstream.VGraphDestroyedRecord;
import com.github.bamirov.vunion.graphstream.VGraphDiff;
import com.github.bamirov.vunion.graphstream.VGraphElementRecord;
import com.github.bamirov.vunion.graphstream.VLinkDiff;
import com.github.bamirov.vunion.graphstream.VLinkUpdate;
import com.github.bamirov.vunion.graphstream.VSubgraphDiff;
import com.github.bamirov.vunion.graphstream.VSubgraphElementRecord;
import com.github.bamirov.vunion.graphstream.VSubgraphSyncRecord;
import com.github.bamirov.vunion.version.VGraphVersion;

public class GraphDiffCreator {
	public static final String graphName = "graph0";
	public static final String subgraph0Name = "subgraph0";
	public static final String subgraph1Name = "subgraph1";
	
	private VLinkDiff<Long, Long> createLink(Long linkId, Boolean isTombstone, Long version,
			String key, String content, Long linkedElementId, Long linkedElementVersion) {

		VLinkDiff.VLinkDiffBuilder<Long, Long> builder = VLinkDiff.<Long, Long>builder()
		.linkId(linkId)
		.linkedElementId(linkedElementId);
		
		VLinkUpdate<Long, Long> linkUpdate = null;
		if (isTombstone != null) {
			Long elementId = linkId;
			Optional<String> keyOpt = key == null ? Optional.empty() : Optional.of(key);
			linkUpdate = new VLinkUpdate<Long, Long>(elementId, version, keyOpt, content, isTombstone);
			
			builder.linkUpdate(Optional.of(linkUpdate));
		}
		
		Optional<Long> linkedElementVersionUpdate = null;
		if (linkedElementVersion != null) {
			linkedElementVersionUpdate = Optional.of(linkedElementVersion);
			builder.linkedElementVersionUpdate(linkedElementVersionUpdate);
		}
		
		return builder.build();
	}
	
	private VLinkDiff<Long, Long> createLink(Long linkId, Long linkedElementId, Boolean isTombstone, Long version,
			String key, String content) {
		return createLink(linkId, isTombstone, version, key, content, linkedElementId, null);
	}
	
	private VLinkDiff<Long, Long> createLink(Long linkId, Long linkedElementId, Long linkedElementVersion) {
		return createLink(linkId, null, null, null, null, linkedElementId, linkedElementVersion);
	}
	
	private VVertexType<Long, Long> createVertexType(long elementId, long version, String key, String content, 
			String vertexTypeName) {
		VVertexType<Long, Long> vertexType = VVertexType.<Long, Long>builder()
				.elementId(elementId)
				.version(version)
				.key(key == null ? Optional.empty() : Optional.of(key))
				.content(content)
				
				.vertexTypeName(vertexTypeName)
				
				.build();
		
		return vertexType;
	}
	
	private VVertex<Long, Long> createVertex(long elementId, long version, String key, String content, 
			long vertexTypeId) {
		VVertex<Long, Long> vertex = VVertex.<Long, Long>builder()
				
				.elementId(elementId)
				.version(version)
				.key(key == null ? Optional.empty() : Optional.of(key))
				.content(content)
				.vertexTypeId(vertexTypeId)
				
				.build();
		
		return vertex;
	}

	private VEdgeType<Long, Long> createEdgeType(long elementId, long version, String key, String content, 
			String edgeTypeName) {
		VEdgeType<Long, Long> edgeType = VEdgeType.<Long, Long>builder()
				.elementId(elementId)
				.version(version)
				.key(key == null ? Optional.empty() : Optional.of(key))
				.content(content)
				
				.edgeTypeName(edgeTypeName)
				
				.build();
		
		return edgeType;
	}
	
	private VEdge<Long, Long> createEdge(long elementId, long version, String key, String content, 
			long edgeTypeId, long vertexFromId, long vertexToId, boolean isDirected) {
		VEdge<Long, Long> edge = VEdge.<Long, Long>builder()
				.elementId(elementId)
				.version(version)
				.key(key == null ? Optional.empty() : Optional.of(key))
				.content(content)
				
				.edgeTypeId(edgeTypeId)
				.vertexFromId(vertexFromId)
				.vertexToId(vertexToId)
				.isDirected(isDirected)
				
				.build();
		
		return edge;
	}
	
	//1. New vertex type, 2 vertexes +links to subgraph0
	public VGraphDiff<Long, Long> createDiff1() {
		Map<Long, VVertexType<Long, Long>> vertexTypes = new HashMap<>();
		VVertexType<Long, Long> vertexType = createVertexType(1L, 1L, "vertexTypeKey1", "<sample vertex type>", "vertexType0");
		vertexTypes.put(vertexType.getElementId(), vertexType);
		
		Map<Long, VVertex<Long, Long>> vertexes = new HashMap<>();
		VVertex<Long, Long> vertex1 = createVertex(2L, 2L, "vertexKey1", "<sample vertex content>", 1L);
		VVertex<Long, Long> vertex2 = createVertex(5L, 5L, "vertexKey2", "<sample vertex content>", 1L);
		vertexes.put(vertex1.getElementId(), vertex1);
		vertexes.put(vertex2.getElementId(), vertex2);
		
		Map<Long, VLinkDiff<Long, Long>> linkDiffs = new HashMap<>();
		VLinkDiff<Long, Long> link1 = createLink(3L, false, 3L, "linkKey1", "<sample link content>", 1L, 1L);
		VLinkDiff<Long, Long> link2 = createLink(4L, false, 4L, "linkKey2", "<sample link content>", 2L, 2L);
		VLinkDiff<Long, Long> link3 = createLink(6L, false, 6L, "linkKey3", "<sample link content>", 5L, 5L);
		linkDiffs.put(link1.getLinkedElementId(), link1);
		linkDiffs.put(link2.getLinkedElementId(), link2);
		linkDiffs.put(link3.getLinkedElementId(), link3);
		
		Map<String, VSubgraphDiff<Long, Long>> subgraphs = new HashMap<>();
		VSubgraphDiff<Long, Long> subgraph = VSubgraphDiff.<Long, Long>builder()
				.name(subgraph0Name)
				.subgraphVersionTo(6L)
				.linkUpdatesByElementId(Optional.of(linkDiffs))
				
				.build(); 
		subgraphs.put(subgraph.getName(), subgraph);
		
		VGraphDiff<Long, Long> diff1 = VGraphDiff.<Long, Long>builder()
				.graphName(graphName)
				.vertexTypes(Optional.of(vertexTypes))
				.vertexes(Optional.of(vertexes))
				.subgraphs(Optional.of(subgraphs))
				
				.build();
		
		return diff1;
	}
	
	//2. New edge type, edge +links to subgraph0
	public VGraphDiff<Long, Long> createDiff2() {
		VGraphVersion<Long> from = VGraphVersion.<Long>builder()
			.subgraphVersion(subgraph0Name, 6L)
			.build();
		
		Map<Long, VEdgeType<Long, Long>> edgeTypes = new HashMap<>();
		VEdgeType<Long, Long> edgeType = createEdgeType(7L, 7L, "edgeTypeKey1", "<sample edge type>", "edgeType0");
		edgeTypes.put(edgeType.getElementId(), edgeType);
		
		Map<Long, VEdge<Long, Long>> edges = new HashMap<>();
		VEdge<Long, Long> edge = createEdge(8L, 8L, "edgeKey1", "<sample edge content>", 7L, 2L, 5L, false);
		edges.put(edge.getElementId(), edge);
		
		Map<Long, VLinkDiff<Long, Long>> linkDiffs = new HashMap<>();
		VLinkDiff<Long, Long> link1 = createLink(9L, false, 9L, "linkKey4", "<sample link content>", 7L, 7L);
		VLinkDiff<Long, Long> link2 = createLink(10L, false, 10L, "linkKey5", "<sample link content>", 8L, 8L);
		linkDiffs.put(link1.getLinkedElementId(), link1);
		linkDiffs.put(link2.getLinkedElementId(), link2);
		
		Map<String, VSubgraphDiff<Long, Long>> subgraphs = new HashMap<>();
		VSubgraphDiff<Long, Long> subgraph = VSubgraphDiff.<Long, Long>builder()
				.name(subgraph0Name)
				.subgraphVersionTo(10L)
				.linkUpdatesByElementId(Optional.of(linkDiffs))
				
				.build();
		subgraphs.put(subgraph.getName(), subgraph);
		
		VGraphDiff<Long, Long> diff2 = VGraphDiff.<Long, Long>builder()
				.from(from)
				.graphName(graphName)
				.edgeTypes(Optional.of(edgeTypes))
				.edges(Optional.of(edges))
				.subgraphs(Optional.of(subgraphs))
			
				.build();
		
		return diff2;
	}
	
	//3. New subgraph, links for existing elements (vertexes, edges, vertex types, edge types) - to subgraph1
	public VGraphDiff<Long, Long> createDiff3() {
		VGraphVersion<Long> from = VGraphVersion.<Long>builder()
				.subgraphVersion(subgraph0Name, 10L)
				.build();
		
		Map<Long, VVertexType<Long, Long>> vertexTypes = new HashMap<>();
		VVertexType<Long, Long> vertexType = createVertexType(1L, 1L, "vertexTypeKey1", "<sample vertex type>", "vertexType0");
		vertexTypes.put(vertexType.getElementId(), vertexType);
		
		Map<Long, VVertex<Long, Long>> vertexes = new HashMap<>();
		VVertex<Long, Long> vertex1 = createVertex(2L, 2L, "vertexKey1", "<sample vertex content>", 1L);
		VVertex<Long, Long> vertex2 = createVertex(5L, 5L, "vertexKey2", "<sample vertex content>", 1L);
		vertexes.put(vertex1.getElementId(), vertex1);
		vertexes.put(vertex2.getElementId(), vertex2);
		
		Map<Long, VEdgeType<Long, Long>> edgeTypes = new HashMap<>();
		VEdgeType<Long, Long> edgeType = createEdgeType(7L, 7L, "edgeTypeKey1", "<sample edge type>", "edgeType0");
		edgeTypes.put(edgeType.getElementId(), edgeType);
		
		Map<Long, VEdge<Long, Long>> edges = new HashMap<>();
		VEdge<Long, Long> edge = createEdge(8L, 8L, "edgeKey1", "<sample edge content>", 7L, 2L, 5L, false);
		edges.put(edge.getElementId(), edge);
		
		Map<Long, VLinkDiff<Long, Long>> linkDiffs1 = new HashMap<>();
		VLinkDiff<Long, Long> link11 = createLink(11L, false, 11L, "linkKey6", "<sample link content>", 1L, 1L);
		VLinkDiff<Long, Long> link12 = createLink(12L, false, 12L, "linkKey7", "<sample link content>", 2L, 2L);
		VLinkDiff<Long, Long> link13 = createLink(13L, false, 13L, "linkKey8", "<sample link content>", 5L, 5L);
		VLinkDiff<Long, Long> link14 = createLink(14L, false, 14L, "linkKey9", "<sample link content>", 7L, 7L);
		VLinkDiff<Long, Long> link15 = createLink(15L, false, 15L, "linkKey10", "<sample link content>", 8L, 8L);
		
		linkDiffs1.put(link11.getLinkedElementId(), link11);
		linkDiffs1.put(link12.getLinkedElementId(), link12);
		linkDiffs1.put(link13.getLinkedElementId(), link13);
		linkDiffs1.put(link14.getLinkedElementId(), link14);
		linkDiffs1.put(link15.getLinkedElementId(), link15);
		
		Map<String, VSubgraphDiff<Long, Long>> subgraphs = new HashMap<>();
		VSubgraphDiff<Long, Long> subgraph1 = VSubgraphDiff.<Long, Long>builder()
				.name(subgraph1Name)
				.subgraphVersionTo(15L)
				.linkUpdatesByElementId(Optional.of(linkDiffs1))
				
				.build();
		subgraphs.put(subgraph1.getName(), subgraph1);
		
		VGraphDiff<Long, Long> diff3 = VGraphDiff.<Long, Long>builder()
				.from(from)
				.graphName(graphName)
				.vertexTypes(Optional.of(vertexTypes))
				.vertexes(Optional.of(vertexes))
				.edgeTypes(Optional.of(edgeTypes))
				.edges(Optional.of(edges))
				.subgraphs(Optional.of(subgraphs))
				
				.build();
		
		return diff3;
	}
	
	//4. Vertex updated
	public VGraphDiff<Long, Long> createDiff4() {
		VGraphVersion<Long> from = VGraphVersion.<Long>builder()
				.subgraphVersion(subgraph0Name, 10L)
				.subgraphVersion(subgraph1Name, 15L)
				.build();
		
		Map<Long, VVertex<Long, Long>> vertexes = new HashMap<>();
		VVertex<Long, Long> vertex = createVertex(2L, 16L, "vertexKey1-1", "<sample vertex content-1>", 1L);
		vertexes.put(vertex.getElementId(), vertex);
		
		Map<Long, VLinkDiff<Long, Long>> linkDiffs0 = new HashMap<>();
		VLinkDiff<Long, Long> link0 = createLink(4L, 2L, 16L);
		linkDiffs0.put(link0.getLinkedElementId(), link0);
		
		Map<Long, VLinkDiff<Long, Long>> linkDiffs1 = new HashMap<>();
		VLinkDiff<Long, Long> link1 = createLink(12L, 2L, 16L);
		linkDiffs1.put(link1.getLinkedElementId(), link1);
		
		Map<String, VSubgraphDiff<Long, Long>> subgraphs = new HashMap<>();
		VSubgraphDiff<Long, Long> subgraph0 = VSubgraphDiff.<Long, Long>builder()
				.name(subgraph0Name)
				.subgraphVersionTo(16L)
				.linkUpdatesByElementId(Optional.of(linkDiffs0))
				
				.build();
		VSubgraphDiff<Long, Long> subgraph1 = VSubgraphDiff.<Long, Long>builder()
				.name(subgraph1Name)
				.subgraphVersionTo(16L)
				.linkUpdatesByElementId(Optional.of(linkDiffs1))
				
				.build();
		subgraphs.put(subgraph0.getName(), subgraph0);
		subgraphs.put(subgraph1.getName(), subgraph1);
		
		VGraphDiff<Long, Long> diff4 = VGraphDiff.<Long, Long>builder()
				.from(from)
				.graphName(graphName)
				.vertexes(Optional.of(vertexes))
				.subgraphs(Optional.of(subgraphs))
				
				.build();
		
		return diff4;
	}
	
	//5. Edge updated
	public VGraphDiff<Long, Long> createDiff5() {
		VGraphVersion<Long> from = VGraphVersion.<Long>builder()
				.subgraphVersion(subgraph0Name, 16L)
				.subgraphVersion(subgraph1Name, 16L)
				.build();
		
		Map<Long, VEdge<Long, Long>> edges = new HashMap<>();
		VEdge<Long, Long> edge = createEdge(8L, 17L, "edgeKey1-1", "<sample edge content-1>", 7L, 2L, 5L, true);
		edges.put(edge.getElementId(), edge);
		
		Map<Long, VLinkDiff<Long, Long>> linkDiffs0 = new HashMap<>();
		VLinkDiff<Long, Long> link0 = createLink(10L, 8L, 17L);
		linkDiffs0.put(link0.getLinkedElementId(), link0);
		
		Map<Long, VLinkDiff<Long, Long>> linkDiffs1 = new HashMap<>();
		VLinkDiff<Long, Long> link1 = createLink(15L, 8L, 17L);
		linkDiffs1.put(link1.getLinkedElementId(), link1);
	
		Map<String, VSubgraphDiff<Long, Long>> subgraphs = new HashMap<>();
		VSubgraphDiff<Long, Long> subgraph0 = VSubgraphDiff.<Long, Long>builder()
				.name(subgraph0Name)
				.subgraphVersionTo(17L)
				.linkUpdatesByElementId(Optional.of(linkDiffs0))
				
				.build();
		VSubgraphDiff<Long, Long> subgraph1 = VSubgraphDiff.<Long, Long>builder()
				.name(subgraph1Name)
				.subgraphVersionTo(17L)
				.linkUpdatesByElementId(Optional.of(linkDiffs1))
				
				.build();
		subgraphs.put(subgraph0.getName(), subgraph0);
		subgraphs.put(subgraph1.getName(), subgraph1);
		
		VGraphDiff<Long, Long> diff5 = VGraphDiff.<Long, Long>builder()
				.from(from)
				.graphName(graphName)
				.edges(Optional.of(edges))
				.subgraphs(Optional.of(subgraphs))
				
				.build();
		
		return diff5;
	}

	//6. Link updated
	public VGraphDiff<Long, Long> createDiff6() {
		VGraphVersion<Long> from = VGraphVersion.<Long>builder()
				.subgraphVersion(subgraph0Name, 17L)
				.subgraphVersion(subgraph1Name, 17L)
				.build();
		
		Map<Long, VLinkDiff<Long, Long>> linkDiffs0 = new HashMap<>();
		VLinkDiff<Long, Long> link0 = createLink(10L, 8L, false, 18L, "linkKey5-1", "<sample link content-1>");
		linkDiffs0.put(link0.getLinkedElementId(), link0);
		
		Map<String, VSubgraphDiff<Long, Long>> subgraphs = new HashMap<>();
		VSubgraphDiff<Long, Long> subgraph0 = VSubgraphDiff.<Long, Long>builder()
				.name(subgraph0Name)
				.subgraphVersionTo(18L)
				.linkUpdatesByElementId(Optional.of(linkDiffs0))
				
				.build();
		subgraphs.put(subgraph0.getName(), subgraph0);
		
		VGraphDiff<Long, Long> diff6 = VGraphDiff.<Long, Long>builder()
				.from(from)
				.graphName(graphName)
				.subgraphs(Optional.of(subgraphs))
				
				.build();
		
		return diff6;
	}

	//7.1. GraphElement created
	public VGraphDiff<Long, Long> createDiff71() {
		VGraphVersion<Long> from = VGraphVersion.<Long>builder()
				.subgraphVersion(subgraph0Name, 18L)
				.subgraphVersion(subgraph1Name, 17L)
				.build();
		
		VGraphElementRecord<Long, Long> graphElementRecord = VGraphElementRecord.<Long, Long>builder()
				.graphElementUpdateVersion(19L)
				.graphElement(Optional.of(
							VGraphElement.<Long, Long>builder()
								.elementId(16L)
								.version(19L)
								.key(Optional.of("graphElementKey1"))
								.content("<sample graph element content>")
								.build()
						))
				.build();
		
		VGraphDiff<Long, Long> diff71 = VGraphDiff.<Long, Long>builder()
				.from(from)
				.graphName(graphName)
				.graphElementRecord(Optional.of(graphElementRecord))
				
				.build();
		
		return diff71;
	}

	//7.2. GraphElement updated
	public VGraphDiff<Long, Long> createDiff72() {
		VGraphVersion<Long> from = VGraphVersion.<Long>builder()
				.graphVersion(Optional.of(19L))
				.subgraphVersion(subgraph0Name, 18L)
				.subgraphVersion(subgraph1Name, 17L)
				.build();
		
		VGraphElementRecord<Long, Long> graphElementRecord = VGraphElementRecord.<Long, Long>builder()
				.graphElementUpdateVersion(20L)
				.graphElement(Optional.of(
							VGraphElement.<Long, Long>builder()
								.elementId(16L)
								.version(20L)
								.key(Optional.of("graphElementKey1-1"))
								.content("<sample graph element content-1>")
								.build()
						))
				.build();
		
		VGraphDiff<Long, Long> diff72 = VGraphDiff.<Long, Long>builder()
				.from(from)
				.graphName(graphName)
				.graphElementRecord(Optional.of(graphElementRecord))
				
				.build();
		
		return diff72;
	}

	//8.1. SubgraphElement created
	public VGraphDiff<Long, Long> createDiff81() {
		VGraphVersion<Long> from = VGraphVersion.<Long>builder()
				.graphVersion(Optional.of(20L))
				.subgraphVersion(subgraph0Name, 18L)
				.subgraphVersion(subgraph1Name, 17L)
				.build();
		
		VSubgraphElementRecord<Long, Long> subgraphElementRecord = VSubgraphElementRecord.<Long, Long>builder()
				.subgraphElementUpdateVersion(21L)
				.subgraphElement(Optional.of(
							VSubgraphElement.<Long, Long>builder()
								.elementId(17L)
								.version(21L)
								.key(Optional.of("subgraphElementKey1"))
								.content("<sample subgraph element content>")
								.build()
						))
				.build();
		
		Map<String, VSubgraphDiff<Long, Long>> subgraphs = new HashMap<>();
		VSubgraphDiff<Long, Long> subgraph0 = VSubgraphDiff.<Long, Long>builder()
				.name(subgraph0Name)
				.subgraphVersionTo(21L)
				.subgraphElementRecord(Optional.of(subgraphElementRecord))
				
				.build();
		subgraphs.put(subgraph0.getName(), subgraph0);

		VGraphDiff<Long, Long> diff81 = VGraphDiff.<Long, Long>builder()
				.from(from)
				.graphName(graphName)
				.subgraphs(Optional.of(subgraphs))
				
				.build();
		
		return diff81;
	}

	//8.2. SubgraphElement updated
	public VGraphDiff<Long, Long> createDiff82() {
		VGraphVersion<Long> from = VGraphVersion.<Long>builder()
				.graphVersion(Optional.of(20L))
				.subgraphVersion(subgraph0Name, 21L)
				.subgraphVersion(subgraph1Name, 17L)
				.build();
		
		VSubgraphElementRecord<Long, Long> subgraphElementRecord = VSubgraphElementRecord.<Long, Long>builder()
				.subgraphElementUpdateVersion(22L)
				.subgraphElement(Optional.of(
							VSubgraphElement.<Long, Long>builder()
								.elementId(17L)
								.version(22L)
								.key(Optional.of("subgraphElementKey1-1"))
								.content("<sample subgraph element content-1>")
								.build()
						))
				.build();
		
		Map<String, VSubgraphDiff<Long, Long>> subgraphs = new HashMap<>();
		VSubgraphDiff<Long, Long> subgraph0 = VSubgraphDiff.<Long, Long>builder()
				.name(subgraph0Name)
				.subgraphVersionTo(22L)
				.subgraphElementRecord(Optional.of(subgraphElementRecord))
				
				.build();
		subgraphs.put(subgraph0.getName(), subgraph0);

		VGraphDiff<Long, Long> diff82 = VGraphDiff.<Long, Long>builder()
				.from(from)
				.graphName(graphName)
				.subgraphs(Optional.of(subgraphs))
				
				.build();
		
		return diff82;
	}
	
	//9. Vertex Type renamed
	public VGraphDiff<Long, Long> createDiff9() {
		VGraphVersion<Long> from = VGraphVersion.<Long>builder()
				.graphVersion(Optional.of(20L))
				.subgraphVersion(subgraph0Name, 22L)
				.subgraphVersion(subgraph1Name, 17L)
				.build();
		
		Map<Long, VVertexType<Long, Long>> vertexTypes = new HashMap<>();
		VVertexType<Long, Long> vertexType = createVertexType(1L, 23L, "vertexTypeKey1-1", "<sample vertex type-1>", "vertexType0-1");
		vertexTypes.put(vertexType.getElementId(), vertexType);
		
		Map<Long, VLinkDiff<Long, Long>> linkDiffs0 = new HashMap<>();
		VLinkDiff<Long, Long> link0 = createLink(3L, 1L, 23L);
		linkDiffs0.put(link0.getLinkedElementId(), link0);
		
		Map<Long, VLinkDiff<Long, Long>> linkDiffs1 = new HashMap<>();
		VLinkDiff<Long, Long> link1 = createLink(11L, 1L, 23L);
		linkDiffs1.put(link1.getLinkedElementId(), link1);
		
		Map<String, VSubgraphDiff<Long, Long>> subgraphs = new HashMap<>();
		VSubgraphDiff<Long, Long> subgraph0 = VSubgraphDiff.<Long, Long>builder()
				.name(subgraph0Name)
				.subgraphVersionTo(23L)
				.linkUpdatesByElementId(Optional.of(linkDiffs0))
				
				.build();
		VSubgraphDiff<Long, Long> subgraph1 = VSubgraphDiff.<Long, Long>builder()
				.name(subgraph1Name)
				.subgraphVersionTo(23L)
				.linkUpdatesByElementId(Optional.of(linkDiffs1))
				
				.build();
		subgraphs.put(subgraph0.getName(), subgraph0);
		subgraphs.put(subgraph1.getName(), subgraph1);
		
		VGraphDiff<Long, Long> diff9 = VGraphDiff.<Long, Long>builder()
			.from(from)
			.graphName(graphName)
			.vertexTypes(Optional.of(vertexTypes))
			.subgraphs(Optional.of(subgraphs))
			
			.build();

		return diff9;
	}
	
	//10. Edge Type renamed
	public VGraphDiff<Long, Long> createDiff10() {
		VGraphVersion<Long> from = VGraphVersion.<Long>builder()
				.graphVersion(Optional.of(20L))
				.subgraphVersion(subgraph0Name, 23L)
				.subgraphVersion(subgraph1Name, 23L)
				.build();

		Map<Long, VEdgeType<Long, Long>> edgeTypes = new HashMap<>();
		VEdgeType<Long, Long> edgeType = createEdgeType(7L, 24L, "edgeTypeKey1-1", "<sample edge type-1>", "edgeType0-1");
		edgeTypes.put(edgeType.getElementId(), edgeType);
		
		Map<Long, VLinkDiff<Long, Long>> linkDiffs0 = new HashMap<>();
		VLinkDiff<Long, Long> link0 = createLink(9L, 7L, 24L);
		linkDiffs0.put(link0.getLinkedElementId(), link0);
		
		Map<Long, VLinkDiff<Long, Long>> linkDiffs1 = new HashMap<>();
		VLinkDiff<Long, Long> link1 = createLink(14L, 7L, 24L);
		linkDiffs1.put(link1.getLinkedElementId(), link1);
		
		Map<String, VSubgraphDiff<Long, Long>> subgraphs = new HashMap<>();
		VSubgraphDiff<Long, Long> subgraph0 = VSubgraphDiff.<Long, Long>builder()
				.name(subgraph0Name)
				.subgraphVersionTo(24L)
				.linkUpdatesByElementId(Optional.of(linkDiffs0))
				
				.build();
		VSubgraphDiff<Long, Long> subgraph1 = VSubgraphDiff.<Long, Long>builder()
				.name(subgraph1Name)
				.subgraphVersionTo(24L)
				.linkUpdatesByElementId(Optional.of(linkDiffs1))
				
				.build();
		subgraphs.put(subgraph0.getName(), subgraph0);
		subgraphs.put(subgraph1.getName(), subgraph1);
		
		VGraphDiff<Long, Long> diff10 = VGraphDiff.<Long, Long>builder()
			.from(from)
			.graphName(graphName)
			.edgeTypes(Optional.of(edgeTypes))
			.subgraphs(Optional.of(subgraphs))
			
			.build();

		return diff10;
	}
	
	//11. Delete link
	public VGraphDiff<Long, Long> createDiff11() {
		//remove link id 15 / edge id 8
		VGraphVersion<Long> from = VGraphVersion.<Long>builder()
				.graphVersion(Optional.of(20L))
				.subgraphVersion(subgraph0Name, 24L)
				.subgraphVersion(subgraph1Name, 24L)
				.build();
		
		Map<String, VSubgraphDiff<Long, Long>> subgraphs = new HashMap<>();
		VSubgraphDiff<Long, Long> subgraph0 = VSubgraphDiff.<Long, Long>builder()
				.name(subgraph1Name)
				.subgraphVersionTo(25L)
				.elementSync(Optional.of(
							VElementSyncRecord.<Long, Long>builder()
							.elementSyncVersion(25L)
							.elementId(1L)
							.elementId(2L)
							.elementId(5L)
							.elementId(7L)
							
							.build()
						))
				
				.build();
		subgraphs.put(subgraph0.getName(), subgraph0);
		
		VGraphDiff<Long, Long> diff11 = VGraphDiff.<Long, Long>builder()
			.from(from)
			.graphName(graphName)
			.subgraphs(Optional.of(subgraphs))
			
			.build();

		return diff11;
	}
	
	//13. Delete subgraph
	public VGraphDiff<Long, Long> createDiff13() {
		VGraphVersion<Long> from = VGraphVersion.<Long>builder()
				.graphVersion(Optional.of(20L))
				.subgraphVersion(subgraph0Name, 24L)
				.subgraphVersion(subgraph1Name, 25L)
				.build();
		
		VGraphDiff<Long, Long> diff13 = VGraphDiff.<Long, Long>builder()
				.from(from)
				.graphName(graphName)
				.subgraphSync(Optional.of(
							VSubgraphSyncRecord.<Long>builder()
							.subgraphSyncVersion(26L)
							.subgraphName(subgraph0Name)
							
							.build()
						))
				
				.build();

		return diff13;
	}
	
	//14. GraphElement deleted
	public VGraphDiff<Long, Long> createDiff14() {
		VGraphVersion<Long> from = VGraphVersion.<Long>builder()
				.graphVersion(Optional.of(26L))
				.subgraphVersion(subgraph0Name, 24L)
				.build();
		
		VGraphElementRecord<Long, Long> graphElementRecord = VGraphElementRecord.<Long, Long>builder()
				.graphElementUpdateVersion(27L)
				.build();
		
		VGraphDiff<Long, Long> diff14 = VGraphDiff.<Long, Long>builder()
				.from(from)
				.graphName(graphName)
				.graphElementRecord(Optional.of(graphElementRecord))
				
				.build();
		
		return diff14;
	}
	
	//15. SubgraphElement deleted
	public VGraphDiff<Long, Long> createDiff15() {
		VGraphVersion<Long> from = VGraphVersion.<Long>builder()
				.graphVersion(Optional.of(27L))
				.subgraphVersion(subgraph0Name, 24L)
				.build();
		
		VSubgraphElementRecord<Long, Long> subgraphElementRecord = VSubgraphElementRecord.<Long, Long>builder()
				.subgraphElementUpdateVersion(28L)
				.build();
		
		Map<String, VSubgraphDiff<Long, Long>> subgraphs = new HashMap<>();
		VSubgraphDiff<Long, Long> subgraph0 = VSubgraphDiff.<Long, Long>builder()
				.name(subgraph0Name)
				.subgraphVersionTo(28L)
				.subgraphElementRecord(Optional.of(subgraphElementRecord))
				
				.build();
		subgraphs.put(subgraph0.getName(), subgraph0);
		
		VGraphDiff<Long, Long> diff15 = VGraphDiff.<Long, Long>builder()
				.from(from)
				.graphName(graphName)
				.subgraphs(Optional.of(subgraphs))
				
				.build();
		
		return diff15;
	}
	
	//16.1. Destroy graph
	public VGraphDiff<Long, Long> createDiff161() {
		VGraphVersion<Long> from = VGraphVersion.<Long>builder()
				.graphVersion(Optional.of(27L))
				.subgraphVersion(subgraph0Name, 28L)
				.build();
		
		VGraphDestroyedRecord<Long> destroyedRecord = VGraphDestroyedRecord.<Long>builder()
				.destroyRecoverVersion(29L)
				.isDestroyed(true)
				.build();
		
		VGraphDiff<Long, Long> diff161 = VGraphDiff.<Long, Long>builder()
				.from(from)
				.graphName(graphName)
				.destroyedRecord(Optional.of(destroyedRecord))
				
				.build();
		
		return diff161;
	}

	//16.2. Recover graph
	public VGraphDiff<Long, Long> createDiff162() {
		VGraphVersion<Long> from = VGraphVersion.<Long>builder()
				.graphVersion(Optional.of(29L))
				.build();
		
		VGraphDestroyedRecord<Long> destroyedRecord = VGraphDestroyedRecord.<Long>builder()
				.destroyRecoverVersion(30L)
				.isDestroyed(false)
				.build();
		
		VGraphDiff<Long, Long> diff162 = VGraphDiff.<Long, Long>builder()
				.from(from)
				.graphName(graphName)
				.destroyedRecord(Optional.of(destroyedRecord))
				
				.build();
		
		//TODO: add recovered graph contents
		
		return diff162;
	}
}
