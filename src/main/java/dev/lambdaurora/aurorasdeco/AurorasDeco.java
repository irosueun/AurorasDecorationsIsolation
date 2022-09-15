/*
 * Copyright (c) 2021-2022 LambdAurora <email@lambdaurora.dev>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.lambdaurora.aurorasdeco;



import dev.lambdaurora.aurorasdeco.registry.AurorasDecoRegistry;
import dev.lambdaurora.aurorasdeco.resource.AurorasDecoPack;
import dev.lambdaurora.aurorasdeco.resource.AurorasDecoPackCreator;
import dev.lambdaurora.aurorasdeco.util.AuroraUtil;
import dev.lambdaurora.aurorasdeco.world.gen.DynamicWorldGen;
import net.minecraft.block.Blocks;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.quiltmc.qsl.registry.api.event.RegistryMonitor;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;
import org.quiltmc.qsl.resource.loader.api.ResourcePackActivationType;

/**
 * Represents the Aurora's Decorations mod.
 *
 * @author LambdAurora
 * @version 1.0.0
 * @since 1.0.0
 */
public class AurorasDeco implements ModInitializer {
	public static final String NAMESPACE = "aurorasdeco";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final AurorasDecoPack RESOURCE_PACK = new AurorasDecoPack(ResourceType.SERVER_DATA);

	@Override
	public void onInitialize(ModContainer mod) {
		AurorasDecoRegistry.init();

		RegistryMonitor.create(Registry.ITEM).forAll(context -> {});


		DynamicWorldGen.init();

		ResourceLoader.registerBuiltinResourcePack(id("swamp_worldgen"), ResourcePackActivationType.NORMAL,
				Text.literal("Aurora's Deco").formatted(Formatting.GOLD)
						.append(Text.literal(" - ").formatted(Formatting.GRAY))
						.append(Text.literal("Swamp Tweaks").formatted(Formatting.DARK_GREEN))
		);
		ResourceLoader.get(ResourceType.SERVER_DATA).registerResourcePackProfileProvider(new AurorasDecoPackCreator());
	}

	public static boolean isDevMode() {
		return QuiltLoader.isDevelopmentEnvironment();
	}

	public static void log(String message) {
		if (isDevMode())
			LOGGER.info("\033[32m" + message + "\033[0m");
		else
			LOGGER.info("[AurorasDeco] " + message);
	}

	public static void warn(String message, Object... params) {
		if (isDevMode())
			LOGGER.warn("\033[33m" + message + "\033[0m", params);
		else
			LOGGER.warn("[AurorasDeco] " + message, params);
	}

	public static void error(String message, Object... params) {
		if (isDevMode())
			LOGGER.error("\033[31;1m" + message + "\033[0m", params);
		else
			LOGGER.error("[AurorasDeco] " + message, params);
	}

	public static void debug(String message, Object... params) {
		if (isDevMode()) {
			LOGGER.info("\033[38;5;214m[Debug] \033[32;1m" + message + "\033[0m", params);
		}
	}

	public static void debugWarn(String message, Object... params) {
		if (isDevMode()) {
			LOGGER.info("\033[38;5;214m[Debug] \033[31;1m" + message + "\033[0m", params);
		}
	}

	public static Identifier id(String path) {
		return new Identifier(NAMESPACE, path);
	}
}
