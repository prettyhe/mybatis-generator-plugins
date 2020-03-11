package com.alpha.coding4j.mybatis.generator.plugins;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * ModelAddAnnotationPlugin
 *
 * @version 1.0
 * Date: 2020-03-07
 */
public class ModelAddAnnotationPlugin extends PluginAdapter {

    public static final String ANNOTATION_CLASS = "annotationClass";
    public static final String ANNOTATION_VALUE = "annotationValue";

    private String annotationClass;
    private String annotationValue;

    public String getAnnotationClass() {
        return annotationClass;
    }

    public String getAnnotationValue() {
        return annotationValue;
    }

    @Override
    public boolean validate(List<String> warnings) {
        annotationClass = properties.getProperty(ANNOTATION_CLASS);
        annotationValue = properties.getProperty(ANNOTATION_VALUE);

        boolean valid = stringHasValue(annotationClass) && stringHasValue(annotationValue);

        if (!valid) {
            if (!stringHasValue(annotationClass)) {
                warnings.add(String.format("%s requires the %s property",
                        this.getClass().getSimpleName(), ANNOTATION_CLASS));
            }
            if (!stringHasValue(annotationValue)) {
                warnings.add(String.format("%s requires the %s property",
                        this.getClass().getSimpleName(), ANNOTATION_VALUE));
            }
        }

        return valid;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        for (String className : annotationClass.split(",")) {
            topLevelClass.addImportedType(new FullyQualifiedJavaType(className));
        }
        topLevelClass.addAnnotation(annotationValue);
        return true;
    }

}
