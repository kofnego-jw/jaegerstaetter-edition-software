package at.kulinz.jaegerstaetter.pdfgenerator.service;

import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import java.io.File;

public class PdfGeneratorConstants {
    public static final String FONTS_RESOURCES_PATTERN = "/fonts/*";

    private static void doRegister(File f, String family, int weight, BaseRendererBuilder.FontStyle fontStyle, boolean subset, PdfRendererBuilder builder) {
        // System.out.println("registering..." + f.getName());
        builder.useFont(f, family, weight, fontStyle, subset);
    }

    public static void registerFont(File fontFile, PdfRendererBuilder builder) {
        switch (fontFile.getName()) {
            // Montserrat
            case "Montserrat-Thin.ttf" ->
                    doRegister(fontFile, "Montserrat", 100, BaseRendererBuilder.FontStyle.NORMAL, true, builder);
            case "Montserrat-ThinItalic.ttf" ->
                    doRegister(fontFile, "Montserrat", 100, BaseRendererBuilder.FontStyle.ITALIC, true, builder);
            case "Montserrat-ExtraLight.ttf" ->
                    doRegister(fontFile, "Montserrat", 200, BaseRendererBuilder.FontStyle.NORMAL, true, builder);
            case "Montserrat-ExtraLightItalic.ttf" ->
                    doRegister(fontFile, "Montserrat", 200, BaseRendererBuilder.FontStyle.ITALIC, true, builder);
            case "Montserrat-Light.ttf" ->
                    doRegister(fontFile, "Montserrat", 300, BaseRendererBuilder.FontStyle.NORMAL, true, builder);
            case "Montserrat-LightItalic.ttf" ->
                    doRegister(fontFile, "Montserrat", 300, BaseRendererBuilder.FontStyle.ITALIC, true, builder);
            case "Montserrat-Regular.ttf" ->
                    doRegister(fontFile, "Montserrat", 400, BaseRendererBuilder.FontStyle.NORMAL, true, builder);
            case "Montserrat-Italic.ttf" ->
                    doRegister(fontFile, "Montserrat", 400, BaseRendererBuilder.FontStyle.ITALIC, true, builder);
            case "Montserrat-Medium.ttf" ->
                    doRegister(fontFile, "Montserrat", 500, BaseRendererBuilder.FontStyle.NORMAL, true, builder);
            case "Montserrat-MediumItalic.ttf" ->
                    doRegister(fontFile, "Montserrat", 500, BaseRendererBuilder.FontStyle.ITALIC, true, builder);
            case "Montserrat-SemiBold.ttf" ->
                    doRegister(fontFile, "Montserrat", 600, BaseRendererBuilder.FontStyle.NORMAL, true, builder);
            case "Montserrat-SemiBoldItalic.ttf" ->
                    doRegister(fontFile, "Montserrat", 600, BaseRendererBuilder.FontStyle.ITALIC, true, builder);
            case "Montserrat-Bold.ttf" ->
                    doRegister(fontFile, "Montserrat", 700, BaseRendererBuilder.FontStyle.NORMAL, true, builder);
            case "Montserrat-BoldItalic.ttf" ->
                    doRegister(fontFile, "Montserrat", 700, BaseRendererBuilder.FontStyle.ITALIC, true, builder);
            case "Montserrat-ExtraBold.ttf" ->
                    doRegister(fontFile, "Montserrat", 800, BaseRendererBuilder.FontStyle.NORMAL, true, builder);
            case "Montserrat-ExtraBoldItalic.ttf" ->
                    doRegister(fontFile, "Montserrat", 800, BaseRendererBuilder.FontStyle.ITALIC, true, builder);
            case "Montserrat-Black.ttf" ->
                    doRegister(fontFile, "Montserrat", 900, BaseRendererBuilder.FontStyle.NORMAL, true, builder);
            case "Montserrat-BlackItalic.ttf" ->
                    doRegister(fontFile, "Montserrat", 900, BaseRendererBuilder.FontStyle.ITALIC, true, builder);
            // Roboto Slab
            case "RobotoSlab-Thin.ttf" ->
                    doRegister(fontFile, "Roboto Slab", 100, BaseRendererBuilder.FontStyle.NORMAL, true, builder);
            case "RobotoSlab-ExtraLight.ttf" ->
                    doRegister(fontFile, "Roboto Slab", 200, BaseRendererBuilder.FontStyle.NORMAL, true, builder);
            case "RobotoSlab-Light.ttf" ->
                    doRegister(fontFile, "Roboto Slab", 300, BaseRendererBuilder.FontStyle.NORMAL, true, builder);
            case "RobotoSlab-Regular.ttf" ->
                    doRegister(fontFile, "Roboto Slab", 400, BaseRendererBuilder.FontStyle.NORMAL, true, builder);
            case "RobotoSlab-Medium.ttf" ->
                    doRegister(fontFile, "Roboto Slab", 500, BaseRendererBuilder.FontStyle.NORMAL, true, builder);
            case "RobotoSlab-SemiBold.ttf" ->
                    doRegister(fontFile, "Roboto Slab", 600, BaseRendererBuilder.FontStyle.NORMAL, true, builder);
            case "RobotoSlab-Bold.ttf" ->
                    doRegister(fontFile, "Roboto Slab", 700, BaseRendererBuilder.FontStyle.NORMAL, true, builder);
            case "RobotoSlab-ExtraBold.ttf" ->
                    doRegister(fontFile, "Roboto Slab", 800, BaseRendererBuilder.FontStyle.NORMAL, true, builder);
            case "RobotoSlab-Black.ttf" ->
                    doRegister(fontFile, "Roboto Slab", 900, BaseRendererBuilder.FontStyle.NORMAL, true, builder);
            // Roboto
            case "Roboto-Thin.ttf" ->
                    doRegister(fontFile, "Roboto", 100, BaseRendererBuilder.FontStyle.NORMAL, true, builder);
            case "Roboto-ThinItalic.ttf" ->
                    doRegister(fontFile, "Roboto", 100, BaseRendererBuilder.FontStyle.ITALIC, true, builder);
            case "Roboto-Light.ttf" ->
                    doRegister(fontFile, "Roboto", 300, BaseRendererBuilder.FontStyle.NORMAL, true, builder);
            case "Roboto-LightItalic.ttf" ->
                    doRegister(fontFile, "Roboto", 300, BaseRendererBuilder.FontStyle.ITALIC, true, builder);
            case "Roboto-Regular.ttf" ->
                    doRegister(fontFile, "Roboto", 400, BaseRendererBuilder.FontStyle.NORMAL, true, builder);
            case "Roboto-Italic.ttf" ->
                    doRegister(fontFile, "Roboto", 400, BaseRendererBuilder.FontStyle.ITALIC, true, builder);
            case "Roboto-Medium.ttf" ->
                    doRegister(fontFile, "Roboto", 500, BaseRendererBuilder.FontStyle.NORMAL, true, builder);
            case "Roboto-MediumItalic.ttf" ->
                    doRegister(fontFile, "Roboto", 500, BaseRendererBuilder.FontStyle.ITALIC, true, builder);
            case "Roboto-Bold.ttf" ->
                    doRegister(fontFile, "Roboto", 700, BaseRendererBuilder.FontStyle.NORMAL, true, builder);
            case "Roboto-BoldItalic.ttf" ->
                    doRegister(fontFile, "Roboto", 700, BaseRendererBuilder.FontStyle.ITALIC, true, builder);
            case "Roboto-Black.ttf" ->
                    doRegister(fontFile, "Roboto", 900, BaseRendererBuilder.FontStyle.NORMAL, true, builder);
            case "Roboto-BlackItalic.ttf" ->
                    doRegister(fontFile, "Roboto", 900, BaseRendererBuilder.FontStyle.ITALIC, true, builder);
            default -> throw new RuntimeException("Cannot register font.");

        }

    }

}
