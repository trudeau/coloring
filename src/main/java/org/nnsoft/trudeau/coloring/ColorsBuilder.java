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

import java.util.Set;

/**
 * Builder to specify the set of colors for coloring the graph.
 *
 * @param <N> the Graph nodes type
 */
public interface ColorsBuilder<N>
{

    /**
     * Specifies the set of colors for coloring the graph.
     *
     * @param <C> the Color type.
     * @param colors the set of colors for coloring the graph.
     * @return the coloring algorithm selector.
     */
    <C> ColoringAlgorithmsSelector<N, C> withColors( Set<C> colors );

}
