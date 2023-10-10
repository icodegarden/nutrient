package io.github.icodegarden.nutrient.designpattern.factorymethod;
                                                         
public class StrawberryGardener implements FruitGardener 
{
    public Fruit factory()
    {
        return new Apple();
    }
}
