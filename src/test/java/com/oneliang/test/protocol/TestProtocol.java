package com.oneliang.test.protocol;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.oneliang.util.common.ClassUtil;
import com.oneliang.util.common.ObjectUtil;
import com.oneliang.util.common.StringUtil;
import com.oneliang.util.common.ClassUtil.ClassProcessor;

public class TestProtocol {

	private static final ClassProcessor DEFAULT_CLASS_PROCESSOR = ClassUtil.DEFAULT_CLASS_PROCESSOR;

	public static void main(String[] args){
		long c=16777215;
		System.out.println(Long.toBinaryString(c));
//		int[] mapping=new int[]{1,2,1,4,1,4,4,6,1,1};
		List<ProtocolMapping> protocolMappingList=new ArrayList<ProtocolMapping>();
		ProtocolMapping protocolMapping=new ProtocolMapping();
		protocolMapping.setField("headLen");
		protocolMapping.setByteCount(1);
		protocolMappingList.add(protocolMapping);
		protocolMapping=new ProtocolMapping();
		protocolMapping.setField("head");
		protocolMapping.setByteCount(2);
		protocolMappingList.add(protocolMapping);
		protocolMapping=new ProtocolMapping();
		protocolMapping.setField("dataLen");
		protocolMapping.setByteCount(1);
		protocolMappingList.add(protocolMapping);
		protocolMapping=new ProtocolMapping();
		protocolMapping.setField("dataHead");
		protocolMapping.setByteCount(4);
		protocolMappingList.add(protocolMapping);
		protocolMapping=new ProtocolMapping();
		protocolMapping.setField("type");
		protocolMapping.setByteCount(1);
		protocolMappingList.add(protocolMapping);
		protocolMapping=new ProtocolMapping();
		protocolMapping.setField("tagInfo");
		protocolMapping.setByteCount(4);
		protocolMappingList.add(protocolMapping);
		protocolMapping=new ProtocolMapping();
		protocolMapping.setField("random");
		protocolMapping.setByteCount(4);
		protocolMappingList.add(protocolMapping);
		protocolMapping=new ProtocolMapping();
		protocolMapping.setField("time");
		protocolMapping.setByteCount(6);
		protocolMappingList.add(protocolMapping);
		protocolMapping=new ProtocolMapping();
		protocolMapping.setField("battery");
		protocolMapping.setByteCount(1);
		protocolMappingList.add(protocolMapping);
		protocolMapping=new ProtocolMapping();
		protocolMapping.setField("txPower");
		protocolMapping.setByteCount(1);
		protocolMappingList.add(protocolMapping);
		
		Protocol protocol=new Protocol();
		String source="02010415475a4a54020000000130c575bd0e010112060064c5";
		byte[] a=StringUtil.hexStringToByteArray(source);
		int total=0;
		for(ProtocolMapping i:protocolMappingList){
			total+=i.getByteCount();
		}
		if(a!=null&&a.length==total){
			ByteArrayInputStream byteInputStream=new ByteArrayInputStream(a);
			int index=0;
			Method[] methods = protocol.getClass().getMethods();
			for (Method method : methods) {
				String methodName = method.getName();
				String fieldName = ObjectUtil.methodNameToFieldName(methodName, false);
				for(ProtocolMapping i:protocolMappingList){
					if(fieldName!=null&&fieldName.equals(i.getField())){
						byte[] buffer=new byte[i.getByteCount()];
						byteInputStream.read(buffer, 0, i.getByteCount());
						index+=i.getByteCount();
						System.out.println(buffer.length);
//						if (StringUtil.isNotBlank(fieldName)) {
//							Object value = null;
//							try {
//								value = method.invoke(object, new Object[] {});
//							} catch (Exception e) {
//								throw new MethodInvokeException(e);
//							}
//						}
						break;
					}
				}
			}
			System.out.println(index);
		}
	}

	public static class Protocol{
		private short headLen=0;//1
		private short[] head=null;//2
		private short dataLen=0;//1
		private char[] dataHead=null;//4
		private short type=-1;//1
		private short[] tagInfo=null;//4
		private short[] random=null;//4
		private short[] time=null;//6
		private short battery=0;//1
		private short txPower=0;//1
		/**
		 * @return the headLen
		 */
		public short getHeadLen() {
			return headLen;
		}
		/**
		 * @param headLen the headLen to set
		 */
		public void setHeadLen(short headLen) {
			this.headLen = headLen;
		}
		/**
		 * @return the head
		 */
		public short[] getHead() {
			return head;
		}
		/**
		 * @param head the head to set
		 */
		public void setHead(short[] head) {
			this.head = head;
		}
		/**
		 * @return the dataLen
		 */
		public short getDataLen() {
			return dataLen;
		}
		/**
		 * @param dataLen the dataLen to set
		 */
		public void setDataLen(short dataLen) {
			this.dataLen = dataLen;
		}
		/**
		 * @return the dataHead
		 */
		public char[] getDataHead() {
			return dataHead;
		}
		/**
		 * @param dataHead the dataHead to set
		 */
		public void setDataHead(char[] dataHead) {
			this.dataHead = dataHead;
		}
		/**
		 * @return the type
		 */
		public short getType() {
			return type;
		}
		/**
		 * @param type the type to set
		 */
		public void setType(short type) {
			this.type = type;
		}
		/**
		 * @return the tagInfo
		 */
		public short[] getTagInfo() {
			return tagInfo;
		}
		/**
		 * @param tagInfo the tagInfo to set
		 */
		public void setTagInfo(short[] tagInfo) {
			this.tagInfo = tagInfo;
		}
		/**
		 * @return the random
		 */
		public short[] getRandom() {
			return random;
		}
		/**
		 * @param random the random to set
		 */
		public void setRandom(short[] random) {
			this.random = random;
		}
		/**
		 * @return the time
		 */
		public short[] getTime() {
			return time;
		}
		/**
		 * @param time the time to set
		 */
		public void setTime(short[] time) {
			this.time = time;
		}
		/**
		 * @return the battery
		 */
		public short getBattery() {
			return battery;
		}
		/**
		 * @param battery the battery to set
		 */
		public void setBattery(short battery) {
			this.battery = battery;
		}
		/**
		 * @return the txPower
		 */
		public short getTxPower() {
			return txPower;
		}
		/**
		 * @param txPower the txPower to set
		 */
		public void setTxPower(short txPower) {
			this.txPower = txPower;
		}
	}

	public static class ProtocolMapping{
		private String field=null;
		private int byteCount=0;
		/**
		 * @return the field
		 */
		public String getField() {
			return field;
		}
		/**
		 * @param field the field to set
		 */
		public void setField(String field) {
			this.field = field;
		}
		/**
		 * @return the byteCount
		 */
		public int getByteCount() {
			return byteCount;
		}
		/**
		 * @param byteCount the byteCount to set
		 */
		public void setByteCount(int byteCount) {
			this.byteCount = byteCount;
		}
	}
}
