package org.nnsoft.trudeau.coloring;

/*
 *   Copyright 2013 The Trudeau Project
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

/**
 * Builder for selecting the coloring algorithm to perform.
 *
 * @param <V> the Graph vertices type
 * @param <E> the Graph edges type
 * @param <C> the Color vertices type
 */
public interface ColoringAlgorithmsSelector<V, E, C>
{

    /**
     * Colors the graph such that no two adjacent vertices share the same color.
     *
     * @return The color - vertex association.
     */
    ColoredVertices<V, C> applyingGreedyAlgorithm();

    /**
     * Graph m-coloring algorithm. This algorithm uses a brute-force backtracking
     * procedure to find a graph color.
     *
     * @return The color - vertex association.
     */
    ColoredVertices<V, C> applyingBackTrackingAlgorithm();

    /**
     * Graph m-coloring algorithm. This algorithm uses a brute-force backtracking
     * procedure to find a graph color using a predefined set of colors.
     *
     * @param partialColoredVertex subset of vertices already colored.
     * @return The color - vertex association.
     */
    ColoredVertices<V, C> applyingBackTrackingAlgorithm( ColoredVertices<V, C> partialColoredVertex );

}
