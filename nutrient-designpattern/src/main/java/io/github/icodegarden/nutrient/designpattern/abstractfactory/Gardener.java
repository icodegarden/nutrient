package io.github.icodegarden.nutrient.designpattern.abstractfactory;

public interface Gardener
{
    public Fruit createFruit(String name);

    public Veggie createVeggie(String name);
}
