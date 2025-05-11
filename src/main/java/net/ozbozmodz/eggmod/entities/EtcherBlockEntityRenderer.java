package net.ozbozmodz.eggmod.entities;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class EtcherBlockEntityRenderer implements BlockEntityRenderer<EtcherBlockEntity> {

    public EtcherBlockEntityRenderer(BlockEntityRendererFactory.Context context){}

    @Override
    public void render(EtcherBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        ItemStack templateStack = entity.getStack(0);
        ItemStack eggStack = entity.getStack(2);
        renderTemplate(entity, templateStack, matrices, vertexConsumers, itemRenderer);
        renderEgg(entity, eggStack, matrices, vertexConsumers, itemRenderer);
    }

    // Render a template against the back wall of the etcher, facing its direction
    private void renderTemplate(EtcherBlockEntity entity, ItemStack templateStack, MatrixStack matrices,VertexConsumerProvider vertexConsumers, ItemRenderer itemRenderer){
        Direction dir = entity.getTemplateDirection();
        float offsetX = 0.5f;
        float offsetZ = 0.1875f;
        float x;
        float y = 0.4375f;
        float z;
        // Make sure the template remains in the right spot
        switch (dir){
            case EAST -> {x = 1-offsetZ; z = offsetX;}
            case WEST -> {x = offsetZ; z = 1-offsetX;}
            case NORTH -> {x = offsetX; z = offsetZ;}
            case SOUTH -> {x = 1-offsetX; z = 1-offsetZ;}
            default -> {x = offsetX; z = offsetZ;}
        }
        // Transform the texture based on the parameters
        matrices.push();
        matrices.translate(x, y, z);
        matrices.scale(0.25f, 0.25f, 0.25f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.getTemplateDirection().asRotation()));

        itemRenderer.renderItem(templateStack, ModelTransformationMode.GUI, getLightLevel(entity.getWorld(), entity.getPos()),
                OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
        matrices.pop();
    }

    // Render the egg and have it spin based on the rotation from the blockentity
    private void renderEgg(EtcherBlockEntity entity, ItemStack eggStack, MatrixStack matrices,VertexConsumerProvider vertexConsumers, ItemRenderer itemRenderer){
        Direction dir = entity.getTemplateDirection();
        float offsetX = 0.5f;
        float offsetZ = 0.5625f;
        float x;
        float y = 0.45f;
        float z;
        float deg = entity.getEggRotation();
        switch (dir){
            case EAST -> {x = 1-offsetZ; z = offsetX;}
            case WEST -> {x = offsetZ; z = 1-offsetX;}
            case NORTH -> {x = offsetX; z = offsetZ;}
            case SOUTH -> {x = 1-offsetX; z = 1-offsetZ;}
            default -> {x = offsetX; z = offsetZ;}
        }
        matrices.push();
        matrices.translate(x, y, z);
        matrices.scale(0.3f, 0.3f, 0.3f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(deg));

        itemRenderer.renderItem(eggStack, ModelTransformationMode.GUI, getLightLevel(entity.getWorld(), entity.getPos()),
                OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 2);
        matrices.pop();
    }

    private int getLightLevel(World world, BlockPos pos){
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }
}
