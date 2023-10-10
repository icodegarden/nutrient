package io.github.icodegarden.nutrient.designpattern.visitor;

public interface Visitor
{
    void visit(NodeA node);

    void visit(NodeB node);
}
