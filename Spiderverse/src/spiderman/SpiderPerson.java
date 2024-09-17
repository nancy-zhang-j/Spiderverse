package spiderman;

public class SpiderPerson {

    private int location;
    private String name;
    private int signature;
    private int time;

    public SpiderPerson(int location, String name, int signature, int time)
    {
        this.location = location;
        this.name = name;
        this.signature = signature;
        this.time = time;
    }

    public void setLoc(int location)
    {
        this.location = location;
    }

    public void setTime(int time)
    {
        this.time = time;
    }

    public int returnTime()
    {
        return time;
    }
    public int returnLocation()
    {
        return location;
    }
    public String returnName()
    {
        return name;
    }

    public int returnSignature()
    {
        return signature;
    }

}
