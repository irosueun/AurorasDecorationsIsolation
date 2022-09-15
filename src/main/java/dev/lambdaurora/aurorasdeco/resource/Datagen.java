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

package dev.lambdaurora.aurorasdeco.resource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.lambdaurora.aurorasdeco.AurorasDeco;
import dev.lambdaurora.aurorasdeco.client.AurorasDecoClient;
import dev.lambdaurora.aurorasdeco.resource.datagen.BlockStateBuilder;
import dev.lambdaurora.aurorasdeco.resource.datagen.ModelBuilder;
import dev.lambdaurora.aurorasdeco.resource.datagen.MultipartBlockStateBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.item.Item;
import net.minecraft.recipe.Recipe;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quiltmc.qsl.recipe.api.serializer.QuiltRecipeSerializer;

import java.util.regex.Pattern;

import static dev.lambdaurora.aurorasdeco.AurorasDeco.id;
import static dev.lambdaurora.aurorasdeco.util.AuroraUtil.jsonArray;

/**
 * Represents the Aurora's Decorations data generator.
 *
 * @author LambdAurora
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Datagen {
	public static final Logger LOGGER = LogManager.getLogger("aurorasdeco:datagen");

	private static final Identifier WALL_LANTERN_ATTACHMENT = id("block/wall_lantern_attachment");
	private static final Identifier WALL_LANTERN_ATTACHMENT_EXTENDED1 = id("block/wall_lantern_attachment_extended1");
	private static final Identifier WALL_LANTERN_ATTACHMENT_EXTENDED2 = id("block/wall_lantern_attachment_extended2");

	private static final Identifier TEMPLATE_LANTERN_MODEL = new Identifier("block/template_lantern");
	private static final Identifier TEMPLATE_HANGING_LANTERN_MODEL = new Identifier("block/template_hanging_lantern");
	private static final Identifier TEMPLATE_SLEEPING_BAG_FOOT_MODEL = id("block/template/sleeping_bag_foot");
	private static final Identifier TEMPLATE_SLEEPING_BAG_HEAD_MODEL = id("block/template/sleeping_bag_head");
	private static final Identifier TEMPLATE_SLEEPING_BAG_ITEM_MODEL = id("item/template/sleeping_bag");
	private static final Identifier TEMPLATE_SEAT_REST_ITEM_MODEL = id("item/template/seat_rest");

	private static final Identifier BIG_FLOWER_POT_MODEL = id("block/big_flower_pot/big_flower_pot");
	private static final Identifier BIG_FLOWER_POT_WITH_MYCELIUM_MODEL = id("block/big_flower_pot/mycelium");
	private static final Identifier LOG_STUMP_LEAF_TEXTURE = id("block/log_stump_leaf");

	static final Identifier SHELF_BETTERGRASS_DATA = id("bettergrass/data/shelf");

	private static final Pattern PLANKS_TO_BASE_ID = Pattern.compile("[_/]planks$");
	private static final Pattern PLANKS_SEPARATOR_DETECTOR = Pattern.compile("[/]planks$");
	private static final Pattern LOG_TO_BASE_ID = Pattern.compile("[_/]log$");
	private static final Pattern LOG_SEPARATOR_DETECTOR = Pattern.compile("[/]log$");
	private static final Pattern STEM_TO_BASE_ID = Pattern.compile("[_/]stem$");
	private static final Pattern STEM_SEPARATOR_DETECTOR = Pattern.compile("[/]stem$");

	private Datagen() {
		throw new UnsupportedOperationException("Someone tried to instantiate a static-only class. How?");
	}

	public static void registerBetterGrassLayer(Identifier blockId, Identifier data) {
		var json = new JsonObject();
		json.addProperty("type", "layer");
		json.addProperty("data", data.toString());
		AurorasDecoClient.RESOURCE_PACK.putJson(
				ResourceType.CLIENT_RESOURCES,
				new Identifier(blockId.getNamespace(), "bettergrass/states/" + blockId.getPath()),
				json
		);
	}

	public static void registerBetterGrassLayer(Block block, Identifier data) {
		registerBetterGrassLayer(Registry.BLOCK.getId(block), data);
	}

	private static JsonObject generateBlockLootTableSimplePool(Identifier id, boolean copyName) {
		var pool = new JsonObject();
		pool.addProperty("rolls", 1.0);
		pool.addProperty("bonus_rolls", 0.0);

		var entries = new JsonArray();

		var entry = new JsonObject();
		entry.addProperty("type", "minecraft:item");
		entry.addProperty("name", id.toString());

		if (copyName) {
			var function = new JsonObject();
			function.addProperty("function", "minecraft:copy_name");
			function.addProperty("source", "block_entity");
			entry.add("functions", jsonArray(function));
		}

		entries.add(entry);

		pool.add("entries", entries);

		var survivesExplosion = new JsonObject();
		survivesExplosion.addProperty("condition", "minecraft:survives_explosion");
		pool.add("conditions", jsonArray(survivesExplosion));

		return pool;
	}

	public static JsonObject simpleBlockLootTable(Identifier id, boolean copyName) {
		var root = new JsonObject();
		root.addProperty("type", "minecraft:block");
		var pools = new JsonArray();
		pools.add(generateBlockLootTableSimplePool(id, copyName));

		root.add("pools", pools);

		return root;
	}

	private static JsonObject benchBlockLootTable(Identifier id) {
		var root = new JsonObject();
		root.addProperty("type", "minecraft:block");
		var pools = new JsonArray();
		pools.add(generateBlockLootTableSimplePool(id, true));

		{
			var restPool = new JsonObject();
			pools.add(restPool);
			restPool.addProperty("rolls", 1.0);
			var entries = new JsonArray();
			restPool.add("entries", entries);
			var entry = new JsonObject();
			entries.add(entry);
			entry.addProperty("type", "minecraft:dynamic");
			entry.addProperty("name", "aurorasdeco:seat_rest");
		}

		root.add("pools", pools);

		return root;
	}

	public static void registerBenchBlockLootTable(Block block) {
		var id = Registry.BLOCK.getId(block);
		AurorasDeco.RESOURCE_PACK.putJson(
				ResourceType.SERVER_DATA,
				new Identifier(id.getNamespace(), "loot_tables/blocks/" + id.getPath()),
				benchBlockLootTable(id)
		);
	}

	private static JsonObject doubleBlockLootTable(Identifier id) {
		var root = new JsonObject();
		root.addProperty("type", "minecraft:block");
		var pools = new JsonArray();
		pools.add(generateBlockLootTableSimplePool(id, true));

		{
			var pool = generateBlockLootTableSimplePool(id, true);
			pools.add(pool);

			var conditions = pool.getAsJsonArray("conditions");
			var condition = new JsonObject();
			conditions.add(condition);
			condition.addProperty("condition", "minecraft:block_state_property");
			condition.addProperty("block", id.toString());
			{
				var properties = new JsonObject();
				properties.addProperty("type", "double");
				condition.add("properties", properties);
			}
		}

		root.add("pools", pools);

		return root;
	}

	public static void registerDoubleBlockLootTable(Block block) {
		var id = Registry.BLOCK.getId(block);
		AurorasDeco.RESOURCE_PACK.putJson(
				ResourceType.SERVER_DATA,
				new Identifier(id.getNamespace(), "loot_tables/blocks/" + id.getPath()),
				doubleBlockLootTable(id)
		);
	}

	private static String candleLikeBlockLootTable(Identifier blockId, Identifier itemId) {
		return """
				{
				  "type": "minecraft:block",
				  "pools": [
				    {
				      "rolls": 1.0,
				      "bonus_rolls": 0.0,
				      "entries": [
				        {
				          "type": "minecraft:item",
				          "functions": [
				            {
				              "function": "minecraft:set_count",
				              "conditions": [
				                {
				                  "condition": "minecraft:block_state_property",
				                  "block": "${block}",
				                  "properties": {
				                    "candles": "2"
				                  }
				                }
				              ],
				              "count": 2.0,
				              "add": false
				            },
				            {
				              "function": "minecraft:set_count",
				              "conditions": [
				                {
				                  "condition": "minecraft:block_state_property",
				                  "block": "${block}",
				                  "properties": {
				                    "candles": "3"
				                  }
				                }
				              ],
				              "count": 3.0,
				              "add": false
				            },
				            {
				              "function": "minecraft:set_count",
				              "conditions": [
				                {
				                  "condition": "minecraft:block_state_property",
				                  "block": "${block}",
				                  "properties": {
				                    "candles": "4"
				                  }
				                }
				              ],
				              "count": 4.0,
				              "add": false
				            },
				            {
				              "function": "minecraft:explosion_decay"
				            }
				          ],
				          "name": "${item}"
				        }
				      ]
				    }
				  ]
				}
				"""
				.replace("${block}", blockId.toString())
				.replace("${item}", itemId.toString());
	}



	public static void dropsSelf(Block block) {
		registerSimpleBlockLootTable(Registry.BLOCK.getId(block), Registry.ITEM.getId(block.asItem()),
				block instanceof BlockWithEntity);
	}

	public static void registerSimpleBlockLootTable(Identifier blockId, Identifier itemId, boolean copyName) {
		AurorasDeco.RESOURCE_PACK.putJson(
				ResourceType.SERVER_DATA,
				new Identifier(blockId.getNamespace(), "loot_tables/blocks/" + blockId.getPath()),
				simpleBlockLootTable(itemId, copyName)
		);
	}

	@SuppressWarnings("unchecked")
	public static JsonObject recipe(Recipe<?> recipe) {
		if (!(recipe.getSerializer() instanceof QuiltRecipeSerializer<?>))
			throw new UnsupportedOperationException("Cannot serialize recipe " + recipe);

		return ((QuiltRecipeSerializer<Recipe<?>>) recipe.getSerializer()).toJson(recipe);
	}






	public static void generateClientData(ResourceManager resourceManager) {

	}


	private static void generateSimpleItemModel(Item item) {
		var itemId = Registry.ITEM.getId(item);
		generateSimpleItemModel(new Identifier(itemId.getNamespace(), "item/" + itemId.getPath()));
	}

	private static void generateSimpleItemModel(Identifier id) {
		modelBuilder(new Identifier("item/generated"))
				.texture("layer0", id)
				.register(id);
	}

	public static String toPath(Identifier id, ResourceType type) {
		return toPath(id, type, "");
	}

	public static String toPath(Identifier id, ResourceType type, String prefix) {
		return type.getDirectory() + '/' + id.getNamespace() + '/' + prefix + id.getPath();
	}

	public static BlockStateBuilder blockStateBuilder(Block block) {
		return new BlockStateBuilder(block);
	}

	public static MultipartBlockStateBuilder multipartBlockStateBuilder(Block block) {
		return new MultipartBlockStateBuilder(block);
	}

	public static ModelBuilder modelBuilder(Identifier parent) {
		return new ModelBuilder(parent);
	}
}
