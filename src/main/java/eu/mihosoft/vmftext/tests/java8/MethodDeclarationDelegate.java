package eu.mihosoft.vmftext.tests.java8;

import eu.mihosoft.vmf.runtime.core.DelegatedBehavior;

public class MethodDeclarationDelegate implements DelegatedBehavior<MethodDeclaration> {

    private MethodDeclaration caller;

    @Override
    public void setCaller(MethodDeclaration caller) {
        this.caller = caller;
    }

    public boolean returnsVoid() {
        return caller.getType().getVoidType();
    }

}
