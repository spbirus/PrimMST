// CS 1501 Summer 2016
// Modified by Samuel Birus
// Modification of Sedgewick and Wayne weighted, directed graph

public class Graph {
    private int V;
    private int E;
    private Bag<DirectedEdge>[] adj;
    private boolean[] connected;

    /**
     * Create an empty edge-weighted digraph with V vertices.
     */
    public Graph(int V) {
        if (V < 0) throw new RuntimeException("Number of vertices must be nonnegative");
        this.V = V;
        this.E = 0;
        adj = (Bag<DirectedEdge>[]) new Bag[V];
        connected = new boolean[V];
        for (int v = 0; v < V; v++){
            adj[v] = new Bag<DirectedEdge>();
            connected[v] = true;
        }
    }

    /**
     * Create a edge-weighted digraph with V vertices and E edges.
     */
    public Graph(int V, int E) {
        this(V);
        if (E < 0) throw new RuntimeException("Number of edges must be nonnegative");
        for (int i = 0; i < E; i++) {
            int v = (int) (Math.random() * V);
            int w = (int) (Math.random() * V);
            double weight = Math.round(100 * Math.random()) / 100.0;
            DirectedEdge e = new DirectedEdge(v, w, weight);
            addEdge(e);
        }
    }

    /**
     * Create an edge-weighted digraph from input stream.
     */
    public Graph(In in) {
        this(in.readInt());
        int E = in.readInt();
        for (int i = 0; i < E; i++) {
            int v = in.readInt();
            int w = in.readInt();
            int weight = in.readInt();
            addEdge(new DirectedEdge(v, w, weight));
            addEdge(new DirectedEdge(w, v, weight));
        }
    }


   /**
     * Return the number of vertices in this digraph.
     */
    public int V() {
        return V;
    }

   /**
     * Return the number of edges in this digraph.
     */
    public int E() {
        return E;
    }


   /**
     * Add the edge e to this digraph.
     */
    public void addEdge(DirectedEdge e) {
        int v = e.from();
        adj[v].add(e);
        E++;
    }

    
    //allows the addition of new edges to the graph
    public void addNewEdge(DirectedEdge e){
        E++;
        int v = e.from();
        adj[v].add(e);
        connected[v] = true;
    }

    //increase the adj bag to make room for new item
    public void increase(){
        //increase the total vertices
        V++;
        //increase adj
        Bag<DirectedEdge>[] temp = (Bag<DirectedEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++){
            temp[v] = new Bag<DirectedEdge>();
        }

        for(int i = 0; i < (V-1); i++){
            for(DirectedEdge obj : adj[i]){
                temp[i].add(obj);
            }
        }     

        adj = temp;

        //increase connected
        boolean[] tempBool = new boolean[V];
        for(int k = 0; k < (V-1); k++){
            tempBool[k] = connected[k];
        } 
        connected = tempBool;
    }


   /**
     * Return the edges leaving vertex v as an Iterable.
     * To iterate over the edges leaving vertex v, use foreach notation:
     * <tt>for (DirectedEdge e : graph.adj(v))</tt>.
     */
    public Iterable<DirectedEdge> adj(int v) {
        return adj[v];
    }

   /**
     * Return all edges in this graph as an Iterable.
     * To iterate over the edges, use foreach notation:
     * <tt>for (DirectedEdge e : graph.edges())</tt>.
     */
    public Iterable<DirectedEdge> edges() {
        Bag<DirectedEdge> list = new Bag<DirectedEdge>();
        for (int v = 0; v < V; v++) {
            for (DirectedEdge e : adj(v)) {
                list.add(e);
            }
        }
        return list;
    } 

   /**
     * Return number of edges leaving v.
     */
    public int outdegree(int v) {
        return adj[v].size();
    }

    
    //change an edge
    public void changeEdge(int v, int w, double weight){
        for(int i = 0; i < V; i++){
            for(DirectedEdge e : adj(i)){
                if(e.from() == v && e.to() == w){
                    e.changeWeight(weight);
                    System.out.println("Weight of edge " + e + " changed to " + weight);
                    break;
                }
            }
        }
    }


   /**
     * Return a string representation of this graph.
     */
    public String toString() {
        String NEWLINE = System.getProperty("line.separator");
        StringBuilder s = new StringBuilder();
        for (int v = 0; v < V; v++) {
            if(connected[v] == true){
                s.append(v + ": ");
                for (DirectedEdge e : adj[v]) {
                    if(connected[e.from()] == true && connected[e.to()] == true){
                        s.append(e + "  ");
                    }
                }
                s.append(NEWLINE);
            }
        }
        return s.toString();
    }

    public String printVertex(int v){
        String NEWLINE = System.getProperty("line.separator");
        StringBuilder s = new StringBuilder();
        if(connected[v] == true){
            s.append(v + ": ");
            for (DirectedEdge e : adj[v]) {
                if(connected[e.from()] == true && connected[e.to()] == true){
                    s.append(e + "  ");
                }
            }
            //s.append(NEWLINE);
        }

        return s.toString();
    }


    //change if the node is connected or not
    public void downConnect(int v){
        connected[v] = false;
    }

    //change if the node is connected or not
    public void upConnect(int v){
        connected[v] = true;
    }

    //check connection of the node
    public boolean checkConnection(int v){
        return connected[v];
    }

    //display if the graph is connected or not
    public void networkConnectivity(){
        for(int i = 0; i < V; i++){
            if(connected[i] == false){
                System.out.println("\nThe network is disconnected");
                break;
            }
            if(V - 1 == i){
                System.out.println("\nThe network is currently connected");
            }
        }

        
    }

    //display which nodes are currently up
    public void currentNodesUp(){
        System.out.println("\nThe following nodes are currently up:");
        for(int i = 0; i < V; i++){
            if(connected[i] == true){
                System.out.print(i + " ");
            }
        }
        System.out.println("");

        System.out.println("\nThe following nodes are currently down:");
        for(int i = 0; i < V; i++){
            if(connected[i] == false){
                System.out.print(i + " ");
            }
        }
        System.out.println("");
    }

    /**
     * Test client.
     */
    public static void main(String[] args) {
        // int V = Integer.parseInt(args[0]);
        // int E = Integer.parseInt(args[1]);
        // EdgeWeightedDigraph G = new EdgeWeightedDigraph(V, E);
        // StdOut.println(G);
    }

}
