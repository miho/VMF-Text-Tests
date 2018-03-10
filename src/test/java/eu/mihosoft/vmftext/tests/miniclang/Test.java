package eu.mihosoft.vmftext.tests.miniclang;

import eu.mihosoft.vmftext.tests.miniclang.parser.MiniClangModelParser;
import eu.mihosoft.vmftext.tests.miniclang.unparser.*;
import eu.mihosoft.vtcc.interpreter.CInterpreter;
import eu.mihosoft.vtcc.interpreter.StringPrintStream;
import junit.framework.Assert;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class Test {
    @org.junit.Test
    public void parseUnparseRunCodeTest() throws IOException {

        MiniClangModelParser parser = new MiniClangModelParser();
        MiniClangModelUnparser unparser = new MiniClangModelUnparser();
        unparser.setFormatter(new MyFormatter());

        String code = new String(Files.readAllBytes(Paths.get("test-code/transpose-blocking.c")));
        MiniClangModel model = parser.parse(code);

        String newCode = unparser.unparse(model);

        System.out.println(newCode);

        StringPrintStream stringPrintStream = new StringPrintStream();
        CInterpreter.execute(newCode).print(stringPrintStream,System.err).waitFor();

        String output = stringPrintStream.toString().replaceAll("\\R","\n");

        System.out.println(output);

        Assert.assertEquals(
                "==FALSE: B isn't the transpose of A\n" +
                "==TRUE:  B is the transpose of A\n",
                output);

    }

}


/**
 * Simple formatter for C-style languages.
 */
class MyFormatter extends BaseFormatter {


    @Override
    public void pre(MiniClangModelUnparser unparser, Formatter.RuleInfo ruleInfo, PrintWriter w) {

        // TODO Allow rule consume() to provide manually...

        String ruleText = ruleInfo.getRuleText();
        String prevRuleText = getPrevRuleInfo().getRuleText();


        if (Objects.equals(prevRuleText, "{")) {
            incIndent();
        }

        if (Objects.equals(ruleText, "}")) {
            decIndent();
        }

        boolean lineBreak = Objects.equals(prevRuleText, "{")
                || Objects.equals(prevRuleText, "}")
                || (Objects.equals(prevRuleText, ";")
                || ruleText.equals("#define"));

        if (matchInclude(prevRuleText)) {
            lineBreak = true;
        }

        if (matchDefine(prevRuleText)) {
            lineBreak = true;
        }

        if (insideFor(prevRuleText)) {
            lineBreak = false;
        }

        if ("else".equals(ruleText) && "}".equals(prevRuleText)) {
            lineBreak = false;
            w.append(" ");
        }


        if (lineBreak) {
            w.append('\n').append(getIndent());
        } else if (IdentifierExpressionUnparser.
                matchIdentifierExpressionAlt0(prevRuleText + ruleText)
                && !ruleText.equals(";") && !ruleText.equals(")") && !ruleText.equals("(")
                && !ruleText.equals(",")
                || prevRuleText.equals(",")
                || prevRuleText.equals("(")
                || ruleText.equals(")")
                || ruleText.equals("{")
                || prevRuleText.equals("#define")
                ) {
            w.append(" ");
        }

        if (prevRuleText.startsWith("//") && prevRuleText.endsWith("\n")) {
            w.append(getIndent());
        }

        super.pre(unparser, ruleInfo, w);

    }

    private boolean insideFor(String prevRuleText) {
        if (prevRuleText.startsWith("for")) {
            setBoolState("for-started", true);
        }

        if (getBoolState("for-started")) {
            String stringState = getStringState("for-;");
            if (stringState == null) stringState = "";
            if (stringState.length() == 2) {
                setStringState("for-;", "");
                setBoolState("for-started", false);
                return false;
            }
            if (";".equals(prevRuleText)) {
                setStringState("for-;", stringState + ";");
            }
        }
        return getBoolState("for-started");
    }

    private boolean matchInclude(String prevRuleText) {
        boolean lineBreak = false;
        if (prevRuleText.startsWith("#include")) {
            setBoolState("include-started", true);
        }
        if (getBoolState("include-started")) {
            setStringState("include-buffer", getStringState("include-buffer") + prevRuleText);
            if (">".equals(prevRuleText)) {
                setBoolState("include-started", false);
                setStringState("include-buffer", "");
                lineBreak = true;
            }
        }
        return lineBreak;
    }

    private boolean matchDefine(String prevRuleText) {
        boolean lineBreak = false;
        if (prevRuleText.startsWith("#define")) {
            setBoolState("define-started", true);
        }
        if (getBoolState("define-started")) {
            setStringState("define-buffer", getStringState("define-buffer") + prevRuleText);
            if (IntLiteralUnparser.matchIntLiteralAlt0(prevRuleText)) {
                setBoolState("define-started", false);
                setStringState("define-buffer", "");
                lineBreak = true;
            }
        }
        return lineBreak;
    }

}
