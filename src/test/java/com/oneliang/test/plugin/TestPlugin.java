package com.oneliang.test.plugin;

import com.oneliang.frame.plugin.Plugin;
import com.oneliang.frame.plugin.PluginAsyncHttpDownloader;
import com.oneliang.frame.plugin.PluginBean;
import com.oneliang.frame.plugin.PluginDownloader;
import com.oneliang.frame.plugin.PluginFileBean;
import com.oneliang.frame.plugin.PluginGroupBean;

public class TestPlugin implements PluginGroupBean.OnLoadedListener,PluginFileBean.OnLoadedListener{

	private void test(){
		PluginGroupBean pluginGroupBean=new PluginGroupBean();
		pluginGroupBean.setId("plugin.group.a");
		//source
		PluginFileBean pluginFileBean=new PluginFileBean();
		pluginFileBean.setId("local.source.code.file");
		pluginFileBean.setType(PluginFileBean.TYPE_SOURCE_CODE);
		PluginBean pluginBean=new PluginBean();
		Plugin plugin=new Plugin(){
			public void destroy() {
			}
			public void dispatch(Command command) {
			}
			public String getId() {
				return "plugin.code";
			}
			public void initialize() {
				System.out.println("initialize:"+this.getClass());
			}
			public String[] publicAction() {
				return null;
			}
			
		};
		pluginBean.setId(plugin.getId());
		pluginBean.setPluginInstance(plugin);
		pluginFileBean.addPluginBean(pluginBean);
		pluginFileBean.setOnLoadedListener(this);
		pluginGroupBean.addPluginFileBean(pluginFileBean);
		//plugin a
		pluginFileBean=new PluginFileBean();
		pluginFileBean.setId("local.jar.file.plugin.a");
		pluginFileBean.setType(PluginFileBean.TYPE_JAR);
		pluginFileBean.setSource(PluginFileBean.SOURCE_LOCAL);
		pluginFileBean.setUrl("D:/plugin-a.jar");
		pluginFileBean.setOnLoadedListener(this);
		pluginGroupBean.addPluginFileBean(pluginFileBean);
		//plugin b
		pluginFileBean=new PluginFileBean();
		pluginFileBean.setId("local.jar.file.plugin.b");
		pluginFileBean.setType(PluginFileBean.TYPE_JAR);
		pluginFileBean.setSource(PluginFileBean.SOURCE_HTTP);
		pluginFileBean.setUrl("http://localhost:8080/manager/plugin-b.jar");
		pluginFileBean.setSaveFile("E:/b.jar");
		pluginFileBean.setOnLoadedListener(this);
		pluginGroupBean.addPluginFileBean(pluginFileBean);
		
		pluginGroupBean.setOnLoadedListener(this);
		pluginGroupBean.loadPluginFileBean();
//		pluginBean.setUrl("D:/plugin-a.jar");
//		JarClassLoader jarClassLoader=new JarClassLoader(Thread.currentThread().getContextClassLoader());
//		List<Class<?>> classList=new ArrayList<Class<?>>();
//		classList.addAll(JarUtil.extractClassFromJarFile(jarClassLoader, "D:/plugin-a.jar"));
//		classList.addAll(JarUtil.extractClassFromJarFile(jarClassLoader, "D:/plugin-b.jar"));
//		for(Class<?> clazz:classList){
//			if(ObjectUtil.isInterfaceImplement(clazz, Plugin.class)){e
//				Plugin plugin=(Plugin)clazz.newInstance();
//				plugin.initialize();
//			}
//		}
//		Thread.sleep(1000000);
	}

	public void onLoaded(PluginGroupBean pluginGroupBean) {
		System.out.println(pluginGroupBean);
	}

	public void onLoaded(PluginFileBean pluginFileBean) {
		if("local.jar.file.plugin.a".equals(pluginFileBean.getId())){
			
		}
		System.out.println("id:"+pluginFileBean.getId());
	}

	public static void main(String[] args) throws Exception{
		new TestPlugin().test();
	}
}
