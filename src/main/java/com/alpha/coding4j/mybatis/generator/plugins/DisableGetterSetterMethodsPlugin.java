package com.alpha.coding4j.mybatis.generator.plugins;

import java.util.List;
import java.util.Optional;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * DisableGetterSetterMethodsPlugin
 *
 * @version 1.0
 * Date: 2020-03-11
 */
public class DisableGetterSetterMethodsPlugin extends PluginAdapter {

    private static final String DISABLE_GETTER = "disableGetter";
    private static final String DISABLE_SETTER = "disableSetter";

    private boolean disableGetter;
    private boolean disableSetter;

    @Override
    public boolean validate(List<String> warnings) {
        disableGetter = Optional.ofNullable(properties.getProperty(DISABLE_GETTER))
                .filter(StringUtility::stringHasValue)
                .filter(StringUtility::isTrue).isPresent();
        disableSetter = Optional.ofNullable(properties.getProperty(DISABLE_SETTER))
                .filter(StringUtility::stringHasValue)
                .filter(StringUtility::isTrue).isPresent();
        return disableGetter || disableSetter;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass,
                                              IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return !disableGetter;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method,
                                              TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable,
                                              ModelClassType modelClassType) {
        return !disableSetter;
    }

}
