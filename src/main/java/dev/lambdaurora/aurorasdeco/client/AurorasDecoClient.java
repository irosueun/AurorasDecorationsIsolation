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

package dev.lambdaurora.aurorasdeco.client;

import com.terraformersmc.terraform.boat.api.client.TerraformBoatClientHelper;
import com.terraformersmc.terraform.sign.SpriteIdentifierRegistry;
import dev.lambdaurora.aurorasdeco.AurorasDeco;


import dev.lambdaurora.aurorasdeco.registry.AurorasDecoPlants;
import dev.lambdaurora.aurorasdeco.resource.AurorasDecoPack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.resource.ResourceType;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientLifecycleEvents;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientWorldTickEvents;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

import static dev.lambdaurora.aurorasdeco.registry.AurorasDecoRegistry.*;

/**
 * Represents the Aurora's Decorations client mod.
 *
 * @author LambdAurora
 * @version 1.0.0
 * @since 1.0.0
 */
@Environment(EnvType.CLIENT)
public class AurorasDecoClient implements ClientModInitializer {
	public static final AurorasDecoPack RESOURCE_PACK = new AurorasDecoPack(ResourceType.CLIENT_RESOURCES);


	@Override
	public void onInitializeClient(ModContainer mod) {
		this.initEntityRenderers();
		this.initBlockRenderLayers();


		/* Signs */
		SpriteIdentifierRegistry.INSTANCE.addIdentifier(new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, JACARANDA_SIGN_BLOCK.getTexture()));


		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) ->
						world != null && pos != null
								? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor());




		ModelLoadingRegistry.INSTANCE.registerModelProvider(RenderRule::reload);


	}



	private void initEntityRenderers() {
		TerraformBoatClientHelper.registerModelLayers(AurorasDeco.id("jacaranda"));
	}

	private void initBlockRenderLayers() {
		BlockRenderLayerMap.put(RenderLayer.getCutout(),
				JACARANDA_DOOR,
				JACARANDA_TRAPDOOR,
				AurorasDecoPlants.JACARANDA_SAPLING,
				AurorasDecoPlants.POTTED_JACARANDA_SAPLING
		);
	}

}
