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
 * 2. a lines, each with:
 *      i.    The dimension number (int)
 *      ii.   The number of canon events for the dimension (int)
 *      iii.  The dimension weight (int)
 * 
 * Step 2:
 * SpiderverseInputFile name is passed through the command line as args[1]
 * Read from the SpiderverseInputFile with the format:
 * 1. d (int): number of people in the file
 * 2. d lines, each with:
 *      i.    The dimension they are currently at (int)
 *      ii.   The name of the person (String)
 *      iii.  The dimensional signature of the person (int)
 * 
 * Step 3:
 * ColliderOutputFile name is passed in through the command line as args[2]
 * Output to ColliderOutputFile with the format:
 * 1. e lines, each with a different dimension number, then listing
 *       all of the dimension numbers connected to that dimension (space separated)
 * 
 * @author Seth Kelley
 */

public class Collider {

    private ClusterNode[] dimension;

    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.Collider <dimension INput file> <spiderverse INput file> <collider OUTput file>");
                return;
        }

        String inputFile = args[0]; //dim
        String inputFile2 = args[1]; //spiders
        String outputFile = args[2]; //out

        Clusters c = new Clusters(); //clusters
        c.createTable(inputFile);
        c.addSpiders(c.returnCluster(), inputFile2);
        
        Collider col = new Collider(); //collider
        col.Dimensionlist(c.returnCluster(), inputFile);
        
        StdOut.setFile(outputFile);
        col.printTable();
        //col.printSpider();
        
    }

    public void Dimensionlist (ClusterNode[] cluster, String input)
        {
            StdIn.setFile(input);
            int dimensions = StdIn.readInt(); //number of dimensions

            ClusterNode[] dimension2 = new ClusterNode[dimensions]; //one index for each dimension
            int dim = 0; //dimension counter
            
            
            for(int i = 0; i<cluster.length; i++) //for every cluster index
            {
                ClusterNode head = cluster[i]; //head of a cluster
                ClusterNode ptr = cluster[i].next();//start of cluster index
                ClusterNode traverse = cluster[i]; //to traverse entire array
                
                int headlocation = returnIndex(head, dimension2); 

                if(headlocation > -1) //if exist
                {
                    ClusterNode headhead = dimension2[headlocation]; 
                    while(headhead.next() != null)
                    {
                        headhead = headhead.next();
                    }
                    headhead.setNextClusterNode(ptr);
                
                }
                else
                {
                    dimension2[dim] = head;
                    dim++;
                }

                while(ptr != null)
                {
                    int location = returnIndex(ptr, dimension2);
                    if(location > -1) //exists
                    {
                        ClusterNode tempHead = new ClusterNode(head.getDimension(), null);
                        ClusterNode ptr2 = dimension2[location];
                        while(ptr2.next() != null)
                        {
                            ptr2 = ptr2.next();
                        }
                        ptr2.setNextClusterNode(tempHead);

                    }
                    else
                    {
                        dimension2[dim] = new ClusterNode(ptr.getDimension(), null);
                        ClusterNode tempHead = new ClusterNode(head.getDimension(), null);
                        dimension2[dim].setNextClusterNode(tempHead);
                        dim++;
                    }
                    ptr = ptr.next();
                }

                    }
                    dimension = dimension2;        }


        public void printTable()
        {
        for(int i = 0; i<dimension.length; i++) //for each index
        {
            ClusterNode ptr = dimension[i]; //new ptr

            while(ptr != null) 
            {   
                StdOut.print(ptr.getDimension().getNumber() + " ");
                ptr = ptr.next();
            }

            StdOut.println();
        }
         }

        public void printSpider()
        {
            for(int i = 0; i<dimension.length; i++)
            {
                ClusterNode ptr = dimension[i];
                while(ptr != null)
                {
                    ArrayList<SpiderPerson> spiders = ptr.getDimension().returnSpiderPerson(); //unfinished
                    for(int j = 0; j<spiders.size(); j++)
                    {
                        System.out.print(spiders.get(j).returnName());
                    }
                    ptr = ptr.next();
                }
                System.out.println();
            }
        }

        

        public int returnIndex(int integer, ClusterNode[] list)
        {
            for(int i = 0; i<list.length; i++)
            {
                if(list[i] != null && list[i].getDimension().getNumber() == integer)
                {
                    return i;
                }
            }
            return -1;
        }

        public int returnIndex(ClusterNode clusternode, ClusterNode[] list)
        {
            for(int i = 0; i<list.length; i++)
            {
                if(list[i] != null && list[i].getDimension().getNumber() == clusternode.getDimension().getNumber())
                {
                    return i;
                }
            }
            return -1;
        }

        public ClusterNode[] returnList()
        {
            return dimension;
        }
        
}