package com.oneliang.frame.section;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.oneliang.util.common.MathUtil;

public class SectionDiffData {

	public static final int OPCODE_MOVE=0;
	public static final int OPCODE_INCREASE=1;

	public final List<SectionPosition> sectionPositionMoveList;
	public final List<SectionPosition> sectionPositionIncreaseList;

	public SectionDiffData(List<SectionPosition> sectionPositionMoveList, List<SectionPosition> sectionPositionIncreaseList) {
		this.sectionPositionMoveList=sectionPositionMoveList;
		this.sectionPositionIncreaseList=sectionPositionIncreaseList;
	}

	public byte[] toByteArray() throws Exception{
		ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
		if(this.sectionPositionMoveList!=null&&!this.sectionPositionMoveList.isEmpty()){
			ByteArrayOutputStream moveListOutputStream=new ByteArrayOutputStream();
			for(SectionPosition sectionPosition:this.sectionPositionMoveList){
				moveListOutputStream.write(MathUtil.intToByteArray(sectionPosition.getFromIndex()));
				moveListOutputStream.write(MathUtil.intToByteArray(sectionPosition.getToIndex()));
			}
			byteArrayOutputStream.write(MathUtil.intToByteArray(OPCODE_MOVE));
			byte[] moveByteArray=moveListOutputStream.toByteArray();
			byteArrayOutputStream.write(MathUtil.intToByteArray(moveByteArray.length));
			byteArrayOutputStream.write(moveByteArray);
		}
		if(this.sectionPositionIncreaseList!=null&&!this.sectionPositionIncreaseList.isEmpty()){
			ByteArrayOutputStream increaseListOutputStream=new ByteArrayOutputStream();
			for(SectionPosition sectionPosition:this.sectionPositionIncreaseList){
				increaseListOutputStream.write(MathUtil.intToByteArray(sectionPosition.getToIndex()));
				byte[] sectionByteArray=sectionPosition.getByteArray();
				increaseListOutputStream.write(MathUtil.intToByteArray(sectionByteArray.length));
				increaseListOutputStream.write(sectionByteArray);
			}
			byteArrayOutputStream.write(MathUtil.intToByteArray(OPCODE_INCREASE));
			byte[] increaseByteArray=increaseListOutputStream.toByteArray();
			byteArrayOutputStream.write(MathUtil.intToByteArray(increaseByteArray.length));
			byteArrayOutputStream.write(increaseByteArray);
		}
		return byteArrayOutputStream.toByteArray();
	}

	public static SectionDiffData parseFrom(byte[] byteArray) throws Exception{
		ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(byteArray);
		return parseFrom(byteArrayInputStream);
	}

	public static SectionDiffData parseFrom(InputStream inputStream) throws Exception{
		List<SectionPosition> sectionPositionMoveList=new ArrayList<SectionPosition>();
		List<SectionPosition> sectionPositionIncreaseList=new ArrayList<SectionPosition>();
		byte[] buffer=new byte[4];
		inputStream.read(buffer);
		Queue<Integer> opcodeQueue=new ConcurrentLinkedQueue<Integer>();
		int opcode=MathUtil.byteArrayToInt(buffer);
		opcodeQueue.add(opcode);
		while (!opcodeQueue.isEmpty()) {
			opcode = opcodeQueue.poll();
			switch (opcode) {
			case OPCODE_MOVE: {
				inputStream.read(buffer);
				int moveListLength = MathUtil.byteArrayToInt(buffer) / 2 / 4;
				for (int i = 0; i < moveListLength; i++) {
					inputStream.read(buffer);
					int fromIndex = MathUtil.byteArrayToInt(buffer);
					inputStream.read(buffer);
					int toIndex = MathUtil.byteArrayToInt(buffer);
					sectionPositionMoveList.add(new SectionPosition(fromIndex, toIndex));
				}
				int length=inputStream.read(buffer);
				if(length>0){
					opcode = MathUtil.byteArrayToInt(buffer);
					opcodeQueue.add(opcode);
				}
			}
				break;
			case OPCODE_INCREASE: {
				inputStream.read(buffer);
				int totalLength = MathUtil.byteArrayToInt(buffer);
				while(totalLength>0){
					totalLength-=inputStream.read(buffer);
					int toIndex = MathUtil.byteArrayToInt(buffer);
					totalLength-=inputStream.read(buffer);
					int valueLength = MathUtil.byteArrayToInt(buffer);
					byte[] valueBuffer=new byte[valueLength];
					totalLength-=inputStream.read(valueBuffer);
					sectionPositionIncreaseList.add(new SectionPosition(-1,toIndex,valueBuffer));
				}
			}
				break;
			}
		}
		return new SectionDiffData(sectionPositionMoveList,sectionPositionIncreaseList);
	}
}
