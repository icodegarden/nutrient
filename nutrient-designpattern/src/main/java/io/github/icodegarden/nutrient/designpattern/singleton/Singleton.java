package io.github.icodegarden.nutrient.designpattern.singleton;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class Singleton {

	private static volatile Singleton instance;

	private Singleton() {
	}

	public static Singleton getSingleton() {
		if (instance == null) {
			synchronized (Singleton.class) {
				if (instance == null) {
					instance = new Singleton();
				}
			}
		}
		return instance;
	}
}
