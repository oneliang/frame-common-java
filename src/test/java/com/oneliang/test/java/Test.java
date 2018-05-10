package com.oneliang.test.java;

import java.io.RandomAccessFile;

import com.oneliang.util.common.StringUtil;
import com.oneliang.util.concurrent.ThreadPool;
import com.oneliang.util.concurrent.ThreadTask;
import com.oneliang.util.file.FileUtil;

public class Test {
    public static void main(String[] args) throws Exception {
        final int segmentSize = 2;
        FileUtil.writeFile("/D:/a.txt", new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
        RandomAccessFile randomAccessFile = new RandomAccessFile("/D:/a.txt", "r");
        long length = randomAccessFile.length();
        int segmentCount = (int) Math.round(((double) length / segmentSize) + 0.5d);
        System.out.println("segment size:" + segmentCount);
        for (int i = 0; i < segmentCount; i++) {
            randomAccessFile.seek(i * segmentSize);
            if (i < segmentCount - 1) {
                byte[] buffer = new byte[segmentSize];
                randomAccessFile.read(buffer, 0, buffer.length);
                System.out.println(buffer[0] + "," + buffer[1]);
            } else {
                int leftLength = (int) (length - i * segmentSize);
                byte[] buffer = new byte[leftLength];
                int readCount = randomAccessFile.read(buffer, 0, buffer.length);
                System.out.println(buffer[0]);
            }
        }
        randomAccessFile.close();
    }

    public static void main2(String[] args) {
        ThreadPool threadPool = new ThreadPool();
        threadPool.setMinThreads(1);
        threadPool.setMaxThreads(4);
        for (int i = 0; i < 10; i++) {
            final int j = i;
            threadPool.addThreadTask(new ThreadTask() {
                public void runTask() {
                    System.out.println("a" + j);
                    try {
                        Thread.sleep(j * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
        threadPool.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadPool.interrupt();
    }

    public static void main1(String[] args) {
        // try {
        // String keyString = "aaabbbccc";
        // String json = "{id:'id',name:'name您好吗',type:0}";
        // KeyGenerator localKeyGenerator = KeyGenerator.getInstance("DES");
        // localKeyGenerator.init(new SecureRandom(keyString.getBytes()));
        // Key key = localKeyGenerator.generateKey();
        // Cipher cipher = Cipher.getInstance("DES");
        // cipher.init(Cipher.ENCRYPT_MODE, key);
        // byte[] array = cipher.doFinal(json.getBytes());
        // System.out.println(StringUtil.byteToHexString(array));
        // cipher.init(Cipher.DECRYPT_MODE, key);
        // System.out.println(new String(cipher.doFinal(array)));
        //
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        String string = "!(/a.do?action=b&b=a&&bb&&cc)";
        String andRegex = "^[\\S]*&&[\\S]*$";
        String orRegex = "^[\\S]*\\|\\|[\\S]*$";
        String singleNoRegex = "^!\\S*$";
        String andNoRegex = "^!\\([\\S]*&&[\\S]*\\)$";
        String orNoRegex = "^!\\([\\S]*\\|\\|[\\S]*\\)$";
        if (StringUtil.isMatchRegex(string, andNoRegex)) {
            System.out.println("and no");
        } else if (StringUtil.isMatchRegex(string, orNoRegex)) {
            System.out.println("or no");
        } else if (StringUtil.isMatchRegex(string, singleNoRegex)) {
            System.out.println("single no");
        } else if (StringUtil.isMatchRegex(string, andRegex)) {
            System.out.println("and");
        } else if (StringUtil.isMatchRegex(string, orRegex)) {
            System.out.println("or");
        } else {
            System.out.println("single");
        }
    }
}
