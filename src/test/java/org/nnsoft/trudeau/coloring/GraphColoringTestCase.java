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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.nnsoft.trudeau.coloring.ColoringSolver.coloring;
import static org.nnsoft.trudeau.connector.GraphConnector.populate;

import java.util.Set;

import org.junit.Test;
import org.nnsoft.trudeau.connector.AbstractMutableGraphConnection;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

/**
 *
 */
public class GraphColoringTestCase extends AbstractColoringTest
{

    private Set<Integer> colors = createColorsList( 11 );

    @Test( expected = NullPointerException.class )
    public void testNullGraph()
        throws NotEnoughColorsException
    {
        coloring( null ).withColors( null ).applyingGreedyAlgorithm();
    }

    @Test( expected = NullPointerException.class )
    public void testNullColorGraph()
        throws NotEnoughColorsException
    {
        MutableGraph<String> g =
            GraphBuilder.undirected().build();

        coloring( g ).withColors( null ).applyingBackTrackingAlgorithm();
    }

    @Test
    public void testEmptyGraph()
        throws NotEnoughColorsException
    {
        MutableGraph<String> g =
            GraphBuilder.undirected().build();

        ColoredNodes<String, Integer> coloredNodes = coloring( g ).withColors( createColorsList( 1 ) ).applyingGreedyAlgorithm();
        assertNotNull( coloredNodes );
        assertEquals( 0, coloredNodes.getRequiredColors() );
    }

    @Test( expected = NotEnoughColorsException.class )
    public void testNotEnoughtColorGreedyGraph()
        throws NotEnoughColorsException
    {
        final String two = "2";

        MutableGraph<String> g = GraphBuilder.undirected().build();
        populate( g )
        .withConnections( new AbstractMutableGraphConnection<String>()
        {

            @Override
            public void connect()
            {
                String one = addNode( "1" );
                addNode( two );
                String three = addNode( "3" );

                connect( one ).to( two );
                connect( two ).to( three );
                connect( three ).to( one );
            }

        } );
        coloring( g ).withColors( createColorsList( 1 ) ).applyingGreedyAlgorithm();
    }

    @Test
    public void testCromaticNumber()
        throws NotEnoughColorsException
    {
        MutableGraph<String> g = GraphBuilder.undirected().build();
        populate( g )
        .withConnections( new AbstractMutableGraphConnection<String>()
        {

            @Override
            public void connect()
            {
                String one = addNode( "1" );
                String two = addNode( "2" );
                String three = addNode( "3" );

                connect( one ).to( two );
                connect( two ).to( three );
                connect( three ).to( one );
            }

        } );

        ColoredNodes<String, Integer> coloredNodes =
                        coloring( g ).withColors( colors ).applyingGreedyAlgorithm();
        checkColoring( g, coloredNodes );
    }

    @Test
    public void testCromaticNumberComplete()
        throws NotEnoughColorsException
    {
        MutableGraph<String> g1 =
            GraphBuilder.undirected().build();
        buildCompleteGraph( 100, g1 );

        ColoredNodes<String, Integer> coloredNodes =
                        coloring( g1 ).withColors( createColorsList( 100 ) ).applyingGreedyAlgorithm();
        checkColoring( g1, coloredNodes );
    }

    @Test
    public void testCromaticNumberBiparted()
        throws NotEnoughColorsException
    {
        MutableGraph<String> g1 =
            GraphBuilder.undirected().build();
        buildBipartedGraph( 100, g1 );

        ColoredNodes<String, Integer> coloredNodes =
                        coloring( g1 ).withColors( colors ).applyingGreedyAlgorithm();

        checkColoring( g1, coloredNodes );
    }

    @Test
    public void testCromaticNumberSparseGraph()
        throws NotEnoughColorsException
    {
        MutableGraph<String> g1 =
            GraphBuilder.undirected().build();
        for ( int i = 0; i < 100; i++ )
        {
            g1.addNode( String.valueOf( i ) );
        }

        ColoredNodes<String, Integer> coloredNodes =
                        coloring( g1 ).withColors( colors ).applyingGreedyAlgorithm();

        assertEquals( 1, coloredNodes.getRequiredColors() );
        checkColoring( g1, coloredNodes );
    }

    /**
     * see <a href="http://en.wikipedia.org/wiki/Crown_graph">wiki</a> for more details
     */
    @Test
    public void testCrawnGraph()
        throws NotEnoughColorsException
    {
        MutableGraph<String> g =
            GraphBuilder.undirected().build();
        buildCrownGraph( 6, g );

        ColoredNodes<String, Integer> coloredNodes =
                        coloring( g ).withColors( colors ).applyingGreedyAlgorithm();
        checkColoring( g, coloredNodes );
    }

    @Test
    public void testSudoku()
        throws NotEnoughColorsException
    {
        MutableGraph<String> g1 =
            GraphBuilder.undirected().build();
        buildSudokuGraph( g1 );

        ColoredNodes<String, Integer> sudoku =
                        coloring( g1 ).withColors( colors ).applyingGreedyAlgorithm();
        checkColoring( g1, sudoku );
    }

}
