package org.nnsoft.trudeau.coloring;

/*
 *   Copyright 2013 - 2018 The Trudeau Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;

import com.google.common.graph.Graph;

/**
 * {@link ColorsBuilder} implementation
 *
 * @param <N> the Graph nodes type
 */
final class DefaultColorsBuilder<N>
    implements ColorsBuilder<N>
{

    private final Graph<N> graph;

    /**
     * Creates a new instance of {@link DefaultColorsBuilder} for the input graph.
     * @param graph the graph
     */
    public DefaultColorsBuilder( Graph<N> graph )
    {
        this.graph = graph;
    }

    /**
     * {@inheritDoc}
     */
    public <C> ColoringAlgorithmsSelector<N, C> withColors( Set<C> colors )
    {
        colors = checkNotNull( colors, "Colors set must be not null" );
        return new DefaultColoringAlgorithmsSelector<N, C>( graph, colors );
    }

}
