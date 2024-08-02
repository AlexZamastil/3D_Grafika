package PROJECT;

import transforms.Vec3D;

import java.util.ArrayList;
import java.util.List;

public class Materials {
    public List<Vec3D> DIV = new ArrayList<>();
    public List<Vec3D> SPEC = new ArrayList<>();
    public List<Vec3D> AMB = new ArrayList<>();
    public List<String> NAME = new ArrayList<>();
    public List<Float> SHININES = new ArrayList<>();
    public List<Integer> TEXTUREINDEX = new ArrayList<>();
    public List<String> TEXTUREFILE = new ArrayList<>();
    public List<String> TEXTURESPECFILE = new ArrayList<>();
    public List<Integer> TEXTURESPECINDEX = new ArrayList<>();

    public Materials() {

    }

    @Override
    public String toString() {
        return "Materials{" +
                "DIV=" + DIV +
                ", SPEC=" + SPEC +
                ", AMB=" + AMB +
                ", NAME=" + NAME +
                ", SHININES=" + SHININES +
                ", TEXTUREINDEX=" + TEXTUREINDEX +
                ", TEXTUREFILE=" + TEXTUREFILE +
                ", TEXTURESPECFILE=" + TEXTURESPECFILE +
                ", TEXTURESPECINDEX=" + TEXTURESPECINDEX +
                '}';
    }
}