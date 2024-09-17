package spiderman;
import java.util.*;

public class Dimension {

private int number;
private int canonEvents;
private int weight; 
private ArrayList<SpiderPerson> spiders;

public Dimension(int number, int canonEvents, int weight)
{
    this.number = number;
    this.canonEvents = canonEvents;
    this.weight = weight;
    this.spiders = new ArrayList<SpiderPerson>();
}

public Dimension(int number, int canonEvents, int weight, ArrayList<SpiderPerson> spiders)
{
    this.number = number;
    this.canonEvents = canonEvents;
    this.weight = weight;
    this.spiders = spiders;
}

public int getNumber()
{
    return number;
}

public int getCanon()
{
    return canonEvents;
}

public int getWeight()
{
    return weight;
}
public void addPersons (ArrayList<SpiderPerson> persons)
{
    this.spiders = persons;
}

public void addSpider(SpiderPerson spider)
{
    spiders.add(spider);
}

public ArrayList<SpiderPerson> returnSpiderPerson()
{
    return spiders;
}
}
