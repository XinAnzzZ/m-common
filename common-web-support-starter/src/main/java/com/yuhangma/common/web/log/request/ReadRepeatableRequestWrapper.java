package com.yuhangma.common.web.log.request;

import org.springframework.lang.Nullable;
import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Moore.Ma
 * @version 1.0
 * @since 2021/12/3
 */
public class ReadRepeatableRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 流中的内容将被缓存在这里
     */
    private final byte[] cachedContent;

    public ReadRepeatableRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        // 将流中内容缓存起来
        cachedContent = StreamUtils.copyToByteArray(request.getInputStream());
    }

    public byte[] getCachedContent() {
        return this.cachedContent;
    }

    @Override
    public ServletInputStream getInputStream() {
        return new ReadRepeatableInputStream(cachedContent);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(), getCharacterEncoding()));
    }

    private static class ReadRepeatableInputStream extends ServletInputStream {

        private byte[] body;

        private int lastRetrievedIndex = -1;

        @Nullable
        private ReadListener listener;

        public ReadRepeatableInputStream(byte[] body) {
            this.body = body;
        }

        @Override
        public int read() throws IOException {
            if (isFinished()) {
                return -1;
            }
            int i = body[++lastRetrievedIndex];
            if (isFinished() && listener != null) {
                try {
                    listener.onAllDataRead();
                } catch (IOException e) {
                    listener.onError(e);
                    throw e;
                }
            }
            return i;
        }

        @Override
        public boolean isFinished() {
            return lastRetrievedIndex == body.length - 1;
        }

        @Override
        public boolean isReady() {
            return isFinished();
        }

        @Override
        public void setReadListener(ReadListener listener) {
            if (listener == null) {
                throw new IllegalArgumentException("listener cann not be null");
            }
            if (this.listener != null) {
                throw new IllegalArgumentException("listener has been set");
            }
            this.listener = listener;
            if (!isFinished()) {
                try {
                    listener.onAllDataRead();
                } catch (IOException e) {
                    listener.onError(e);
                }
            } else {
                try {
                    listener.onAllDataRead();
                } catch (IOException e) {
                    listener.onError(e);
                }
            }
        }

        @Override
        public int available() {
            return body.length - lastRetrievedIndex - 1;
        }

        @Override
        public void close() {
            lastRetrievedIndex = body.length - 1;
            body = null;
        }
    }
}
