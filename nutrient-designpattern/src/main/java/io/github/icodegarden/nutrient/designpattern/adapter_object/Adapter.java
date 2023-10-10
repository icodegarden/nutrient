package io.github.icodegarden.nutrient.designpattern.adapter_object;

public class Adapter implements Target {
public Adapter(Adaptee adaptee){
        super();
        this.adaptee = adaptee;
    }

    public void sampleOperation1(){
        adaptee.sampleOperation1();
    }

    public void sampleOperation2(){
        // Write your code here
    }

    private Adaptee adaptee;
}
