package grondag.canvas.compat;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;

import net.fabricmc.loader.api.FabricLoader;

import grondag.canvas.CanvasMod;

public class DynocapsHolder {
	private static boolean warnRender = true;

	public static DynoCapsRender handler = (profiler, matrixStack, immediate, camPos) -> {};

	static {
		if (FabricLoader.getInstance().isModLoaded("dynocaps")) {

			final MethodHandles.Lookup lookup = MethodHandles.lookup();

			try {
				final Class<?> clazz = Class.forName("com.biom4st3r.dynocaps.features.BoxRenderer");
				final Method render = clazz.getDeclaredMethod("render", MatrixStack.class, VertexConsumerProvider.Immediate.class, int.class, Vec3d.class);
				final MethodHandle renderHandler = lookup.unreflect(render);

				handler = (profiler, matrixStack, immediate, camPos) -> {
					try  {
						profiler.swap("dynocaps");
						renderHandler.invokeExact(matrixStack, immediate, 1, camPos);
					} catch (final Throwable e) {
						if (warnRender) {
							CanvasMod.LOG.warn("Unable to call DynoCaps BoxRenderer.render hook due to exception:", e);
							CanvasMod.LOG.warn("Subsequent errors will be suppressed");
							warnRender = false;
						}
					}
				};

				CanvasMod.LOG.info("Found DynoCaps - compatibility hook enabled");
			} catch (final Exception e)  {
				CanvasMod.LOG.warn("Unable to find DynoCaps render hook due to exception:", e);
			}
		}
	}

	public interface DynoCapsRender {
		void render(Profiler profiler, MatrixStack matrixStack, VertexConsumerProvider.Immediate immediate, Vec3d camPos);
	}
}
