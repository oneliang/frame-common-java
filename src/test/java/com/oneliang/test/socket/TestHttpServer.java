package com.oneliang.test.socket;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.oneliang.Constants;
import com.oneliang.frame.log.LoggerContext;
import com.oneliang.util.concurrent.ThreadPool;
import com.oneliang.util.concurrent.ThreadTask;
import com.oneliang.util.logging.Logger;
import com.oneliang.util.logging.LoggerManager;

public class TestHttpServer {

	private String path=null;
	private int port=8080;
	
	public TestHttpServer(String path,int port) {
		this.path=path;
		this.port=port;
	}
	
	public void service() throws Exception {
		ThreadPool threadPool=new ThreadPool();
		threadPool.setMinThreads(1);
		threadPool.setMaxThreads(20);
		try {
			threadPool.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ServerSocket serverSocket = new ServerSocket(this.port);
		while (true) {
			Socket socket = serverSocket.accept();
			System.out.println(socket);
			threadPool.addThreadTask(new HttpProcessor(this.path,socket));
		}
	}
	
	public static void main(String[] args) {
		String classesRealPath=System.getProperty("user.dir")+"/bin";
		String logConfigFilePath="/com/lwx/test/concurrent/threadPoolLogConfig.xml";
		LoggerContext loggerContext=new LoggerContext();
		loggerContext.setClassesRealPath(classesRealPath);
		loggerContext.setProjectRealPath(classesRealPath);
		try {
			loggerContext.initialize(logConfigFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		TestHttpServer httpServer=new TestHttpServer("D:/webRoot",8080);
		try {
			httpServer.service();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
class HttpProcessor implements ThreadTask{

	private static final Logger logger=LoggerManager.getLogger(HttpProcessor.class);

	private static final String HTTP_GET="GET";
	private static final String HTTP_POST="POST";
	
	private String path=null;
	private Socket socket=null;
	
	public HttpProcessor(String path,Socket socket) {
		this.path=path;
		this.socket=socket;
	}
	
	private void doRequest(InputStream inputStream,OutputStream outputStream) throws Exception{
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String line = bufferedReader.readLine();
		String method = line.substring(0, 4).trim();
		OutputStream out = socket.getOutputStream();
		String requestPath = line.split(" ")[1];
		if (HTTP_GET.equalsIgnoreCase(method)) {
			System.out.println("do get......");
			this.doGet(requestPath, out);
		} else if (HTTP_POST.equalsIgnoreCase(method)) {
			System.out.println("do post......");
			this.doPost(requestPath, out);
		}
		bufferedReader.close();
		outputStream.close();
	}
	
	private void doGet(String requestPath,OutputStream outputStream) throws Exception{
		String filePath=this.path+requestPath;
		if (new File(filePath).exists()) {
			// 从服务器根目录下找到用户请求的文件并发送回浏览器
			InputStream inputStream = new FileInputStream(filePath);
			byte[] buffer=new byte[Constants.Capacity.BYTES_PER_KB];
			int length=-1;
			while((length=inputStream.read(buffer,0,buffer.length))!=-1){
				outputStream.write(buffer,0,length);
			}
			outputStream.flush();
			inputStream.close();
			logger.info("request complete.");
		}
	}
	
	private void doPost(String requestPath,OutputStream outputStream) throws Exception{
		doGet(requestPath,outputStream);
	}
	
	public void runTask() {
		try {
			doRequest(this.socket.getInputStream(),this.socket.getOutputStream());
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("socket closed.");
	}
}