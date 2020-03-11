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
