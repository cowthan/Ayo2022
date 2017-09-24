
package org.ayo.editor.model;

/**
 * 传给服务器的视频json model
 */
public class VideoModel {
    /**
     * 视频大小
     */
    public long size;
    /**
     * 视频类型
     */
    public String mime;

    /**
     * 视频宽
     */
    public int width;

    /**
     * 视频长
     */
    public int height;

    /**
     * 视频时长
     */
    public double length;

    /**
     * 视频占位符 {{v1}}
     */
    public String id;

    /**
     * 视频路径
     */
    public String url;
}
