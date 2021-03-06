package org.zalando.nakadi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.luben.zstd.ZstdInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.zalando.problem.Problem;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.zip.GZIPInputStream;

import static org.springframework.http.HttpHeaders.CONTENT_ENCODING;
import static org.zalando.problem.Status.NOT_ACCEPTABLE;

public class CompressionBodyRequestFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(CompressionBodyRequestFilter.class);

    private final ObjectMapper objectMapper;

    public CompressionBodyRequestFilter(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public final void doFilter(final ServletRequest servletRequest,
                               final ServletResponse servletResponse,
                               final FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        final Optional<String> contentEncodingOpt = Optional.ofNullable(
                request.getHeader(CONTENT_ENCODING));
        if (contentEncodingOpt.isPresent() && !HttpMethod.POST.matches(request.getMethod())) {
            reportNotAcceptableError((HttpServletResponse) servletResponse, request);
            return;
        } else if (contentEncodingOpt.isPresent()) {
            final String contentEncoding = contentEncodingOpt.get();
            if (contentEncoding.contains("gzip")) {
                request = new FilterServletRequestWrapper(request, GZIPInputStream::new);
            } else if (contentEncoding.contains("zstd")) {
                request = new FilterServletRequestWrapper(request, ZstdInputStream::new);
            }
        }

        chain.doFilter(request, servletResponse);
    }

    private void reportNotAcceptableError(final HttpServletResponse response,
                                          final HttpServletRequest request)
            throws IOException {

        response.setStatus(NOT_ACCEPTABLE.getStatusCode());
        final PrintWriter writer = response.getWriter();
        final Problem problem = Problem.valueOf(NOT_ACCEPTABLE,
                request.getMethod() + " method doesn't support gzip content encoding");
        writer.write(objectMapper.writeValueAsString(problem));
        writer.close();
    }

    @Override
    public void init(final FilterConfig filterConfig) {
        // filter is stateless, nothing to init
    }

    @Override
    public void destroy() {
        // filter is stateless, nothing to destroy
    }

    static class FilterServletRequestWrapper extends HttpServletRequestWrapper {

        private final StreamConverter streamConverter;

        interface StreamConverter {
            InputStream to(InputStream is) throws IOException;
        }

        FilterServletRequestWrapper(
                final HttpServletRequest request,
                final StreamConverter streamConverter) {
            super(request);
            this.streamConverter = streamConverter;
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return new ServletInputStreamWrapper(streamConverter.to(super.getInputStream()));
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(this.getInputStream()));
        }
    }

    static class ServletInputStreamWrapper extends ServletInputStream {

        private final InputStream inputStream;

        ServletInputStreamWrapper(final InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public int read() throws IOException {
            return inputStream.read();
        }

        @Override
        public void close() throws IOException {
            inputStream.close();
        }

        @Override
        public boolean isFinished() {
            try {
                return inputStream.available() == 0;
            } catch (final IOException e) {
                LOG.error("Error occurred when reading request input stream", e);
                return false;
            }
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(final ReadListener listener) {
            throw new UnsupportedOperationException("Not supported");
        }
    }

}
