package spiderman;
import java.util.*;

/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * DimensionInputFile name is passed through the command line as args[0]
 * Read from the DimensionsInputFile with the format:
 * 1. The first line with three numbers:
 *      i.    a (int): number of dimensions in the graph
 *      ii.   b (int): the initial size of the cluster table prior to rehashing
 *      iii.  c (double): the capacity(threshold) used to rehash the cluster table 
 * 
 * Step 2:
 * ClusterOutputFile name is passed in through the command line as args[1]
 * Output to ClusterOutputFile with the format:
 * 1. n lines, listing all of the dimension numbers connected to 
 *    that dimension in order (space separated)
 *    n is the size of the cluster table.
 * 
 * @author Seth Kelley
 */

public class Clusters {
    
    private ClusterNode[] clusters; //next one
    private int tableSize;
    private double threshold;
   

    public static void main(String[] args) {

        if ( args.length < 2 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.Clusters <dimension INput file> <collider OUTput file>");
                return;
        }

        String inputFile = args[0];
        String outputFile = args[1];

        Clusters c = new Clusters();

        c.createTable(inputFile);

        StdOut.setFile(outputFile);

        c.printTable();

    }

    public Clusters()
    {
        clusters = null;
        tableSize = 0;
        threshold = 0;
    }

    public ClusterNode[] returnCluster()
    {
        return clusters;
    }

    public void createTable(String input)
    {  
        StdIn.setFile(input);
        int dimensions = StdIn.readInt(); //number of dimensions
        tableSize = StdIn.readInt(); //initial size of hash table 
        threshold = StdIn.readInt(); //capacity or threshold

        clusters = new ClusterNode[tableSize];
        int counter = 0;

        for(int i = 0; i<dimensions; i++)
        {
            int dimensionNum = StdIn.readInt(); //dimension num
            int canons = StdIn.readInt(); //canon events
            int weight = StdIn.readInt(); //dimension weight
           

            Dimension dimension = new Dimension(dimensionNum, canons, weight);
            ClusterNode clusterNode = new ClusterNode(dimension, null);

            if(clusters[calculateIndex(dimension)] == null) //no node
            {
                clusters[calculateIndex(dimension)] = clusterNode;
            }
            else //is a node
            {
                clusterNode.setNextClusterNode(clusters[calculateIndex(dimension)]);
                clusters[calculateIndex(dimension)] = clusterNode;
            }
           
            counter++;

            if((double)counter/tableSize >= threshold)
            {
                rehash();
            }
            
        }
        addLast();
    }

    public int calculateIndex(Dimension dimension)
    {
        int dim = dimension.getNumber();
        return dim % tableSize;
    } 
    
    public void rehash()
    {
        ClusterNode[] clusters2 = new ClusterNode[tableSize*2]; //new table
        int newTableSize = tableSize*2; //new table size

        for(int i = 0; i<clusters.length; i++) //for each index
        {
            ClusterNode ptr = clusters[i]; //new ptr

            while(ptr != null) 
            {
                ClusterNode ptr2 = ptr.next();
                int rehash = ptr.getDimension().getNumber() % newTableSize;

                ptr.setNextClusterNode(clusters2[rehash]);
                clusters2[rehash] = ptr;

                ptr = ptr2;
            }
        }

        clusters = clusters2;
        tableSize = newTableSize;

    }

    public void addLast()
    {
        
        for(int i = 0; i<clusters.length; i++)
        {
            int first = i-1;
            int second = i-2;

            if(first == -1)
            {
                first = clusters.length-1;
            }

            if(second == -1)
            {
                second = clusters.length-1;
            }
            else if (second == -2)
            {
                second = clusters.length-2;
            }
        
           ClusterNode ptr = clusters[i];
           while(ptr.next() != null)
           {
                ptr = ptr.next();
           }
           ClusterNode node1 = new ClusterNode(clusters[first].getDimension(), null);
           ptr.setNextClusterNode(node1);
           ptr = ptr.next();

           ClusterNode node2 = new ClusterNode(clusters[second].getDimension(), null);
           ptr.setNextClusterNode(node2);

        }
    }

    public void addSpiders(ClusterNode[] clusters, String input)
    {
        StdIn.setFile(input); //spider
        int allSpiders = StdIn.readInt();

        for(int i = 0; i<allSpiders; i++)
        {
            int current = StdIn.readInt();
            String name = StdIn.readString();
            int signature = StdIn.readInt();

            SpiderPerson spider = new SpiderPerson(current, name, signature, 0);

            for(int j = 0; j<clusters.length; j++)
            {
                ClusterNode ptr = clusters[j];
                while(ptr != null)
                {
                    if(ptr.getDimension().getNumber() == current) //spider is at current
                    {
                        ArrayList<SpiderPerson> spiderPeople = ptr.getDimension().returnSpiderPerson(); //returns current dimenson's spiderpeople
                        
                        if(!spiderPeople.contains(spider))
                        {
                            spiderPeople.add(spider);
                        }
                    }
                    ptr = ptr.next();
                }
            }
        }

    }


    public void printTable()
    {
        for(int i = 0; i<clusters.length; i++) //for each index
        {
            ClusterNode ptr = clusters[i]; //new ptr

            while(ptr != null) 
            {   
                StdOut.print(ptr.getDimension().getNumber() + " ");
                ptr = ptr.next();
            }

            StdOut.println();
        }
    }

}

    
