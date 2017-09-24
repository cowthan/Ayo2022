package org.ayo.editor.emoj.model;

import java.util.List;

/**
 * Created by hujinghui on 16/4/14.
 */
public class EmojiCategoryModel {
    public List<EmojiModel> data;
    public int pkgId;
    public int type;
    public int scenario;
    public String icon;
    public String name;

    public List<EmojiModel> getData() {
        return data;
    }

    public void setData(List<EmojiModel> data) {
        this.data = data;
    }

    public int getPkgId() {
        return pkgId;
    }

    public void setPkgId(int pkgId) {
        this.pkgId = pkgId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getScenario() {
        return scenario;
    }

    public void setScenario(int scenario) {
        this.scenario = scenario;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
