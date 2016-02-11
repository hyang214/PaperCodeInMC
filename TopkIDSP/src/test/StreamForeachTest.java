package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

/**
 * author: Hao 
 * date:Jan 22, 2016
 * time:4:17:59 PM
 * purpose:
 */
public class StreamForeachTest {
	public static void main(String[] args) {
		List<A> list = new ArrayList<>();
		for(int i = 0 ; i < 10 ; i ++){
			list.add(new A(i));
		}
		list.stream().forEach(new Consumer<A>() {

			@Override
			public void accept(A t) {
				System.out.println(t);
				t.calculate();
				System.out.println(t);
			}
		});
	}
}

class A{
	public int a;
	public int b;
	public int c;
	public A(){
		Random r = new Random();
		this.a = r.nextInt(100);
		this.b = r.nextInt(100);
	}
	public A(int i){
		this.a = i;
		this.b = i;
	}
	public void calculate(){
		c = a + b;
	}
	@Override
	public String toString() {
		return a +" + " + b + " = " + c;
	}
}
