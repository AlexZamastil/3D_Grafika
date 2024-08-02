package PROJECT;

import transforms.Vec2D;
import transforms.Vec3D;

import java.util.ArrayList;
import java.util.List;

public class Model {
    public List<Vec3D> vertices = new ArrayList<>();
    public List<Vec3D> normals = new ArrayList<>();
    public List<Vec2D> textures = new ArrayList<>();
    public List<Face> faces = new ArrayList<>();

    public Model() {

    }

}
