

package ben.study.codesnippets;

import ben.study.flappy.Main;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import static org.lwjgl.opengl.GL13.*;


public class MainSkeleton implements Runnable {
    private int width = 1280;
    private int height = 720;

    private Thread thread;
    private boolean running = false;

    private long glfwWindow;


    public void start() {
        running = true;
        thread = new Thread(this, "Game");
        thread.start();
    }

    private void init() {
        if (!glfwInit()) {
            System.err.println("Could not initialize GLFW!");
            return;
        }

        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        glfwWindow = glfwCreateWindow(width, height, "Flappy", NULL, NULL);
        if (glfwWindow == NULL) {
            System.err.println("Could not create GLFW window!");
            return;
        }

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        glfwSetWindowPos(
                glfwWindow,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        // Enable v-sync
        glfwSwapInterval(1);

        glfwShowWindow(glfwWindow);
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);
        glActiveTexture(GL_TEXTURE1);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        System.out.println("OpenGL: " + glGetString(GL_VERSION));

    }

    public void run() {
        init();
        while (running) {
            update();
            render();
            if (glfwWindowShouldClose(glfwWindow))
                running = false;
        }

        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
    }


    private void update() {
        glfwPollEvents();
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        int error = glGetError();
        if (error != GL_NO_ERROR)
            System.out.println(error);

        glfwSwapBuffers(glfwWindow);
    }

    public static void main(String[] args) {
        new Main().start();
    }

}
