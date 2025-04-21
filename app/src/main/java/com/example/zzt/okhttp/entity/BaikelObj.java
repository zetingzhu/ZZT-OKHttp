package com.example.zzt.okhttp.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author: zeting
 * @date: 2025/4/10
 */
public class BaikelObj implements Serializable {

    /**
     * id
     */
    private int id;
    /**
     * subLemmaId
     */
    private int subLemmaId;
    /**
     * newLemmaId
     */
    private int newLemmaId;
    /**
     * key
     */
    private String key;
    /**
     * desc
     */
    private String desc;
    /**
     * title
     */
    private String title;
    /**
     * card
     */
    private List<CardDTO> card;
    /**
     * image
     */
    private String image;
    /**
     * src
     */
    private String src;
    /**
     * imageHeight
     */
    private int imageHeight;
    /**
     * imageWidth
     */
    private int imageWidth;
    /**
     * isSummaryPic
     */
    private String isSummaryPic;
    /**
     * abstractX
     */
    private String abstractX;
    /**
     * moduleIds
     */
    private List<Long> moduleIds;
    /**
     * url
     */
    private String url;
    /**
     * wapUrl
     */
    private String wapUrl;
    /**
     * hasOther
     */
    private int hasOther;
    /**
     * totalUrl
     */
    private String totalUrl;
    /**
     * catalog
     */
    private List<String> catalog;
    /**
     * wapCatalog
     */
    private List<String> wapCatalog;
    /**
     * logo
     */
    private String logo;
    /**
     * copyrights
     */
    private String copyrights;
    /**
     * customImg
     */
    private String customImg;
    /**
     * redirect
     */
    private RedirectDTO redirect;

    /**
     * RedirectDTO
     */
    public static class RedirectDTO {
        /**
         * from
         */
        private String from;
        /**
         * to
         */
        private String to;
    }

    /**
     * CardDTO
     */
    public static class CardDTO {
        /**
         * key
         */
        private String key;
        /**
         * name
         */
        private String name;
        /**
         * value
         */
        private List<String> value;
        /**
         * format
         */
        private List<String> format;
    }
}
