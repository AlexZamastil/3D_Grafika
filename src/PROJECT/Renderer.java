package PROJECT;


import global.*;
import lwjglutils.OGLTexture2D;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import transforms.Vec2D;
import transforms.Vec3D;

import java.io.File;
import java.io.IOException;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;


import static global.GluUtils.gluLookAt;
import static global.GluUtils.gluPerspective;
import static global.GlutUtils.glutSolidSphere;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class Renderer extends AbstractRenderer {
    private boolean per = true, flat = false, light = false;
    private final double cameraSpeed = 1;
    private float dx, dy, ox, oy;
    private boolean mouseButton1 = false;
    private float zenit, azimut;
    private GLCamera camera;
    public static List<OGLTexture2D> textures, textures3, specTextures, specTextures3;
    public Model m, m2, m3;
    public Materials mtl, mtl2, mtl3;
    public float angle = 0.0f;

    public Renderer() {
        super();
        glfwKeyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (action == GLFW_REPEAT) {
                    switch (key) {
                        case GLFW_KEY_A -> camera.left(cameraSpeed);
                        case GLFW_KEY_W -> camera.forward(cameraSpeed);
                        case GLFW_KEY_S -> camera.backward(cameraSpeed);
                        case GLFW_KEY_D -> camera.right(cameraSpeed);
                        case GLFW_KEY_LEFT_SHIFT -> camera.up(cameraSpeed);
                        case GLFW_KEY_LEFT_CONTROL -> camera.down(cameraSpeed);
                    }
                }
                if (action == GLFW_PRESS) {
                    switch (key) {
                        case GLFW_KEY_A -> camera.left(cameraSpeed);
                        case GLFW_KEY_W -> camera.forward(cameraSpeed);
                        case GLFW_KEY_S -> camera.backward(cameraSpeed);
                        case GLFW_KEY_D -> camera.right(cameraSpeed);
                        case GLFW_KEY_P -> per = !per;
                        case GLFW_KEY_F -> flat = !flat;
                        case GLFW_KEY_L -> light = !light;
                        case GLFW_KEY_LEFT_SHIFT -> camera.up(cameraSpeed);
                        case GLFW_KEY_LEFT_CONTROL -> camera.down(cameraSpeed);
                    }
                }
            }
        };
        glfwMouseButtonCallback = new GLFWMouseButtonCallback() {

            @Override
            public void invoke(long window, int button, int action, int mods) {
                DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
                DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
                glfwGetCursorPos(window, xBuffer, yBuffer);
                double x = xBuffer.get(0);
                double y = yBuffer.get(0);

                mouseButton1 = glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == GLFW_PRESS;

                if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {
                    ox = (float) x;
                    oy = (float) y;
                }
            }

        };

        glfwCursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double x, double y) {
                if (mouseButton1) {
                    dx = (float) x - ox;
                    dy = (float) y - oy;
                    ox = (float) x;
                    oy = (float) y;
                    zenit -= dy / width * 180;
                    if (zenit > 90)
                        zenit = 90;
                    if (zenit <= -90)
                        zenit = -90;
                    azimut += dx / height * 180;
                    azimut = azimut % 360;
                    camera.setAzimuth(Math.toRadians(azimut));
                    camera.setZenith(Math.toRadians(zenit));
                    dx = 0;
                    dy = 0;
                }
            }
        };
        glfwScrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double dx, double dy) {
                if (dy > 0) {
                    camera.forward(cameraSpeed);
                } else if (dy < 0) {
                    camera.backward(cameraSpeed);
                }
            }
        };

    }

    @Override
    public void init() {
        super.init();
        glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        try {
            m = OBJLoader.loadModel(new File("src/donutOBJ.obj"));
            mtl = MTLLoader.loadMaterials(new File("src/donutMTL.mtl"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        textures = new ArrayList<>();
        for (String file : mtl.TEXTUREFILE) {
            if (file != null) {
                try {

                    OGLTexture2D texture2D = new OGLTexture2D(file);

                    textures.add(texture2D);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        specTextures = new ArrayList<>();
        for (String file : mtl.TEXTURESPECFILE) {
            if (file != null) {
                try {

                    OGLTexture2D texture2D = new OGLTexture2D(file);

                    specTextures.add(texture2D);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }

        int i = 0;
        for (Face f : m.faces) {
            i++;

            if (mtl.TEXTUREINDEX.contains(mtl.NAME.indexOf(f.materialName))) {
                glActiveTexture(GL_TEXTURE0);
                textures.get(mtl.NAME.indexOf(f.materialName)).bind();
                glNewList(i, GL_COMPILE);
                renderWithTexture(m, f);
                glEndList();
            } else if (mtl.TEXTURESPECINDEX.contains(mtl.NAME.indexOf(f.materialName))) {
                glActiveTexture(GL_TEXTURE1);
                specTextures.get(mtl.NAME.indexOf(f.materialName)).bind();
                glNewList(i, GL_COMPILE);
                renderWithTexture(m, f);
                glEndList();
            } else {
                glNewList(i, GL_COMPILE);
                renderWithoutTexture(m, f);
                glEndList();
            }
        }
        try {
            m2 = OBJLoader.loadModel(new File("src/carOBJ.obj"));
            mtl2 = MTLLoader.loadMaterials(new File("src/carMTL.mtl"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Face f : m2.faces) {
            i++;
            glNewList(i, GL_COMPILE);
            renderWithoutTexture(m2, f);
            glEndList();
        }
        try {
            m3 = OBJLoader.loadModel(new File("src/tableOBJ.obj"));
            mtl3 = MTLLoader.loadMaterials(new File("src/tableMTL.mtl"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        textures3 = new ArrayList<>();
        glActiveTexture(GL_TEXTURE0);
        for (String file : mtl3.TEXTUREFILE) {
            if (file != null) {
                try {
                    OGLTexture2D texture2D = new OGLTexture2D(file);
                    textures3.add(texture2D);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    OGLTexture2D texture2Dtemp = new OGLTexture2D("grass3_cyc.jpg");
                    textures3.add(texture2Dtemp);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        specTextures3 = new ArrayList<>();
        for (String file : mtl3.TEXTURESPECFILE) {
            if (file != null) {
                try {

                    OGLTexture2D texture2D = new OGLTexture2D(file);

                    specTextures3.add(texture2D);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        for (Face f : m3.faces) {
            i++;
            if (mtl3.TEXTUREINDEX.contains(mtl3.NAME.indexOf(f.materialName))) {
                glActiveTexture(GL_TEXTURE0);
                textures3.get(mtl3.NAME.indexOf(f.materialName)).bind();
                glNewList(i, GL_COMPILE);
                renderWithTexture(m3, f);
                glEndList();
            } else if (mtl3.TEXTURESPECINDEX.contains(mtl3.NAME.indexOf(f.materialName))) {

                glActiveTexture(GL_TEXTURE1);
                textures3.get(mtl3.NAME.indexOf(f.materialName)).bind();
                glNewList(i, GL_COMPILE);
                renderWithTexture(m3, f);
                glEndList();
            } else {
                glNewList(i, GL_COMPILE);
                renderWithoutTexture(m3, f);
                glEndList();
            }

        }
        float[] light_dif = new float[]{1, 1, 1, 1};
        float[] light_amb = new float[]{1, 1, 1, 1};
        float[] light_spec = new float[]{1, 1, 1, 1};

        glLightfv(GL_LIGHT0, GL_AMBIENT, light_amb);
        glLightfv(GL_LIGHT0, GL_DIFFUSE, light_dif);
        glLightfv(GL_LIGHT0, GL_SPECULAR, light_spec);


        camera = new GLCamera();
        camera.setPosition(new Vec3D(12, 5, 10));
        gluLookAt(0, 0, 0, 0, 0, 0, 0, 0, 1);
        camera.setFirstPerson(true);
    }

    @Override
    public void display() {
        glViewport(0, 0, width, height);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
        glEnable(GL_TEXTURE_2D);
        glActiveTexture(GL_TEXTURE0);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        camera.setMatrix();
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        if (per)
            gluPerspective(90, width / (float) height, 1f, 1000f);
        else
            glOrtho(-20 * width / (float) height,
                    20 * width / (float) height,
                    -20, 20, 0.00001f, 10000.0f);
        glMatrixMode(GL_MODELVIEW);

        drawAxis();

        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        glTranslatef(10, 10, 25);
        glColor3f(1.0f, 1.0f, 0.0f);
        //glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        glutSolidSphere(1, 10, 10);
        glPopMatrix();

        float[] light_position;
        if (!light) {
            light_position = new float[]{10, 10, 25, 1.0f};
        } else {

            light_position = new float[]{10, 10, 25, 0.0f};
        }
        glLightfv(GL_LIGHT0, GL_POSITION, light_position);
        glFrontFace(GL_CCW);

        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);

        if (flat)
            glShadeModel(GL_FLAT);
        else
            glShadeModel(GL_SMOOTH);

        glEnable(GL_TEXTURE_2D);
        try {
            scene();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        glDisable(GL_LIGHTING);
        glDisable(GL_LIGHT0);

    }

    public void drawAxis() {
        glBegin(GL_LINES);
        glColor3f(1f, 0f, 0f);
        glVertex3f(0f, 0f, 0f);
        glVertex3f(10f, 0f, 0f);

        glColor3f(0f, 1f, 0f);
        glVertex3f(0f, 0f, 0f);
        glVertex3f(0f, 0f, 10f);

        glColor3f(0f, 0f, 1f);
        glVertex3f(0f, 0f, 0f);
        glVertex3f(0f, 11f, 0f);
        glEnd();

    }

    public void scene() throws IOException {

        glPushMatrix();
        glColor3f(1f, 1f, 1f);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glEnable(GL_NORMALIZE);
        glCullFace(GL_BACK);
        glDepthMask(true);


        glLightfv(GL_LIGHT0, GL_POSITION, new float[]{10, 10, 25, 1.0f});
        glEnable(GL_LIGHTING);

        int i = 0;

        for (Face f : m.faces) {
            i++;
            if (mtl.TEXTUREINDEX.contains(mtl.NAME.indexOf(f.materialName))) {
                textures.get(mtl.NAME.indexOf(f.materialName)).bind();
            }
            setMaterials(f.materialName, mtl);
            glCallList(i);
        }

        for (Face f : m2.faces) {
            i++;

            if (f.materialName.equals("Window_Glass")) {
                glEnable(GL_BLEND);
                glDepthMask(false);
                glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
                glMaterialfv(GL_FRONT, GL_AMBIENT, new float[]{0.1f, 0.1f, 0.1f, 0.5f});
                glMaterialfv(GL_FRONT, GL_DIFFUSE, new float[]{0.1f, 0.1f, 0.1f, 0.5f});
                renderWithoutTexture(m2, f);
                glDepthMask(true);
                glDisable(GL_BLEND);
                glEnd();

            } else {

                setMaterials(f.materialName, mtl2);

                glCallList(i);
            }
        }
        for (Face f : m3.faces) {
            i++;
            if (mtl3.TEXTUREINDEX.contains(mtl3.NAME.indexOf(f.materialName))) {
                textures3.get(mtl3.NAME.indexOf(f.materialName)).bind();
                setMaterials(f.materialName, mtl3);
                glCallList(i);
            } else {
                setMaterials(f.materialName, mtl3);
                glCallList(i);
            }
        }

        glEnd();
        glPopMatrix();
    }


    public void setMaterials(String s, Materials mtl) {

        int index;
        index = mtl.NAME.indexOf(s);

        float tempx = (float) mtl.AMB.get(index).getX();
        float tempy = (float) mtl.AMB.get(index).getY();
        float tempz = (float) mtl.AMB.get(index).getZ();


        glMaterialfv(GL_FRONT, GL_AMBIENT, new float[]{tempx, tempy, tempz, 1});

        float tempx2 = (float) mtl.DIV.get(index).getX();
        float tempy2 = (float) mtl.DIV.get(index).getY();
        float tempz2 = (float) mtl.DIV.get(index).getZ();
        glMaterialfv(GL_FRONT, GL_DIFFUSE, new float[]{tempx2, tempy2, tempz2, 1});

        float tempx3 = (float) mtl.SPEC.get(index).getX();
        float tempy3 = (float) mtl.SPEC.get(index).getY();
        float tempz3 = (float) mtl.SPEC.get(index).getZ();

        glMaterialfv(GL_FRONT, GL_SPECULAR, new float[]{tempx3, tempy3, tempz3, 1});

        glMaterialfv(GL_FRONT, GL_EMISSION, new float[]{0f, 0f, 0f, 0f});
        glMaterialf(GL_FRONT_AND_BACK, GL_ALPHA, 1.0f);
        glMaterialf(GL_FRONT, GL_SHININESS, mtl.SHININES.get(index));
    }


    public void renderWithoutTexture(Model m, Face f) {
        glBegin(GL_TRIANGLES);
        Vec3D n1 = m.normals.get((int) f.normal.getX() - 1);
        glNormal3d(n1.getX(), n1.getY(), n1.getZ());
        Vec3D v1 = m.vertices.get((int) f.vertex.getX() - 1);
        glVertex3d(v1.getX(), v1.getY(), v1.getZ());

        Vec3D n2 = m.normals.get((int) f.normal.getY() - 1);
        glNormal3d(n2.getX(), n2.getY(), n2.getZ());
        Vec3D v2 = m.vertices.get((int) f.vertex.getY() - 1);
        glVertex3d(v2.getX(), v2.getY(), v2.getZ());

        Vec3D n3 = m.normals.get((int) f.normal.getZ() - 1);
        glNormal3d(n3.getX(), n3.getY(), n3.getZ());
        Vec3D v3 = m.vertices.get((int) f.vertex.getZ() - 1);
        glVertex3d(v3.getX(), v3.getY(), v3.getZ());
        glEnd();
    }

    public void renderWithTexture(Model m, Face f) {
        glBegin(GL_TRIANGLES);
        Vec2D t1 = m.textures.get((int) f.texture.getX() - 1);
        glTexCoord2d(t1.getX(), t1.getY());
        Vec3D n1 = m.normals.get((int) f.normal.getX() - 1);
        glNormal3d(n1.getX(), n1.getY(), n1.getZ());
        Vec3D v1 = m.vertices.get((int) f.vertex.getX() - 1);
        glVertex3d(v1.getX(), v1.getY(), v1.getZ());
        Vec2D t2 = m.textures.get((int) f.texture.getY() - 1);
        glTexCoord2d(t2.getX(), t2.getY());
        Vec3D n2 = m.normals.get((int) f.normal.getY() - 1);
        glNormal3d(n2.getX(), n2.getY(), n2.getZ());
        Vec3D v2 = m.vertices.get((int) f.vertex.getY() - 1);
        glVertex3d(v2.getX(), v2.getY(), v2.getZ());
        Vec2D t3 = m.textures.get((int) f.texture.getZ() - 1);
        glTexCoord2d(t3.getX(), t3.getY());
        Vec3D n3 = m.normals.get((int) f.normal.getZ() - 1);
        glNormal3d(n3.getX(), n3.getY(), n3.getZ());
        Vec3D v3 = m.vertices.get((int) f.vertex.getZ() - 1);
        glVertex3d(v3.getX(), v3.getY(), v3.getZ());
        glEnd();
    }

}