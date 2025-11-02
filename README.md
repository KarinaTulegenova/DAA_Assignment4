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
<img width="1566" height="70" alt="image" src="https://github.com/user-attachments/assets/daf5620d-57e7-4c01-8afb-58ee856dabd6" />

**Key Operations:**
* index[v]: Discovery time of vertex 𝑣— the order in which 𝑣is first visited.
* lowlink[v]: The smallest index reachable from vertex 𝑣(either directly or through descendants in DFS).

**Time Complexity:** O(V + E)

**Space Complexity:** O(V)

**Characteristics:**
* Detects all SCCs in a single DFS pass
* Foundation for graph condensation and DAG analysis
* Useful for identifying circular dependencies in smart-city networks
* One-pass DFS: Unlike Kosaraju’s, no need to reverse the graph.
* Same Time Complexity: 𝑂(𝑉+𝐸), where 𝑉is the number of vertices and 𝐸is the number of edges.

<img width="700" height="287" alt="image" src="https://github.com/user-attachments/assets/8b69ff02-1acb-49cc-80e0-60a51ad27665" />


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
<img width="382" height="340" alt="image" src="https://github.com/user-attachments/assets/db79510f-d567-49e2-b2ac-0970b549d5de" />

* Computing in-degree for each vertex
* Queue-based iteration and edge removal

**Time Complexity:** O(V + E)
**Space Complexity:** O(V)

**Characteristics:**

* Guarantees a valid linear order
* Required for dynamic programming on DAGs
<img width="520" height="340" alt="image" src="https://github.com/user-attachments/assets/1df732f5-dd6f-4c6a-898d-5bcf14569017" />



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
<img width="1136" height="362" alt="image" src="https://github.com/user-attachments/assets/d9d21eaa-699b-4985-bb27-3489f944aa76" />
<img width="306" height="294" alt="image" src="https://github.com/user-attachments/assets/bb59f4ac-d610-4d84-97dc-41f9fce8c692" />

### Collected Metrics

* Number of vertices and edges
* Number of detected SCCs
* DAG vertices and edges after condensation
* Execution time (ms)
* Operation counters (DFS calls, queue operations, relaxations)
<img width="1372" height="277" alt="image" src="https://github.com/user-attachments/assets/e861d895-f3fb-4847-84d4-46ba9eabde15" />

### Total Analysis
<img width="1739" height="376" alt="image" src="https://github.com/user-attachments/assets/09644491-8c8e-4c56-be82-01fbf831205d" />
The table below summarizes the experimental results for three datasets of different sizes — small, medium, and large.
Each dataset represents a directed weighted graph, and several metrics were collected during processing.

n (vertices) — number of nodes in the graph.
edges — number of directed connections between nodes.
scc — number of strongly connected components (SCCs) detected by Tarjan’s algorithm.
dag_nodes / dag_edges — number of nodes and edges in the condensed Directed Acyclic Graph (DAG) formed after merging SCCs.
src_comp — ID of the source component used for shortest and longest path calculations.
sp_relax / lp_relax — number of edge relaxations performed during shortest and longest path algorithms.
time_ms / time_us — total execution time in milliseconds and microseconds.
operations_count — total number of algorithmic operations (DFS traversals, queue operations, relaxations).

This table helps visualize how graph size affects computational complexity.
Larger datasets show higher execution time and more operations, demonstrating the scalability of the algorithms.



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

