package br.almadaapps.civilapp.domains;

/**
 * Created by vinicius-almada on 21/03/17.
 */

public abstract class News {


    public static class NDynamic extends News {
        private String link;
        private String linkImg;

        public NDynamic(String link, String linkImg) {
            this.link = link;
            this.linkImg = linkImg;
        }

        public String getLinkImg() {
            return linkImg;
        }

        public void setLinkImg(String linkImg) {
            this.linkImg = linkImg;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }

    public static class NFeatured extends News {
        private String text;
        private String link;
        private String linkImg;

        public NFeatured(String text, String link, String linkImg) {
            this.text = text;
            this.link = link;
            this.linkImg = linkImg;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getLinkImg() {
            return linkImg;
        }

        public void setLinkImg(String linkImg) {
            this.linkImg = linkImg;
        }
    }

    public static class NNonImage extends News {
        private String link;
        private String text;
        private String comment;

        public NNonImage(String link, String text, String comment) {
            this.link = link;
            this.text = text;
            this.comment = comment;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }

    public static class NSmallImages extends News {
        private String link;
        private String linkImg;
        private String text;

        public NSmallImages(String link, String linkImg, String text) {
            this.link = link;
            this.linkImg = linkImg;
            this.text = text;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getLinkImg() {
            return linkImg;
        }

        public void setLinkImg(String linkImg) {
            this.linkImg = linkImg;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
