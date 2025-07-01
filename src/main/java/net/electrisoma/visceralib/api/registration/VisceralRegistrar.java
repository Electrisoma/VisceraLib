package net.electrisoma.visceralib.api.registration;

public class VisceralRegistrar extends AbstractVisceralRegistrar<VisceralRegistrar> {
    protected VisceralRegistrar(String modId) {
        super(modId);
    }

    public static VisceralRegistrar create(String modId) {
        return new VisceralRegistrar(modId);
    }
}
