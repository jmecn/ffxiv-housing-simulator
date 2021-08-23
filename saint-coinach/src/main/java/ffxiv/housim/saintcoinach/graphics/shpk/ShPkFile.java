package ffxiv.housim.saintcoinach.graphics.shpk;

import ffxiv.housim.saintcoinach.io.PackFile;
import lombok.Getter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class ShPkFile {

    @Getter
    private PackFile file;
    @Getter
    private ShPkHeader header;
    @Getter
    private List<Parameter> parameters;

    public ShPkFile(PackFile file) {
        this.file = file;

        byte[] bytes = file.getData();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        this.header = new ShPkHeader(buffer);

        // TODO: All the other things

        List<Parameter> params = new ArrayList<>(header.getParameterHeaders().size());
        for (ParameterHeader paramHeader : header.getParameterHeaders()) {
            params.add(new Parameter(this, paramHeader, buffer));
        }
        parameters = List.copyOf(params);
    }
}
