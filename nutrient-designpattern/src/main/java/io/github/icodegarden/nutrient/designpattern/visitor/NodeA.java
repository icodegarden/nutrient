package io.github.icodegarden.nutrient.designpattern.visitor;

public class NodeA extends Node
{
    public void accept(Visitor visitor)
    {
        visitor.visit(this);
    }

    public String operationA()
    {
        return "NodeA is visited";
    }
}
