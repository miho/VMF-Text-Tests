package eu.mihosoft.vmftext.tests.java8;
import eu.mihosoft.vcollections.VList;
import eu.mihosoft.vmf.runtime.core.DelegatedBehavior;


import java.util.Arrays;
import java.util.stream.Collectors;

public class PackageDeclarationDelegate implements DelegatedBehavior<PackageDeclaration> {
    private PackageDeclaration caller;

    @Override
    public void setCaller(PackageDeclaration caller) {
        this.caller = caller;
    }

    public String packageNameAsString() {
        return caller.getPackageName().getElement().stream().collect(Collectors.joining("."));
    }

    public void defPackageNameFromString(String packageName) {
        QualifiedName name = QualifiedName.newBuilder().
                withElement(VList.newInstance(Arrays.asList(packageName.split("\\.")))).build();

        caller.setPackageName(name);
    }
}
