package com.rspsi.plugin;

import com.displee.cache.index.Index;
import com.jagex.Client;
import com.jagex.cache.loader.anim.FrameLoader;
import com.jagex.cache.loader.anim.GraphicLoader;
import com.jagex.cache.loader.config.VariableBitLoader;
import com.jagex.cache.loader.map.MapIndexLoader;
import com.jagex.cache.loader.object.ObjectDefinitionLoader;
import com.jagex.cache.loader.textures.TextureLoader;
import com.jagex.net.ResourceResponse;
import com.rspsi.cache.CacheFileType;
import com.rspsi.plugin.loader530.*;
import com.rspsi.plugins.ClientPlugin;

public class Plugin530 implements ClientPlugin {

    private void touchAdditionalClasses(Class... classes) {
        for (Class clazz : classes)
            clazz.isArray();
    }

    private AnimationFrameLoader frameLoader;
    private FloorDefLoader floorLoader;
    private ObjectDefLoader objLoader;
    private AnimationDefLoader animDefLoader;
    private SpotAnimationLoader graphicLoader;
    private VarbitLoader varbitLoader;
    private MapIndexLoaderOSRS mapIndexLoader;
    private TextureLoaderOSRS textureLoader;
    private AnimationSkinLoader skeletonLoader;

    @Override
    public void initializePlugin() {
        touchAdditionalClasses(MapSceneLoader.class, MapSceneLoader.MapScene.class, TextureLoaderOSRS.MaterialColour.class);
        objLoader = new ObjectDefLoader();
        floorLoader = new FloorDefLoader();
        frameLoader = new AnimationFrameLoader();
        animDefLoader = new AnimationDefLoader();

        mapIndexLoader = new MapIndexLoaderOSRS();
        textureLoader = new TextureLoaderOSRS();
        skeletonLoader = new AnimationSkinLoader();
        graphicLoader = new SpotAnimationLoader();
        varbitLoader = new VarbitLoader();

        MapIndexLoader.instance = mapIndexLoader;
        GraphicLoader.instance = graphicLoader;
        VariableBitLoader.instance = varbitLoader;
        FrameLoader.instance = frameLoader;
        ObjectDefinitionLoader.instance = objLoader;
        com.jagex.cache.loader.floor.FloorDefinitionLoader.instance = floorLoader;
        com.jagex.cache.loader.anim.FrameBaseLoader.instance = skeletonLoader;
        TextureLoader.instance = textureLoader;
        com.jagex.cache.loader.anim.AnimationDefinitionLoader.instance = animDefLoader;
    }

    @Override
    public void onGameLoaded(Client client) {

        frameLoader.init(client.getCache().readFile(CacheFileType.ANIMATION),
                client.getCache().readFile(CacheFileType.SKELETON));

        Index configIndex = client.getCache().readFile(CacheFileType.CONFIG);

        floorLoader.decodeUnderlays(configIndex.archive(1));
        floorLoader.decodeOverlays(configIndex.archive(4));
        varbitLoader.decodeVarbits(client.getCache().getCacheLibrary().index(22));
        objLoader.decodeObjects(client.getCache().getCacheLibrary().index(16));

        animDefLoader.init(client.getCache().getCacheLibrary().index(20));
        graphicLoader.decodeGraphics(client.getCache().readFile(CacheFileType.SPOT));

        MapSceneLoader mapSceneLoader = new MapSceneLoader();
        mapSceneLoader.init(client, configIndex.archive(34), client.getCache().readFile(CacheFileType.SPRITE));

        Index mapIndex = client.getCache().readFile(CacheFileType.MAP);
        mapIndexLoader.init(mapIndex);


        textureLoader.init(client.getCache().getCacheLibrary().index(26).archive(0));

    }

    @Override
    public void onResourceDelivered(ResourceResponse arg0) {
        // TODO Auto-generated method stub

    }

}
