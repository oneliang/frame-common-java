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

    public static final int OPCODE_MOVE = 0;
    public static final int OPCODE_INCREASE = 1;
    public static final int OPCODE_MERGE_MOVE = 2;
    public static final int EMPTY_FROM = -1;// 0xffffffff
    private static final int MODE_MOVE = 0;
    private static final int MODE_MERGE_MOVE = 1;

    private int mode = MODE_MOVE;
    private int sectionSize = 0;

    public final List<SectionPosition> sectionPositionMoveList;
    public final List<SectionPosition> sectionPositionIncreaseList;
    private final List<MergeSectionPosition> mergeSectionPositionList;

    /**
     * constructor
     * 
     * @param sectionPositionMoveList
     * @param sectionPositionIncreaseList
     */
    public SectionDiffData(List<SectionPosition> sectionPositionMoveList, List<SectionPosition> sectionPositionIncreaseList) {
        this.sectionPositionMoveList = sectionPositionMoveList;
        if (this.sectionPositionMoveList != null) {
            this.sectionSize += this.sectionPositionMoveList.size();
        }
        this.sectionPositionIncreaseList = sectionPositionIncreaseList;
        if (this.sectionPositionIncreaseList != null) {
            this.sectionSize += this.sectionPositionIncreaseList.size();
        }
        int moveByteSize = sectionPositionMoveList.size() * 2 * 4;
        System.out.println("move total byte size:" + moveByteSize);
        List<MergeSectionPosition> mergeSectionPositionList = mergeSectionPosition(sectionPositionMoveList);
        int mergeMoveByteSize = mergeSectionPositionList.size() * 4 * 4;
        System.out.println("merge move total byte size:" + mergeMoveByteSize);
        if (mergeMoveByteSize < moveByteSize) {
            mode = MODE_MERGE_MOVE;
            this.mergeSectionPositionList = mergeSectionPositionList;
        } else {
            this.mergeSectionPositionList = null;
        }
        System.out.println("mode:" + mode);
    }

    /**
     * merge section position
     * 
     * @param sectionPositionMoveList
     * @return List<MergeSectionPosition>
     */
    public static List<MergeSectionPosition> mergeSectionPosition(List<SectionPosition> sectionPositionMoveList) {
        List<MergeSectionPosition> mergeSectionPositionList = new ArrayList<MergeSectionPosition>();
        if (sectionPositionMoveList != null) {
            SectionPosition pre = null;
            MergeSectionPosition merge = null;
            for (SectionPosition cur : sectionPositionMoveList) {
                if (pre != null) {
                    if ((cur.getFromIndex() - pre.getFromIndex() == 1) && (cur.getToIndex() - pre.getToIndex() == 1)) {
                        // System.out.println(String.format("Section
                        // merge,f(%s->%s),t(%s->%s)",pre.getFromIndex(),cur.getFromIndex(),pre.getToIndex(),cur.getToIndex()));
                        if (merge == null) {
                            merge = new MergeSectionPosition();
                            mergeSectionPositionList.add(merge);
                        }
                        if (merge.oldBegin == -1) {
                            merge.oldBegin = pre.getFromIndex();
                        }
                        merge.oldEnd = cur.getFromIndex();
                        if (merge.newBegin == -1) {
                            merge.newBegin = pre.getToIndex();
                        }
                        merge.newEnd = cur.getToIndex();
                        // System.out.println(String.format("after
                        // merge:(%s->%s),new:(%s->%s)",merge.oldBegin,merge.oldEnd,merge.newBegin,merge.newEnd));
                    } else {
                        merge = null;
                        MergeSectionPosition original = new MergeSectionPosition();
                        original.oldBegin = pre.getFromIndex();
                        original.oldEnd = pre.getFromIndex();
                        original.newBegin = pre.getToIndex();
                        original.newEnd = pre.getToIndex();
                        mergeSectionPositionList.add(original);
                    }
                }
                pre = cur;
            }
        }
        for (MergeSectionPosition merge : mergeSectionPositionList) {
            System.out.println(String.format("after merge:to:(%s ~ %s),from:(%s ~ %s)", merge.newBegin, merge.newEnd, merge.oldBegin, merge.oldEnd));
        }
        return mergeSectionPositionList;
    }

    @Deprecated
    public byte[] toByteArray2() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (this.sectionPositionMoveList != null && !this.sectionPositionMoveList.isEmpty()) {
            ByteArrayOutputStream moveListOutputStream = new ByteArrayOutputStream();
            for (SectionPosition sectionPosition : this.sectionPositionMoveList) {
                moveListOutputStream.write(MathUtil.intToByteArray(sectionPosition.getFromIndex()));
                moveListOutputStream.write(MathUtil.intToByteArray(sectionPosition.getToIndex()));
            }
            byteArrayOutputStream.write(MathUtil.intToByteArray(OPCODE_MOVE));
            byte[] moveByteArray = moveListOutputStream.toByteArray();
            byteArrayOutputStream.write(MathUtil.intToByteArray(moveByteArray.length));
            byteArrayOutputStream.write(moveByteArray);
        }
        if (this.sectionPositionIncreaseList != null && !this.sectionPositionIncreaseList.isEmpty()) {
            ByteArrayOutputStream increaseListOutputStream = new ByteArrayOutputStream();
            for (SectionPosition sectionPosition : this.sectionPositionIncreaseList) {
                increaseListOutputStream.write(MathUtil.intToByteArray(sectionPosition.getToIndex()));
                byte[] sectionByteArray = sectionPosition.getByteArray();
                increaseListOutputStream.write(MathUtil.intToByteArray(sectionByteArray.length));
                increaseListOutputStream.write(sectionByteArray);
            }
            byteArrayOutputStream.write(MathUtil.intToByteArray(OPCODE_INCREASE));
            byte[] increaseByteArray = increaseListOutputStream.toByteArray();
            byteArrayOutputStream.write(MathUtil.intToByteArray(increaseByteArray.length));
            byteArrayOutputStream.write(increaseByteArray);
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * move: type[4]length[4]data[[4][4][4]...]
     * 
     * merge move:type[4]length[4]data[to[4][4]from[4][4]...]
     * 
     * increase:type[4]length[4]data[index[4]length[4]data[n]]
     * 
     * @return byte[]
     * @throws Exception
     */
    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (this.mode == MODE_MOVE) {
            int[] moveArray = new int[this.sectionSize];
            for (int i = 0; i < moveArray.length; i++) {
                moveArray[i] = EMPTY_FROM;
            }
            if (this.sectionPositionMoveList != null && !this.sectionPositionMoveList.isEmpty()) {
                byteArrayOutputStream.write(MathUtil.intToByteArray(OPCODE_MOVE));
                byteArrayOutputStream.write(MathUtil.intToByteArray(moveArray.length * 4));
                for (SectionPosition sectionPosition : this.sectionPositionMoveList) {
                    moveArray[sectionPosition.getToIndex()] = sectionPosition.getFromIndex();
                }
                for (int i = 0; i < moveArray.length; i++) {
                    byteArrayOutputStream.write(MathUtil.intToByteArray(moveArray[i]));
                }
            }
        } else {
            if (this.mergeSectionPositionList != null && !this.mergeSectionPositionList.isEmpty()) {
                int mergeMoveLength = this.mergeSectionPositionList.size();
                byteArrayOutputStream.write(MathUtil.intToByteArray(OPCODE_MERGE_MOVE));
                byteArrayOutputStream.write(MathUtil.intToByteArray(mergeMoveLength * 4 * 4));
                for (MergeSectionPosition mergeSectionPosition : this.mergeSectionPositionList) {
                    byteArrayOutputStream.write(MathUtil.intToByteArray(mergeSectionPosition.newBegin));
                    byteArrayOutputStream.write(MathUtil.intToByteArray(mergeSectionPosition.newEnd));
                    byteArrayOutputStream.write(MathUtil.intToByteArray(mergeSectionPosition.oldBegin));
                    byteArrayOutputStream.write(MathUtil.intToByteArray(mergeSectionPosition.oldEnd));
                }
            }
        }
        if (this.sectionPositionIncreaseList != null && !this.sectionPositionIncreaseList.isEmpty()) {
            ByteArrayOutputStream increaseListOutputStream = new ByteArrayOutputStream();
            for (SectionPosition sectionPosition : this.sectionPositionIncreaseList) {
                byte[] sectionByteArray = sectionPosition.getByteArray();
                increaseListOutputStream.write(MathUtil.intToByteArray(sectionPosition.getToIndex()));
                increaseListOutputStream.write(MathUtil.intToByteArray(sectionByteArray.length));
                increaseListOutputStream.write(sectionByteArray);
            }
            byte[] increaseByteArray = increaseListOutputStream.toByteArray();
            byteArrayOutputStream.write(MathUtil.intToByteArray(OPCODE_INCREASE));
            byteArrayOutputStream.write(MathUtil.intToByteArray(increaseByteArray.length));
            byteArrayOutputStream.write(increaseByteArray);
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * parse from
     * 
     * @param byteArray
     * @return
     * @throws Exception
     */
    public static SectionDiffData parseFrom(byte[] byteArray) throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        return parseFrom(byteArrayInputStream);
    }

    /**
     * parse from
     * 
     * @param inputStream
     * @return SectionDiffData
     * @throws Exception
     */
    public static SectionDiffData parseFrom(InputStream inputStream) throws Exception {
        List<SectionPosition> sectionPositionMoveList = new ArrayList<SectionPosition>();
        List<SectionPosition> sectionPositionIncreaseList = new ArrayList<SectionPosition>();
        byte[] buffer = new byte[4];
        inputStream.read(buffer);
        Queue<Integer> opcodeQueue = new ConcurrentLinkedQueue<Integer>();
        int opcode = MathUtil.byteArrayToInt(buffer);
        opcodeQueue.add(opcode);
        while (!opcodeQueue.isEmpty()) {
            opcode = opcodeQueue.poll();
            switch (opcode) {
            case OPCODE_MOVE: {
                inputStream.read(buffer);
                int allSectionLength = MathUtil.byteArrayToInt(buffer) / 4;// all
                                                                           // section
                                                                           // length
                for (int i = 0; i < allSectionLength; i++) {
                    inputStream.read(buffer);
                    int fromIndex = MathUtil.byteArrayToInt(buffer);
                    sectionPositionMoveList.add(new SectionPosition(fromIndex, i));
                }
                int length = inputStream.read(buffer);
                if (length > 0) {
                    opcode = MathUtil.byteArrayToInt(buffer);
                    opcodeQueue.add(opcode);
                }
            }
                break;
            case OPCODE_INCREASE: {
                inputStream.read(buffer);
                int totalLength = MathUtil.byteArrayToInt(buffer);
                while (totalLength > 0) {
                    totalLength -= inputStream.read(buffer);
                    int toIndex = MathUtil.byteArrayToInt(buffer);
                    totalLength -= inputStream.read(buffer);
                    int valueLength = MathUtil.byteArrayToInt(buffer);
                    byte[] valueBuffer = new byte[valueLength];
                    totalLength -= inputStream.read(valueBuffer);
                    sectionPositionIncreaseList.add(new SectionPosition(-1, toIndex, valueBuffer));
                }
            }
                break;
            case OPCODE_MERGE_MOVE: {
                inputStream.read(buffer);
                int allSectionLength = MathUtil.byteArrayToInt(buffer) / 4 / 4;// all
                // section
                // length
                for (int i = 0; i < allSectionLength; i++) {
                    inputStream.read(buffer);
                    int toBeginIndex = MathUtil.byteArrayToInt(buffer);
                    inputStream.read(buffer);
                    int toEndIndex = MathUtil.byteArrayToInt(buffer);
                    inputStream.read(buffer);
                    int fromBeginIndex = MathUtil.byteArrayToInt(buffer);
                    inputStream.read(buffer);
                    int fromEndIndex = MathUtil.byteArrayToInt(buffer);
                    if (toEndIndex - toBeginIndex != fromEndIndex - fromBeginIndex) {
                        throw new RuntimeException(String.format("diff data error.to(%s->%s)from(%s->%s),", toBeginIndex, toEndIndex, fromBeginIndex, fromEndIndex));
                    } else {
                        int count = toEndIndex - toBeginIndex;
                        for (int j = 0; j <= count; j++) {
                            sectionPositionMoveList.add(new SectionPosition(fromBeginIndex + j, toBeginIndex + j));
                        }
                    }
                }
                int length = inputStream.read(buffer);
                if (length > 0) {
                    opcode = MathUtil.byteArrayToInt(buffer);
                    opcodeQueue.add(opcode);
                }
            }
                break;
            }

        }
        return new SectionDiffData(sectionPositionMoveList, sectionPositionIncreaseList);
    }
}
