package de.zolitas.createdyeablechains;

import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinPlugin implements IMixinConfigPlugin {
  @Override
  public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
    // should not load the mixin if Colored Chain Conveyors is not loaded
    return !mixinClassName.equals("de.zolitas.createdyeablechains.mixin.compat.CCCCChainConveyorRendererMixin")
        || (FMLLoader.getLoadingModList().getModFileById("create_colored_chain_conveyor") != null);
  }

  // methods below are not used

  @Override
  public void onLoad(String mixinPackage) {

  }

  @Override
  public String getRefMapperConfig() {
    return null;
  }



  @Override
  public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

  }

  @Override
  public List<String> getMixins() {
    return null;
  }

  @Override
  public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

  }

  @Override
  public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

  }
}
