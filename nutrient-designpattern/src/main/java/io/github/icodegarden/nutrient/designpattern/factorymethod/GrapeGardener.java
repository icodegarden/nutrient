package io.github.icodegarden.nutrient.designpattern.factorymethod;
                                                         
public class GrapeGardener implements FruitGardener 
{
    public Fruit factory()
    {
        return new Grape();
    }
}
