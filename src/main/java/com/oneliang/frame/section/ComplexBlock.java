package com.oneliang.frame.section;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class ComplexBlock extends UnitBlock {

	protected byte[] beforeByteArray=null;
	protected List<Block> generateBlockList=new CopyOnWriteArrayList<Block>();

	public ComplexBlock() {
		super(0);
	}

	public ComplexBlock(byte[] beforeByteArray){
		this();
		this.beforeByteArray=beforeByteArray;
	}

	public void parse(InputStream inputStream) throws Exception {
		int index=0;
		ByteArrayInputStream byteArrayInputStream=null;
		if(this.beforeByteArray!=null){
			byteArrayInputStream=new ByteArrayInputStream(this.beforeByteArray);
		}
		Queue<BlockWrapper> blockWrapperQueue=this.getParseBlockWrapperQueue();
		if(blockWrapperQueue!=null){
			while(!blockWrapperQueue.isEmpty()){
				BlockWrapper blockWrapper=blockWrapperQueue.poll();
				int id=blockWrapper.getId();
				Block block=blockWrapper.getBlock();
				beforeRead(index,id,block);
				try{
					if(byteArrayInputStream!=null&&byteArrayInputStream.available()>0){
						block.parse(byteArrayInputStream);
					}else{
						block.parse(inputStream);
					}
				}catch (Exception e) {
					System.err.println(this);
					throw e;
				}
				this.totalSize+=block.getTotalSize();
				afterRead(index,id,block);
//				log("index:"+index+",value:"+StringUtil.byteToHexString(buffer));
				index++;
			}
		}
	}

	/**
	 * to byte array
	 * @return byte[]
	 */
	public byte[] toByteArray() {
		ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
		List<Block> blockList=this.generateBlockList;
		if(blockList!=null){
			try{
				for(Block block:blockList){
					byteArrayOutputStream.write(block.toByteArray());
					byteArrayOutputStream.flush();
				}
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				try{
					byteArrayOutputStream.close();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return byteArrayOutputStream.toByteArray();
	}

	/**
	 * get parse block queue
	 * @return Queue<BlockWrapper>
	 */
	protected abstract Queue<BlockWrapper> getParseBlockWrapperQueue();

	/**
	 * before read default empty method
	 * @param currentIndex
	 * @param currentId
	 * @param currentBlock
	 * @throws Exception
	 */
	protected void beforeRead(int currentIndex,int currentId,Block currentBlock) throws Exception{}

	/**
	 * after read
	 * @param currentIndex
	 * @param currentId
	 * @param currentBlock
	 * @throws Exception
	 */
	protected void afterRead(int currentIndex,int currentId,Block currentBlock) throws Exception{
		this.generateBlockList.add(currentBlock);
	}

	/**
	 * set value,just implement in UnitBlock
	 * @param value
	 */
	public void setValue(byte[] value) {
		throw new RuntimeException("Not implement in "+this.getClass()+",just implement in "+super.getClass());
	}

	/**
	 * get value,just implement in UnitBlock,please use method toByteArray();
	 * @return the value
	 */
	public byte[] getValue() {
		throw new RuntimeException("Not implement in "+this.getClass()+",just implement in "+super.getClass()+",please use method toByteArray()");
	}
}
