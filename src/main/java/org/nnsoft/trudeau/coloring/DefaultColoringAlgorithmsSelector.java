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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.graph.Graph;

/**
 * {@link ColoringAlgorithmsSelector} implementation.
 *
 * @param <N> the Graph nodes type
 * @param <C> the Color nodes type
 */
final class DefaultColoringAlgorithmsSelector<N, C>
    implements ColoringAlgorithmsSelector<N, C>
{

    private final Graph<N> graph;

    private final Set<C> colors;

    public DefaultColoringAlgorithmsSelector( Graph<N> graph, Set<C> colors )
    {
        this.graph = graph;
        this.colors = colors;
    }

    /**
     * {@inheritDoc}
     */
    public ColoredNodes<N, C> applyingGreedyAlgorithm()
        throws NotEnoughColorsException
    {
        final ColoredNodes<N, C> coloredNodes = new ColoredNodes<N, C>();

        // decreasing sorting all nodes by degree.
        final UncoloredOrderedNodes<N> uncoloredOrderedNodes = new UncoloredOrderedNodes<N>();

        for ( N node : graph.nodes() )
        {
            uncoloredOrderedNodes.addVertexDegree( node, graph.degree( node ) );
        }

        // search coloring
        Iterator<N> it = uncoloredOrderedNodes.iterator();
        Iterator<C> colorsIt = colors.iterator();
        while ( it.hasNext() )
        {
            if ( !colorsIt.hasNext() )
            {
                throw new NotEnoughColorsException( colors );
            }
            C color = colorsIt.next();

            // this list contains all vertex colors with the current color.
            List<N> currentColorNodes = new ArrayList<N>();
            Iterator<N> uncoloredNodesIterator = uncoloredOrderedNodes.iterator();
            while ( uncoloredNodesIterator.hasNext() )
            {
                N uncoloredNode = uncoloredNodesIterator.next();

                boolean foundAnAdjacentNode = false;
                for ( N currentColoredNode : currentColorNodes )
                {
                    if ( graph.hasEdgeConnecting( currentColoredNode, uncoloredNode ) )
                    {
                        // we've found that 'uncoloredVtx' is adiacent to
                        // 'currentColoredVtx'
                        foundAnAdjacentNode = true;
                        break;
                    }
                }

                if ( !foundAnAdjacentNode )
                {
                    // It's possible to color the vertex 'uncoloredVtx', it has
                    // no connected vertex into
                    // 'currentcoloredvtx'
                    uncoloredNodesIterator.remove();
                    coloredNodes.addColor( uncoloredNode, color );
                    currentColorNodes.add( uncoloredNode );
                }
            }

            it = uncoloredOrderedNodes.iterator();
        }

        return coloredNodes;
    }

    /**
     * {@inheritDoc}
     */
    public ColoredNodes<N, C> applyingBackTrackingAlgorithm()
        throws NotEnoughColorsException
    {
        return applyingBackTrackingAlgorithm( new ColoredNodes<N, C>() );
    }

    /**
     * {@inheritDoc}
     */
    public ColoredNodes<N, C> applyingBackTrackingAlgorithm( ColoredNodes<N, C> partialColoredVertex )
        throws NotEnoughColorsException
    {
        partialColoredVertex = checkNotNull( partialColoredVertex, "PartialColoredVertex must be not null" );

        final List<N> nodesList = new ArrayList<N>();

        for ( N node : graph.nodes() )
        {
            if ( !partialColoredVertex.containsColoredNode( node ) )
            {
                nodesList.add( node );
            }
        }

        if ( backtraking( -1, nodesList, partialColoredVertex ) )
        {
            return partialColoredVertex;
        }

        throw new NotEnoughColorsException( colors );
    }

    /**
     * This is the recursive step.
     *
     * @param result The set that will be returned
     * @param element the element
     * @return true if there is a valid coloring for the graph, false otherwise.
     */
    private boolean backtraking( int currentVertexIndex, List<N> nodesList, ColoredNodes<N, C> coloredNodes )
    {
        if ( currentVertexIndex != -1
                        && isThereColorConflict( nodesList.get( currentVertexIndex ), coloredNodes ) )
        {
            return false;
        }

        if ( currentVertexIndex == nodesList.size() - 1 )
        {
            return true;
        }

        int next = currentVertexIndex + 1;
        N nextNode = nodesList.get( next );
        for ( C color : colors )
        {
            coloredNodes.addColor( nextNode, color );
            boolean isDone = backtraking( next, nodesList, coloredNodes );
            if ( isDone )
            {
                return true;
            }
        }
        coloredNodes.removeColor( nextNode );
        return false;
    }

    /**
     * Tests if there is some adjacent nodes with the same color.
     *
     * @param currentNode
     * @return
     */
    private boolean isThereColorConflict( N currentNode, ColoredNodes<N, C> coloredNodes )
    {
        if ( currentNode == null )
        {
            return false;
        }

        C nextNodeColor = coloredNodes.getColor( currentNode );
        if ( nextNodeColor == null )
        {
            return false;
        }

        for ( N abj : graph.adjacentNodes( currentNode ) )
        {
            C adjColor = coloredNodes.getColor( abj );
            if ( adjColor != null && nextNodeColor.equals( adjColor ) )
            {
                return true;
            }

        }
        return false;
    }

}
