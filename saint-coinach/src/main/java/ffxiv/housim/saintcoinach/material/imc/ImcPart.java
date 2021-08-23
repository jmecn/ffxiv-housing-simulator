package ffxiv.housim.saintcoinach.material.imc;

import lombok.Getter;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ImcPart {

    @Getter
    private byte bit;
    @Getter
    private List<ImcVariant> variants = new ArrayList<>();

    ImcPart(ByteBuffer buffer, byte bit) {
        this.bit = bit;

        ImcVariant v = new ImcVariant(buffer);
        variants.add(v);
    }

    public ImcVariant getDefaultVariant() {
        return variants.get(0);
    }

    public ImcVariant getVariant(int index) {
        return variants.get(index);
    }
}
