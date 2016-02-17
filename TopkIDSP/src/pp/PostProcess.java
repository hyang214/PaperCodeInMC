package pp;

import java.util.Collection;

/**
 * author: Hao 
 * date:Feb 17, 2016
 * time:4:01:47 PM
 * purpose: do post process for result
 */
public interface PostProcess<I,O> {
	
	/**
	 * input the result into post process object
	 */
	public abstract void inputResult(Collection<I> result);
	
	/**
	 * output the post processed result from post process object
	 */
	public abstract Collection<O> outputResult();
	
	/**
	 * print the post processed result 
	 */
	public abstract void print();
	
}
