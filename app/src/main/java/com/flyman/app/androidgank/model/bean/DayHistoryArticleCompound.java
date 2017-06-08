package com.flyman.app.androidgank.model.bean;

/**
 * @author Flyman
 * @ClassName DayHistoryArticleCompound
 * @description 用于最终显示列表对象
 * @date 2017-5-7 17:53
 */
public class DayHistoryArticleCompound {
    private DayHistoryResult.DayHistoryArticle article;
    private int type;
    private String date;

    public DayHistoryArticleCompound(int type, DayHistoryResult.DayHistoryArticle article) {
        this.article = article;
        this.type = type;
    }

    public DayHistoryArticleCompound(int type, String date) {
        this.type = type;
        this.date = date;
    }

    public DayHistoryResult.DayHistoryArticle getArticle() {
        return article;
    }

    public void setArticle(DayHistoryResult.DayHistoryArticle article) {
        this.article = article;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public interface Type {
        int android = 0;
        int iOS = 1;
        int video = 2;
        int recommend = 3;
        int expand = 4;
        int beauty = 5;
        int date = 6;
    }

    @Override
    public String toString() {
        return "DayHistoryArticleCompound{" +
                "article=" + article +
                ", type=" + type +
                ", date='" + date + '\'' +
                '}';
    }
}
