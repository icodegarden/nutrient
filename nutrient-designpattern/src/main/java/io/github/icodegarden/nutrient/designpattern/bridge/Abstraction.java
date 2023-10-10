package io.github.icodegarden.nutrient.designpattern.bridge;

abstract public class Abstraction
{
    public void operation()
    {
    	imp.operationImp();
    }

    /**
     * @link aggregation
     * @directed
     * @supplierRole impl*/
    private Implementor imp;
}
