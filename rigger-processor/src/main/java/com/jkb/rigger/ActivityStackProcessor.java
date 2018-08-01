package com.jkb.rigger;

import com.google.auto.service.AutoService;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * The Activity Stack processor powered by APT and JavaPoet.
 *
 * <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 * <a href="https://github.com/justkiddingbaby">Github</a>
 * <a href="http://blog.justkiddingbaby.com">Blog</a>
 *
 * @since Aug 1,2018
 */
@SupportedAnnotationTypes("com.jkb.fragment.swiper.annotation.Swiper")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class ActivityStackProcessor extends AbstractProcessor {

    private static final String CLASS_NAME_SWIPE = "com.jkb.fragment.swiper.annotation.Swiper";
    private static final String CLASS_NAME_ACTIVITY = "android.app.Activity";

    private Filer filer;
    private Elements elements;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        System.out.println("ActivityStackProcessor init");
        filer = processingEnvironment.getFiler();
        elements = processingEnvironment.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.println("ActivityStackProcessor process");
        try {
            Class<?> swipeClass = Class.forName(CLASS_NAME_SWIPE);
            List<TypeElement> typeElements = new ArrayList<>();
            Set<? extends Element> puppetElements = roundEnvironment
                .getElementsAnnotatedWith((Class<? extends Annotation>) swipeClass);
            for (Element element : puppetElements) {
                ElementKind kind = element.getKind();
                if (kind != ElementKind.CLASS) {
                    System.out.println("Puppet Annotation can only used on class");
                    return false;
                }
                if (element instanceof TypeElement) {
                    typeElements.add((TypeElement) element);
                }
            }
            return generateActivityStackManager(typeElements);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean generateActivityStackManager(List<TypeElement> typeElements) {
        if (typeElements == null || typeElements.isEmpty()) {
            System.out.println("@HelloProcessor is not found!!!");
            return false;
        }
        try {
            Class<?> activityClass = Class.forName(CLASS_NAME_ACTIVITY);
            for (TypeElement element : typeElements) {
                String qualifedName = element.getQualifiedName().toString();
                Class<?> targetClass = Class.forName(qualifedName);
                System.out.println("found class marked by @Swiper : " + targetClass);
                if (!targetClass.isAssignableFrom(activityClass)) {
                    System.out.println("filter class not extends Activity : " + targetClass);
                    continue;
                }

            }
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
