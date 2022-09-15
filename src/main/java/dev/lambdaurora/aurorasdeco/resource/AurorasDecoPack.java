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
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import com.mojang.blaze3d.texture.NativeImage;
import dev.lambdaurora.aurorasdeco.AurorasDeco;
import dev.lambdaurora.aurorasdeco.mixin.client.NativeImageAccessor;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.Material;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.pack.AbstractFileResourcePack;
import net.minecraft.resource.pack.ResourcePack;
import net.minecraft.resource.pack.metadata.ResourceMetadataReader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.resource.loader.impl.ModResourcePackUtil;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AurorasDecoPack implements ResourcePack {
	private static final Logger LOGGER = LogManager.getLogger();

	private final Set<String> namespaces = new HashSet<>();
	private final Map<String, byte[]> resources = new Object2ObjectOpenHashMap<>();
	private final ResourceType type;

	private boolean hasRegisteredOneTimeResources = false;

	public AurorasDecoPack(ResourceType type) {
		this.type = type;
	}

	public AurorasDecoPack rebuild(ResourceType type, @Nullable ResourceManager resourceManager) {
		this.resources.clear();
		this.namespaces.clear();
		this.namespaces.add("aurorasdeco");



		return type == ResourceType.CLIENT_RESOURCES ? this.rebuildClient(resourceManager) : this.rebuildData();
	}

	public AurorasDecoPack rebuildClient(ResourceManager resourceManager) {
		Datagen.generateClientData(resourceManager);

		return this;
	}

	private void registerTag(String[] types, Identifier id, Stream<Identifier> entries) {
		var root = new JsonObject();
		root.addProperty("replace", false);
		var values = new JsonArray();

		entries.forEach(value -> values.add(value.toString()));

		root.add("values", values);

		for (var type : types) {
			this.putJson("data/" + id.getNamespace() + "/tags/" + type + "/" + id.getPath() + ".json",
					root);
		}
	}

	public AurorasDecoPack rebuildData() {
		if (!this.hasRegisteredOneTimeResources) {

			this.hasRegisteredOneTimeResources = true;
		}



		LOGGER.info("Registered " + this.resources.size() + " resources.");

		return this;
	}

	public void putResource(String resource, byte[] data) {
		this.resources.put(resource, data);

		if (QuiltLoader.isDevelopmentEnvironment()) {
			try {
				var path = Paths.get("debug", "aurorasdeco").resolve(resource);
				Files.createDirectories(path.getParent());
				Files.write(path, data, StandardOpenOption.CREATE, StandardOpenOption.WRITE,
						StandardOpenOption.TRUNCATE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void putJsonText(ResourceType type, Identifier id, String json) {
		this.namespaces.add(id.getNamespace());

		String path = Datagen.toPath(id, type) + ".json";
		this.putText(path, json);
	}

	public void putText(String resource, String text) {
		this.putResource(resource, text.getBytes(StandardCharsets.UTF_8));
	}

	public void putJson(ResourceType type, Identifier id, JsonObject json) {
		this.namespaces.add(id.getNamespace());

		String path = Datagen.toPath(id, type) + ".json";
		this.putJson(path, json);
	}

	public void putJson(String resource, JsonObject json) {
		var stringWriter = new StringWriter();
		var jsonWriter = new JsonWriter(stringWriter);
		jsonWriter.setLenient(true);
		jsonWriter.setIndent("  ");
		try {
			Streams.write(json, jsonWriter);
		} catch (IOException e) {
			LOGGER.error("Failed to write JSON at {}.", resource, e);
		}
		this.putText(resource, stringWriter.toString());
	}

	public void putImage(Identifier id, NativeImage image) {
		this.namespaces.add(id.getNamespace());

		String path = Datagen.toPath(id, ResourceType.CLIENT_RESOURCES, "textures/") + ".png";
		this.putImage(path, image);
	}

	public void putImage(String location, NativeImage image) {
		var byteOut = new ByteArrayOutputStream();
		var out = Channels.newChannel(byteOut);
		// Please forgive me
		((NativeImageAccessor) (Object) image).aurorasdeco$write(out);

		this.putResource(location, byteOut.toByteArray());
		try {
			out.close();
		} catch (IOException e) {
			LOGGER.warn("Could not close output channel for texture " + location + ".", e);
		}
	}

	@Override
	public @Nullable InputStream openRoot(String fileName) throws IOException {
		var metadata = QuiltLoader.getModContainer(AurorasDeco.NAMESPACE).get().metadata();

		if (ModResourcePackUtil.containsDefault(metadata, fileName)) {
			return ModResourcePackUtil.openDefault(metadata,
					this.type,
					fileName);
		}

		byte[] data;
		if ((data = this.resources.get(fileName)) != null) {
			return new ByteArrayInputStream(data);
		}
		throw new IOException("Generated resources pack has no data or alias for " + fileName);
	}

	@Override
	public InputStream open(ResourceType type, Identifier id) throws IOException {
		return this.openRoot(type.getDirectory() + "/" + id.getNamespace() + "/" + id.getPath());
	}

	@Override
	public Collection<Identifier> findResources(ResourceType type, String namespace, String prefix,
			Predicate<Identifier> pathFilter) {
		var start = type.getDirectory() + "/" + namespace + "/" + prefix;
		return this.resources.keySet().stream()
				.filter(s -> s.startsWith(start))
				.map(AurorasDecoPack::fromPath)
				.filter(pathFilter)
				.collect(Collectors.toList());
	}

	@Override
	public boolean contains(ResourceType type, Identifier id) {
		var path = type.getDirectory() + "/" + id.getNamespace() + "/" + id.getPath();
		return this.resources.containsKey(path);
	}

	@Override
	public Set<String> getNamespaces(ResourceType type) {
		return this.namespaces;
	}

	@Override
	public <T> @Nullable T parseMetadata(ResourceMetadataReader<T> metaReader) throws IOException {
		InputStream inputStream = this.openRoot("pack.mcmeta");
		Throwable error = null;

		T metadata;
		try {
			metadata = AbstractFileResourcePack.parseMetadata(metaReader, inputStream);
		} catch (Throwable e) {
			error = e;
			throw e;
		} finally {
			if (inputStream != null) {
				if (error != null) {
					try {
						inputStream.close();
					} catch (Throwable e) {
						error.addSuppressed(e);
					}
				} else {
					inputStream.close();
				}
			}
		}

		return metadata;
	}

	@Override
	public String getName() {
		return "Aurora's Decorations Virtual Pack";
	}

	@Override
	public void close() {
		if (this.type == ResourceType.CLIENT_RESOURCES) {
			this.resources.clear();
			this.namespaces.clear();
		}
	}

	private static Identifier fromPath(String path) {
		String[] split = path.replaceAll("((assets)|(data))/", "").split("/", 2);

		return new Identifier(split[0], split[1]);
	}
}
