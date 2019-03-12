package com.example.zkapp_map.bean;

public class TaskBaseBean {


    /**
     * page : 0
     * limit : 10
     * screen : {"type":0,"style":0,"range":{"r":3000,"lat":0,"lng":0},"reward":0,"credit":0}
     * order : 0
     */

    private int page;
    private int limit;
    private ScreenBean screen;
    private int order;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public ScreenBean getScreen() {
        return screen;
    }

    public void setScreen(ScreenBean screen) {
        this.screen = screen;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public static class ScreenBean {
        /**
         * type : 0
         * style : 0
         * range : {"r":3000,"lat":0,"lng":0}
         * reward : 0
         * credit : 0
         */

        private int type;
        private int style;
        private RangeBean range;
        private int reward;
        private int credit;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getStyle() {
            return style;
        }

        public void setStyle(int style) {
            this.style = style;
        }

        public RangeBean getRange() {
            return range;
        }

        public void setRange(RangeBean range) {
            this.range = range;
        }

        public int getReward() {
            return reward;
        }

        public void setReward(int reward) {
            this.reward = reward;
        }

        public int getCredit() {
            return credit;
        }

        public void setCredit(int credit) {
            this.credit = credit;
        }

        public static class RangeBean {
            /**
             * r : 3000
             * lat : 0
             * lng : 0
             */

            private int r;
            private double lat;
            private double lng;

            public int getR() {
                return r;
            }

            public void setR(int r) {
                this.r = r;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }
        }
    }
}
