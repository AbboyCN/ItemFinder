package com.abboycn.itemfinder.searcher;

import com.google.gson.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Box;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ZoneLoader {
    private static final List<Zone> LOADED_ZONES = new ArrayList<>();

    public static class Zone {
        public final Box box;

        public Zone(Box box) {
            this.box = box;
        }
    }

    public static void load() {
        try (InputStream in = ZoneLoader.class.getResourceAsStream("/assets/itemfinder/zones/searchZones.json")) {
            if (in == null) {
                throw new RuntimeException("File lost!");
            }

            JsonObject json = JsonParser.parseReader(new InputStreamReader(in)).getAsJsonObject();
            JsonArray zones = json.getAsJsonArray("zones");

            for (JsonElement elem : zones) {
                JsonObject zone = elem.getAsJsonObject();
                JsonArray min = zone.getAsJsonArray("min");
                JsonArray max = zone.getAsJsonArray("max");

                // 使用Vec3d构造Box
                LOADED_ZONES.add(new Zone(
                        new Box(
                                new Vec3d(
                                        min.get(0).getAsDouble(),
                                        min.get(1).getAsDouble(),
                                        min.get(2).getAsDouble()
                                ),
                                new Vec3d(
                                        max.get(0).getAsDouble(),
                                        max.get(1).getAsDouble(),
                                        max.get(2).getAsDouble()
                                )
                        )
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load zones!", e);
        }
    }

    public static List<Zone> getZones() {
        return Collections.unmodifiableList(LOADED_ZONES);
    }
}