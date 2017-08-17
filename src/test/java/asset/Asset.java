package asset;

/**
 * Created by badri on 8/17/17.
 */
public class Asset {
    private int assetId;
    private String text;
    private String keywords;
    private String thumbnail;
    private int dateCreated;
    private int dateModified;

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(int dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getDateModified() {
        return dateModified;
    }

    public void setDateModified(int dateModified) {
        this.dateModified = dateModified;
    }
}
