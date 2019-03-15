package com.pratham.assessment.domain;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "ContentTable")
public class ContentTable implements Comparable, Parcelable {

    @NonNull
    @PrimaryKey
    @SerializedName("nodeid")
    public String nodeId;

    @SerializedName("level")
    public String level;

    @SerializedName("resourceid")
    public String resourceId;

    @SerializedName("parentid")
    public String parentId;

    @SerializedName("nodedesc")
    public String nodeDesc;

    @SerializedName("nodetype")
    public String nodeType;

    @SerializedName("nodetitle")
    public String nodeTitle;

    @SerializedName("resourcepath")
    public String resourcePath;

    @SerializedName("resourcetype")
    public String resourceType;

    @SerializedName("nodeserverimage")
    public String nodeServerImage;

    @SerializedName("nodeimage")
    public String nodeImage;

    @SerializedName("nodeeage")
    public String nodeAge;


    public String isDownloaded;
    public String contentType;
    public String contentLanguage;
    public String nodeKeywords;
    private boolean onSDCard = false;

    public ContentTable() {

    }


    public ContentTable(Parcel in) {
        level = in.readString();
        nodeId = in.readString();
        resourceId = in.readString();
        parentId = in.readString();
        nodeDesc = in.readString();
        nodeType = in.readString();
        nodeTitle = in.readString();
        resourcePath = in.readString();
        resourceType = in.readString();
        nodeServerImage = in.readString();
        nodeAge = in.readString();
        isDownloaded = in.readString();
        contentType = in.readString();
        contentLanguage = in.readString();
        nodeKeywords = in.readString();
        onSDCard = in.readByte() != 0;
    }

    public String getNodeImage() {
        return nodeImage;
    }

    public void setNodeImage(String nodeImage) {
        this.nodeImage = nodeImage;
    }

    public static final Creator<ContentTable> CREATOR = new Creator<ContentTable>() {
        @Override
        public ContentTable createFromParcel(Parcel in) {
            return new ContentTable(in);
        }

        @Override
        public ContentTable[] newArray(int size) {
            return new ContentTable[size];
        }
    };

    public String getNodeTitle() {
        return nodeTitle;
    }

    public void setNodeTitle(String nodeTitle) {
        this.nodeTitle = nodeTitle;
    }

    public String getNodeServerImage() {
        return nodeServerImage;
    }

    public void setNodeServerImage(String nodeServerImage) {
        this.nodeServerImage = nodeServerImage;
    }

    public String getIsDownloaded() {
        return isDownloaded;
    }

    public void setIsDownloaded(String isDownloaded) {
        this.isDownloaded = isDownloaded;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentLanguage() {
        return contentLanguage;
    }

    public void setContentLanguage(String contentLanguage) {
        this.contentLanguage = contentLanguage;
    }

    public String getNodeKeywords() {
        return nodeKeywords;
    }

    public void setNodeKeywords(String nodeKeywords) {
        this.nodeKeywords = nodeKeywords;
    }

    public String getNodeAge() {
        return nodeAge;
    }

    public void setNodeAge(String nodeAge) {
        this.nodeAge = nodeAge;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getNodeDesc() {
        return nodeDesc;
    }

    public void setNodeDesc(String nodeDesc) {
        this.nodeDesc = nodeDesc;
    }


    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public boolean isOnSDCard() {
        return onSDCard;
    }

    public void setOnSDCard(boolean onSDCard) {
        this.onSDCard = onSDCard;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        ContentTable compare = (ContentTable) o;
        if (compare.getNodeId() != null) {
            if (isDownloaded.equalsIgnoreCase("1") || isDownloaded.equalsIgnoreCase("true"))
                isDownloaded = "1";
            else
                isDownloaded = "0";

            if (compare.getNodeId().equalsIgnoreCase(this.nodeId) && compare.isDownloaded() == Boolean.parseBoolean(isDownloaded))
                return 0;
            else return 1;
        } else {
            return 0;
        }
    }

    private boolean isDownloaded() {
        if (isDownloaded.equalsIgnoreCase("1") || isDownloaded.equalsIgnoreCase("true"))
            isDownloaded = "true";
        else
            isDownloaded = "false";

        return Boolean.valueOf(isDownloaded);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nodeId);
        dest.writeString(nodeType);
        dest.writeString(nodeTitle);
        dest.writeString(nodeKeywords);
        dest.writeString(nodeAge);
        dest.writeString(nodeDesc);
//        dest.writeString(nodeimage);
        dest.writeString(nodeServerImage);
        dest.writeString(resourceId);
        dest.writeString(resourceType);
        dest.writeString(resourcePath);
        dest.writeInt(Integer.parseInt(level));
        dest.writeString(contentLanguage);
        dest.writeString(parentId);
        dest.writeString(contentType);
        if (isDownloaded.equalsIgnoreCase("1") || isDownloaded.equalsIgnoreCase("true"))
            isDownloaded = "1";
        else
            isDownloaded = "0";
        dest.writeString(isDownloaded);
        dest.writeByte((byte) (onSDCard ? 1 : 0));
    }

}