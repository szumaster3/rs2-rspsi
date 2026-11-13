/**
* Copyright (c) Kyle Fricilone
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.rspsi.misc;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Kyle Friz
 * 
 * @since Jun 30, 2015
 */
@Slf4j
public final class XTEAManager {

	@Getter
      private static final Map<Integer, int[]> maps = new ConcurrentHashMap<>();
      public static final int[] NULL_KEYS = new int[4];


      public static int[] lookupMap(int region) {
            return maps.getOrDefault(region, NULL_KEYS);
      }

      public static boolean load(File xteaDir) throws IOException {
    	  	if(!xteaDir.isDirectory()) {
    	  		loadFromJSON(xteaDir);
    	  	} else {
    	  		 final File[] files = xteaDir.listFiles() == null ? new File[0] : xteaDir.listFiles();

    	            for (File file : files) {
    	                  if (file.getName().endsWith(".txt")) {
    	                        final int region = Integer.valueOf(file.getName().substring(0, file.getName().indexOf(".txt")));
    	                        final int[] keys = Files.lines(xteaDir.toPath().resolve(file.getName())).map(Integer::valueOf).mapToInt(Integer::intValue).toArray();
    	                        maps.put(region, keys);
    	                  }
    	            }

    	  	}
           
            return !maps.isEmpty();
      }
      

      public static void loadFromJSON(File file) {
            try (Reader reader = new FileReader(file)) {
                  Gson gson = new Gson();
                  JsonElement root = new JsonParser().parse(reader);
                  Map<Integer, int[]> loaded = new HashMap<>();
                  if (root.isJsonObject() && root.getAsJsonObject().has("xteas")) {
                        root = root.getAsJsonObject().get("xteas");
                  }
                  if (root.isJsonArray()) {
                        for (JsonElement entry : root.getAsJsonArray()) {
                              JsonObject record = entry.getAsJsonObject();
                              if (record.has("regionId") && record.get("keys").isJsonPrimitive()) {
                                    int region = record.get("regionId").getAsInt();
                                    int[] keys = Arrays.stream(record.get("keys").getAsString().split(","))
                                          .map(String::trim).mapToInt(Integer::parseInt).toArray();
                                    loaded.put(region, keys);
                              } else {
                                    XTEA key = gson.fromJson(record, XTEA.class);
                                    loaded.put(key.getRegion(), key.getKeys());
                              }
                        }
                  } else {
                        loaded.putAll(gson.fromJson(root, new TypeToken<Map<Integer, int[]>>(){}.getType()));
                  }
                  loaded.values().removeIf(keys -> keys == null || keys.length != 4);
                  maps.clear();
                  maps.putAll(loaded);
            } catch (Exception e) {
                  maps.clear();
                  log.error("Failed to read XTEA keys from {}", file, e);
            }
      }
}
