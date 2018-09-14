//Samuel Birus
//Credit to Segwick and Wayne for some of the classes required for this project

import java.util.Scanner;
import java.util.ArrayList;

public class Network{
	private static Graph G;
	private static double pathWeight = 0; //weight of the path
	private static int startPath = 0; //where the path starts, gets changed from 0
	private static int pathNumber = 1; //number of path, useful for output
	private static ArrayList<DirectedEdge> path; //keeps track of each path in recursive loop
	private static boolean[] visited; //needed for path determination so that it doesn't loop itself

	public static void main(String[] args){
		//read in input file and create graph from it
		System.out.println("\nINPUT FILE: " + args[0]);
		System.out.println("-----------------------");
		In in = new In(args[0]);
		G = new Graph(in);

		//menu for the options
		while(true){
			Scanner reader = new Scanner(System.in);

			//read in command and branch to right if statement
			System.out.print("\nPlease enter a command: ");
			String option = reader.next();

			if(option.toUpperCase().equals("R")){
				//display current active network
				System.out.println("Command R:");
				System.out.println("----------");

				//print connection
				G.networkConnectivity();
				//print nodes currently up and down
				G.currentNodesUp();

				//print graph
				System.out.println("\n" + G.toString());

			}else if(option.toUpperCase().equals("M")){
				//show MST
				System.out.println("Command M:");
				System.out.println("----------");

				PrimMST mst = new PrimMST(G);

				System.out.println("\nTotal weight = " + mst.weight());
				System.out.println("\nThe edges in the MST follow: ");
        		for (DirectedEdge e : mst.edges()){
            		System.out.println(e);
            	}

			}else if(option.toUpperCase().equals("S")){
				//display shortest path
				int i = reader.nextInt();
				int j = reader.nextInt();

				System.out.println("Command S " + i + " " + j + ": ");
				System.out.println("--------------");

				DijkstraSP sp = new DijkstraSP(G, i);
				
        		
            		if (sp.hasPathTo(j)) {
            			//if there is a path
            			System.out.println("\nShortest path from " + i + " to " + j);
                		System.out.printf("\tTotal: (%.2f)  \n",sp.distTo(j));
                		for (DirectedEdge e : sp.pathTo(j)) {
                    		System.out.println("\t\t" + e + "   ");
                		}
                		System.out.println();
            		}
            		else {
            			//if there isn't a path
                		System.out.printf("There is no path from %d to %d\n", i, j);
            		}
        			

			}else if(option.toUpperCase().equals("P")){
				//display distinct path
				int i = reader.nextInt();
				int j = reader.nextInt();
				int x = reader.nextInt();

				System.out.println("Command P " + i + " " + j + " " + x+ ":");
				System.out.println("-----------------");
				System.out.println("Distinct Paths from " + i + " to " + j +" (differing by at least one edge):\n");

				//setup for the recursive call
				startPath = i;
				path = new ArrayList<DirectedEdge>();
				visited = new boolean[G.V()];
				for(int k = 0; k < G.V(); k++){
					visited[k] = false;
				}

				//start of recursive call
				distinctPaths(i, j, x);

				System.out.println("\nTotal Paths: " + (pathNumber - 1));

				//reset pathNumber to 1 so that paths can be called again on a new set
				pathNumber = 1;
				

			}else if(option.toUpperCase().equals("D")){
				//node i will go down

				//check if i is a valid node
				int i = reader.nextInt();
				while(i < 0 || i > G.V()){
					System.out.println("Error: not a valid vertex.  \nEnter another number:");
					i = reader.nextInt();
				}

				System.out.println("Command D " + i + ":");
				System.out.println("------------");

				G.downConnect(i);
				System.out.println("Vertex " + i + " has gone down");

			}else if(option.toUpperCase().equals("U")){
				//node i will go up

				//check if i is a valid node
				int i = reader.nextInt();
				while(i < 0 || i > G.V()){
					System.out.println("Error: not a valid vertex.  \nEnter another number:");
					i = reader.nextInt();
				}

				System.out.println("Command U " + i + ":");
				System.out.println("------------");

				G.upConnect(i);
				System.out.println("Vertex " + i + " is back online");

			}else if(option.toUpperCase().equals("C")){
				//change weight of edge
				int i = reader.nextInt();
				int j = reader.nextInt();
				double x = reader.nextDouble();

				System.out.println("Command C " + i + " " + j + " " + x+ ":");
				System.out.println("-----------------");

				//change the weight of the edge
				G.changeEdge(i, j, x);


			}else if(option.toUpperCase().equals("Q")){
				//quit
				System.out.println("Command Q:");
				System.out.println("----------");

				System.out.println("\nYou quit the program");

				break;

			}else if(option.toUpperCase().equals("A")){
				//add vertices

				//-----------------------------------------
				//COMMAND FOR INSERTING IS 
				//A i x
				//where i is the old vertex that the new vertex is connecting to 
				//and x is the weight of that new edge
				//-----------------------------------------
				//EXTRA CREDIT

				int j = G.V();
				G.increase();

				while(true){
					//ask for which other vertices to connect to 
					//check if i is a valid node
					int i = reader.nextInt();
					while(i < 0 || i > G.V()){
						System.out.println("Error: not a valid vertex.  \nEnter another number:");
						i = reader.nextInt();
					}

					double weight = reader.nextDouble();

					G.addNewEdge(new DirectedEdge(j, i, weight));
            		G.addNewEdge(new DirectedEdge(i, j, weight));

            		//stops the addition of new edges
            		System.out.println("Enter \"C\" to continue entering edges for the new vertex. Enter \"Q\"to quit entering edges");
            		String stop = reader.next();
            		if(stop.toUpperCase().equals("Q")){
            			break;
            		}
            		System.out.println("Enter a vertex to connect to and a weight");
				}
			}else{
				System.out.println("Illegal Command");
			}

		}		
	}

	private static void distinctPaths(int i, int j, int w){

		//check if both the nodes are active or not
		//crucial to working with nodes up and nodes down
		//unsure if this had to be added
		if(G.checkConnection(i) && G.checkConnection(j)){

			//check if there is a complete path
			if(i == j && pathWeight <= w){
				System.out.println("Path " + pathNumber + ": Total weight: "+ pathWeight);
				for(DirectedEdge e : path){
					System.out.print("\t" + e );
				}	
				System.out.println("\n");
				pathNumber++;
			}

			//check if the total weight is larger than inputed
			if(pathWeight <= w){
				for (DirectedEdge e : G.adj(i)){
					//add the weight
					pathWeight += e.weight();
					//add the edge
					path.add(e);
					//check off that the node was visited
					visited[i] = true;

					//call recursively
					if(visited[e.to()] == false)
						distinctPaths(e.to(), j, w);

					//undo all of the previous adds
					pathWeight -= e.weight();
					path.remove(e);
					visited[i] = false;
				}
			} else {
				return;
			}
		}

	}
}