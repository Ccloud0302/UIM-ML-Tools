package com.markerhub.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class QAEntityItem implements Serializable{
	private long uuid;
	private String name;//显示名称
	private String type;
	private String modelVersion;
	private String code;
	private String alias;
	private String namespace;
	private String language;
	private boolean translated;
	private boolean extendable;

	private String color;//对应关系数据库字段
	private Integer r;

	public String getModelVersion() {
		return modelVersion;
	}

	public void setModelVersion(String modelVersion) {
		this.modelVersion = modelVersion;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public boolean isTranslated() {
		return translated;
	}

	public void setTranslated(boolean translated) {
		this.translated = translated;
	}

	public boolean isExtendable() {
		return extendable;
	}

	public void setExtendable(boolean extendable) {
		this.extendable = extendable;
	}


	/*private String x;
	private String y;*/
	public long getUuid() {
		return uuid;
	}
	public void setUuid(long uuid) {
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Integer getR() {
		return r;
	}
	public void setR(Integer r) {
		this.r = r;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	/*public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}*/
	
}
