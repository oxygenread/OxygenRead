package com.dragonlz.oxygenread.UI.Model;

/**
 * Created by Dragon丶Lz on 2015/8/11.
 */
public class Function {

    private String name;

    private int imageId;

    public Function(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
