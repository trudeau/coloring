coloring
========

[Graph coloring](http://en.wikipedia.org/wiki/Graph_coloring) problem solver implementation

# Usage

Given a generic `org.nnsoft.trudeau.api.UndirectedGraph<V, E>` instance and a `java.util.Set<C>` of colors that have to be used to solve the problem, users can find solutions by applying the [Greedy](http://en.wikipedia.org/wiki/Greedy_coloring) algorithm:

```
import static org.nnsoft.trudeau.coloring.ColoringSolver.coloring;

import java.util.Set;
import org.nnsoft.trudeau.api.UndirectedGraph;
import org.nnsoft.trudeau.coloring.ColoredNodes;

…

UndirectedGraph<V, E> graph;
Set<C> colors;

// variables initialization omissed

ColoredNodes<V, C> coloredNodes =
    coloring( graph ).withColors( colors ).applyingGreedyAlgorithm();
```

or the [Backtracking](https://secweb.cs.odu.edu/~zeil/cs361/web/website/Lectures/npprobs/pages/ar01s01s01.html) algorithm:

```
ColoredNodes<V, C> coloredNodes =
    coloring( graph ).withColors( colors ).applyingBackTrackingAlgorithm();

```

the Backtracking agorithm also accepts a pre-defined subset of vertices already colored:

```
ColoredNodes<V, C> predefinedColors = new ColoredNodes<V, C>();
predefinedColors.addColor( V, C );

ColoredNodes<V, C> coloredNodes =
    coloring( graph ).withColors( colors ).applyingBackTrackingAlgorithm( predefinedColors );
```