package eu.mihosoft.vmftext.tests.miniclang;

import eu.mihosoft.vcollections.VList;
import eu.mihosoft.vmf.runtime.core.DelegatedBehavior;
import eu.mihosoft.vmf.runtime.core.VObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ControlFlowChildNodeDelegate implements DelegatedBehavior<ControlFlowChildNode> {
    private ControlFlowChildNode scope;

    @Override
    public void setCaller(ControlFlowChildNode caller) {
        this.scope = caller;
    }

    /**
     * Returns a list of all ancestors of this element.
     *
     * @return a list of all ancestors of this element
     */
    public VList<ControlFlowScope> parentScopes() {
        List<ControlFlowScope> ancestors = new ArrayList<>();

        ControlFlowScope parent = getParentScope(scope).orElse(null);

        while (parent != null) {
            ancestors.add(parent);
            parent = getParentScope(parent).orElse(null);
        }

        Collections.reverse(ancestors);

        return VList.newInstance(ancestors);
    }

    /**
     * Returns parent scope of the current code element.
     * @param cF code element
     * @return parent scope if present; empty optional otherwise
     */
    private static Optional<ControlFlowScope> getParentScope(VObject cF) {
        VObject obj = cF;
        for (VObject parent : obj.vmf().content().referencedBy()) {
            if (parent instanceof ControlFlowScope) {
                return Optional.of((ControlFlowScope) parent);
            }
            Optional<ControlFlowScope> pScope = getParentScope(parent);
            if (pScope.isPresent()) {
                return pScope;
            }
        }

        return Optional.empty();
    }
    
    
}
