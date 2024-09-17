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
 * HubInputFile name is passed through the command line as args[2]
 * Read from the HubInputFile with the format:
 * One integer
 *      i.    The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * CollectedOutputFile name is passed in through the command line as args[3]
 * Output to CollectedOutputFile with the format:
 * 1. e Lines, listing the Name of the anomaly collected with the Spider who
 *    is at the same Dimension (if one exists, space separated) followed by 
 *    the Dimension number for each Dimension in the route (space separated)
 * 
 * @author Seth Kelley
 */

public class CollectAnomalies {

    private Collider collider;
    private ClusterNode[] list;
    
    public static void main(String[] args) {

        if ( args.length < 4 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.CollectAnomalies <dimension INput file> <spiderverse INput file> <hub INput file> <collected OUTput file>");
                return;
        }

        String inputFile = args[0]; //dimensions
        String inputFileSpi = args[1]; //spiders
        String inputFileHub = args[2]; //hub
        String outPutFile = args[3]; //output

        Clusters cluster = new Clusters();

        cluster.createTable(inputFile);
        cluster.addSpiders(cluster.returnCluster(), inputFileSpi);

        Collider collider = new Collider();
        collider.Dimensionlist(cluster.returnCluster(), inputFile);

        StdOut.setFile(outPutFile);

        CollectAnomalies anomalies = new CollectAnomalies(collider, collider.returnList());
        anomalies.findAnomalies(inputFileHub, outPutFile, collider);

   
        

        
        //go through spiderverse, check if 928, if 928 skip
        //if find one where no match, found an anomaly
        //do BFS to find from hub to anomaly
        //check if spider there
        //if spider, reverse, no spider, no reverse + add reverse
        
    }

    public CollectAnomalies(Collider collider, ClusterNode[] list)
    {
        this.collider = collider;
        this.list = list;

    }

    public void findAnomalies(String hub, String output, Collider collider)
    {
        StdIn.setFile(hub);
        int hubNum = StdIn.readInt(); //hub
        int hindex = collider.returnIndex(hubNum, collider.returnList()); //index of hub in collider list

        ClusterNode start = list[hindex];
        BFS(start);
       
    }
    public void BFS(ClusterNode head)
    {
        int initial = collider.returnIndex(head,list); //index of original node

        ClusterNode[] heads = new ClusterNode[list.length]; //keep track of parents
        boolean[] alrVisited = new boolean[list.length]; //already visited
        Queue <ClusterNode> toVisit = new LinkedList<ClusterNode>(); //queue


        heads[initial] = head; //parent to parent

        alrVisited[initial] = true; //set it to true
        toVisit.add(head); //add to queue

        while(!toVisit.isEmpty()) //queue is not empty
        {
            ClusterNode dNode = toVisit.remove(); //curent node we're visitng

            SpiderPerson anom = hasAnomaly(dNode); //check for anom
            SpiderPerson spidey = hasSpider(dNode); //check for friendly spider

            if(anom!=null && !dNode.equals(head)) //anomaly and we're not at 928
            {
                StdOut.print(anom.returnName() + " "); //return anomaly name
                //dont forget to remove it
                ClusterNode traversal = dNode; //we need to get back

                if(spidey != null) //we has a spider
                {
                    StdOut.print(spidey.returnName() + " ");

                    while(traversal != heads[collider.returnIndex(traversal, list)]) //current node is not its parent
                    {
                        int curr = traversal.getDimension().getNumber();
                        StdOut.print(curr + " ");
                        traversal = heads[collider.returnIndex(traversal, list)];
                    }
                    if(traversal == heads[collider.returnIndex(traversal, list)])
                    {
                        int curr = traversal.getDimension().getNumber();
                        StdOut.print(curr);
                    }
                    //return spider
                    //return anomaly
                    dNode.getDimension().returnSpiderPerson().remove(anom);
                    head.getDimension().returnSpiderPerson().add(anom);
                    anom.setLoc(head.getDimension().getNumber());

                    dNode.getDimension().returnSpiderPerson().remove(spidey);
                    head.getDimension().returnSpiderPerson().add(spidey);
                    spidey.setLoc(head.getDimension().getNumber());
                }
                else //no spidey :(
                {
                    int counter = 0;
                    ClusterNode[] clusterPath = new ClusterNode[list.length];
                    while(traversal != heads[collider.returnIndex(traversal, list)]) //doesn't equal parent
                    {
                        clusterPath[counter] = traversal;
                        counter++;
                        traversal = heads[collider.returnIndex(traversal, list)];
                        
                    }
                    if(traversal == heads[collider.returnIndex(traversal,list)]) //equals parent
                    {
                        clusterPath[counter] = traversal;
                        counter++;
                    }
                    for(int i = counter-1; i>0; i--)
                    {
                        StdOut.print(clusterPath[i].getDimension().getNumber() + " ");
                    }
                    for(int i = 0; i<counter; i++)
                    {
                        StdOut.print(clusterPath[i].getDimension().getNumber() + " ");
                    }
                    dNode.getDimension().returnSpiderPerson().remove(anom);
                    head.getDimension().returnSpiderPerson().add(anom);
                    anom.setLoc(head.getDimension().getNumber());
                }
                StdOut.println();
            }

            ClusterNode ptr = dNode.next();
            while(ptr != null)
            {
                int index = collider.returnIndex(ptr, list);
                if(!alrVisited[index])
                {
                    toVisit.add(list[index]);
                    alrVisited[index] = true;
                    heads[index] = dNode;
                    
                }
                ptr = ptr.next();
            }

        }

        //find route
        //check if spider
        //if spider, reverse
        //if no spider, just add the reverse
    }


    public SpiderPerson hasSpider(ClusterNode clusternode)
    {
        ArrayList<SpiderPerson> spiders = clusternode.getDimension().returnSpiderPerson();
        for(int i = 0; i<spiders.size(); i++)
        {
            int current = spiders.get(i).returnLocation();
            int signature = spiders.get(i).returnSignature();

            if(current == signature)
            return spiders.get(i);
        }
        return null;
    }

    public SpiderPerson hasAnomaly(ClusterNode clusternode)
    {
        ArrayList<SpiderPerson> spiders = clusternode.getDimension().returnSpiderPerson();

        for(int i = 0; i<spiders.size(); i++)
        {
            int current = spiders.get(i).returnLocation();
            int signature = spiders.get(i).returnSignature();

            if(current != signature)
            return spiders.get(i);
        }
        return null;

    }

    public void readAnomalies(String inputFile)
    {
        StdIn.setFile(inputFile);
        while(!StdIn.isEmpty())
        {
            String anomaly = StdIn.readString();
            
        }
    }
}
