package PROJECT;

import transforms.Vec3D;

import java.util.ArrayList;
import java.util.List;

public class Material {
    public Vec3D DIV;
    public Vec3D SPEC;
    public Vec3D AMB;
    public String NAME;
    public Float SHININES;

    public boolean hasTexture;
    public String textureFile;

    public Material() {

    }

    @Override
    public String toString() {
        return "Material{" +
                "DIV=" + DIV +
                ", SPEC=" + SPEC +
                ", AMB=" + AMB +
                ", NAME='" + NAME + '\'' +
                ", SHININES=" + SHININES +
                ", hasTexture=" + hasTexture +
                '}';
    }

    public Material(Vec3D DIV, Vec3D SPEC, Vec3D AMB, String NAME, Float SHININES, boolean hasTexture) {
        this.DIV = DIV;
        this.SPEC = SPEC;
        this.AMB = AMB;
        this.NAME = NAME;
        this.SHININES = SHININES;
        this.hasTexture = hasTexture;
    }

    public Material(Vec3D DIV, Vec3D SPEC, Vec3D AMB, String NAME, Float SHININES, boolean hasTexture, String textureFile) {
        this.DIV = DIV;
        this.SPEC = SPEC;
        this.AMB = AMB;
        this.NAME = NAME;
        this.SHININES = SHININES;
        this.hasTexture = hasTexture;
        this.textureFile = textureFile;
    }
}

