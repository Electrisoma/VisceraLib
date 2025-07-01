package net.electrisoma.visceralib.api.registration.helpers;

import net.electrisoma.visceralib.data.providers.VisceralLangProvider;

@FunctionalInterface
public interface LangContributor {
    void contributeToLang(VisceralLangProvider provider);
}
