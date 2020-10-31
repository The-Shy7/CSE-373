package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */


    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        HashMap<V, E> spt = new HashMap<>();

        if (Objects.equals(start, end)) {
            return spt;
        }

        HashMap<V, Double> distTo = new HashMap<>();
        ExtrinsicMinPQ<V> perimeter = createMinPQ();

        // initialize distTo with all nodes mapped to infinity, except start to 0
        for (E edge : graph.outgoingEdgesFrom(start)) {
            distTo.put(edge.to(), Double.POSITIVE_INFINITY);
        }

        perimeter.add(start, 0);
        distTo.put(start, 0.0);

        while (!perimeter.isEmpty()) {
            V from = perimeter.removeMin();

            if (Objects.equals(from, end)) {
                break;
            }

            // standard Dijkstra's algorithm.
            for (E edge : graph.outgoingEdgesFrom(from)) {
                V to = edge.to();

                // initialize all undiscovered edges around u to infinity
                if (!distTo.containsKey(to)) {
                    distTo.put(to, Double.POSITIVE_INFINITY);
                }

                double old = distTo.get(to);
                double newDist = distTo.get(from) + edge.weight();

                if (newDist < old) {
                    spt.put(to, edge);
                    distTo.put(to, newDist);

                    if (perimeter.contains(to)) {
                        perimeter.changePriority(to, newDist);
                    } else {
                        perimeter.add(to, newDist);
                    }
                }
            }
        }

        return spt;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }

        if (spt.containsKey(end)) {
            List<E> edges = new LinkedList<>();
            E edge = spt.get(end);
            V to;
            edges.add(edge);

            while (!Objects.equals(edge.from(), start)) {
                to = edge.from();
                edge = spt.get(to);
                edges.add(edge);
            }

            Collections.reverse(edges);
            return new ShortestPath.Success<>(edges);
        }

        return new ShortestPath.Failure<>();
    }

}
