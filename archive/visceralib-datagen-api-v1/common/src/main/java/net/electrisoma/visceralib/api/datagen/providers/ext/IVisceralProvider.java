package net.electrisoma.visceralib.api.datagen.providers.ext;

public interface IVisceralProvider {

    default String viscera$getName(String original) {
        return original;
    }
}
