package com.oneliang.frame.section;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.oneliang.util.common.Generator;
import com.oneliang.util.common.StringUtil;

public class SectionDiff {

    public SectionDiffData diff(List<Section> oldSectionList, List<Section> newSectionList) throws Exception {

        final List<SectionPosition> sectionPositionMoveList = new ArrayList<SectionPosition>();
        final List<SectionPosition> sectionPositionIncreaseList = new ArrayList<SectionPosition>();

        Map<String, Set<Integer>> oldSectionMap = new HashMap<String, Set<Integer>>();
        for (int i = 0; i < oldSectionList.size(); i++) {
            Section oldSection = oldSectionList.get(i);
            String key = StringUtil.byteArrayToHexString(oldSection.toByteArray());
            Set<Integer> positionSet = null;
            if (oldSectionMap.containsKey(key)) {
                positionSet = oldSectionMap.get(key);
            } else {
                positionSet = new HashSet<Integer>();
                oldSectionMap.put(key, positionSet);
            }
            positionSet.add(i);
        }
        System.out.println(String.format("old section list size:%s,old section map size:%s", oldSectionList.size(), oldSectionMap.size()));
        for (int i = 0; i < newSectionList.size(); i++) {
            Section newSection = newSectionList.get(i);
            String key = StringUtil.byteArrayToHexString(newSection.toByteArray());
            if (oldSectionMap.containsKey(key)) {
                Set<Integer> oldPositionSet = oldSectionMap.get(key);
                int j = i;
                if (oldPositionSet.contains(i)) {
                    j = i;// same position
                } else {
                    j = oldPositionSet.iterator().next();// pick one
                }
                sectionPositionMoveList.add(new SectionPosition(j, i));
                if (i != j) {
                    System.out.println(String.format("section find in old index(old->new)(index:%s->%s)", j, i));
                } else {
                    System.out.println(String.format("section find in old index(old->new)(index:%s->%s),no need to move,but copy", j, i));
                }
            } else {
                sectionPositionIncreaseList.add(new SectionPosition(-1, i, newSection.toByteArray()));
                System.out.println(String.format("section increase in new index(new)(index:%s,value:%s)", i, StringUtil.byteArrayToHexString(newSection.toByteArray())));
            }
        }
        for (SectionPosition sectionPosition : sectionPositionMoveList) {
            System.out.println("move:" + sectionPosition);
        }
        for (SectionPosition sectionPosition : sectionPositionIncreaseList) {
            System.out.println("increase:" + sectionPosition);
        }
        return new SectionDiffData(sectionPositionMoveList, sectionPositionIncreaseList);
    }

    // @SuppressWarnings("unchecked")
    public byte[] patch(List<Section> oldSectionList, List<SectionPosition> newSectionPositionMoveList, List<SectionPosition> newSectionPositionIncreaseList) throws Exception {
        System.out.println("----------start patch----------");
        int newSectionSize = 0;
        if (newSectionPositionMoveList != null) {
            newSectionSize += newSectionPositionMoveList.size();
        }
        if (newSectionPositionIncreaseList != null) {
            newSectionSize += newSectionPositionIncreaseList.size();
        }
        Section[] oldSectionArray = oldSectionList.toArray(new Section[] {});
        Section[] newSectionArray = new Section[newSectionSize];
        // move
        if (newSectionPositionMoveList != null) {
            for (SectionPosition sectionPosition : newSectionPositionMoveList) {
                int fromIndex = sectionPosition.getFromIndex();
                int toIndex = sectionPosition.getToIndex();
                if (fromIndex == SectionDiffData.EMPTY_FROM) {
                    System.out.println(String.format("may be increase,index:%s", toIndex));
                    continue;
                }
                // if (toIndex + 1 > newSectionArray.length) {
                // Section[] tempNewSectionArray = new Section[toIndex + 1];
                // System.arraycopy(newSectionArray, 0, tempNewSectionArray, 0,
                // newSectionArray.length);
                // newSectionArray = tempNewSectionArray;
                // }
                // move fromIndex to toIndex
                System.out.println("from:" + fromIndex + ",to:" + toIndex + "," + StringUtil.byteArrayToHexString(oldSectionArray[fromIndex].toByteArray()));
                newSectionArray[toIndex] = oldSectionArray[fromIndex];
            }
        }
        // increase
        if (newSectionPositionIncreaseList != null) {
            for (SectionPosition sectionPosition : newSectionPositionIncreaseList) {
                int toIndex = sectionPosition.getToIndex();// new index,maybe
                                                           // bigger then old
                                                           // index
                // if (toIndex + 1 > newSectionArray.length) {
                // Section[] tempNewSectionArray = new Section[toIndex + 1];
                // System.arraycopy(newSectionArray, 0, tempNewSectionArray, 0,
                // newSectionArray.length);
                // newSectionArray = tempNewSectionArray;
                // }
                byte[] byteArray = sectionPosition.getByteArray();
                newSectionArray[toIndex] = new UnitSection(byteArray);
            }
        }
        // merge data
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i = 0; i < newSectionArray.length; i++) {
            if (newSectionArray[i] != null) {
                byteArrayOutputStream.write(newSectionArray[i].toByteArray());
                System.out.println("index:" + i + ",value:" + StringUtil.byteArrayToHexString(newSectionArray[i].toByteArray()));
            } else {
                System.out.println(String.format("error, can no be null, may be a bug, index:%s", i));
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    public void printSectionList(List<Section> sectionList) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (Section section : sectionList) {
            if (section != null) {
                byteArrayOutputStream.write(section.toByteArray());
            }
        }
        System.out.println("length:" + byteArrayOutputStream.toByteArray().length + "," + StringUtil.byteArrayToHexString(Generator.MD5ByteArray(byteArrayOutputStream.toByteArray())));
    }
}
