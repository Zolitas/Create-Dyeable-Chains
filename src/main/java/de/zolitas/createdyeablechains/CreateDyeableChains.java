package de.zolitas.createdyeablechains;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(CreateDyeableChains.MOD_ID)
public class CreateDyeableChains {
  public static final String MOD_ID = "create_dyeable_chains";
  private static final Logger LOGGER = LogUtils.getLogger();

  public CreateDyeableChains(IEventBus modEventBus, ModContainer modContainer) {
  }
}
