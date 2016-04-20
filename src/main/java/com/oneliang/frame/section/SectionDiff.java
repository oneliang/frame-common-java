package com.oneliang.frame.section;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oneliang.util.common.Generator;
import com.oneliang.util.common.StringUtil;

public class SectionDiff {

	public SectionDiffData diff(List<Section> oldSectionList,List<Section> newSectionList) throws Exception{

		List<SectionPosition> sectionPositionMoveList=new ArrayList<SectionPosition>();
		List<SectionPosition> sectionPositionIncreaseList=new ArrayList<SectionPosition>();

		Map<String,Integer> oldSectionMap=new HashMap<String,Integer>();
		for(int i=0;i<oldSectionList.size();i++){
			Section oldSection=oldSectionList.get(i);
			oldSectionMap.put(StringUtil.byteArrayToHexString(oldSection.toByteArray()),i);
		}
		for(int i=0;i<newSectionList.size();i++){
			Section newSection=newSectionList.get(i);
			String key=StringUtil.byteArrayToHexString(newSection.toByteArray());
			if(oldSectionMap.containsKey(key)){
				int j=oldSectionMap.get(key);
				sectionPositionMoveList.add(new SectionPosition(j,i));
				if(i!=j){
					System.out.println(String.format("section find in old index(old->new)(index:%s->%s", j,i));
				}else{
					System.out.println(String.format("section find in old index(old->new)(index:%s->%s),no need to move,but copy", j,i));
				}
			}else{
				sectionPositionIncreaseList.add(new SectionPosition(-1,i, newSection.toByteArray()));
				System.out.println(String.format("section increase in new index(new)(index:%s,value:%s)", i,StringUtil.byteArrayToHexString(newSection.toByteArray())));
			}
		}
		for(SectionPosition sectionPosition:sectionPositionMoveList){
			System.out.println("move:"+sectionPosition);
		}
		for(SectionPosition sectionPosition:sectionPositionIncreaseList){
			System.out.println("increase:"+sectionPosition);
		}
		return new SectionDiffData(sectionPositionMoveList, sectionPositionIncreaseList);
	}

//	@SuppressWarnings("unchecked")
	public void patch(List<Section> oldSectionList,List<SectionPosition> sectionPositionMoveList,List<SectionPosition> sectionPositionIncreaseList) throws Exception{
		System.out.println("----------start patch----------");
		Section[] oldSectionArray=oldSectionList.toArray(new Section[]{});
		Section[] newSectionArray=new Section[oldSectionArray.length];
			//move
		if(sectionPositionMoveList!=null){
			for(SectionPosition sectionPosition:sectionPositionMoveList){
				int fromIndex=sectionPosition.getFromIndex();//old index
				int toIndex=sectionPosition.getToIndex();//new index,maybe bigger then old index
				if(toIndex+1>newSectionArray.length){
					Section[] tempNewSectionArray=new Section[toIndex+1];
					System.arraycopy(newSectionArray, 0, tempNewSectionArray, 0, newSectionArray.length);
					newSectionArray=tempNewSectionArray;
				}
				//move fromIndex to toIndex
				System.out.println("from:"+fromIndex+",to:"+toIndex+","+StringUtil.byteArrayToHexString(oldSectionArray[fromIndex].toByteArray()));
				newSectionArray[toIndex]=oldSectionArray[fromIndex];
			}
		}
		//increase
		if(sectionPositionIncreaseList!=null){
			for(SectionPosition sectionPosition:sectionPositionIncreaseList){
				int toIndex=sectionPosition.getToIndex();//new index,maybe bigger then old index
				if(toIndex+1>newSectionArray.length){
					Section[] tempNewSectionArray=new Section[toIndex+1];
					System.arraycopy(newSectionArray, 0, tempNewSectionArray, 0, newSectionArray.length);
					newSectionArray=tempNewSectionArray;
				}
				byte[] byteArray=sectionPosition.getByteArray();
				newSectionArray[toIndex]=new UnitSection(byteArray);
			}
		}
		//print
//		System.out.println("----------end patch then print----------");
//		for(int i=0;i<newSectionArray.length;i++){
//			if(newSectionArray[i]!=null){
//				System.out.println("index:"+i+",value:"+StringUtil.byteArrayToHexString(newSectionArray[i].toByteArray()));
//			}
//		}
	}

	public void printSectionList(List<Section> sectionList) throws Exception{
		ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
		for(Section section:sectionList){
			if(section!=null){
				byteArrayOutputStream.write(section.toByteArray());
			}
		}
		System.out.println("length:"+byteArrayOutputStream.toByteArray().length+","+StringUtil.byteArrayToHexString(Generator.MD5ByteArray(byteArrayOutputStream.toByteArray())));
	}
}
