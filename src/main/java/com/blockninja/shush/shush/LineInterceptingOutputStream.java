package com.blockninja.shush.shush;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class LineInterceptingOutputStream extends OutputStream {
    private final OutputStream original;
    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    public LineInterceptingOutputStream(OutputStream original) {
        this.original = original;
    }

    @Override
    public void write(int b) throws IOException {
        if (b == '\n') {
            String line = buffer.toString(StandardCharsets.UTF_8);
            buffer.reset();

            if (!Shush.shouldSuppress(line)) {
                original.write(line.getBytes(StandardCharsets.UTF_8));
                original.write('\n');
            }
        } else {
            buffer.write(b);
        }
    }

    @Override
    public void flush() throws IOException {
        // Just in case something is left to flush
        if (buffer.size() > 0) {
            String line = buffer.toString(StandardCharsets.UTF_8);
            buffer.reset();

            if (!Shush.shouldSuppress(line)) {
                original.write(line.getBytes(StandardCharsets.UTF_8));
                original.write('\n');
            }
        }

        original.flush();
    }

    @Override
    public void close() throws IOException {
        flush();
        original.close();
    }
}