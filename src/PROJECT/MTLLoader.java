package PROJECT;

import transforms.Vec3D;

import java.io.*;

public class MTLLoader {

    public static String name;

    public static String textureFile, textureSpecFile;

    public static Materials loadMaterials(File f) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        Materials m = new Materials();
        String line;
        int i = -1;
        while ((line = reader.readLine()) != null) {
            textureFile = null;
            if (line.startsWith("Ka ")) {
                float x = Float.parseFloat(line.split(" ")[1]);
                float y = Float.parseFloat(line.split(" ")[2]);
                float z = Float.parseFloat(line.split(" ")[3]);
                m.AMB.add(new Vec3D(x, y, z));
            } else if (line.startsWith("Kd ")) {
                float x = Float.parseFloat(line.split(" ")[1]);
                float y = Float.parseFloat(line.split(" ")[2]);
                float z = Float.parseFloat(line.split(" ")[3]);
                m.DIV.add(new Vec3D(x, y, z));
            } else if (line.startsWith("Ks ")) {
                float x = Float.parseFloat(line.split(" ")[1]);
                float y = Float.parseFloat(line.split(" ")[2]);
                float z = Float.parseFloat(line.split(" ")[3]);
                m.SPEC.add(new Vec3D(x, y, z));

            } else if (line.startsWith("Ns ")) {
                float x = Float.parseFloat(line.split(" ")[1]);
                m.SHININES.add(x);

            } else if (line.startsWith("newmtl ")) {
                if (m.NAME.size() > m.TEXTUREFILE.size()) {
                    m.TEXTUREFILE.add(null);
                }
                if (m.NAME.size() > m.TEXTURESPECFILE.size()) {
                    m.TEXTURESPECFILE.add(null);
                }
                m.NAME.add(String.valueOf(line.split(" ")[1]));

                i++;
            } else if (line.startsWith("map_Kd ")) {

                textureFile = String.valueOf(line.split(" ")[1]);
                m.TEXTUREFILE.add(String.valueOf(line.split(" ")[1]));
                m.TEXTUREINDEX.add(i);
            } else if (line.startsWith("map_Ks ")) {

                textureSpecFile = String.valueOf(line.split(" ")[1]);
                m.TEXTURESPECFILE.add(String.valueOf(line.split(" ")[1]));
                m.TEXTURESPECINDEX.add(i);
            }

        }
        reader.close();
        System.out.println(m);
        return m;
    }
}
