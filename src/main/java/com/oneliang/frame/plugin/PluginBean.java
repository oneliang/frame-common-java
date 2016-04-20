package com.oneliang.frame.plugin;

public class PluginBean {

	private String id=null;
	private Plugin pluginInstance=null;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the pluginInstance
	 */
	public Plugin getPluginInstance() {
		return pluginInstance;
	}

	/**
	 * @param pluginInstance the pluginInstance to set
	 */
	public void setPluginInstance(Plugin pluginInstance) {
		this.pluginInstance = pluginInstance;
	}
}
