package com.oneliang.frame.plugin;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.oneliang.frame.broadcast.BroadcastManager;
import com.oneliang.frame.broadcast.BroadcastReceiver;
import com.oneliang.frame.broadcast.Message;
import com.oneliang.util.jar.JarClassLoader;

public class PluginGroupBean implements BroadcastReceiver{

	private String id=null;
	private List<PluginFileBean> pluginFileBeanList=new CopyOnWriteArrayList<PluginFileBean>();
	private Map<String,PluginFileBean> pluginFileBeanMap=new ConcurrentHashMap<String, PluginFileBean>();
	private OnLoadedListener onLoadedListener=null;
	private JarClassLoader jarClassLoader=new JarClassLoader(Thread.currentThread().getContextClassLoader());;
	private PluginDownloader defaultPluginDownloader=new PluginAsyncHttpDownloader();
	final BroadcastManager broadcastManager=new BroadcastManager();
	static final String ACTION_PLUGIN_FILE_FINISHED="action.plugin.file.finished";
	static final String KEY_PLUGIN_FILE_ID="key.plugin.file.id";
	{
		this.broadcastManager.start();
	}

	/**
	 * load plugin file bean
	 */
	public void loadPluginFileBean(){
		this.broadcastManager.registerBroadcastReceiver(new String[]{ACTION_PLUGIN_FILE_FINISHED}, this);
		for(PluginFileBean pluginFileBean:this.pluginFileBeanList){
			if(pluginFileBean!=null){
				pluginFileBean.setBroadcastManager(this.broadcastManager);
				pluginFileBean.setJarClassLoader(this.jarClassLoader);
				if(pluginFileBean.getPluginDownloader()==null){
					pluginFileBean.setPluginDownloader(defaultPluginDownloader);
				}
				pluginFileBean.loadPluginBean();
			}
		}
	}

	/**
	 * receive
	 */
	public void receive(String action, Message message) {
		if(action!=null){
			if(action.equals(ACTION_PLUGIN_FILE_FINISHED)){
				boolean finished=true;
				for(PluginFileBean pluginFileBean:this.pluginFileBeanList){
					if(!pluginFileBean.isFinished()){
						finished=false;
						break;
					}
				}
				if(finished){
					if(this.onLoadedListener!=null){
						this.broadcastManager.unregisterBroadcastReceiver(this);
						this.onLoadedListener.onLoaded(this);
					}
				}
			}
		}
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
	 * add plugin file bean
	 * @param pluginFileBean
	 */
	public void addPluginFileBean(PluginFileBean pluginFileBean){
		this.pluginFileBeanList.add(pluginFileBean);
		this.pluginFileBeanMap.put(pluginFileBean.getId(), pluginFileBean);
	}

	/**
	 * @param onLoadedListener the onLoadedListener to set
	 */
	public void setOnLoadedListener(OnLoadedListener onLoadedListener) {
		this.onLoadedListener = onLoadedListener;
	}

	/**
	 * @param jarClassLoader the jarClassLoader to set
	 */
	public void setJarClassLoader(JarClassLoader jarClassLoader) {
		if(jarClassLoader!=null){
			this.jarClassLoader = jarClassLoader;
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
		public void onLoaded(PluginGroupBean pluginGroupBean);
	}
}
