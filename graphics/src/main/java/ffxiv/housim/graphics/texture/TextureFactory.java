package ffxiv.housim.graphics.texture;

import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.image.ColorSpace;
import com.jme3.util.BufferUtils;
import ffxiv.housim.saintcoinach.texture.ImageFile;
import ffxiv.housim.saintcoinach.texture.ImageHeader;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

@Slf4j
public class TextureFactory {

    public static Texture get(@NonNull ImageFile imageFile) {
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
                image = getImage(imageFile, Image.Format.RGB5A1);
                break;
            }
            case A16R16G16B16Float: {
                image = getImage(imageFile, Image.Format.RGBA16F);
                break;
            }
            case R3G3B2:
                break;
            case A4R4G4B4:
                break;
            case A8R8G8B8_Cube:
                break;
            case Unknown:
                break;
        }
        Texture2D texture = new Texture2D(image);

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
}
