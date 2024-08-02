package PROJECT;

import transforms.Vec3D;

public class Face {
    public Vec3D vertex;
    public Vec3D normal;
    public Vec3D texture;
    public int objectIndex;
    public String materialName;

    public Face(Vec3D vertex, Vec3D normal, Vec3D texture, int objectIndex, String materialName) {
        this.vertex = vertex;
        this.normal = normal;
        this.texture = texture;
        this.objectIndex = objectIndex;
        this.materialName = materialName;
    }

    @Override
    public String toString() {
        return "Face{" +
                "vertex=" + vertex +
                ", normal=" + normal +
                ", texture=" + texture +
                ", objectIndex=" + objectIndex +
                ", materialName='" + materialName + '\'' +
                '}';
    }

}
