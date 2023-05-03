package com.otaliastudios.cameraview.filters;

import android.opengl.GLES20;

import androidx.annotation.NonNull;

import com.otaliastudios.cameraview.filter.BaseFilter;
import com.otaliastudios.opengl.core.Egloo;

/**
 * Inverts the input colors. This is also known as negative effect.
 */
public class GroupFilter extends BaseFilter {
    private static final String FRAGMENT_SHADER = "#extension GL_OES_EGL_image_external : require\n" +
            "precision mediump float;" +

            "varying highp vec2 vTextureCoord;" +
            "uniform lowp samplerExternalOES sTexture;" +

            "uniform highp vec2 center;" +
            "uniform highp float radius;" +
            "uniform highp float scale;" +

            "void main() {" +
            "highp vec2 textureCoordinateToUse = vTextureCoord;" +
            "highp float dist = distance(center, vTextureCoord);" +
            "textureCoordinateToUse -= center;" +
            "if (dist < radius) {" +
            "highp float percent = 1.0 - ((radius - dist) / radius) * scale;" +
            "percent = percent * percent;" +
            "textureCoordinateToUse = textureCoordinateToUse * percent;" +
            "}" +
            "textureCoordinateToUse += center;" +
            "vec4 tex = texture2D(sTexture, textureCoordinateToUse);" +
            "vec4 color = tex;" +
            "float colorR = (1.0 - color.r) / 1.0;" +
            "float colorG = (1.0 - color.g) / 1.0;" +
            "float colorB = (1.0 - color.b) / 1.0;" +
            "vec4 finalCol = vec4(colorR, colorG, colorB, color.a);" +
            "gl_FragColor = finalCol;" +
            "}";

    private final float centerX = 0.45f;
    private final float centerY = 0.5f;
    //    private final float radius = 0.0f;
//    private final float scale = 0.0f;
    private final float radius = 0.35f;
    private final float scale = 0.45f;

    private int locationCenter = -1;
    private int locationRadius = -1;
    private int locationScale = -1;

    public GroupFilter() {
    }

    @NonNull
    @Override
    public String getFragmentShader() {
        return FRAGMENT_SHADER;
    }

    @Override
    public void onCreate(int programHandle) {
        super.onCreate(programHandle);
        locationCenter = GLES20.glGetUniformLocation(programHandle, "center");
        locationRadius = GLES20.glGetUniformLocation(programHandle, "radius");
        locationScale = GLES20.glGetUniformLocation(programHandle, "scale");


        Egloo.checkGlProgramLocation(locationCenter, "center");
        Egloo.checkGlProgramLocation(locationRadius, "radius");
        Egloo.checkGlProgramLocation(locationScale, "scale");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationCenter = -1;
        locationRadius = -1;
        locationScale = -1;
    }

    @Override
    protected void onPreDraw(long timestampUs, @NonNull float[] transformMatrix) {
        super.onPreDraw(timestampUs, transformMatrix);
        GLES20.glUniform2f(locationCenter, centerX, centerY);
        GLES20.glUniform1f(locationRadius, radius);
        GLES20.glUniform1f(locationScale, scale);
        Egloo.checkGlError("glUniform1f");
    }
}
