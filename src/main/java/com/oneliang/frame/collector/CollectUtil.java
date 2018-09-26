package com.oneliang.frame.collector;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import com.oneliang.Constants;
import com.oneliang.util.file.FileUtil;
import com.oneliang.util.http.HttpUtil;
import com.oneliang.util.http.HttpUtil.AdvancedOption;
import com.oneliang.util.http.HttpUtil.Callback;
import com.oneliang.util.http.HttpUtil.HttpNameValue;
import com.oneliang.util.logging.Logger;
import com.oneliang.util.logging.LoggerManager;

public final class CollectUtil {

    private static final Logger logger = LoggerManager.getLogger(CollectUtil.class);

    /**
     * collect from http
     * 
     * @param httpUrl
     * @param httpHeaderList
     * @return ByteArrayOutputStream
     */
    public static ByteArrayOutputStream collectFromHttp(String httpUrl, List<HttpNameValue> httpHeaderList) {
        return collectFromHttp(httpUrl, httpHeaderList, null);
    }

    /**
     * collect from http
     * 
     * @param httpUrl
     * @param httpHeaderList
     * @param advancedOption
     * @return ByteArrayOutputStream
     */
    public static ByteArrayOutputStream collectFromHttp(String httpUrl, List<HttpNameValue> httpHeaderList, AdvancedOption advancedOption) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        HttpUtil.sendRequestGet(httpUrl, httpHeaderList, advancedOption, new Callback() {
            public void httpOkCallback(Map<String, List<String>> headerFieldMap, InputStream inputStream, int contentLength) throws Exception {
                boolean needToUnzip = false;
                if (headerFieldMap != null && headerFieldMap.containsKey(Constants.Http.HeaderKey.CONTENT_ENCODING)) {
                    needToUnzip = headerFieldMap.get(Constants.Http.HeaderKey.CONTENT_ENCODING).contains("gzip");
                }
                InputStream newInputStream = inputStream;
                if (needToUnzip) {
                    newInputStream = new GZIPInputStream(inputStream);
                }
                byte[] buffer = new byte[Constants.Capacity.BYTES_PER_KB];
                int dataLength = -1;
                while ((dataLength = newInputStream.read(buffer, 0, buffer.length)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, dataLength);
                    byteArrayOutputStream.flush();
                }
                byteArrayOutputStream.close();
            }

            public void exceptionCallback(Exception exception) {
                exception.printStackTrace();
                logger.error(Constants.Base.EXCEPTION, exception);
            }

            public void httpNotOkCallback(int responseCode, Map<String, List<String>> headerFieldMap) throws Exception {
                logger.error(String.format("response not ok, http:%s, response code:%s", httpUrl, responseCode));
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
        String filename = httpUrl.replace(Constants.Symbol.SLASH_LEFT, Constants.Symbol.DOLLAR).replace(Constants.Symbol.COLON, Constants.Symbol.AT).replace(Constants.Symbol.QUESTION_MARK, "#");
        String fullFilename = cacheDirectory + "/" + filename + Constants.File.TXT;
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
            byte[] buffer = new byte[Constants.Capacity.BYTES_PER_KB];
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
