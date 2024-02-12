package com.mr_toad.h_plus.api;

import com.mr_toad.h_plus.core.HPlus;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class EntityDepthStatsChangeEvent extends Event {

    public final Mob entity;

    public EntityDepthStatsChangeEvent(Mob entity) {
        this.entity = entity;
    }

    public void removeModifier(UUID uuid, Attribute attribute) {
        AttributeInstance instance = this.getInstanceWithCheck(attribute);
        instance.removeModifier(uuid);
    }

    public void addModifier(@Nullable UUID uuid, String name, Attribute attribute, ModifierType modifierType, double mod, AttributeModifier.Operation operation) {
        boolean isPermanent = modifierType == ModifierType.PERMANENT;
        AttributeInstance instance = this.getInstanceWithCheck(attribute);
        AttributeModifier modifier = uuid == null ? new AttributeModifier(name, mod, operation) : new AttributeModifier(uuid, name, mod, operation);
        this.resolveModifier(isPermanent, instance, modifier);
    }

    public void resolveModifier(boolean permanent, AttributeInstance instance, AttributeModifier modifier) {
        if (permanent) {
            instance.addPermanentModifier(modifier);
        } else {
            instance.addTransientModifier(modifier);
        }
    }

    public AttributeInstance getInstanceWithCheck(Attribute attribute) {
        AttributeInstance instance = this.getEntity().getAttribute(attribute);
        if (instance == null) {
            HPlus.LOGGER.error("{} doesn't have Attribute: {}", this.getEntity().getClass().getName(), attribute.getDescriptionId());
            return null;
        }
        return instance;
    }

    public Mob getEntity() {
        return this.entity;
    }

    public enum ModifierType {
        PERMANENT,
        TRANSIENT
    }

}
