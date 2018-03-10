package eu.mihosoft.vmftext.tests.miniclang;

import eu.mihosoft.vmf.runtime.core.DelegatedBehavior;

import java.util.List;
import java.util.Optional;

public class ControlFlowDelegate implements DelegatedBehavior<ControlFlowScope> {
    private ControlFlowScope scope;

    @Override
    public void setCaller(ControlFlowScope caller) {
        this.scope = caller;
    }

    /**
     * Resolves a variable by name.
     * @param name variable name
     * @return the variable if present; empty optional otherwise
     */
    public Optional<DeclStatement> resolveVariable(String name) {

        // list of scopes to visit (order is current to root)
        List<ControlFlowScope> scopes = scope.parentScopes();
        scopes.add(0, scope);

        // for each scope check whether it has a declaration that matches the
        // requested variable (by name) and return it if is present
        for (ControlFlowScope cF : scopes) {
            Optional<DeclStatement> variableDecl = cF.vmf().content().stream(DeclStatement.class).
                    filter(declStatement ->
                            name.equals(declStatement.getVarName())
                    ).findFirst();

            if (variableDecl.isPresent()) {
                return variableDecl;
            }
        }

        // if the root scope is not a function declaration we return early since there's
        // nothing to check
        ControlFlowScope rootScope = scopes.get(scopes.size() - 1);
        if (!(rootScope instanceof FunctionDecl)) return Optional.empty();

        // check function parameters
        FunctionDecl fDecl = (FunctionDecl) rootScope;
        Optional<? extends DeclStatement> paramDecl =
                fDecl.getParams().stream().filter(parameter -> name.equals(parameter.getVarName()))
                        .findFirst();

        // return if parameter matches (by name)
        if (paramDecl.isPresent()) {
            return (Optional<DeclStatement>) paramDecl;
        }

        // nothing found
        return Optional.empty();
    }

}
