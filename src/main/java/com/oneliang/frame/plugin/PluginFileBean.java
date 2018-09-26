package com.oneliang.frame.plugin;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.oneliang.Constants;
import com.oneliang.frame.broadcast.BroadcastManager;
import com.oneliang.frame.broadcast.Message;
import com.oneliang.util.common.ObjectUtil;
import com.oneliang.util.jar.JarClassLoader;
import com.oneliang.util.jar.JarUtil;
import com.oneliang.util.logging.Logger;
import com.oneliang.util.logging.LoggerManager;

public class PluginFileBean {

	private static final Logger logger=LoggerManager.getLogger(PluginFileBean.class);

	public static final int TYPE_SOURCE_CODE=0;
	public static final int TYPE_JAR=1;
	public static final int SOURCE_LOCAL=0;
	public static final int SOURCE_HTTP=1;
	public static final int SOURCE_OTHER=2;

	private Map<String,PluginBean> pluginBeanMap=new ConcurrentHashMap<String, PluginBean>();

	private String id=null;
	private int type=TYPE_SOURCE_CODE;
	private int source=SOURCE_LOCAL;
	private String url=null;
	private String saveFile=null;
	private OnLoadedListener onLoadedListener=null;
	private JarClassLoader jarClassLoader=null;
	private PluginDownloader pluginDownloader=null;
	private boolean finished=false;
	private BroadcastManager broadcastManager=null;

	/**
	 * find plugin
	 * @param pluginId
	 * @return Plugin
	 */
	public Plugin findPlugin(String pluginId){
		Plugin plugin=null;
		if(pluginBeanMap.containsKey(pluginId)){
			PluginBean pluginBean=pluginBeanMap.get(pluginId);
			if(pluginBean!=null){
				plugin=pluginBean.getPluginInstance();
			}
		}
		return plugin;
	}

	/**
	 * load plugin bean
	 */
	public void loadPluginBean(){
		switch(this.type){
		case TYPE_SOURCE_CODE:
			loadPluginBeanByCode();
			break;
		case TYPE_JAR:
			loadPluginBeanByJar();
			break;
		}
	}

	/**
	 * load plugin bean by code
	 */
	private void loadPluginBeanByCode(){
		Iterator<Entry<String,PluginBean>> iterator=this.pluginBeanMap.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String,PluginBean> entry=iterator.next();
			PluginBean pluginBean=entry.getValue();
			if(pluginBean!=null){
				Plugin plugin=pluginBean.getPluginInstance();
				if(plugin!=null){
					plugin.initialize();
				}
			}
		}
		if(this.onLoadedListener!=null){
			this.setFinished(true);
			this.onLoadedListener.onLoaded(this);
		}
	}

	/**
	 * load plugin bean by jar
	 */
	private void loadPluginBeanByJar(){
		if(this.jarClassLoader!=null){
			switch(this.source){
			case SOURCE_HTTP:
				if(this.pluginDownloader!=null){
					this.pluginDownloader.download(this);
				}
				break;
			case SOURCE_LOCAL:
				try {
					List<Class<?>> classList=JarUtil.extractClassFromJarFile(this.jarClassLoader, this.url);
					for(Class<?> clazz:classList){
						if(ObjectUtil.isInterfaceImplement(clazz, Plugin.class)){
							Plugin plugin=(Plugin)clazz.newInstance();
							PluginBean pluginBean=new PluginBean();
							pluginBean.setId(plugin.getId());
							pluginBean.setPluginInstance(plugin);
							this.addPluginBean(pluginBean);
							plugin.initialize();
						}
					}
				} catch (Exception e) {
					logger.error(Constants.Base.EXCEPTION, e);
				}
				if(this.onLoadedListener!=null){
					this.setFinished(true);
					this.onLoadedListener.onLoaded(this);
				}
				break;
			}
		}
	}

	/**
	 * @param pluginBean
	 */
	public void addPluginBean(PluginBean pluginBean){
		if(pluginBean!=null){
			this.pluginBeanMap.put(pluginBean.getId(), pluginBean);
		}
	}
	/**
	 * @return the pluginBeanMap
	 */
	public Map<String, PluginBean> getPluginBeanMap() {
		return pluginBeanMap;
	}
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
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @return the source
	 */
	public int getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(int source) {
		this.source = source;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the saveFile
	 */
	public String getSaveFile() {
		return saveFile;
	}

	/**
	 * @param saveFile the saveFile to set
	 */
	public void setSaveFile(String saveFile) {
		this.saveFile = saveFile;
	}

	/**
	 * @param onLoadedListener the onLoadedListener to set
	 */
	public void setOnLoadedListener(OnLoadedListener onLoadedListener) {
		this.onLoadedListener = onLoadedListener;
	}

	/**
	 * @return the onLoadedListener
	 */
	public OnLoadedListener getOnLoadedListener() {
		return onLoadedListener;
	}

	/**
	 * @param jarClassLoader the jarClassLoader to set
	 */
	void setJarClassLoader(JarClassLoader jarClassLoader) {
		this.jarClassLoader = jarClassLoader;
	}

	/**
	 * @param pluginDownloader the pluginDownloader to set
	 */
	public void setPluginDownloader(PluginDownloader pluginDownloader) {
		this.pluginDownloader = pluginDownloader;
	}

	/**
	 * @return the pluginDownloader
	 */
	PluginDownloader getPluginDownloader() {
		return pluginDownloader;
	}
	/**
	 * @return the finished
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * @param finished the finished to set
	 */
	public void setFinished(boolean finished) {
		this.finished = finished;
		if(this.finished&&this.broadcastManager!=null){
			Message message=new Message();
			message.addAction(PluginGroupBean.ACTION_PLUGIN_FILE_FINISHED);
			message.putObject(PluginGroupBean.KEY_PLUGIN_FILE_ID, this.id);
			broadcastManager.sendBroadcast(message);
		}
	}
	/**
	 * @author oneliang
	 */
	public static abstract interface OnLoadedListener{
		/**
		 * on finished
		 * @param pluginFileBean
		 */
		public void onLoaded(PluginFileBean pluginFileBean);
	}
	/**
	 * @param broadcastManager the broadcastManager to set
	 */
	void setBroadcastManager(BroadcastManager broadcastManager) {
		this.broadcastManager = broadcastManager;
	}
}
