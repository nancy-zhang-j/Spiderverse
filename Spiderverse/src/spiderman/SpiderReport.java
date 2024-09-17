package spiderman;

import java.util.ArrayList;

public class SpiderReport {

private int canons;
private String name;
private boolean success;
private ArrayList<ClusterNode> path;

public SpiderReport(int canons, String name, boolean success, ArrayList<ClusterNode> path)
{
    this. canons = canons;
    this.name = name;
    this.success = success;
    this.path = path;
}

public int returnCanons()
{
    return canons;
}
public String returnName()
{
    return name;
}
public boolean returnSuccess()
{
    return success;
}
public ArrayList<ClusterNode> returnPath()
{
    return path;
}
}
