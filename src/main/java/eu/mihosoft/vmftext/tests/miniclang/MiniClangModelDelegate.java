package eu.mihosoft.vmftext.tests.miniclang;

import eu.mihosoft.vmf.runtime.core.DelegatedBehavior;

public class MiniClangModelDelegate implements DelegatedBehavior<MiniClangModel> {
    private MiniClangModel model;

    @Override
    public void setCaller(MiniClangModel caller) {
        this.model = caller;
    }

}
