package com.oneliang.test.java;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.oneliang.Constant;

public class UdpTest {

	public static void main(String[] args){
		try {
            DatagramSocket datagramSocket=new DatagramSocket();
            String data="aaabbbccc您好吗？哈哈？。。基，我很好啊";
            byte[] dataByteArray=data.getBytes(Constant.Encoding.UTF8);
            DatagramPacket datagramPacket=new DatagramPacket(dataByteArray,dataByteArray.length);
            InetAddress inetAddress=InetAddress.getByName("192.168.0.102");
            datagramPacket.setAddress(inetAddress);
            datagramPacket.setPort(50000);
            datagramSocket.send(datagramPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	private void receive() throws Exception{
		DatagramSocket datagramSocket=new DatagramSocket(50000);
		while(true){
			try{
				byte[] buffer=new byte[Constant.Capacity.BYTES_PER_KB];
				DatagramPacket datagramPacket=new DatagramPacket(buffer,buffer.length);  
				datagramSocket.receive(datagramPacket);
				int totalLength=datagramPacket.getLength();
				byte[] receiveData=datagramPacket.getData();
				byte[] data=null;
				System.out.println(totalLength);
				if(receiveData.length>totalLength){
					data=new byte[totalLength];
					System.arraycopy(receiveData, 0, data, 0, totalLength);
				}else if(receiveData.length==totalLength){
					data=receiveData;
				}else{
					
				}
				System.out.println(datagramPacket.getAddress()+","+new String(data,Constant.Encoding.UTF8));
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
