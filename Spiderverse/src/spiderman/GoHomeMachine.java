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
 * Read from the SpotInputFile with the format:
 * One integer
 *      i.    The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * AnomaliesInputFile name is passed through the command line as args[3]
 * Read from the AnomaliesInputFile with the format:
 * 1. e (int): number of anomalies in the file
 * 2. e lines, each with:
 *      i.   The Name of the anomaly which will go from the hub dimension to their home dimension (String)
 *      ii.  The time allotted to return the anomaly home before a canon event is missed (int)
 * 
 * Step 5:
 * ReportOutputFile name is passed in through the command line as args[4]
 * Output to ReportOutputFile with the format:
 * 1. e Lines (one for each anomaly), listing on the same line:
 *      i.   The number of canon events at that anomalies home dimensionafter being returned
 *      ii.  Name of the anomaly being sent home
 *      iii. SUCCESS or FAILED in relation to whether that anomaly made it back in time
 *      iv.  The route the anomaly took to get home
 * 
 * @author Seth Kelley
 */

public class GoHomeMachine {

    ClusterNode[] list;
    ClusterNode hubNode;
    ArrayList<SpiderReport> reports;
    
    public static void main(String[] args) {

        if ( args.length < 5 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.GoHomeMachine <dimension INput file> <spiderverse INput file> <hub INput file> <anomalies INput file> <report OUTput file>");
                return;
        }

        String inputFile = args[0]; //dimensions
        String inputFileSpi = args[1]; //spiders
        String inputFileHub = args[2]; //hub
        String inputFileAnom = args[3];
        String outPutFile = args[4]; //output

        Clusters cluster = new Clusters();

        cluster.createTable(inputFile);
        cluster.addSpiders(cluster.returnCluster(), inputFileSpi);

        Collider collider = new Collider();
        collider.Dimensionlist(cluster.returnCluster(), inputFile);

        CollectAnomalies anomalies = new CollectAnomalies(collider, collider.returnList());
        anomalies.findAnomalies(inputFileHub, outPutFile, collider);

        StdOut.setFile(outPutFile);

        GoHomeMachine home = new GoHomeMachine(collider, inputFileHub);
        home.sendHome(inputFileAnom, outPutFile);

    }

    public GoHomeMachine(Collider collider, String hub)
    {
        this.list = collider.returnList();
        StdIn.setFile(hub);
        int hubInt = StdIn.readInt();
        this.hubNode = list[collider.returnIndex(hubInt, list)];
        this.reports = new ArrayList<SpiderReport>();
    }

    public void sendHome(String input, String output)
    {
        StdIn.setFile(input); //anomalies
        int numOfAnomalies = StdIn.readInt();
        ArrayList<SpiderPerson> atHub = hubNode.getDimension().returnSpiderPerson(); //all spiders present

        //find parent node paths
        int[] path = DijkstraAlgo(hubNode, list);
        
        for(int i = 0; i<numOfAnomalies; i++) //each anom
        {
            String name = StdIn.readString(); //name
            int time = StdIn.readInt(); //min time to send back
            
            for(int j = 0; j<atHub.size(); j++)
            {
                if(name.equals(atHub.get(j).returnName())) //if at hub + anomaly
                 {
                    SpiderReport reporting = createSpiderReport(atHub.get(j), path, time);
                    reports.add(reporting);
                    
                    
                }
            }
        }
        for(int i = 0; i<reports.size(); i++)
         {
             printSpiderReport(reports.get(i));
         }
        //for every returned person in arraylist, give path and their 
    }

    public int[] DijkstraAlgo(ClusterNode start, ClusterNode[] list)
    {
        int nodes = list.length; //length of AdjList
        int startIndex = index(start.getDimension().getNumber()); //index of dimension that's the hub

        ArrayList<ClusterNode> done = new ArrayList<>(); //finished vertices
        ArrayList<ClusterNode> fringe = new ArrayList<>(); //unfinished
        
        int[] parent = new int[nodes]; //parents
        int[] shortestTime = new int[nodes]; //known shortest times

        for(int i = 0; i<nodes; i++) //every vertex set distance and parent to inf, null
        {   
            shortestTime[i] = Integer.MAX_VALUE;
            parent[i] = -1; //no parent
        }

        shortestTime[startIndex] = list[startIndex].getDimension().getWeight(); //change weight of starting node
        parent[startIndex] = -1; //no parent
        //here's the actual algo now

        fringe.add(start); //add node to fringe

        while(!fringe.isEmpty())
        {
            ClusterNode ptrSmallest = fringe.remove(0);//remove from fringe
            



            int smallest = Integer.MAX_VALUE;

            //find shortest time
            
            // while(ptr != null) //traverse for smallest 
            // {
            //     if(ptr.getDimension().getWeight() < smallest)
            //     {
            //     ptrSmallest = ptr; //point to smallest node
            //     smallest = ptr.getDimension().getWeight();
            //     }
            //     ptr = ptr.next();
            // }

            done.add(ptrSmallest);
            //update neighbors
            int nextIndex = index(ptrSmallest.getDimension().getNumber()); //Index of min
            ClusterNode ptr2 = list[nextIndex].next(); //next after head

            while(ptr2 != null) //for each neighbor
            {
                if(!done.contains(ptr2)) //not in done set
                {
                    if(shortestTime[index(ptr2.getDimension().getNumber())] == Integer.MAX_VALUE)
                    {
                        shortestTime[index(ptr2.getDimension().getNumber())] = shortestTime[nextIndex] + ptrSmallest.getDimension().getWeight() + ptrSmallest.getDimension().getWeight();
                        fringe.add(ptr2);
                        parent[index(ptr2.getDimension().getNumber())] = index(ptrSmallest.getDimension().getNumber());
                        //parent.set(index(ptr2.getDimension().getNumber()), ptrSmallest); 
                    }
                    else if(shortestTime[index(ptr2.getDimension().getNumber())] > shortestTime[nextIndex] + ptrSmallest.getDimension().getWeight() + ptrSmallest.getDimension().getWeight())
                    {
                        shortestTime[index(ptr2.getDimension().getNumber())] = shortestTime[nextIndex] + ptrSmallest.getDimension().getWeight() + ptrSmallest.getDimension().getWeight();
                        parent[index(ptr2.getDimension().getNumber())] = index(ptrSmallest.getDimension().getNumber());
                        //parent.set(index(ptr2.getDimension().getNumber()), ptrSmallest); 

                    }
                }
                ptr2 = ptr2.next();
            }


        }
        // for(int i = 0; i<parent.length; i++) //just debuggin
        // {
        //     StdOut.println(parent[i]);
        // }
        return parent;

    }

    public SpiderReport createSpiderReport(SpiderPerson spider, int[] parent, int weight)
    {
        int indexHome = index(spider.returnSignature()); //index of spider's home dim in collider
        int indexHub = index(spider.returnLocation()); //should be hub
        ArrayList<ClusterNode> path = new ArrayList<>();
        int time = 0;
        boolean success = false;

        int index = indexHome;
        while(index != -1)
        {   
            path.add(list[index]); //add current index to arraylist
            time = time + list[index].getDimension().getWeight()*2;
            index = parent[index];//move to the next
        }
       //weight + dimension(current)
        if(weight > time)
        {
            success = true;
        }
        SpiderReport spiReport = new SpiderReport(0, spider.returnName(), success, path);
        return spiReport; 

        
    }

    public void printSpiderReport(SpiderReport report)
    {
        int canons = 0;
        String name = report.returnName();
        boolean success = report.returnSuccess();
        ArrayList<ClusterNode> path = report.returnPath();

        
        StdOut.print(canons + " " + name + " ");
        if(success)
        {
            StdOut.print( "SUCCESS" + " ");
        }
        else
        {
            StdOut.print( "FAILED" + " ");
        }
       
        for(int i = path.size()-1; i>=0; i--)
         {
            StdOut.print(path.get(i).getDimension().getNumber() + " ");
        }
         
        StdOut.println();


    }

    public int index(int dimension)
    {
        for(int i = 0; i<list.length; i++)
        {
            if(list[i].getDimension().getNumber() == dimension)
            {
                return i;
            }
        }
        return -99;
    }
}
