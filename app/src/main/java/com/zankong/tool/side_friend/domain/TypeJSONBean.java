package com.zankong.tool.side_friend.domain;

import java.util.List;

public class TypeJSONBean {

    /**
     * styleId : 101
     * styleName : 其他
     * types : [{"id":249,"style":101,"name":"其他","valid":0,"skillCertId":"","hasOrigin":0,"hasDestination":0,"advanceMoney":0,"tag":"","isNeedAdvancePay":0,"num":26,"createdAt":"2018-10-10T16:00:00.000Z","updatedAt":"2019-02-20T12:01:42.000Z"}]
     */

    private int styleId;
    private String styleName;
    private List<TypesBean> types;

    public int getStyleId() {
        return styleId;
    }

    public void setStyleId(int styleId) {
        this.styleId = styleId;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public List<TypesBean> getTypes() {
        return types;
    }

    public void setTypes(List<TypesBean> types) {
        this.types = types;
    }

    public static class TypesBean {
        /**
         * id : 249
         * style : 101
         * name : 其他
         * valid : 0
         * skillCertId :
         * hasOrigin : 0
         * hasDestination : 0
         * advanceMoney : 0
         * tag :
         * isNeedAdvancePay : 0
         * num : 26
         * createdAt : 2018-10-10T16:00:00.000Z
         * updatedAt : 2019-02-20T12:01:42.000Z
         */

        private int id;
        private int style;
        private String name;
        private int valid;
        private String skillCertId;
        private int hasOrigin;
        private int hasDestination;
        private int advanceMoney;
        private String tag;
        private int isNeedAdvancePay;
        private int num;
        private String createdAt;
        private String updatedAt;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStyle() {
            return style;
        }

        public void setStyle(int style) {
            this.style = style;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValid() {
            return valid;
        }

        public void setValid(int valid) {
            this.valid = valid;
        }

        public String getSkillCertId() {
            return skillCertId;
        }

        public void setSkillCertId(String skillCertId) {
            this.skillCertId = skillCertId;
        }

        public int getHasOrigin() {
            return hasOrigin;
        }

        public void setHasOrigin(int hasOrigin) {
            this.hasOrigin = hasOrigin;
        }

        public int getHasDestination() {
            return hasDestination;
        }

        public void setHasDestination(int hasDestination) {
            this.hasDestination = hasDestination;
        }

        public int getAdvanceMoney() {
            return advanceMoney;
        }

        public void setAdvanceMoney(int advanceMoney) {
            this.advanceMoney = advanceMoney;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public int getIsNeedAdvancePay() {
            return isNeedAdvancePay;
        }

        public void setIsNeedAdvancePay(int isNeedAdvancePay) {
            this.isNeedAdvancePay = isNeedAdvancePay;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}
