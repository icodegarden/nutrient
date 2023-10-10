package io.github.icodegarden.nutrient.designpattern.visitor;

public class Client
{
    private static ObjectStructure aObjects;
    private static Visitor visitor;

    static public void main(String[] args)
    {
        aObjects = new ObjectStructure();

        aObjects.add(new NodeA());
        aObjects.add(new NodeB());

        visitor = new VisitorA();
        aObjects.action(visitor);

    }
}