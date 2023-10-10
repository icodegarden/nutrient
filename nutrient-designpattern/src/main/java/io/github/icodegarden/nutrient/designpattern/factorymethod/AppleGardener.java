package io.github.icodegarden.nutrient.designpattern.factorymethod;
                                                         
public class AppleGardener implements FruitGardener 
{
    public Fruit factory()
    {
        return new Apple();
    }
}
