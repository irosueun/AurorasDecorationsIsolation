/*
 * Copyright (c) 2022 LambdAurora <email@lambdaurora.dev>
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


import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;

import static dev.lambdaurora.aurorasdeco.AurorasDeco.id;

/**
 * Represents the registered screen handlers of Aurora's Decorations.
 *
 * @author LambdAurora
 * @version 1.0.0-beta.7
 * @since 1.0.0-beta.6
 */
public class AurorasDecoScreenHandlers {
	private AurorasDecoScreenHandlers() {
		throw new UnsupportedOperationException("AurorasDecoScreenHandlers only contains static definitions.");
	}

	private static <SH extends ScreenHandler> ScreenHandlerType<SH> register(String name, ScreenHandlerType<SH> type) {
		return Registry.register(Registry.SCREEN_HANDLER, id(name), type);
	}

	static void init() {}
}
