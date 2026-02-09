package net.electrisoma.visceralib.api.datagen.v1.providers.client.model;

import net.electrisoma.visceralib.api.datagen.v1.providers.client.VisceralModelProvider;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModelBuilder {

    private final ModelTemplate template;
    private final VisceralModelProvider.VisualModelBuilder parent;
    private final TextureMapping mapping = new TextureMapping();

    public ModelBuilder(ModelTemplate template, VisceralModelProvider.VisualModelBuilder parent) {
        this.template = template;
        this.parent = parent;
    }

    public ModelBuilder texture(TextureSlot slot, ResourceLocation texture) {
        mapping.put(slot, texture);
        return this;
    }

    public void save(ResourceLocation id) {
        template.create(id, mapping, parent::addModel);
    }

    public void save(Block block) {
        save(ModelLocationUtils.getModelLocation(block));
    }

    public void save(Item item) {
        save(ModelLocationUtils.getModelLocation(item));
    }
}