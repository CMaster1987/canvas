package grondag.canvas.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import grondag.canvas.core.CanvasBufferBuilder;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;

@Mixin(Tessellator.class)
public class MixinTessellator {
    @Shadow private BufferRenderer renderer;
    
    @Redirect(method = "<init>*", require = 1, at = @At(value = "NEW", args = "class=net/minecraft/client/render/BufferBuilder"))
    private BufferBuilder newBuferBuilder(int bufferSizeIn) {
        return new CanvasBufferBuilder(bufferSizeIn);
    }
    
    //TODO: WIP
//    @Redirect(method = "draw", require = 1, at = @At(value = "INVOKE", args = "class=net/minecraft/client/render/BufferBuilder"))
//    private void onDraw(BufferBuilder buffer) {
//        final VertexCollectorList vcList = ((CanvasBufferBuilder)buffer).vcList;
//        if(vcList.isEmpty()) {
//            renderer.draw(buffer);
//        } else {
//            vcList.packUploadTranslucent();
//        }
//    }
}