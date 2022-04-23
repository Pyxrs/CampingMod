package io.github.simplycmd.camping.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.simplycmd.camping.Main;
import io.github.simplycmd.camping.ModBoatVariants;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.SignType;
import org.apache.commons.io.FilenameUtils;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

import static net.minecraft.client.render.TexturedRenderLayers.SIGNS_ATLAS_TEXTURE;

@Mixin(EntityModelLayers.class)
public class EntityModelLayersMixin {

    private static File dumpTexture(File parentName, Identifier texture)
    {
        var textureFolder = new File(FilenameUtils.removeExtension(parentName.getAbsolutePath()) + "_textures");
        textureFolder.mkdirs();
        var textureFile = new File(textureFolder, texture.toString().replace(":","_").replace("/","_") + ".png");

        dumpTexture(texture, textureFile);

        return textureFile;
    }

    private static void dumpTexture(Identifier texture, File target)
    {
        try (NativeImage nativeimage = downloadTexture(texture))
        {
            nativeimage.writeTo(target);
        }
        catch (Exception exception)
        {
            //LOGGER.warn("Couldn't save screenshot", exception);
        }
    }

    private static NativeImage downloadTexture(Identifier texture) {
        var id = MinecraftClient.getInstance().getTextureManager().getTexture(texture).getGlId();
        RenderSystem.bindTexture(id);
        int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
        int height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
        NativeImage nativeimage = new NativeImage(width, height, false);
        nativeimage.loadFromTextureImage(0, false);
        //nativeimage.mirrorVertically();
        return nativeimage;
    }

    @Inject(method = "createSign", at = @At("HEAD"), cancellable = true)
    private static void createSign(SignType type, CallbackInfoReturnable<EntityModelLayer> cir) {
        if (type == Main.PINE_SIGN_TYPE)
            cir.setReturnValue(    create("sign/" + "pine", "main"));
    }

    @Inject(method = "createBoat", at = @At("HEAD"), cancellable = true)
    private static void createBoat(BoatEntity.Type type, CallbackInfoReturnable<EntityModelLayer> cir) {
        if (type == ModBoatVariants.PINE)
            cir.setReturnValue(    create("boat/" + "pine", "main"));
    }

    @Unique
    private static boolean s = true;

    @Unique
    private static EntityModelLayer create(String id, String layer) {
        if (!s) {
            dumpTexture(new File(MinecraftClient.getInstance().runDirectory, "signs"), SIGNS_ATLAS_TEXTURE);
        }
        s = true;

        //System.out.println(id + ", quack, " + layer);
        return new EntityModelLayer(new Identifier("camping", id), layer);
    }
}
