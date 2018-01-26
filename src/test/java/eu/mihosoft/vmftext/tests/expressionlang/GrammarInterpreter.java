package eu.mihosoft.vmftext.tests.expressionlang;


class GrammarInterpreter {

    public Double execute(ExpressionLangModel model) {

        // run the interpreter
        return toValue(model.getRoot().getExpression()).getValue();
    }

    NumberExpr toValue(Expr e) {
        if(e instanceof OpExpr) {
            OpExpr opExpr = (OpExpr) e;
            return toValue(opExpr);
        } else if(e instanceof NumberExpr) {
            return (NumberExpr) e;
        } else if(e instanceof ParanExpr) {
            return toValue(((ParanExpr)e).getExpression());
        }

        String msg = "Error in [line " + e.getCodeRange().getStart().getLine()+", column "+
                    e.getCodeRange().getStart().getCharPosInLine()
                    + "]: expression is not a value" + ".";
        System.err.println(msg);
        throw new RuntimeException(msg);
    }

    NumberExpr toValue(OpExpr e) {

        NumberExpr left = toValue(e.getLeft());
        NumberExpr right = toValue(e.getRight());

        if("+".equals(e.getOperator())) {
            return NumberExpr.newBuilder().withValue(
                    left.getValue() + right.getValue()
            ).build();
        } else if("-".equals(e.getOperator())) {
            return NumberExpr.newBuilder().withValue(
                    left.getValue() - right.getValue()
            ).build();
        } else if("*".equals(e.getOperator())) {
            return NumberExpr.newBuilder().withValue(
                    left.getValue() * right.getValue()
            ).build();
        } else if("/".equals(e.getOperator())) {
            return NumberExpr.newBuilder().withValue(
                    left.getValue() / right.getValue()
            ).build();
        }

        return null;
    }
}

