package com.github.bamirov.vunion.graph;

import java.util.Optional;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
public class VEdgeType<V extends Comparable<V>, I> extends VElement<V, I> {
	protected final String edgeTypeName;

	@Builder
	public VEdgeType(I elementId, V version, Optional<String> key, String content,
			String edgeTypeName) {
		super(elementId, version, key, content);
		this.edgeTypeName = edgeTypeName;
	}
}
