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

package dev.lambdaurora.aurorasdeco.mixin.client;

import dev.lambdaurora.aurorasdeco.AurorasDeco;


import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.ModelVariantMap;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Injects the big flower pot dynamic models.
 * <p>
 * Had to use priorities to win over LBG, this sucks.
 * An API probably should be made.
 *
 * @author LambdAurora
 * @version 1.0.0
 * @since 1.0.0
 */
@Mixin(value = ModelLoader.class, priority = 900)
public abstract class ModelLoaderMixin {
	@Shadow
	@Final
	private ResourceManager resourceManager;
	@Shadow
	@Final
	private ModelVariantMap.DeserializationContext variantMapDeserializationContext;

	@Unique
	private boolean aurorasdeco$firstRun = true;
	@Unique
	private final List<Identifier> aurorasdeco$visitedModels = new ArrayList<>();

	@Shadow
	protected abstract void putModel(Identifier id, UnbakedModel unbakedModel);

	@Shadow
	@Final
	private Map<Identifier, UnbakedModel> modelsToBake;

	@Inject(method = "putModel", at = @At("HEAD"), cancellable = true)
	private void onPutModel(Identifier id, UnbakedModel unbakedModel, CallbackInfo ci) {
		if (id instanceof ModelIdentifier modelId
				&& !this.aurorasdeco$visitedModels.contains(id)) {
			if (!modelId.getVariant().equals("inventory")) {
				if (this.aurorasdeco$firstRun) {
					this.aurorasdeco$firstRun = false;


				}

			}
		}
	}
}
