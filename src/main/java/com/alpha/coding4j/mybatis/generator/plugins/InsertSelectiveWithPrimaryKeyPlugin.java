package com.alpha.coding4j.mybatis.generator.plugins;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import org.mybatis.generator.runtime.dynamic.sql.elements.AbstractMethodGenerator;
import org.mybatis.generator.runtime.dynamic.sql.elements.MethodAndImports;

/**
 * InsertSelectiveWithPrimaryKeyPlugin
 *
 * @version 1.0
 * Date: 2020-03-10
 */
public class InsertSelectiveWithPrimaryKeyPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean clientInsertSelectiveMethodGenerated(Method method, Interface interfaze,
                                                        IntrospectedTable introspectedTable) {
        final boolean ret = super.clientInsertSelectiveMethodGenerated(method, interfaze, introspectedTable);
        if (ret) {
            addMethodToInterface(interfaze, introspectedTable);
        }
        return ret;
    }

    private void addMethodToInterface(Interface interfaze, IntrospectedTable introspectedTable) {
        final MethodAndImports methodAndImports = generateMethodAndImports(introspectedTable);
        Optional.ofNullable(methodAndImports.getImports()).ifPresent(interfaze::addImportedTypes);
        Optional.ofNullable(methodAndImports.getStaticImports()).ifPresent(interfaze::addStaticImports);
        Optional.ofNullable(methodAndImports.getMethod()).ifPresent(interfaze::addMethod);
    }

    private MethodAndImports generateMethodAndImports(IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType recordType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());

        Set<FullyQualifiedJavaType> imports = new HashSet<>();

        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils"));
        imports.add(recordType);

        Method method = new Method("insertSelectiveWithPrimaryKey");
        method.setDefault(true);
        context.getCommentGenerator().addGeneralMethodAnnotation(method, introspectedTable, imports);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addParameter(new Parameter(recordType, "row"));
        String tableFieldName =
                JavaBeansUtil.getValidPropertyName(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        method.addBodyLine("return MyBatis3Utils.insert(this::insert, row, " + tableFieldName + ", c ->");
        List<IntrospectedColumn> columns =
                ListUtilities.removeGeneratedAlwaysColumns(introspectedTable.getAllColumns());
        boolean first = true;
        for (IntrospectedColumn column : columns) {
            String fieldName = AbstractMethodGenerator.calculateFieldName(tableFieldName, column);
            if (column.isSequenceColumn()) {
                if (first) {
                    method.addBodyLine("    c.map(" + fieldName
                            + ").toProperty(\"" + column.getJavaProperty()
                            + "\")");
                    first = false;
                } else {
                    method.addBodyLine("    .map(" + fieldName
                            + ").toProperty(\"" + column.getJavaProperty()
                            + "\")");
                }
            } else {
                String methodName = JavaBeansUtil.getGetterMethodName(column.getJavaProperty(),
                        column.getFullyQualifiedJavaType());
                if (first) {
                    method.addBodyLine("    c.map(" + fieldName
                            + ").toPropertyWhenPresent(\"" + column.getJavaProperty()
                            + "\", row::" + methodName
                            + ")");
                    first = false;
                } else {
                    method.addBodyLine("    .map(" + fieldName
                            + ").toPropertyWhenPresent(\"" + column.getJavaProperty()
                            + "\", row::" + methodName
                            + ")");
                }
            }
        }

        method.addBodyLine(");");

        return MethodAndImports.withMethod(method)
                .withImports(imports)
                .build();
    }

}
