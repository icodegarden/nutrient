package io.github.icodegarden.nutrient.designpattern.abstractfactory;

public class NorthernVeggie implements Veggie
{
    private String name;

    public NorthernVeggie(String name)
    {
    }

    public String getName()
    {
		return name;
	}

    public void setName(String name)
    {
		this.name = name;
	}

}
