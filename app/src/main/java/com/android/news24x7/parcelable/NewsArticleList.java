
package com.android.news24x7.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class NewsArticleList implements Serializable, Parcelable
{

    @SerializedName("status")
    private String status;
    @SerializedName("source")
    private String source;
    @SerializedName("sortBy")
    private String sortBy;
    @SerializedName("articles")
    private List<Article> articles = null;
    public final static Creator<NewsArticleList> CREATOR = new Creator<NewsArticleList>() {


        @SuppressWarnings({
            "unchecked"
        })
        public NewsArticleList createFromParcel(Parcel in) {
            NewsArticleList instance = new NewsArticleList();
            instance.status = ((String) in.readValue((String.class.getClassLoader())));
            instance.source = ((String) in.readValue((String.class.getClassLoader())));
            instance.sortBy = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.articles, (Article.class.getClassLoader()));
            return instance;
        }

        public NewsArticleList[] newArray(int size) {
            return (new NewsArticleList[size]);
        }

    }
    ;
    private final static long serialVersionUID = -7795469478313085510L;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(source);
        dest.writeValue(sortBy);
        dest.writeList(articles);
    }

    public int describeContents() {
        return  0;
    }

}
