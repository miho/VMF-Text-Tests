package eu.mihosoft.vmftext.tests.miniclang;

import eu.mihosoft.vmf.runtime.core.DelegatedBehavior;

import java.util.Optional;

public class ProgramDelegate implements DelegatedBehavior<Program> {
    private Program program;

    @Override
    public void setCaller(Program caller) {
        this.program = caller;
    }

    /**
     * Resolve function declaration.
     * @param fcn function name
     * @param size number of arguments
     * @return function declaration if present; empty optional otherwise
     */
    public Optional<FunctionDecl> resolveFunction(String fcn, int size) {
        return program.vmf().content().stream(FunctionDecl.class).
                filter(fD->fcn.equals(fD.getFunctionName())).
                filter(fD -> fD.getParams().size()==size).findFirst();
    }

    
}
