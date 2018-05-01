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
import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.graph.Graph;

public final class ColoringSolver
{

    private ColoringSolver()
    {
        // do nothing
    }

    /**
     * Create a color builder.
     *
     * @param <N> the Graph nodes type
     * @param <G> the Graph type
     * @param graph the input graph
     * @return an instance of {@link ColorsBuilder}
     */
    public static <N, G extends Graph<N>> ColorsBuilder<N> coloring( G graph )
    {
        graph = checkNotNull( graph, "Coloring can not be calculated on null graph." );
        checkArgument( !graph.isDirected(), "Graph coloring algorithms do not work with directed graphs." );
        return new DefaultColorsBuilder<N>( graph );
    }

}
