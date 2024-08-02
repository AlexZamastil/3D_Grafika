package PROJECT;

import transforms.Vec2D;
import transforms.Vec3D;

import java.io.*;

public class OBJLoader {
    public static int currentIndex = 0;
    public static String currentMaterial;

    public static Model loadModel(File f) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        Model m = new Model();

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("v ")) {
                float x = Float.parseFloat(line.split(" ")[1]);
                float y = Float.parseFloat(line.split(" ")[2]);
                float z = Float.parseFloat(line.split(" ")[3]);
                m.vertices.add(new Vec3D(x, y, z));
            } else if (line.startsWith("vn ")) {
                float x = Float.parseFloat(line.split(" ")[1]);
                float y = Float.parseFloat(line.split(" ")[2]);
                float z = Float.parseFloat(line.split(" ")[3]);
                m.normals.add(new Vec3D(x, y, z));
            } else if (line.startsWith("vt ")) {
                float x = Float.parseFloat(line.split(" ")[1]);
                float y = Float.parseFloat(line.split(" ")[2]);
                m.textures.add(new Vec2D(x, y));
            } else if (line.startsWith("o ")) {
                currentIndex++;
            } else if (line.startsWith("usemtl ")) {
                currentMaterial = String.valueOf(line.split(" ")[1]);
            } else if (line.startsWith("f ")) {
                Vec3D vertexIndices = new Vec3D(Float.parseFloat(line.split(" ")[1].split("/")[0]),
                                                Float.parseFloat(line.split(" ")[2].split("/")[0]),
                                                Float.parseFloat(line.split(" ")[3].split("/")[0]));

                Vec3D textureIndices = new Vec3D(Float.parseFloat(line.split(" ")[1].split("/")[1]),
                                                 Float.parseFloat(line.split(" ")[2].split("/")[1]),
                                                 Float.parseFloat(line.split(" ")[3].split("/")[1]));

                Vec3D normalIndices = new Vec3D(Float.parseFloat(line.split(" ")[1].split("/")[2]),
                                                Float.parseFloat(line.split(" ")[2].split("/")[2]),
                                                Float.parseFloat(line.split(" ")[3].split("/")[2]));
                m.faces.add(new Face(vertexIndices, normalIndices, textureIndices, currentIndex, currentMaterial));
            }

        }
        reader.close();
        System.out.println("VERTICES " + m.vertices.size());
        System.out.println("NORMALS " + m.normals.size());
        System.out.println("TEXTURES " + m.textures.size());
        System.out.println("FACES " + m.faces.size());

        currentIndex = 0;
        return m;
    }
}
