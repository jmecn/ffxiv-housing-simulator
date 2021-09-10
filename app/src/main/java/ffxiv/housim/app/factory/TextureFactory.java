package ffxiv.housim.app.factory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.image.ColorSpace;
import com.jme3.util.BufferUtils;
import ffxiv.housim.saintcoinach.io.Hash;
import ffxiv.housim.saintcoinach.texture.ImageFile;
import ffxiv.housim.saintcoinach.texture.ImageHeader;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.concurrent.ExecutionException;

@Slf4j
public class TextureFactory {

    static Cache<Integer, Texture2D> CACHE;
    static {
        CACHE = CacheBuilder.newBuilder()
                .expireAfterAccess(Duration.ofSeconds(3600))
                .softValues()
                .build();
    }

    public static Texture2D get(@NonNull ImageFile imageFile) {
        int hash = Hash.compute(imageFile.getPath());
        try {
            return CACHE.get(hash, () -> innerGet(imageFile));
        } catch (ExecutionException e) {
            e.printStackTrace();
            return innerGet(imageFile);
        }
    }

    private static Texture2D innerGet(@NonNull ImageFile imageFile) {
        Image image = null;

        switch(imageFile.getFormat()) {
            case A8R8G8B8_1:
            case A8R8G8B8_2:
            case A8R8G8B8_4:
            case A8R8G8B8_5: {
                image = getImage(imageFile, Image.Format.BGRA8);
                break;
            }
            case Dxt1: {
                image = getImage(imageFile, Image.Format.DXT1);
                break;
            }
            case Dxt3: {
                image = getImage(imageFile, Image.Format.DXT3);
                break;
            }
            case Dxt5: {
                image = getImage(imageFile, Image.Format.DXT5);
                break;
            }
            case A1R5G5B5: {
                image = getImageA1R5G5B5(imageFile, Image.Format.RGB5A1);
                break;
            }
            case A16R16G16B16Float: {
                image = getImage(imageFile, Image.Format.RGBA16F);
                break;
            }
            case R3G3B2:
                break;
            case A4R4G4B4:
                image = getImageA4R4G4B4(imageFile, Image.Format.ARGB8);
                break;
            case A8R8G8B8_Cube:
                break;
            case Unknown:
                break;
        }
        if (image == null) {
            log.warn("unsupported image {}, {}", imageFile.getPath(), imageFile.getFormat());
        }
        Texture2D texture = new Texture2D(image);
        texture.setWrap(Texture.WrapMode.Repeat);
        texture.setAnisotropicFilter(16);

        int numMipmaps = imageFile.getImageHeader().getNumMipmaps();
        if (numMipmaps > 1) {
            texture.setMinFilter(Texture.MinFilter.BilinearNearestMipMap);
        }
        return texture;
    }

    public static Image getImage(@NonNull ImageFile imageFile, @NonNull Image.Format format) {
        byte[] data = imageFile.getData();
        ImageHeader header = imageFile.getImageHeader();

        int numMipmaps = header.getNumMipmaps();
        int[] mipmapSizes = new int[numMipmaps];
        if (numMipmaps > 1) {
            int[] mipmapOffsets = header.getMipmapOffsets();
            for (int i = 0; i < numMipmaps - 1; i++) {
                mipmapSizes[i] = mipmapOffsets[i + 1] - mipmapOffsets[i];
            }
            mipmapSizes[numMipmaps - 1] = data.length - mipmapOffsets[numMipmaps - 1];
        } else {
            mipmapSizes[0] = data.length;
        }

        log.debug("mipmapSizes:{}", mipmapSizes);

        ByteBuffer buffer = BufferUtils.createByteBuffer(data);
        return new Image(format, header.getWidth(), header.getHeight(), buffer, mipmapSizes, ColorSpace.sRGB);
    }

    public static Image getImageA1R5G5B5(@NonNull ImageFile imageFile, @NonNull Image.Format format) {
        byte[] bytes = imageFile.getData();
        byte[] data = new byte[bytes.length];
        ImageHeader header = imageFile.getImageHeader();

        for (int i = 0; i < data.length; i += 2) {
            // big endian
            int h = bytes[i] & 0x80;// Alpha at the highest bit
            int l = bytes[i + 1] & 0x80;
            data[i] = (byte) (bytes[i] << 1 | l >> 7);
            data[i + 1] = (byte) (bytes[i + 1] << 1 | h >> 7);
        }

        int numMipmaps = header.getNumMipmaps();
        int[] mipmapSizes = new int[numMipmaps];
        if (numMipmaps > 1) {
            int[] mipmapOffsets = header.getMipmapOffsets();
            for (int i = 0; i < numMipmaps - 1; i++) {
                mipmapSizes[i] = mipmapOffsets[i + 1] - mipmapOffsets[i];
            }
            mipmapSizes[numMipmaps - 1] = data.length - mipmapOffsets[numMipmaps - 1];
        } else {
            mipmapSizes[0] = data.length;
        }

        log.debug("mipmapSizes:{}", mipmapSizes);

        ByteBuffer buffer = BufferUtils.createByteBuffer(data);
        return new Image(format, header.getWidth(), header.getHeight(), buffer, mipmapSizes, ColorSpace.sRGB);
    }


    public static Image getImageA4R4G4B4(@NonNull ImageFile imageFile, @NonNull Image.Format format) {

        byte[] bytes = imageFile.getData();
        byte[] data = new byte[bytes.length * 2];
        ImageHeader header = imageFile.getImageHeader();

        for (int i = 0; i < bytes.length; i += 2) {
            // little endian
            byte a = (byte) (bytes[i + 1] & 0xF0);
            byte r = (byte) ((bytes[i + 1] & 0x0F) << 4);
            byte g = (byte) (bytes[i] & 0xF0);
            byte b = (byte) ((bytes[i] & 0x0F) << 4);
            data[i * 2]     = a;// b
            data[i * 2 + 1] = r;// g
            data[i * 2 + 2] = g;// r
            data[i * 2 + 3] = b;// a
        }

        int numMipmaps = header.getNumMipmaps();
        int[] mipmapSizes = new int[numMipmaps];
        if (numMipmaps > 1) {
            int[] mipmapOffsets = header.getMipmapOffsets();
            for (int i = 0; i < numMipmaps - 1; i++) {
                mipmapSizes[i] = mipmapOffsets[i + 1] - mipmapOffsets[i];
                mipmapSizes[i] <<= 1;
            }
            mipmapSizes[numMipmaps - 1] = data.length - mipmapOffsets[numMipmaps - 1];
            mipmapSizes[numMipmaps - 1] <<= 1;
        } else {
            mipmapSizes[0] = data.length;
            mipmapSizes[0] <<= 1;
        }

        log.debug("mipmapSizes:{}", mipmapSizes);

        ByteBuffer buffer = BufferUtils.createByteBuffer(data);
        return new Image(format, header.getWidth(), header.getHeight(), buffer, mipmapSizes, ColorSpace.sRGB);
    }
}
