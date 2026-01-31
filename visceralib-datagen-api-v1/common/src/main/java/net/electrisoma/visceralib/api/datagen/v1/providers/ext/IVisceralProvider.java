package net.electrisoma.visceralib.api.datagen.v1.providers.ext;

public interface IVisceralProvider {

    default String viscera$getName(String original) {
        return original;
    }
}
