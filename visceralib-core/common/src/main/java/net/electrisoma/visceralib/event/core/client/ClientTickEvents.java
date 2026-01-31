package net.electrisoma.visceralib.event.core.client;

import com.google.auto.service.AutoService;
import net.electrisoma.visceralib.event.core.common.IVisceralListener;
import net.electrisoma.visceralib.platform.core.services.ITickHelper;

import java.util.ArrayList;
import java.util.List;

@AutoService(IVisceralListener.class)
public class ClientTickEvents implements IVisceralListener {

    private static final List<Runnable> TICK_TASKS = new ArrayList<>();

    @Override
    public void register() {
        ITickHelper.INSTANCE.registerTickListener(() -> {
            for (Runnable task : TICK_TASKS) {
                task.run();
            }
        });
    }

    public static void addTickTask(Runnable task) {
        TICK_TASKS.add(task);
    }
}