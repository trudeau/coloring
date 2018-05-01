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

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.junit.Test;
import org.nnsoft.trudeau.connector.AbstractMutableGraphConnection;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

/**
 *
 */
public class GraphColoringBackTrackingTestCase
    extends AbstractColoringTest
{

    @Test( expected = NullPointerException.class )
    public void testNullGraph()
        throws NotEnoughColorsException
    {
        coloring( null ).withColors( null ).applyingBackTrackingAlgorithm();
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

        ColoredNodes<String, Integer> coloredNodes =
            coloring( g ).withColors( createColorsList( 1 ) ).applyingBackTrackingAlgorithm();
        assertNotNull( coloredNodes );
        assertEquals( 0, coloredNodes.getRequiredColors() );
    }

    @Test( expected = NotEnoughColorsException.class )
    public void testNotEnoughtColorGraph()
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
                String three = new String( "3" );

                connect( one ).to( two );
                connect( two ).to( three );
                connect( three ).to( one );
            }

        } );

        coloring( g ).withColors( createColorsList( 1 ) ).applyingBackTrackingAlgorithm();
    }

    @Test
    public void testCromaticNumber()
        throws NotEnoughColorsException
    {
        final String two = new String( "2" );

        MutableGraph<String> g = GraphBuilder.undirected().build();

        populate( g )
        .withConnections( new AbstractMutableGraphConnection<String>()
        {

            @Override
            public void connect()
            {
                String one = addNode( new String( "1" ) );
                addNode( two );
                String three = addNode( new String( "3" ) );

                connect( one ).to( two );
                connect( two ).to( three );
                connect( three ).to( one );
            }

        } );

        ColoredNodes<String, Integer> coloredVertex = new ColoredNodes<String, Integer>();
        coloredVertex.addColor( two, 2 );

        ColoredNodes<String, Integer> coloredNodes =
            coloring( g ).withColors( createColorsList( 3 ) ).applyingBackTrackingAlgorithm( coloredVertex );
        assertNotNull( coloredNodes );
        assertEquals( 3, coloredNodes.getRequiredColors() );
        assertEquals( new Integer( 2 ), coloredNodes.getColor( two ) );
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
            coloring( g1 ).withColors( createColorsList( 100 ) ).applyingBackTrackingAlgorithm();
        assertNotNull( coloredNodes );
        assertEquals( 100, coloredNodes.getRequiredColors() );
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
            coloring( g1 ).withColors( createColorsList( 2 ) ).applyingBackTrackingAlgorithm();
        assertNotNull( coloredNodes );
        assertEquals( 2, coloredNodes.getRequiredColors() );
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
            coloring( g1 ).withColors( createColorsList( 1 ) ).applyingBackTrackingAlgorithm();
        assertNotNull( coloredNodes );
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
            coloring( g ).withColors( createColorsList( 2 ) ).applyingBackTrackingAlgorithm();
        assertNotNull( coloredNodes );
        assertEquals( 2, coloredNodes.getRequiredColors() );
        checkColoring( g, coloredNodes );
    }

    @Test
    public void testSudoku()
        throws Exception
    {
        MutableGraph<String> g1 =
            GraphBuilder.undirected().build();
        String[][] grid = buildSudokuGraph( g1 );

        ColoredNodes<String, Integer> sudoku =
            coloring( g1 ).withColors( createColorsList( 9 ) ).applyingBackTrackingAlgorithm();
        assertNotNull( sudoku );
        checkColoring( g1, sudoku );
        assertEquals( 9, sudoku.getRequiredColors() );

        // Printout the result
        StringBuilder sb = new StringBuilder();
        NumberFormat nf = new DecimalFormat( "00" );
        sb.append( "\n" );
        for ( int i = 0; i < 9; i++ )
        {
            for ( int j = 0; j < 9; j++ )
            {
                sb.append( "| " + nf.format( sudoku.getColor( grid[i][j] ) ) + " | " );
            }
            sb.append( "\n" );
        }
        System.out.println( sb.toString() );
    }

    @Test
    public void testSudokuWithConstraints()
        throws Exception
    {
        MutableGraph<String> g1 =
            GraphBuilder.undirected().build();
        String[][] grid = buildSudokuGraph( g1 );

        ColoredNodes<String, Integer> predefinedColor = new ColoredNodes<String, Integer>();
        predefinedColor.addColor( grid[0][0], 1 );
        predefinedColor.addColor( grid[5][5], 8 );
        predefinedColor.addColor( grid[1][2], 5 );

        ColoredNodes<String, Integer> sudoku =
            coloring( g1 ).withColors( createColorsList( 9 ) ).applyingBackTrackingAlgorithm( predefinedColor );
        assertNotNull( sudoku );
        checkColoring( g1, sudoku );
        assertEquals( 9, sudoku.getRequiredColors() );

        assertEquals( new Integer( 1 ), sudoku.getColor( grid[0][0] ) );
        assertEquals( new Integer( 8 ), sudoku.getColor( grid[5][5] ) );
        assertEquals( new Integer( 5 ), sudoku.getColor( grid[1][2] ) );

        // Printout the result
        StringBuilder sb = new StringBuilder();
        NumberFormat nf = new DecimalFormat( "00" );
        sb.append( "\n" );
        for ( int i = 0; i < 9; i++ )
        {
            for ( int j = 0; j < 9; j++ )
            {
                sb.append( "| " );
                sb.append( nf.format( sudoku.getColor( grid[i][j] ) ) );
                sb.append( " | " );
            }
            sb.append( "\n" );
        }
        System.out.println( sb.toString() );
    }

}
