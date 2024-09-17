package spiderman;

public class ClusterNode {
    private Dimension dimension;
    private ClusterNode next; 

    public ClusterNode (Dimension dimension, ClusterNode next)
    {
        this.dimension = dimension;
        this.next = next;
    }

    public Dimension getDimension()
    {
        return dimension;
    }

    public void setDimension(Dimension dimension)
    {
        this.dimension = dimension;
    }

    public ClusterNode next()
    {
        return next;
    }
    
    public void setNextClusterNode(ClusterNode next)
    {
        this.next = next;
    }

}
