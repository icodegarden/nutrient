package io.github.icodegarden.nutrient.designpattern.abstractfactory;

public class TropicalFruit implements Fruit
{
    private String name;

    public TropicalFruit(String name)
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
