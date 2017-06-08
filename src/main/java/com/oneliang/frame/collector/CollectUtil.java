package com.oneliang.frame.collector;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import com.oneliang.Constant;
import com.oneliang.util.file.FileUtil;
import com.oneliang.util.http.HttpUtil;
import com.oneliang.util.http.HttpUtil.Callback;
import com.oneliang.util.http.HttpUtil.HttpNameValue;

public final class CollectUtil {

    /**
     * collect from http
     * 
     * @param httpUrl
     * @param httpHeaderList
     * @return ByteArrayOutputStream
     */
    public static ByteArrayOutputStream collectFromHttp(String httpUrl, List<HttpNameValue> httpHeaderList) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        HttpUtil.sendRequestGet(httpUrl, httpHeaderList, new Callback() {
            public void httpOkCallback(Map<String, List<String>> headerFieldMap, InputStream inputStream, int contentLength) throws Exception {
                boolean needToUnzip = false;
                if (headerFieldMap != null && headerFieldMap.containsKey(Constant.Http.HeaderKey.CONTENT_ENCODING)) {
                    needToUnzip = headerFieldMap.get(Constant.Http.HeaderKey.CONTENT_ENCODING).contains("gzip");
                }
                InputStream newInputStream = inputStream;
                if (needToUnzip) {
                    newInputStream = new GZIPInputStream(inputStream);
                }
                byte[] buffer = new byte[Constant.Capacity.BYTES_PER_KB];
                int dataLength = -1;
                while ((dataLength = newInputStream.read(buffer, 0, buffer.length)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, dataLength);
                    byteArrayOutputStream.flush();
                }
                byteArrayOutputStream.close();
            }

            public void exceptionCallback(Exception exception) {
                exception.printStackTrace();
            }

            public void httpNotOkCallback(int responseCode, Map<String, List<String>> headerFieldMap) throws Exception {
            }
        });
        return byteArrayOutputStream;
    }

    /**
     * collect from http with cache
     * 
     * @param httpUrl
     * @param httpHeaderList
     * @param cacheDirectory
     * @return ByteArrayOutputStream
     */
    public static ByteArrayOutputStream collectFromHttpWithCache(String httpUrl, List<HttpNameValue> httpHeaderList, String cacheDirectory) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        String filename = httpUrl.replace(Constant.Symbol.SLASH_LEFT, Constant.Symbol.DOLLAR).replace(Constant.Symbol.COLON, Constant.Symbol.AT).replace(Constant.Symbol.QUESTION_MARK, "#");
        String fullFilename = cacheDirectory + "/" + filename + Constant.File.TXT;
        if (FileUtil.isExist(fullFilename)) {
            byteArrayOutputStream = collectFromLocal(fullFilename);
        } else {
            byteArrayOutputStream = collectFromHttp(httpUrl, httpHeaderList);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            if (byteArray.length > 0) {
                FileUtil.writeFile(fullFilename, byteArray);
            }
        }
        return byteArrayOutputStream;
    }

    /**
     * collect from local
     * 
     * @param fullFilename
     * @return ByteArrayOutputStream
     */
    public static ByteArrayOutputStream collectFromLocal(String fullFilename) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(fullFilename);
            byte[] buffer = new byte[Constant.Capacity.BYTES_PER_KB];
            int dataLength = -1;
            while ((dataLength = fileInputStream.read(buffer, 0, buffer.length)) != -1) {
                byteArrayOutputStream.write(buffer, 0, dataLength);
                byteArrayOutputStream.flush();
            }
        } catch (Exception e) {
            throw new CollectUtilException(fullFilename, e);
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (Exception e) {
                throw new CollectUtilException(fullFilename, e);
            }
        }

        return byteArrayOutputStream;
    }

    public static class CollectUtilException extends RuntimeException {
        private static final long serialVersionUID = -5375427316402564383L;

        public CollectUtilException(String message) {
            super(message);
        }

        public CollectUtilException(Throwable cause) {
            super(cause);
        }

        public CollectUtilException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
