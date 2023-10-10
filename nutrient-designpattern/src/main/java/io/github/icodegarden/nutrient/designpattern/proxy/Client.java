package io.github.icodegarden.nutrient.designpattern.proxy;

public class Client
{
  	private static Subject subject;

    static public void main(String[] args)
	{ 
		subject = new ProxySubject();
		subject.request();
		subject.request();
	}
}
