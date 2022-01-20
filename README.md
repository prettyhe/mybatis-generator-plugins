MyBatis Generator Plugins
====================
Set of plugins for the mybatis-generator to further tweak the generated code.

## ModelAddAnnotationPlugin
```xml
<plugin type="com.alpha.coding4j.mybatis.generator.plugins.ModelAddAnnotationPlugin">
	<property name="annotationClass" value="lombok.ToString" />
	<property name="annotationValue" value="@ToString(callSuper = true)" />
</plugin>
```

## ClientAddAnnotationPlugin
```xml
<plugin type="com.alpha.coding4j.mybatis.generator.plugins.ClientAddAnnotationPlugin">
	<property name="annotationClass" value="com.example.annotation.FooAnna" />
	<property name="annotationValue" value="@FooAnna" />
</plugin>
```

## ModelAddChainConsumePlugin
```xml
<plugin type="com.alpha.coding4j.mybatis.generator.plugins.ModelAddChainConsumePlugin" />
```

## ModelFieldRemarkCommentPlugin
```xml
<plugin type="com.alpha.coding4j.mybatis.generator.plugins.ModelFieldRemarkCommentPlugin" />
```

## InsertOrUpdatePlugin
```xml
<plugin type="com.alpha.coding4j.mybatis.generator.plugins.InsertOrUpdatePlugin" >
    <property name="enableTables" value="table_a_name,table_b_name" />
</plugin>
```

## InsertSelectiveWithPrimaryKeyPlugin
```xml
<plugin type="com.alpha.coding4j.mybatis.generator.plugins.InsertSelectiveWithPrimaryKeyPlugin" />
```

## DisableGetterSetterMethodsPlugin
```xml
<plugin type="com.alpha.coding4j.mybatis.generator.plugins.DisableGetterSetterMethodsPlugin" >
    <property name="disableGetter" value="true"/>
    <property name="disableSetter" value="true"/>
</plugin>
```

## SelectMapListPlugin
```xml
<plugin type="com.alpha.coding4j.mybatis.generator.plugins.SelectMapListPlugin" />
```

## SelectByStatementPlugin
```xml
<plugin type="com.alpha.coding4j.mybatis.generator.plugins.SelectByStatementPlugin" />
```

## CountByStatementPlugin
```xml
<plugin type="com.alpha.coding4j.mybatis.generator.plugins.CountByStatementPlugin" />
```

## domainObjectNameSuffix
```xml
<!-- 实体类重命名规则:尾部统一添加Entity -->
<property name="domainObjectNameSuffix" value="Entity" />
<property name="domainObjectNameSuffixExcludeTables" value="table_name" />
```
