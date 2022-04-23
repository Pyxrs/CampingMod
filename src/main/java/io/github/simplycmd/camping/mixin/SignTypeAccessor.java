package io.github.simplycmd.camping.mixin;

import net.minecraft.util.SignType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SignType.class)
public interface SignTypeAccessor {
    @Invoker
    static SignType callRegister(SignType type) {
        throw new UnsupportedOperationException();
    }

    @Invoker("<init>")
    static SignType createSignType(String name) {
        throw new UnsupportedOperationException();
    }
}
