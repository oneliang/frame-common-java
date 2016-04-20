package com.oneliang.frame.plugin;

import com.oneliang.util.http.AsyncHttpDownloader;
import com.oneliang.util.http.DefaultDownloadListener;

public class PluginAsyncHttpDownloader implements PluginDownloader{

	private AsyncHttpDownloader asyncHttpDownloader=new AsyncHttpDownloader(1,5);

	{
		asyncHttpDownloader.start();
	}

	public void download(final PluginFileBean pluginFileBean){
		String httpUrl=pluginFileBean.getUrl();
		String saveFile=pluginFileBean.getSaveFile();
		this.asyncHttpDownloader.download(httpUrl, null, null, 20000, saveFile, new DefaultDownloadListener(){
			public void onFinish() {
				super.onFinish();
				pluginFileBean.setFinished(true);
				PluginFileBean.OnLoadedListener onLoadedListener=pluginFileBean.getOnLoadedListener();
				onLoadedListener.onLoaded(pluginFileBean);
			}
			public void onFailure(Exception exception) {
				this.onFinish();
			}
		});
	}
}
