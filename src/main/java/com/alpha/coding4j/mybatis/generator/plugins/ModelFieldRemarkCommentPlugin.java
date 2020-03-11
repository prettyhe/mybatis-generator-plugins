package com.alpha.coding4j.mybatis.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * ModelFieldRemarkCommentPlugin
 *
 * @version 1.0
 * Date: 2020-03-09
 */
public class ModelFieldRemarkCommentPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                       IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        final boolean generated = super.modelFieldGenerated(field, topLevelClass, introspectedColumn,
                introspectedTable, modelClassType);
        field.addJavaDocLine("/**");
        field.addJavaDocLine(" * " + introspectedColumn.getRemarks());
        field.addJavaDocLine(" */");
        return generated;
    }
}
