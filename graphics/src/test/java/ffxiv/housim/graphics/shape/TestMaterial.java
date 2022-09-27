package ffxiv.housim.graphics.shape;

import com.jme3.math.ColorRGBA;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.material.Material;
import ffxiv.housim.saintcoinach.material.MaterialDefinition;
import ffxiv.housim.saintcoinach.material.MaterialTextureParameter;
import ffxiv.housim.saintcoinach.material.shpk.Parameter;
import ffxiv.housim.saintcoinach.material.shpk.ShPkFile;
import ffxiv.housim.saintcoinach.math.Vector3;
import ffxiv.housim.saintcoinach.scene.model.Model;
import ffxiv.housim.saintcoinach.scene.model.ModelDefinition;
import ffxiv.housim.saintcoinach.scene.model.ModelQuality;
import ffxiv.housim.saintcoinach.scene.model.TransformedModel;
import ffxiv.housim.saintcoinach.scene.sgb.*;
import ffxiv.housim.saintcoinach.texture.ImageFile;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/8/30
 */
@Slf4j
public class TestMaterial {
    String gameDir;
    PackCollection packs;

    @Before
    public void getGameDir() {
        gameDir = System.getenv("FFXIV_HOME");
        assertNotNull(gameDir);
        packs = new PackCollection(gameDir + "/game/sqpack");
    }

    public final static String FORMAT = "bgcommon/hou/indoor/general/%04d/asset/fun_b0_m%04d.sgb";

    @Test
    public void testSgb() {
        for (int i = 0; i < 1240; i++) {
            String path = String.format(FORMAT, i, i);
            if (packs.fileExists(path)) {
                testFurniture(path);
            }
        }
    }

    @Test
    public void test51() {
        String path = String.format(FORMAT, 51, 51);
        testFurniture(path);
    }

    @Test
    public void test899() {
        String path = String.format(FORMAT, 899, 899);
        testFurniture(path);
    }

    @Test
    public void testS() {
        testFurniture("bgcommon/hou/common/blank/asset/com_s_blank.sgb");
    }

    @Test
    public void testM() {
        testFurniture("bgcommon/hou/common/blank/asset/com_m_blank.sgb");
    }

    @Test
    public void testL() {
        testFurniture("bgcommon/hou/common/blank/asset/com_l_blank.sgb");
    }

    private void testFurniture(String path) {
        PackFile file = packs.tryGetFile(path);
        SgbFile sgbFile = new SgbFile(file);
        log.info("{}", path);
        for (ISgbData data : sgbFile.getData()) {
            SgbGroup group = (SgbGroup) data;

            int models = 0;
            for (ISgbEntry e : group.getEntries()) {
                if (e == null) {
                    continue;
                }
                if (e instanceof SgbEntryModel) {
                    SgbEntryModel me = (SgbEntryModel) e;
                    build(me, models++);
                }
            }
        }
    }
    private void build(SgbEntryModel me, int models) {

        TransformedModel transformedModel = me.getModel();

        // transform
        Vector3 trans = transformedModel.getTranslation();
        Vector3 rotate = transformedModel.getRotation();
        Vector3 scale = transformedModel.getScale();

        // model
        ModelDefinition modelDefinition = transformedModel.getModel();

        Model model = modelDefinition.getModel(ModelQuality.High);

        for (ffxiv.housim.saintcoinach.scene.mesh.Mesh m : model.getMeshes()) {
            // material
            build(m.getMaterial());
        }
    }

    private Material build(MaterialDefinition matDef) {
        Material m = matDef.get();

        // Shader vs = shpk.getVertexShader(shpk.getVertexShaderCount() - 1);
        // log.info("vs, params:{}, dxbc:{} bytes", vs.getParameters(), shpk.getDXBC(vs).length);
        // Shader ps = shpk.getPixelShader(shpk.getPixelShaderCount() - 1);
        // log.info("ps, params:{}, dxbc:{} bytes", ps.getParameters(), shpk.getDXBC(ps).length);

        assertEquals(0, m.getUnknownSize() % 4);
        assertEquals(0, m.getDataSize() % 4);
        log.debug("shader:{}\ng_WetnessParameter:{} {}\ncolorSet:{}\ng_MaterialParameter:{}", m.getShader(),
                m.getUnknown0(), Arrays.toString(m.getWetnessParameter()),
                toVec4(m.getColorSetData()),
                Arrays.toString(m.getMaterialParameter()));

        ImageFile[] textureFiles = m.getTextureFiles();
        MaterialTextureParameter[] matParams = m.getTextureParameters();

        ColorRGBA color = new ColorRGBA(1f, 1f, 1f, 1f);
        float[] materialParameter = m.getMaterialParameter();
        if (materialParameter.length > 4) {
            color.set(materialParameter[0], materialParameter[1], materialParameter[2], materialParameter[3]);
        }
        log.debug("color:{}", color);

        ShPkFile shpk = m.getShPk();
        for (MaterialTextureParameter e : matParams) {
            Parameter param = shpk.getParameter(e.getParameterId());
            ImageFile image = textureFiles[e.getTextureIndex()];
            log.debug("param:{}, image:{}", param, image.getPath());
        }
        return m;
    }

    public String toVec4(byte[] data) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < data.length; i+= 4) {
            int id = i/4;
            sb.append("#").append(id).append(":");
            sb.append(String.format("%02x%02x%02x%02x", data[i], data[i+1], data[i+2], data[i+3]));
            sb.append(",");
            if (id % 4 == 3) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
