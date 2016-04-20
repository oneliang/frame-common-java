package com.oneliang.frame.reflect;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ThrowableChain {

	private List<Throwable> throwableList = new CopyOnWriteArrayList<Throwable>();

	/**
	 * add throwable
	 * @param throwable
	 * @return boolean
	 */
	protected boolean addThrowable(Throwable throwable) {
		return this.throwableList.add(throwable);
	}

	/**
	 * get throwable list
	 * @return List<Throwable>
	 */
	public List<Throwable> getThrowableList(){
		return this.throwableList;
	}
}
