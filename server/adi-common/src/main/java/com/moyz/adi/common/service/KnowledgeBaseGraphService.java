package com.moyz.adi.common.service;

import com.moyz.adi.common.cosntant.AdiConstant;
import com.moyz.adi.common.rag.GraphStore;
import com.moyz.adi.common.vo.*;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.filter.comparison.IsEqualTo;
import dev.langchain4j.store.embedding.filter.comparison.IsIn;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class KnowledgeBaseGraphService {

    @Autowired(required = false)
    private GraphStore kbGraphStore;

    public List<GraphVertex> listVerticesByKbUuid(String kbUuid, long maxId, int limit) {
        if (kbGraphStore == null) {
            log.warn("GraphStore is not configured, skipping listVerticesByKbUuid");
            return Collections.emptyList();
        }
        Filter filter = new IsEqualTo(AdiConstant.MetadataKey.KB_UUID, kbUuid);
        return kbGraphStore.searchVertices(GraphVertexSearch.builder()
                .metadataFilter(filter)
                .maxId(maxId)
                .limit(limit)
                .build());
    }

    public List<GraphVertex> listVerticesByKbItemUuid(String kbItemUuid, long maxId, int limit) {
        if (kbGraphStore == null) {
            log.warn("GraphStore is not configured, skipping listVerticesByKbItemUuid");
            return Collections.emptyList();
        }
        Filter filter = new IsEqualTo(AdiConstant.MetadataKey.KB_ITEM_UUID, kbItemUuid);
        return kbGraphStore.searchVertices(
                GraphVertexSearch.builder()
                        .limit(limit)
                        .maxId(maxId)
                        .metadataFilter(filter)
                        .build()
        );
    }

    public List<Triple<GraphVertex, GraphEdge, GraphVertex>> listEdgesByKbUuid(String kbUuid, long maxId, int limit) {
        if (kbGraphStore == null) {
            log.warn("GraphStore is not configured, skipping listEdgesByKbUuid");
            return Collections.emptyList();
        }
        Filter filter = new IsEqualTo(AdiConstant.MetadataKey.KB_UUID, kbUuid);
        return kbGraphStore.searchEdges(GraphEdgeSearch.builder()
                .edge(GraphSearchCondition.builder().metadataFilter(filter).build())
                .maxId(maxId)
                .limit(limit)
                .build());
    }

    public List<Triple<GraphVertex, GraphEdge, GraphVertex>> listEdgesByKbItemUuid(String kbItemUuid, long maxId, int limit) {
        if (kbGraphStore == null) {
            log.warn("GraphStore is not configured, skipping listEdgesByKbItemUuid");
            return Collections.emptyList();
        }
        Filter filter = new IsEqualTo(AdiConstant.MetadataKey.KB_ITEM_UUID, kbItemUuid);
        return kbGraphStore.searchEdges(GraphEdgeSearch.builder()
                .edge(GraphSearchCondition.builder().metadataFilter(filter).build())
                .maxId(maxId)
                .limit(limit)
                .build());
    }

    public Pair<List<GraphVertex>, List<GraphEdge>> getFromTriple(List<Triple<GraphVertex, GraphEdge, GraphVertex>> triples) {
        List<GraphVertex> vertices = new ArrayList<>();
        List<GraphEdge> edges = new ArrayList<>();
        for (Triple<GraphVertex, GraphEdge, GraphVertex> triple : triples) {
            vertices.add(triple.getLeft());
            vertices.add(triple.getRight());
            edges.add(triple.getMiddle());
        }
        return Pair.of(vertices, edges);
    }
}
