package spiderman;
import java.util.ArrayList;

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
 * SpotInputFile name is passed through the command line as args[2]
 * Read from the SpotInputFile with the format:
 * Two integers (line seperated)
 *      i.    Line one: The starting dimension of Spot (int)
 *      ii.   Line two: The dimension Spot wants to go to (int)
 * 
 * Step 4:
 * TrackSpotOutputFile name is passed in through the command line as args[3]
 * Output to TrackSpotOutputFile with the format:
 * 1. One line, listing the dimenstional number of each dimension Spot has visited (space separated)
 * 
 * @author Seth Kelley
 */

public class TrackSpot {

    private ClusterNode[] list; //adjacency list
    private ArrayList<ClusterNode> path;
    
    public static void main(String[] args) {

        if ( args.length < 4 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.TrackSpot <dimension INput file> <spiderverse INput file> <spot INput file> <trackspot OUTput file>");
                return;
        }

        String inputFile = args[0]; //dimensions
        String inputFileSpi = args[1]; //spiders
        String inputFileSpot = args[2]; //spot
        String outPutFile = args[3]; //output

        Clusters cluster = new Clusters();

        cluster.createTable(inputFile);
        cluster.addSpiders(cluster.returnCluster(), inputFileSpi);

        Collider collider = new Collider();
        collider.Dimensionlist(cluster.returnCluster(), inputFile);


        StdOut.setFile(outPutFile);

        TrackSpot spot = new TrackSpot(collider.returnList());
        spot.tracker(inputFileSpot);
    }

    
    public TrackSpot(ClusterNode[] list)
    {
        this.list = list;
        path = new ArrayList<ClusterNode>();
    }

    public boolean DFS(boolean[] alrVisited, int current, int last)
    {
        int ind = index(current); //current index for visited
        alrVisited[ind] = true;

        if(current == last) //already at the right spot
        {
            StdOut.print(current); //case spot is at the right area
            return true;
        }


        StdOut.print(current + " "); //print 
        ClusterNode ptr = list[ind];

        while(ptr != null) //go through the index, coming back
        {
            if(ptr.next() == null) //no next node
            {
                return false;
            }

            int ind2 = index(ptr.next().getDimension().getNumber()); //find where the next node's index is

            if(alrVisited[ind2] == false)
            {
                ClusterNode ptr2 = list[ind2];
                if(DFS(alrVisited, ptr2.getDimension().getNumber(), last))
                return true;

            }
            ptr = ptr.next();
        }
        return false;
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

    public void tracker(String input)
    {
        StdIn.setFile(input);

        int current = StdIn.readInt();
        int last = StdIn.readInt();
        boolean[] alrVisited = new boolean[list.length];

        DFS(alrVisited, current, last);
    }

    public void printPath(ArrayList<ClusterNode> path)
    {
        //put into antho
        for(int i = 0; i<path.size()-1; i++)
        {
            StdOut.print(path.get(i) + " ");
        }
        StdOut.print(path.get(path.size()-1));
    }
}
