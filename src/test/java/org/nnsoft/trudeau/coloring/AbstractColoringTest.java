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

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.Graph;
import com.google.common.graph.MutableGraph;

/**
 * Abstract class used for test coloring.
 */
abstract class AbstractColoringTest
{

    /**
     * Return a random association with index and a color string in RGB.
     */
    protected static Map<Integer, String> createColorMap( int numColor )
    {
        Map<Integer, String> colorCodes = new HashMap<Integer, String>();
        for ( int i = 0; i < 100; i++ )
        {
            Random rnd = new Random( i );
            colorCodes.put( i, String.format( "\"#%2x%2x%2x\"", rnd.nextInt( 255 ), rnd.nextInt( 255 ), rnd.nextInt( 255 ) ) );
        }
        return colorCodes;
    }

    /**
     * Creates a list of integer colors.
     *
     * @param colorNumber number of colors
     * @return the list.
     */
    protected static Set<Integer> createColorsList( int colorNumber )
    {
        Set<Integer> colors = new HashSet<Integer>();
        for ( int j = 0; j < colorNumber; j++ )
        {
            colors.add( j );
        }
        return colors;
    }

    /**
     * This method checks if all connected nodes have different colors.
     *
     * @param g
     * @param coloredNodes
     */
    protected static <N, C> void checkColoring( Graph<N> g,
                                                ColoredNodes<N, C> coloredNodes )
    {
        for ( EndpointPair<N> e : g.edges() )
        {
            C h = coloredNodes.getColor( e.nodeU() );
            C t = coloredNodes.getColor( e.nodeV() );

            assertNotNull( h );
            assertNotNull( t );
            assertTrue( !h.equals( t ) );
        }
    }

    /**
     * Create a Biparted graph
     *
     * @param nNodes number of nodes
     * @param g graph
     */
    protected static void buildBipartedGraph( int nNodes, MutableGraph<String> g )
    {
        // building Graph
        for ( int i = 0; i < nNodes; i++ )
        {
            String v = valueOf( i );
            g.addNode( v );
        }

        List<String> fistPartition = new ArrayList<String>();
        List<String> secondPartition = new ArrayList<String>();

        int count = 0;
        for ( String v1 : g.nodes() )
        {
            if ( count++ == nNodes / 2 )
            {
                break;
            }
            fistPartition.add( v1 );
        }

        count = 0;
        for ( String v2 : g.nodes() )
        {
            if ( count++ < nNodes / 2 )
            {
                continue;
            }
            secondPartition.add( v2 );
        }

        for ( String v1 : fistPartition )
        {
            for ( String v2 : secondPartition )
            {
                if ( !v1.equals( v2 ) )
                {
                    g.putEdge( v1, v2 );
                }
            }
        }
    }

    protected static void buildCrownGraph( int nNodes, MutableGraph<String> g )
    {
        List<String> tmp = new ArrayList<String>();

        for ( int i = 0; i < nNodes; i++ )
        {
            String v = valueOf( i );
            g.addNode( v );
            tmp.add( v );
        }

        for ( int i = 0; i < nNodes; i++ )
        {
            int next = i + 1;
            if ( i == ( nNodes - 1 ) )
            {
                next = 0;
            }
            g.putEdge( tmp.get( i ), tmp.get( next ) );
        }
    }

    /**
     * Creates a graph that contains all classic sudoku contratints.
     *
     * @return
     */
    protected static String[][] buildSudokuGraph( MutableGraph<String> sudoku )
    {
        String[][] grid = new String[9][9];
        // build sudoku grid.
        for ( int row = 0; row < 9; row++ )
        {
            for ( int col = 0; col < 9; col++ )
            {
                grid[row][col] = format( "[%s, %s]", row, col );
                sudoku.addNode( grid[row][col] );
            }
        }

        int[] rowsOffsets = new int[] { 0, 3, 6 };
        int[] colsOffsets = new int[] { 0, 3, 6 };

        // build constraint.
        for ( int rof = 0; rof < 3; rof++ )
        {
            for ( int cof = 0; cof < 3; cof++ )
            {
                List<String> boxes = new ArrayList<String>();
                for ( int row = rowsOffsets[rof]; row < 3 + rowsOffsets[rof]; row++ )
                {
                    for ( int col = colsOffsets[cof]; col < 3 + colsOffsets[cof]; col++ )
                    {
                        boxes.add( grid[row][col] );
                    }
                }

                for ( String v1 : boxes )
                {
                    for ( String v2 : boxes )
                    {
                        if ( !v1.equals( v2 ) )
                        {
                            sudoku.putEdge( v1, v2 );
                        }
                    }
                }
            }
        }

        // create rows constraint
        for ( int j = 0; j < 9; j++ )
        {
            for ( int i = 0; i < 9; i++ )
            {
                for ( int h = 0; h < 9; h++ )
                {
                    String v1 = grid[j][i];
                    String v2 = grid[j][h];

                    if ( !v1.equals( v2 ) )
                    {
                        sudoku.putEdge( v1, v2 );
                    }

                }
            }
        }

        // create cols constraint
        for ( int j = 0; j < 9; j++ )
        {
            for ( int i = 0; i < 9; i++ )
            {
                for ( int h = 0; h < 9; h++ )
                {
                    String v1 = grid[i][j];
                    String v2 = grid[h][j];

                    if ( !v1.equals( v2 ) )
                    {
                        sudoku.putEdge( v1, v2 );
                    }

                }
            }
        }
        return grid;
    }

    /**
     * Creates a complete graph with nNodes
     *
     * @param nNodes number of nodes
     * @param g graph
     */
    protected static void buildCompleteGraph( int nNodes, MutableGraph<String> g )
    {
        // building Graph
        for ( int i = 0; i < nNodes; i++ )
        {
            String v = valueOf( i );
            g.addNode( v );
        }

        for ( String v1 : g.nodes() )
        {
            for ( String v2 : g.nodes() )
            {
                if ( !v1.equals( v2 ) )
                {
                    g.putEdge( v1, v2 );
                }
            }
        }
    }

}
