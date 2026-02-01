package com.everest.hibiscus.mixin.api.internal;

import com.everest.hibiscus.api.modules.compat.annotation.RequiresMod;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class ConditionalMixinPlugin implements IMixinConfigPlugin {

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        try {
            Class<?> mixin = Class.forName(mixinClassName);

            if (mixin.isAnnotationPresent(RequiresMod.class)) {
                RequiresMod r = mixin.getAnnotation(RequiresMod.class);

                String[] mods = r.mods().length > 0
                        ? r.mods() : new String[]{r.mod()};

                for (String mod : mods)
                    if (FabricLoader.getInstance().isModLoaded(mod))
                        return true;

                return false;
            }

        } catch (Throwable ignored) {}

        return true;
    }

    @Override public void onLoad(String pkg) {}
    @Override public String getRefMapperConfig() { return null; }
    @Override public void acceptTargets(Set<String> a, Set<String> b) {}
    @Override public List<String> getMixins() { return null; }
    @Override public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {}
    @Override public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {}
}
