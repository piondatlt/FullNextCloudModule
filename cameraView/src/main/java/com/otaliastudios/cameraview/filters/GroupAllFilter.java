package com.otaliastudios.cameraview.filters;

import android.opengl.GLES20;

import androidx.annotation.NonNull;

import com.otaliastudios.cameraview.filter.BaseFilter;
import com.otaliastudios.cameraview.filter.OneParameterFilter;
import com.otaliastudios.cameraview.filter.ThreeParameterFilter;
import com.otaliastudios.cameraview.filter.TwoParameterFilter;
import com.otaliastudios.opengl.core.Egloo;

/**
 * Inverts the input colors. This is also known as negative effect.
 */
public class GroupAllFilter extends BaseFilter implements TwoParameterFilter {
    private static final String FRAGMENT_SHADER = "#extension GL_OES_EGL_image_external : require\n" +
            "precision mediump float;\n" +
            "varying highp vec2 vTextureCoord;\n" +
            "uniform lowp samplerExternalOES sTexture;\n" +
            "uniform highp vec2 center;\n" +
            "uniform highp float radius;\n" +
            "uniform highp float scale;\n" +
            "\n" +
            "uniform float stepsizeX;\n" +
            "uniform float stepsizeY;\n" +
            "uniform float scaleSharp;\n" +
            "\n" +
            "uniform float isInvert;\n" +
            "\n" +
            "\n" +
            "void main() {\n" +
            "\t\n" +
            "\tvec4 fishEyeFilter = texture2D(sTexture, vTextureCoord);\n" +
            "\thighp vec2 textureCoordinateToUse = vTextureCoord;\n" +
            "\tif(radius > 0.0){\n" +
            "\t\t//fish eye\n" +
            "\t    highp float dist = distance(center, vTextureCoord);\n" +
            "\t    textureCoordinateToUse -= center;\n" +
            "\t    if (dist < radius) {\n" +
            "\t\t    highp float percent = 1.0 - ((radius - dist) / radius) * scale;\n" +
            "\t\t    percent = percent * percent;\n" +
            "\t\t    textureCoordinateToUse = textureCoordinateToUse * percent;\n" +
            "\t    }\n" +
            "\t    textureCoordinateToUse += center;\n" +
            "\t    fishEyeFilter = texture2D(sTexture, textureCoordinateToUse);\n" +
            "\t}\n" +
            "\n" +
            "\t\n" +
            "\t//sharpen\n" +
            "\tvec4 fishEyeAndSharpenFilter = fishEyeFilter;\n" +
            "\tif(scaleSharp > 0.0){\n" +
            "\t    vec3 nbr_color = vec3(0.0, 0.0, 0.0);\n" +
            "\t    vec2 coord;\n" +
            "\t    vec4 color = fishEyeFilter;\n" +
            "\t    coord.x = textureCoordinateToUse.x - 0.5 * stepsizeX;\n" +
            "\t    coord.y = textureCoordinateToUse.y - stepsizeY;\n" +
            "\t    nbr_color += texture2D(sTexture, coord).rgb - color.rgb;\n" +
            "\t    coord.x = textureCoordinateToUse.x - stepsizeX;\n" +
            "\t    coord.y = textureCoordinateToUse.y + 0.5 * stepsizeY;\n" +
            "\t    nbr_color += texture2D(sTexture, coord).rgb - color.rgb;\n" +
            "\t    coord.x = textureCoordinateToUse.x + stepsizeX;\n" +
            "\t    coord.y = textureCoordinateToUse.y - 0.5 * stepsizeY;\n" +
            "\t    nbr_color += texture2D(sTexture, coord).rgb - color.rgb;\n" +
            "\t    coord.x = textureCoordinateToUse.x + stepsizeX;\n" +
            "\t    coord.y = textureCoordinateToUse.y + 0.5 * stepsizeY;\n" +
            "\t    nbr_color += texture2D(sTexture, coord).rgb - color.rgb;\n" +
            "\t    fishEyeAndSharpenFilter = vec4(color.rgb - 2.0 * scaleSharp * nbr_color, color.a);\n" +
            "\t}\n" +
            "\n" +
            "\t\n" +
            "\t\n" +
            "\t\n" +
            "\t//invert\n" +
            "\tvec4 finalFilter = fishEyeAndSharpenFilter;\n" +
            "\tif(isInvert > 0.0){\n" +
            "\t    float colorR = (1.0 - fishEyeAndSharpenFilter.r) / 1.0;\n" +
            "\t    float colorG = (1.0 - fishEyeAndSharpenFilter.g) / 1.0;\n" +
            "\t    float colorB = (1.0 - fishEyeAndSharpenFilter.b) / 1.0;\n" +
            "\t    finalFilter = vec4(colorR, colorG, colorB, fishEyeAndSharpenFilter.a);\n" +
            "\t}\n" +
            "\n" +
            "\t\n" +
            "\t\n" +
            "\t\n" +
            "\t\n" +
            "\t//output\n" +
            "\tgl_FragColor = finalFilter;\n" +
            "}";
    private final float centerX = 0.45f;
    private final float centerY = 0.5f;
    //    private final float radius = 0.0f;
//    private final float scale = 0.0f;
    private static float radius = 0.35f;
    private final float scale = 0.45f;

    private int locationCenter = -1;
    private int locationRadius = -1;
    private int locationScale = -1;


    private float scaleSharp = 0.5f;
    private int width = 1;
    private int height = 1;
    private int scaleLocation = -1;
    private int stepSizeXLocation = -1;
    private int stepSizeYLocation = -1;

    private float isInvertOn = 0;

    private int isInvertOnLocation = 0;


    public GroupAllFilter(){

    }
    public GroupAllFilter(int progress , boolean isInvertOn ,boolean isFishEyeOn) {
        this.scaleSharp = (float) progress / 100;
        if (isInvertOn){
            this.isInvertOn = 1;
        }else {
            this.isInvertOn = 0;
        }
        if (isFishEyeOn){
            this.radius = 0.35f;
        }else {
            this.radius = 0f;
        }
    }

    public void setSharpness(int progress){
        float value = (float)progress / 100;
        if (value < 0.0f) value = 0.0f;
        if (value > 1.0f) value = 1.0f;
        this.scaleSharp = value;
    }

    public void setInvert(float value){
        this.isInvertOn = value;
    }

    public void setInvert(boolean isInvertOn){
        if (isInvertOn){
            this.isInvertOn = 1f;
        }else {
            this.isInvertOn = 0f;
        }
    }

    public void setFishEye(boolean isFishEyeOn){
        if (isFishEyeOn){
            this.radius = 0.35f;
        }else {
            this.radius = 0f;
        }
    }


    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        this.width = width;
        this.height = height;
    }

    public void setSharpness(float value) {
        if (value < 0.0f) value = 0.0f;
        if (value > 1.0f) value = 1.0f;
        this.scaleSharp = value;
    }

    public float getSharpness() {
        return scaleSharp;
    }

    public float getInvert() {
        return isInvertOn;
    }

    public float getRadiusFisheye(){
        return radius;
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

        scaleLocation = GLES20.glGetUniformLocation(programHandle, "scaleSharp");
        stepSizeXLocation = GLES20.glGetUniformLocation(programHandle, "stepsizeX");
        stepSizeYLocation = GLES20.glGetUniformLocation(programHandle, "stepsizeY");
        Egloo.checkGlProgramLocation(scaleLocation, "scaleSharp");
        Egloo.checkGlProgramLocation(stepSizeXLocation, "stepsizeX");
        Egloo.checkGlProgramLocation(stepSizeYLocation, "stepsizeY");

        isInvertOnLocation = GLES20.glGetUniformLocation(programHandle, "isInvert");
        Egloo.checkGlProgramLocation(isInvertOnLocation, "isInvert");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationCenter = -1;
        locationRadius = -1;
        locationScale = -1;

        scaleLocation = -1;
        stepSizeXLocation = -1;
        stepSizeYLocation = -1;

    }

    @Override
    protected void onPreDraw(long timestampUs, @NonNull float[] transformMatrix) {
        super.onPreDraw(timestampUs, transformMatrix);
        GLES20.glUniform2f(locationCenter, centerX, centerY);
        GLES20.glUniform1f(locationRadius, radius);
        GLES20.glUniform1f(locationScale, scale);

        GLES20.glUniform1f(scaleLocation, scaleSharp);
        Egloo.checkGlError("glUniform1f");
        GLES20.glUniform1f(stepSizeXLocation, 1.0F / width);
        Egloo.checkGlError("glUniform1f");
        GLES20.glUniform1f(stepSizeYLocation, 1.0F / height);
        Egloo.checkGlError("glUniform1f");

        GLES20.glUniform1f(isInvertOnLocation, isInvertOn);

    }

    @Override
    public void setParameter1(float value) {
        setSharpness(value);
    }

    @Override
    public float getParameter1() {
        return getSharpness();
    }

    @Override
    public void setParameter2(float value) {
        setInvert(value);
    }

    @Override
    public float getParameter2() {
        return getInvert();
    }
}
