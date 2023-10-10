package io.github.icodegarden.nutrient.designpattern.mediator;

public abstract class Colleague
{
    private Mediator mediator;	

	public Mediator getMediator() 
	{ 
		return mediator; 
	}

	public Colleague(Mediator m) 
	{ 
		mediator = m;
	}

    public abstract void action();

    public void change()
    {
        mediator.colleagueChanged(this);
    }
}
