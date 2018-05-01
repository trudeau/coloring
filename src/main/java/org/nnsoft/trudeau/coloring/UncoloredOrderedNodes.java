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

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;

/**
 * 
 *
 * @param <N>
 */
final class UncoloredOrderedNodes<N>
    implements Comparator<Integer>, Iterable<N>
{

    private final Map<Integer, Set<N>> orderedNodes = new TreeMap<Integer, Set<N>>( this );

    public void addVertexDegree( N n, Integer degree )
    {
        Set<N> nodes = orderedNodes.get( degree );

        if ( nodes == null )
        {
            nodes = new HashSet<N>();
        }

        nodes.add( n );
        orderedNodes.put( degree, nodes );
    }

    /**
     * {@inheritDoc}
     */
    public int compare( Integer o1, Integer o2 )
    {
        return o2.compareTo( o1 );
    }

    public Iterator<N> iterator()
    {
        return new Iterator<N>()
        {

            private Iterator<Integer> keys = orderedNodes.keySet().iterator();

            private Iterator<N> pending = null;

            private N next = null;

            public boolean hasNext()
            {
                if ( next != null )
                {
                    return true;
                }

                while ( ( pending == null ) || !pending.hasNext() )
                {
                    if ( !keys.hasNext() )
                    {
                        return false;
                    }
                    pending = orderedNodes.get( keys.next() ).iterator();
                }

                next = pending.next();
                return true;
            }

            public N next()
            {
                if ( !hasNext() )
                {
                    throw new NoSuchElementException();
                }
                N returned = next;
                next = null;
                return returned;
            }

            public void remove()
            {
                pending.remove();
            }

        };
    }

    /**
     * Returns the number of nodes degrees in the graph.
     *
     * @return the number of nodes degrees in the graph.
     */
    public int size()
    {
        return orderedNodes.size();
    }

}
