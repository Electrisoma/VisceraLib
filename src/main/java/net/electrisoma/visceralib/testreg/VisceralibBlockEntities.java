package net.electrisoma.visceralib.testreg;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.api.registration.VisceralRegistrar;
import net.electrisoma.visceralib.api.registration.entry.BlockEntityEntry;
import net.electrisoma.visceralib.testreg.block_entities.TestBlockEntity;

public class VisceralibBlockEntities {
    public static void init() {
        VisceraLib.LOGGER.info("Registering Block Entities for " + VisceraLib.NAME);
    }

    private static final VisceralRegistrar REGISTRAR = VisceraLib.registrar();

    public static final BlockEntityEntry<TestBlockEntity> MY_BLOCK_ENTITY = REGISTRAR
            .blockEntity("block_entity_thing", TestBlockEntity::new)
            .validBlocks(VisceralibBlocks.MACHINE_BLOCK::get)
            .register();
}
