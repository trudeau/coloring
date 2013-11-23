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

import org.nnsoft.trudeau.api.GraphException;
import org.nnsoft.trudeau.api.VertexPair;
import org.nnsoft.trudeau.inmemory.BaseMutableGraph;
import org.nnsoft.trudeau.inmemory.UndirectedMutableGraph;
import org.nnsoft.trudeau.inmemory.labeled.BaseLabeledEdge;
import org.nnsoft.trudeau.inmemory.labeled.BaseLabeledVertex;

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
     * This method checks if all connected vertices have different colors.
     *
     * @param g
     * @param coloredVertices
     */
    protected static <V, E, C> void checkColoring( UndirectedMutableGraph<V, E> g,
                                                   ColoredVertices<V, C> coloredVertices )
    {
        for ( E e : g.getEdges() )
        {
            VertexPair<V> vp = g.getVertices( e );
            C h = coloredVertices.getColor( vp.getHead() );
            C t = coloredVertices.getColor( vp.getTail() );

            assertNotNull( h );
            assertNotNull( t );
            assertTrue( !h.equals( t ) );
        }
    }

    /**
     * Create a Biparted graph
     *
     * @param nVertices number of vertices
     * @param g graph
     */
    protected static void buildBipartedGraph( int nVertices, UndirectedMutableGraph<BaseLabeledVertex, BaseLabeledEdge> g )
    {
        // building Graph
        for ( int i = 0; i < nVertices; i++ )
        {
            BaseLabeledVertex v = new BaseLabeledVertex( valueOf( i ) );
            g.addVertex( v );
        }

        List<BaseLabeledVertex> fistPartition = new ArrayList<BaseLabeledVertex>();
        List<BaseLabeledVertex> secondPartition = new ArrayList<BaseLabeledVertex>();

        int count = 0;
        for ( BaseLabeledVertex v1 : g.getVertices() )
        {
            if ( count++ == nVertices / 2 )
            {
                break;
            }
            fistPartition.add( v1 );
        }

        count = 0;
        for ( BaseLabeledVertex v2 : g.getVertices() )
        {
            if ( count++ < nVertices / 2 )
            {
                continue;
            }
            secondPartition.add( v2 );
        }

        for ( BaseLabeledVertex v1 : fistPartition )
        {
            for ( BaseLabeledVertex v2 : secondPartition )
            {
                if ( !v1.equals( v2 ) )
                {
                    try
                    {
                        g.addEdge( v1, new BaseLabeledEdge( format( "%s -> %s", v1, v2 ) ), v2 );
                    }
                    catch ( GraphException e )
                    {
                        // ignore
                    }
                }
            }
        }
    }

    protected static void buildCrownGraph( int nVertices, UndirectedMutableGraph<BaseLabeledVertex, BaseLabeledEdge> g )
    {
        List<BaseLabeledVertex> tmp = new ArrayList<BaseLabeledVertex>();

        for ( int i = 0; i < nVertices; i++ )
        {
            BaseLabeledVertex v = new BaseLabeledVertex( valueOf( i ) );
            g.addVertex( v );
            tmp.add( v );
        }

        for ( int i = 0; i < nVertices; i++ )
        {
            int next = i + 1;
            if ( i == ( nVertices - 1 ) )
            {
                next = 0;
            }
            BaseLabeledEdge e = new BaseLabeledEdge( format( "%s -> %s", i, next ) );
            try
            {
                g.addEdge( tmp.get( i ), e, tmp.get( next ) );
            }
            catch ( GraphException ge )
            {
                // ignore
            }
        }
    }

    /**
     * Creates a graph that contains all classic sudoku contratints.
     *
     * @return
     */
    protected static BaseLabeledVertex[][] buildSudokuGraph( UndirectedMutableGraph<BaseLabeledVertex, BaseLabeledEdge> sudoku )
    {
        BaseLabeledVertex[][] grid = new BaseLabeledVertex[9][9];
        // build sudoku grid.
        for ( int row = 0; row < 9; row++ )
        {
            for ( int col = 0; col < 9; col++ )
            {
                grid[row][col] = new BaseLabeledVertex( format( "%s, %s", row, col ) );
                sudoku.addVertex( grid[row][col] );
            }
        }

        int[] rowsOffsets = new int[] { 0, 3, 6 };
        int[] colsOffsets = new int[] { 0, 3, 6 };

        // build constraint.
        for ( int rof = 0; rof < 3; rof++ )
        {
            for ( int cof = 0; cof < 3; cof++ )
            {
                List<BaseLabeledVertex> boxes = new ArrayList<BaseLabeledVertex>();
                for ( int row = rowsOffsets[rof]; row < 3 + rowsOffsets[rof]; row++ )
                {
                    for ( int col = colsOffsets[cof]; col < 3 + colsOffsets[cof]; col++ )
                    {
                        boxes.add( grid[row][col] );
                    }
                }

                for ( BaseLabeledVertex v1 : boxes )
                {
                    for ( BaseLabeledVertex v2 : boxes )
                    {

                        BaseLabeledEdge e = new BaseLabeledEdge( format( "%s -> %s", v1, v2 ) );
                        if ( !v1.equals( v2 ) )
                        {
                            try
                            {
                                sudoku.addEdge( v1, e, v2 );
                            }
                            catch ( GraphException ge )
                            {
                                // ignore
                            }
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
                    BaseLabeledVertex v1 = grid[j][i];
                    BaseLabeledVertex v2 = grid[j][h];

                    if ( !v1.equals( v2 ) )
                    {
                        BaseLabeledEdge e = new BaseLabeledEdge( format( "%s -> %s", v1, v2 ) );
                        try
                        {
                            sudoku.addEdge( v1, e, v2 );
                        }
                        catch ( GraphException ge )
                        {
                            // ignore
                        }
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
                    BaseLabeledVertex v1 = grid[i][j];
                    BaseLabeledVertex v2 = grid[h][j];

                    if ( !v1.equals( v2 ) )
                    {
                        BaseLabeledEdge e = new BaseLabeledEdge( format( "%s -> %s", v1, v2 ) );
                        try
                        {
                            sudoku.addEdge( v1, e, v2 );
                        }
                        catch ( GraphException ge )
                        {
                            // ignore
                        }
                    }

                }
            }
        }
        return grid;
    }



    /**
     * Creates a complete graph with nVertices
     *
     * @param nVertices number of vertices
     * @param g graph
     */
    protected static void buildCompleteGraph( int nVertices, BaseMutableGraph<BaseLabeledVertex, BaseLabeledEdge> g )
    {
        // building Graph
        for ( int i = 0; i < nVertices; i++ )
        {
            BaseLabeledVertex v = new BaseLabeledVertex( valueOf( i ) );
            g.addVertex( v );
        }

        for ( BaseLabeledVertex v1 : g.getVertices() )
        {
            for ( BaseLabeledVertex v2 : g.getVertices() )
            {
                if ( !v1.equals( v2 ) )
                {
                    try
                    {
                        g.addEdge( v1, new BaseLabeledEdge( format( "%s -> %s", v1, v2 ) ), v2 );
                    }
                    catch ( GraphException e )
                    {
                        // ignore
                    }
                }
            }
        }
    }

}
