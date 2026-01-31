package net.electrisoma.visceralib.platform.core.services;

import net.electrisoma.visceralib.api.core.services.ServiceHelper;

public interface ITickHelper {

    ITickHelper INSTANCE = ServiceHelper.load(ITickHelper.class);

    void registerTickListener(Runnable tickTask);
}
