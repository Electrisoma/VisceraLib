package net.electrisoma.visceralib.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
public @interface Env {
    EnvType value();

    enum EnvType {
        CLIENT, SERVER;
    }
}