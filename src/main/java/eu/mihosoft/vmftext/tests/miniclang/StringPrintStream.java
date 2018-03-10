package eu.mihosoft.vmftext.tests.miniclang;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public final class StringPrintStream extends PrintStream{
    private ByteArrayOutputStream baos;
    private PrintStream ps;


    private StringPrintStream(ByteArrayOutputStream outputStream) {
        super(outputStream);
        baos = outputStream;
        ps = this;
    }

    public static StringPrintStream newInstance() {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        StringPrintStream ps = new StringPrintStream(bs);

        return ps;
    }

    public String toString() {
        ps.flush();

        String output = new String(baos.toByteArray(),
                StandardCharsets.UTF_8);

        return output;
    }
}
