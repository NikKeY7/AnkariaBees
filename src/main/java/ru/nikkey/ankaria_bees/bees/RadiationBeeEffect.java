package ru.nikkey.ankaria_bees.bees;

import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.genetics.IBeeEffect;
import forestry.api.genetics.IEffectData;
import forestry.api.genetics.IGenome;
import forestry.core.genetics.EffectData;
import mekanism.api.radiation.IRadiationManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

/** Irradiates entities near a working queen without creating persistent radiation sources. */
public final class RadiationBeeEffect implements IBeeEffect {
    @Override public boolean isDominant() { return false; }

    @Override public IEffectData validateStorage(IEffectData data) {
        return data instanceof EffectData ? data : new EffectData(1, 0);
    }

    @Override public IEffectData doEffect(IGenome genome, IEffectData data, IBeeHousing housing) {
        Level level = housing.getWorldObj();
        if (level.isClientSide || housing.getErrorLogic().hasErrors() || !IRadiationManager.INSTANCE.isRadiationEnabled()) return data;
        int ticks = data.getInteger(0) + 1;
        data.setInteger(0, ticks >= 100 ? 0 : ticks);
        if (ticks >= 100) {
            BlockPos pos = housing.getCoordinates();
            AABB area = new AABB(pos.offset(-2, -2, -2), pos.offset(3, 3, 3));
            for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, area)) {
                IRadiationManager.INSTANCE.radiate(entity, 0.01);
            }
        }
        return data;
    }
}
