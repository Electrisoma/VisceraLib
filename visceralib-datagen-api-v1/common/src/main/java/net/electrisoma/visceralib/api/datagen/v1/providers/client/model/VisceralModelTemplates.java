package net.electrisoma.visceralib.api.datagen.v1.providers.client.model;

import net.electrisoma.visceralib.api.core.resources.RLUtils;
import net.minecraft.data.models.model.ModelTemplate;

import java.util.Optional;

public class VisceralModelTemplates {

    public static final ModelTemplate BUILTIN_ENTITY =
            new ModelTemplate(Optional.of(RLUtils.mc("builtin/entity")), Optional.empty());

    public static final ModelTemplate BARRIER =
            new ModelTemplate(Optional.of(RLUtils.mc("item/barrier")), Optional.empty());
}
