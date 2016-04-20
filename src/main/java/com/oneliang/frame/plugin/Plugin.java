package com.oneliang.frame.plugin;

public abstract interface Plugin {

	public String getId();

	/**
	 * initialize
	 */
	public abstract void initialize();

	/**
	 * dispatch
	 * @param action
	 */
	public abstract void dispatch(Command command);

	/**
	 * public action
	 * @return String[]
	 */
	public abstract String[] publicAction();

	/**
	 * destroy
	 */
	public abstract void destroy();

	public final static class Command{
		private String action=null;
		private Object data=null;
		private Callback callback=null;
		/**
		 * @return the action
		 */
		public String getAction() {
			return action;
		}
		/**
		 * @param action the action to set
		 */
		public void setAction(String action) {
			this.action = action;
		}
		/**
		 * @return the data
		 */
		public Object getData() {
			return data;
		}
		/**
		 * @param data the data to set
		 */
		public void setData(Object data) {
			this.data = data;
		}
		/**
		 * @return the callback
		 */
		public Callback getCallback() {
			return callback;
		}
		/**
		 * @param callback the callback to set
		 */
		public void setCallback(Callback callback) {
			this.callback = callback;
		}
		/**
		 * @author oneliang
		 */
		public static interface Callback{
			/**
			 * callback
			 * @param object
			 */
			public abstract void callback(Object data);
		}
	}
}
