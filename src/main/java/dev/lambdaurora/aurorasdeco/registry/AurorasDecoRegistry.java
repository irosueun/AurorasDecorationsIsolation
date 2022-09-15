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

import com.terraformersmc.terraform.boat.api.TerraformBoatType;
import com.terraformersmc.terraform.boat.api.TerraformBoatTypeRegistry;
import com.terraformersmc.terraform.boat.api.item.TerraformBoatItemHelper;
import com.terraformersmc.terraform.sign.block.TerraformSignBlock;
import com.terraformersmc.terraform.sign.block.TerraformWallSignBlock;

import dev.lambdaurora.aurorasdeco.item.DerivedBlockItem;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SignItem;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.stat.StatFormatter;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import static dev.lambdaurora.aurorasdeco.AurorasDeco.id;
import static net.minecraft.block.Blocks.OAK_PLANKS;
import static net.minecraft.stat.Stats.CUSTOM;

/**
 * Represents the Aurora's Decorations registry.
 *
 * @author LambdAurora
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public final class AurorasDecoRegistry {
	private AurorasDecoRegistry() {
		throw new UnsupportedOperationException("Someone tried to instantiate a static-only class. How?");
	}


	//region Jacaranda
	public static final PillarBlock JACARANDA_LOG_BLOCK = registerWithItem("jacaranda_log",
			createLogBlock(MapColor.PALE_PURPLE, MapColor.TERRACOTTA_PURPLE),
			new QuiltItemSettings().group(ItemGroup.BUILDING_BLOCKS),
			DerivedBlockItem::log);
	public static final PillarBlock STRIPPED_JACARANDA_LOG_BLOCK = registerWithItem("stripped_jacaranda_log",
			new PillarBlock(QuiltBlockSettings.copyOf(Blocks.STRIPPED_OAK_LOG).mapColor(MapColor.TERRACOTTA_PURPLE)),
			new QuiltItemSettings().group(ItemGroup.BUILDING_BLOCKS),
			DerivedBlockItem::strippedLog);
	public static final PillarBlock STRIPPED_JACARANDA_WOOD_BLOCK = registerWithItem("stripped_jacaranda_wood",
			new PillarBlock(QuiltBlockSettings.copyOf(STRIPPED_JACARANDA_LOG_BLOCK)),
			new QuiltItemSettings().group(ItemGroup.BUILDING_BLOCKS),
			DerivedBlockItem::strippedWood);
	public static final PillarBlock JACARANDA_WOOD_BLOCK = registerWithItem("jacaranda_wood",
			createLogBlock(MapColor.TERRACOTTA_PURPLE, MapColor.TERRACOTTA_PURPLE),
			new QuiltItemSettings().group(ItemGroup.BUILDING_BLOCKS),
			DerivedBlockItem::wood);
	public static final Block JACARANDA_PLANKS_BLOCK = registerWithItem("jacaranda_planks",
			new Block(QuiltBlockSettings.copyOf(OAK_PLANKS).mapColor(MapColor.PALE_PURPLE)),
			new QuiltItemSettings().group(ItemGroup.BUILDING_BLOCKS),
			DerivedBlockItem::planks);
	public static final Block JACARANDA_SLAB_BLOCK = registerWithItem("jacaranda_slab",
			new SlabBlock(QuiltBlockSettings.copyOf(JACARANDA_PLANKS_BLOCK)), new QuiltItemSettings().group(ItemGroup.BUILDING_BLOCKS),
			DerivedBlockItem::woodenSlab
	);
	public static final Block JACARANDA_STAIRS_BLOCK = registerWithItem("jacaranda_stairs",
			new StairsBlock(JACARANDA_PLANKS_BLOCK.getDefaultState(), QuiltBlockSettings.copyOf(JACARANDA_PLANKS_BLOCK)),
			new QuiltItemSettings().group(ItemGroup.BUILDING_BLOCKS),
			DerivedBlockItem::woodenStairs
	);
	public static final Block JACARANDA_BUTTON_BLOCK = registerWithItem("jacaranda_button",
			new WoodenButtonBlock(QuiltBlockSettings.copyOf(Blocks.OAK_BUTTON)), new QuiltItemSettings().group(ItemGroup.REDSTONE),
			DerivedBlockItem::woodenButton
	);
	public static final DoorBlock JACARANDA_DOOR = registerWithItem("jacaranda_door",
			new DoorBlock(QuiltBlockSettings.copyOf(Blocks.OAK_DOOR).mapColor(JACARANDA_PLANKS_BLOCK.getDefaultMapColor())),
			new QuiltItemSettings().group(ItemGroup.REDSTONE),
			DerivedBlockItem::door
	);
	public static final FenceBlock JACARANDA_FENCE_BLOCK = registerWithItem("jacaranda_fence",
			new FenceBlock(QuiltBlockSettings.copyOf(JACARANDA_PLANKS_BLOCK)),
			new QuiltItemSettings().group(ItemGroup.DECORATIONS),
			DerivedBlockItem::fence);
	public static final FenceGateBlock JACARANDA_FENCE_GATE_BLOCK = registerWithItem("jacaranda_fence_gate",
			new FenceGateBlock(QuiltBlockSettings.copyOf(JACARANDA_PLANKS_BLOCK)), new QuiltItemSettings().group(ItemGroup.REDSTONE),
			DerivedBlockItem::fenceGate);
	public static final Block JACARANDA_PRESSURE_PLATE_BLOCK = registerWithItem("jacaranda_pressure_plate",
			new PressurePlateBlock(
					PressurePlateBlock.ActivationRule.EVERYTHING,
					QuiltBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE).mapColor(JACARANDA_PLANKS_BLOCK.getDefaultMapColor())
			),
			new QuiltItemSettings().group(ItemGroup.REDSTONE),
			DerivedBlockItem::pressurePlate
	);
	public static final TerraformSignBlock JACARANDA_SIGN_BLOCK = registerBlock("jacaranda_sign",
			new TerraformSignBlock(
					id("entity/sign/jacaranda"),
					QuiltBlockSettings.copyOf(JACARANDA_PLANKS_BLOCK).strength(1.f).noCollision()
			)
	);
	public static final TrapdoorBlock JACARANDA_TRAPDOOR = registerWithItem("jacaranda_trapdoor",
			new TrapdoorBlock(QuiltBlockSettings.copyOf(Blocks.OAK_TRAPDOOR).mapColor(JACARANDA_PLANKS_BLOCK.getDefaultMapColor())),
			new QuiltItemSettings().group(ItemGroup.REDSTONE),
			DerivedBlockItem::trapdoor
	);
	public static final Block JACARANDA_WALL_SIGN_BLOCK = registerBlock("jacaranda_wall_sign",
			new TerraformWallSignBlock(id("entity/sign/jacaranda"), QuiltBlockSettings.copyOf(JACARANDA_SIGN_BLOCK))
	);

	public static final Item JACARANDA_BOAT_ITEM = TerraformBoatItemHelper.registerBoatItem(
			id("jacaranda_boat"), AurorasDecoRegistry::provideJacarandaBoatType, false
	);
	public static final Item JACARANDA_CHEST_BOAT_ITEM = TerraformBoatItemHelper.registerBoatItem(
			id("jacaranda_chest_boat"), AurorasDecoRegistry::provideJacarandaBoatType, true
	);
	public static final TerraformBoatType JACARANDA_BOAT_TYPE = Registry.register(TerraformBoatTypeRegistry.INSTANCE, id("jacaranda"),
			new TerraformBoatType.Builder().item(JACARANDA_BOAT_ITEM).chestItem(JACARANDA_BOAT_ITEM).build()
	);

	public static final SignItem JACARANDA_SIGN_ITEM = registerItem("jacaranda_sign",
			new SignItem(
					new QuiltItemSettings().group(ItemGroup.DECORATIONS),
					JACARANDA_SIGN_BLOCK, JACARANDA_WALL_SIGN_BLOCK
			)
	);

	private static TerraformBoatType provideJacarandaBoatType() {
		return JACARANDA_BOAT_TYPE;
	}
	//endregion



	static <T extends Block> T registerBlock(String name, T block) {
		return Registry.register(Registry.BLOCK, id(name), block);
	}



	private static PillarBlock createLogBlock(MapColor topMapColor, MapColor sideMapColor) {
		return new PillarBlock(
				QuiltBlockSettings.copyOf(Blocks.OAK_LOG)
						.mapColorProvider(state -> state.get(PillarBlock.AXIS).isVertical() ? topMapColor : sideMapColor)
		);
	}

	static <T extends Block> T registerWithItem(String name, T block, Item.Settings settings) {
		return registerWithItem(name, block, settings, BlockItem::new);
	}

	static <T extends Block> T registerWithItem(String name, T block, Item.Settings settings,
			BiFunction<T, Item.Settings, BlockItem> factory) {
		registerItem(name, factory.apply(registerBlock(name, block), settings));
		return block;
	}


	private static <T extends Item> T registerItem(String name, T item) {
		var o = Registry.register(Registry.ITEM, id(name), item);

		return o;
	}

	private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String name,
			BlockEntityType.BlockEntityFactory<T> factory,
			Block... blocks) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, id(name), QuiltBlockEntityTypeBuilder.create(factory, blocks).build());
	}

	private static <R extends Recipe<?>, T extends RecipeSerializer<R>> T register(String name, T recipe) {
		return Registry.register(Registry.RECIPE_SERIALIZER, id(name), recipe);
	}

	private static <T extends Recipe<?>> RecipeType<T> registerRecipeType(final String id) {
		return Registry.register(Registry.RECIPE_TYPE, id(id), new RecipeType<T>() {
			public String toString() {
				return id;
			}
		});
	}

	private static Identifier register(String id, StatFormatter statFormatter) {
		var identifier = id(id);
		Registry.register(Registry.CUSTOM_STAT, id, identifier);
		CUSTOM.getOrCreateStat(identifier, statFormatter);
		return identifier;
	}

	public static void init() {
		AurorasDecoPlants.init();
		AurorasDecoScreenHandlers.init();

		var colors = DyeColor.values();

	}
}
