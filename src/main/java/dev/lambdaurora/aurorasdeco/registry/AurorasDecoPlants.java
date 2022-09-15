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

package dev.lambdaurora.aurorasdeco.registry;

import dev.lambdaurora.aurorasdeco.block.sapling.JacarandaSaplingGenerator;
import dev.lambdaurora.aurorasdeco.item.DerivedBlockItem;
import dev.lambdaurora.aurorasdeco.item.DuckweedItem;
import dev.lambdaurora.aurorasdeco.util.Registrar;
import net.minecraft.block.*;
import net.minecraft.item.ItemGroup;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.List;

import static dev.lambdaurora.aurorasdeco.registry.AurorasDecoRegistry.registerBlock;
import static dev.lambdaurora.aurorasdeco.registry.AurorasDecoRegistry.registerWithItem;

/**
 * Contains the different plants definitions added in Aurora's Decorations.
 *
 * @author LambdAurora
 * @version 1.0.0
 * @since 1.0.0
 */
public final class AurorasDecoPlants {
	private AurorasDecoPlants() {
		throw new UnsupportedOperationException("AurorasDecoPlants only contains static definitions.");
	}

	static void init() {}


	/* Saplings */

	public static final SaplingBlock JACARANDA_SAPLING = registerWithItem("jacaranda_sapling",
			new SaplingBlock(new JacarandaSaplingGenerator(), QuiltBlockSettings.copyOf(Blocks.OAK_SAPLING)),
			new QuiltItemSettings().group(ItemGroup.DECORATIONS),
			DerivedBlockItem::sapling
	);

	public static final FlowerPotBlock POTTED_JACARANDA_SAPLING = registerBlock("potted/jacaranda_sapling",
			new FlowerPotBlock(JACARANDA_SAPLING, QuiltBlockSettings.of(Material.DECORATION).nonOpaque().breakInstantly()));

	/* Leaves */

	public static final LeavesBlock JACARANDA_LEAVES = registerWithItem("jacaranda_leaves",
			new LeavesBlock(QuiltBlockSettings.copyOf(Blocks.BIRCH_LEAVES)),
			new QuiltItemSettings().group(ItemGroup.DECORATIONS),
			DerivedBlockItem::leaves);
	public static final LeavesBlock BUDDING_JACARANDA_LEAVES = registerWithItem("budding_jacaranda_leaves",
			new LeavesBlock(QuiltBlockSettings.copyOf(Blocks.FLOWERING_AZALEA_LEAVES)),
			new QuiltItemSettings().group(ItemGroup.DECORATIONS),
			DerivedBlockItem::leaves);
	public static final LeavesBlock FLOWERING_JACARANDA_LEAVES = registerWithItem("flowering_jacaranda_leaves",
			new LeavesBlock(QuiltBlockSettings.copyOf(BUDDING_JACARANDA_LEAVES)),
			new QuiltItemSettings().group(ItemGroup.DECORATIONS),
			DerivedBlockItem::leaves);


}
