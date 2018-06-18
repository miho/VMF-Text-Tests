package eu.mihosoft.vmftext.tests.java8;

import eu.mihosoft.vmftext.tests.java8.parser.Java8ModelParser;
import eu.mihosoft.vmftext.tests.java8.unparser.BaseFormatter;
import eu.mihosoft.vmftext.tests.java8.unparser.Formatter;
import eu.mihosoft.vmftext.tests.java8.unparser.Java8ModelUnparser;
import org.junit.Assert;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class Test {

    @org.junit.Test
    public void testParseModifyUnparseBenchmark() throws Exception {

        String code = new String(Files.readAllBytes(Paths.get("test-code/Java8RungeKuttaBenchmark.java")));

        Java8ModelParser parser = new Java8ModelParser();
        Java8Model model = parser.parse(code);

        Java8ModelUnparser unparser = new Java8ModelUnparser();
        unparser.setFormatter(new MyFormatter());

        long timeStampBegin = System.nanoTime();

        for (int i = 0; i < 1000; i++) {
            String out = unparser.unparse(model);
        }

        long timeStampEnd = System.nanoTime();

        System.out.println("Duration: " + (timeStampEnd - timeStampBegin) * 1e-9);

    }

    @org.junit.Test
    public void testParseModifyUnparseCompileAndRun() throws Exception {

        System.out.println("START");

        Java8ModelParser parser = new Java8ModelParser();
        Java8Model model = parser.parse(new FileInputStream("test-code/Java8RungeKutta.java"));

        System.out.println("PARSED");

        model.vmf().content().stream().filter(e -> e instanceof ClassDeclaration).map(e -> (ClassDeclaration) e).
                filter(cls -> "Java8RungeKutta".equals(cls.getClassName())).forEach(cls -> cls.setClassName(cls.getClassName() + "Out"));

        Java8ModelUnparser unparser = new Java8ModelUnparser();
        unparser.setFormatter(new MyFormatter());

        try (FileWriter fw = new FileWriter(new File("test-code/Java8RungeKuttaOut.java"))) {
            unparser.unparse(model, fw);
        }

        InMemoryJavaCompiler compiler = InMemoryJavaCompiler.newInstance();

        Class<?> originalClass = compiler.compile("Java8RungeKutta",
                new String(Files.readAllBytes(Paths.get("test-code/Java8RungeKutta.java"))));

        Class<?> unparsedClass = compiler.compile("Java8RungeKuttaOut",
                new String(Files.readAllBytes(Paths.get("test-code/Java8RungeKuttaOut.java"))));


        System.out.println("\nRUNNING original version:");

        originalClass.getMethod("main", String[].class).invoke(null, (Object) new String[0]);

        double valueOrig = originalClass.getField("value").getDouble(null);

        System.out.println("\nRUNNING unparsed version:");

        unparsedClass.getMethod("main", String[].class).invoke(null, (Object) new String[0]);

        double valueUnparsed = originalClass.getField("value").getDouble(null);

        Assert.assertTrue(valueOrig != 0);
        Assert.assertEquals(valueOrig, valueUnparsed, 1e-12);

    }

    @org.junit.Test
    public void testParseUnparseComplexCode() throws Exception {

        String code = new String(Files.readAllBytes(Paths.get("test-code/Java8ComplexCode01.java")));

        Java8ModelParser parser = new Java8ModelParser();
        Java8Model model = parser.parse(code);

        Java8ModelUnparser unparser = new Java8ModelUnparser();
        unparser.setFormatter(new MyFormatter());
        String out = unparser.unparse(model);

    }

    @org.junit.Test
    public void testParseUnparseComplexCodeBenchmark() throws Exception {

        String code = new String(Files.readAllBytes(Paths.get("test-code/Java8ComplexCode01.java")));

        Java8ModelParser parser = new Java8ModelParser();
        Java8Model model = parser.parse(code);


        Java8ModelUnparser unparser = new Java8ModelUnparser();
        unparser.setFormatter(new MyFormatter());

        long timeStampBegin = System.nanoTime();

        for (int i = 0; i < 1000; i++) {


            String out = unparser.unparse(model);

        }

        long timeStampEnd = System.nanoTime();

        System.out.println("Duration: " + (timeStampEnd - timeStampBegin) * 1e-9);

    }

    @org.junit.Test
    public void testParsePerRuleFieldDecl() {

        Java8ModelParser parser = new Java8ModelParser();
        Java8ModelUnparser unparser = new Java8ModelUnparser();

        unparser.setFormatter(new MyFormatter());


        FieldDeclaration fieldDecl = FieldDeclaration.newInstance();
        fieldDecl.setFieldType(TypeType.newBuilder().withSimpleType(
                DoubleType.newBuilder().build()).build());


        VariableDeclarator varDecl = VariableDeclaratorWithExprInit.newBuilder().
                withVarName("myVar1").withInitializer(
                LiteralExpr.newBuilder().withLit(
                        FloatLiteral.newBuilder().withFloatValue(2.5).build()
                ).build()
        ).build();

        fieldDecl.getVarDecls().add(varDecl);

        VariableDeclarator varDecl1 = VariableDeclaratorWithExprInit.newBuilder().
                withVarName("myVar2").withInitializer(
                LiteralExpr.newBuilder().withLit(
                        FloatLiteral.newBuilder().withFloatValue(2.6).build()
                ).build()
        ).build();

        fieldDecl.getVarDecls().add(varDecl1);

        FieldDeclaration fieldDeclFromParser = parser.parseFieldDeclaration("double myVar1 = 2.5, myVar2 = 2.6;");

        Assert.assertEquals(fieldDecl, fieldDeclFromParser);
    }

    @org.junit.Test
    public void testParsePerRuleClassDecl() {

        Java8ModelParser parser = new Java8ModelParser();
        Java8ModelUnparser unparser = new Java8ModelUnparser();

        unparser.setFormatter(new MyFormatter());

        ClassDeclaration cDecl = ClassDeclaration.newInstance();
        ClassBody clsBody = ClassBody.newInstance();

        ClassBodyDeclaration clsBodyDecl = ClassBodyDeclaration.newInstance();
        clsBodyDecl.getModifiers().add(Modifier.newBuilder().withTypeModifier(
                ClassOrInterfaceModifier.newBuilder().withPrivateModifier("private").build()).build());

        MemberDeclaration mDecl = MemberDeclaration.newInstance();

        FieldDeclaration fieldDecl = FieldDeclaration.newInstance();
        fieldDecl.setFieldType(TypeType.newBuilder().withSimpleType(
                DoubleType.newBuilder().build()).build());

        VariableDeclarator varDecl = VariableDeclaratorWithExprInit.newBuilder().
                withVarName("myVar1").withInitializer(
                LiteralExpr.newBuilder().withLit(
                        FloatLiteral.newBuilder().withFloatValue(2.5).build()
                ).build()
        ).build();

        fieldDecl.getVarDecls().add(varDecl);

        VariableDeclarator varDecl1 = VariableDeclaratorWithExprInit.newBuilder().
                withVarName("myVar2").withInitializer(
                LiteralExpr.newBuilder().withLit(
                        FloatLiteral.newBuilder().withFloatValue(2.6).build()
                ).build()
        ).build();

        fieldDecl.getVarDecls().add(varDecl1);

        mDecl.setFieldDecl(fieldDecl);

        clsBodyDecl.setDeclaration(mDecl);
        clsBody.getDeclarations().add(clsBodyDecl);

        cDecl.setBody(clsBody);
        cDecl.setClassName("MyClass");

        ClassDeclaration cDeclFromParser = parser.parseClassDeclaration(
                "class MyClass { private double myVar1 = 2.5, myVar2 = 2.6;}");

        Assert.assertEquals(cDecl, cDeclFromParser);
    }

    public void testCustomClassDefinition() {

        Java8ModelParser parser = new Java8ModelParser();
        Java8ModelUnparser unparser = new Java8ModelUnparser();

        CompilationUnit unit = CompilationUnit.newInstance();

        PackageDeclaration pDecl = PackageDeclaration.newInstance();
        pDecl.defPackageNameFromString("eu.mihosoft.v123");

        unit.setPackageDecl(pDecl);

        TypeDeclaration tDecl = TypeDeclaration.newInstance();
        ClassDeclaration cDecl = ClassDeclaration.newInstance();
        ClassBody clsBody = ClassBody.newInstance();

        ClassBodyDeclaration clsBodyDecl = ClassBodyDeclaration.newInstance();
        clsBodyDecl.getModifiers().add(Modifier.newBuilder().withTypeModifier(
                ClassOrInterfaceModifier.newBuilder().withPrivateModifier("private").build()).build());

        MemberDeclaration mDecl = MemberDeclaration.newInstance();

        FieldDeclaration fieldDecl = FieldDeclaration.newInstance();
        fieldDecl.setFieldType(TypeType.newBuilder().withSimpleType(
                DoubleType.newBuilder().build()).build());

        VariableDeclarator varDecl = VariableDeclaratorWithExprInit.newBuilder().
                withVarName("myVar1").withInitializer(
                LiteralExpr.newBuilder().withLit(
                        FloatLiteral.newBuilder().withFloatValue(2.5).build()
                ).build()
        ).build();

        fieldDecl.getVarDecls().add(varDecl);

        VariableDeclarator varDecl1 = VariableDeclaratorWithExprInit.newBuilder().
                withVarName("myVar2").withInitializer(
                LiteralExpr.newBuilder().withLit(
                        FloatLiteral.newBuilder().withFloatValue(2.6).build()
                ).build()
        ).build();

        fieldDecl.getVarDecls().add(varDecl1);

        mDecl.setFieldDecl(fieldDecl);

        clsBodyDecl.setDeclaration(mDecl);
        clsBody.getDeclarations().add(clsBodyDecl);

        cDecl.setBody(clsBody);
        cDecl.setClassName("MyClass");
        tDecl.setClassDecl(cDecl);

        unit.getTypeDeclarations().add(tDecl);

        System.out.println(unparser.unparse(unit));

        FloatLiteral f1 = FloatLiteral.newBuilder().withFloatValue(2.5).build();
        FloatLiteral f2 = FloatLiteral.newBuilder().withHexValue(2.5).build();

        System.out.println("F1: " + unparser.unparse(f1));
        System.out.println("F2: " + unparser.unparse(f2));
    }


    @org.junit.Test
    public void testChangeVoidMethod() throws Exception {

        String code = "" +
                "class MyClass {\n" +
                "  public int m1() {return 0;}\n" +
                "  public void m2(int a, int b) {\n" +
                "    // do something\n" +
                "  }\n" +
                "  public java.util.List<String> m3(String a, String b) {\n" +
                "    class InnerClass {\n" +
                "      private void innerMethod() {}\n" +
                "    }\n" +
                "    return null;\n" +
                "  }\n" +
                "}\n";

        Java8ModelParser parser = new Java8ModelParser();
        Java8Model model = parser.parse(code);

        model.vmf().content().stream(MethodDeclaration.class).
                filter(mD -> mD.getType().getVoidType()).
                forEach(m ->
                        m.getParams().getParams().add(
                                parser.parseFormalParameter("int insP")
                        )
                );

        Java8ModelUnparser unparser = new Java8ModelUnparser();
        unparser.setFormatter(new MyFormatter());

        String transformed = unparser.unparse(model);

        System.out.println("Original:    " + code);
        System.out.println("Transformed: " + transformed);

        Java8Model transformedModel = parser.parse(transformed);

        Assert.assertEquals(model, transformedModel);

    }
}

class MyFormatter extends BaseFormatter {

    @Override
    public void pre(Java8ModelUnparser unparser, Formatter.RuleInfo ruleInfo, PrintWriter w) {

        // TODO Allow rule consume() to provide manually
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
                || (Objects.equals(prevRuleText, ";"));

        if (insideFor(prevRuleText)) {
            lineBreak = false;
        }

        if ("else".equals(ruleText) && "}".equals(prevRuleText)) {
            lineBreak = false;
            w.append(" ");
        }

        if (lineBreak) {
            w.append('\n').append(getIndent());
//        } else if (IdentifierExprUnparser.
//                matchIdentifierExprAlt0(prevRuleText + ruleText)
//                && !ruleText.equals(";") && !ruleText.equals(")") && !ruleText.equals("(")
//                && !ruleText.equals(",")
//                || prevRuleText.equals(",")
//                || prevRuleText.equals("]")
//                || prevRuleText.equals("(")
//                || ruleText.equals(")")
//                || ruleText.equals("{")
//                || prevRuleText.equals("=")
//                ) {
//            w.append(" ");
        } else if (
                !(getPrevRuleInfo().getParentObject() instanceof QualifiedName)
                        && !(ruleInfo.getParentObject() instanceof PackageDeclaration)
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
            if (stringState == null) {
                stringState = "";
            }
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

}

