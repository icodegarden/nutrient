package io.github.icodegarden.nutrient.designpattern.visitor;

public class VisitorB implements Visitor
{
    public void visit(NodeA nodeA)
    {
        System.out.println( nodeA.operationA() );
    }

    public void visit(NodeB nodeB)
    {
        System.out.println( nodeB.operationB() );
    }
}
