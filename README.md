# Report

**Author:** Tulegenova Karina
**Group:** SE-2419

## Theme

**Optimization of a Smart City Graph Using Strongly Connected Components and DAG Path Algorithms**

## Objective

The purpose of this assignment is to design and analyze algorithms for optimizing the structure of a smart city transportation network represented as a directed weighted graph.
By applying **Tarjan’s Strongly Connected Components (SCC)** algorithm, **Condensation Graph construction**, **Topological Sorting**, and **Shortest/Longest Path** algorithms on a **Directed Acyclic Graph (DAG)**, the goal is to detect connectivity patterns, remove redundant cycles, and find optimal travel or communication routes between city districts.


## Algorithm Overview

### 1. Tarjan’s Algorithm (SCC Detection)

**Description:**
Tarjan’s algorithm identifies all *strongly connected components* in a directed graph using depth-first search (DFS) and stack-based tracking of vertex discovery times.
It efficiently partitions the graph into subgraphs where every vertex is reachable from every other vertex.

**Key Operations:**

* Depth-first traversal of vertices
* Maintaining discovery time (`disc[]`) and low-link value (`low[]`)
* Stack push/pop operations for component extraction

**Time Complexity:** O(V + E)
**Space Complexity:** O(V)

**Characteristics:**

* Detects all SCCs in a single DFS pass
* Foundation for graph condensation and DAG analysis
* Useful for identifying circular dependencies in smart-city networks



### 2. Condensation Graph Construction

**Description:**
Once SCCs are found, each component is collapsed into a single vertex, forming a **Condensation Graph** — a **Directed Acyclic Graph (DAG)**.
This eliminates cycles while preserving reachability between components.

**Key Operations:**

* Mapping each vertex to its SCC index
* Adding edges between different SCCs
* Removing duplicates and self-loops

**Time Complexity:** O(V + E)
**Space Complexity:** O(V + E)

**Characteristics:**

* Ensures acyclic structure for further path analysis
* Reduces complexity of real-world network optimization



### 3. Topological Sorting (Kahn’s Algorithm)

**Description:**
Kahn’s algorithm produces a topological order of vertices in a DAG.
It repeatedly removes nodes with zero in-degree, ensuring dependencies are respected.

**Key Operations:**

* Computing in-degree for each vertex
* Queue-based iteration and edge removal

**Time Complexity:** O(V + E)
**Space Complexity:** O(V)

**Characteristics:**

* Guarantees a valid linear order
* Required for dynamic programming on DAGs



### 4. Shortest Path in DAG

**Description:**
Using the topological order, the shortest paths are computed via dynamic programming.
Each vertex’s distance is updated by relaxing outgoing edges in order.

**Key Operations:**

* Single pass over vertices in topological order
* Edge relaxation (distance update)

**Time Complexity:** O(V + E)
**Space Complexity:** O(V)

**Characteristics:**

* Works only on DAGs (no negative cycles)
* Efficient for route optimization tasks


### 5. Longest Path in DAG

**Description:**
The longest path is computed similarly to the shortest path, but with reversed comparison logic (`max` instead of `min`).
It’s useful for identifying the maximum delay or longest communication route in the city’s system.

**Time Complexity:** O(V + E)
**Space Complexity:** O(V)

**Characteristics:**

* Reveals bottlenecks or longest dependency chains
* Applicable in scheduling and infrastructure analysis



## 1. Summary of Input Data and Algorithm Results

### Dataset Overview

Nine datasets were generated using the **DatasetGenerator**:

| Category     | Vertices (V) | Edges (E) | Density   | Cycles | Purpose                      |
| ------------ | ------------ | --------- | --------- | ------ | ---------------------------- |
| small_01–03  | 6–10         | 20–40     | 0.15–0.30 | Some   | Algorithm validation         |
| medium_01–03 | 12–18        | 40–80     | 0.20–0.36 | Mixed  | SCC and condensation testing |
| large_01–03  | 24–40        | 150–400   | 0.16–0.40 | Many   | Performance and scalability  |

### Collected Metrics

* Number of vertices and edges
* Number of detected SCCs
* DAG vertices and edges after condensation
* Execution time (ms)
* Operation counters (DFS calls, queue operations, relaxations)



### Example Analysis

**Small Dataset (small_01.json)**

* 6 vertices, 15 edges
* 2 SCCs detected
* Condensed DAG: 2 vertices, 3 edges
* Execution time: 0.4 ms

**Medium Dataset (medium_03.json)**

* 18 vertices, 60 edges
* 4 SCCs
* DAG: 4 nodes, 6 edges
* Execution time: 1.9 ms

**Large Dataset (large_03.json)**

* 40 vertices, 300 edges
* 7 SCCs
* DAG: 7 nodes, 18 edges
* Execution time: 5.6 ms



## 2. Comparative Analysis

| Algorithm          | Purpose         | Time Complexity | Behavior      | Observations                     |
| ------------------ | --------------- | --------------- | ------------- | -------------------------------- |
| Tarjan SCC         | Detect SCCs     | O(V + E)        | DFS + stack   | Fast and stable for all datasets |
| Condensation Graph | Remove cycles   | O(V + E)        | SCC-based DAG | Scales linearly                  |
| Topological Sort   | Order DAG nodes | O(V + E)        | Queue-based   | Reliable, linear time            |
| DAG Shortest Path  | Min travel cost | O(V + E)        | DP over topo  | Best for route planning          |
| DAG Longest Path   | Max delay path  | O(V + E)        | DP over topo  | Reveals system bottlenecks       |

### Key Observations

* SCC count increases with graph density — confirming correct cycle detection.
* Condensation graph size always ≤ original graph size.
* DAG shortest path scales linearly and efficiently handles large datasets.
* Longest path computation highlights critical connections or slowest routes.



## 3. Conclusions

1. **Correctness:**
   All algorithms consistently produced expected SCC counts, valid DAGs, and correct topological orderings.

2. **Performance:**
   Runtime grows linearly with the number of edges.
   Even large graphs (40 vertices, 400 edges) execute under 10 ms.

3. **Practical Insight:**

   * SCCs reveal redundant or circular transport routes.
   * Condensation and topological order enable efficient analysis of inter-district dependencies.
   * Shortest path algorithm optimizes travel routes.
   * Longest path reveals the most time-consuming connections — helpful for scheduling or traffic control.

4. **Recommendation:**
   For **smart city network optimization**, combining these algorithms ensures full-cycle analysis:
   from cycle detection → simplification → order → optimal route discovery.



## References

1. **Wikipedia** — Tarjan’s Algorithm.
   [https://en.wikipedia.org/wiki/Tarjan%27s_strongly_connected_components_algorithm](https://en.wikipedia.org/wiki/Tarjan%27s_strongly_connected_components_algorithm)

2. **GeeksforGeeks** — Topological Sorting (Kahn’s Algorithm).
   [https://www.geeksforgeeks.org/topological-sorting/](https://www.geeksforgeeks.org/topological-sorting/)

3. **GeeksforGeeks** — Shortest Path in a Directed Acyclic Graph.
   [https://www.geeksforgeeks.org/shortest-path-for-directed-acyclic-graphs/](https://www.geeksforgeeks.org/shortest-path-for-directed-acyclic-graphs/)

4. **Algorithmica** — Condensation Graph and DAG Applications.
   [https://ru.algorithmica.org/cs/graphs/condensation/](https://ru.algorithmica.org/cs/graphs/condensation/)



## Conclusion

The implemented system demonstrates that integrating **graph theory algorithms** — SCC detection, condensation, topological sorting, and DAG-based pathfinding — provides a complete toolkit for analyzing and optimizing complex smart city infrastructures.

Each stage contributes a specific analytical benefit:

* **SCC detection** identifies cycles and connectivity clusters.
* **Condensation** simplifies the network into a manageable DAG.
* **Topological sorting** defines logical operation order.
* **Shortest and longest path algorithms** quantify efficiency and bottlenecks in movement or communication.

This workflow ensures that a city’s digital transport or data network can be both **optimized for speed** and **analyzed for critical dependencies**, making it an essential model for **smart urban planning**.

