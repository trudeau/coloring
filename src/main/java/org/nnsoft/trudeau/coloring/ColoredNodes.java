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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Maintains the color for each vertex and the required number of colors for {@link org.nnsoft.trudeau.Graph} coloring.
 *
 * @param <N> the Graph nodes type.
 * @param <C> the Color type.
 */
public final class ColoredNodes<N, C>
{

    private final Map<N, C> coloredNodes = new HashMap<N, C>();

    private final List<C> usedColor = new ArrayList<C>();

    /**
     * This class can be instantiated only inside the package
     */
    ColoredNodes()
    {
        // do nothing
    }

    /**
     * Store the input vertex color.
     *
     * @param node the vertex for which storing the color.
     * @param color the input vertex color.
     */
    public void addColor( N node, C color )
    {
        coloredNodes.put( node, color );
        int idx = usedColor.indexOf( color );
        if ( idx == -1 )
        {
            usedColor.add( color );
        }
        else
        {
            usedColor.set( idx, color );
        }
    }

    /**
     * Remove the input vertex color.
     *
     * @param node the vertex for which storing the color.
     */
    void removeColor( N node )
    {
        C color = coloredNodes.remove( node );
        usedColor.remove( color );
    }

    /**
     * Returns the color associated to the input vertex.
     *
     * @param node the vertex for which getting the color.
     * @return the color associated to the input vertex.
     */
    public C getColor( N node )
    {
        node = checkNotNull( node, "Impossible to get the color for a null Vertex" );

        return coloredNodes.get( node );
    }

    /**
     * Returns the number of required colors for coloring the Graph.
     *
     * @return the number of required colors for coloring the Graph.
     */
    public int getRequiredColors()
    {
        return usedColor.size();
    }

    /**
     * Tests if the input node is colored.
     * 
     * @param node the node 
     * @return true if the colored node is contained into the map, false otherwise
     */
    public boolean containsColoredNode( N node )
    {
        return coloredNodes.containsKey( node );
    }

}
