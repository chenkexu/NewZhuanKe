package com.dfwr.zhuanke.zhuanke.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by ckx on 2018/7/19.
 */

public class UserBaseInfo implements Serializable {
    private static final long serialVersionUID = -5373148577991900913L;


        /**
         * todayProfit : 0.42
         * todayStudentPofit : 2.8
         * allWithDrawMoney : 0
         * todayStudentNum : 0
         * articleMoney : 0.84
         * studentPofit : 1.95
         * user : {"createDate":1533545880748,"imgId":"http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTLMR8PF1W7ITA88viaxicgjRFTicy5jEAdhpSYIZgd5NI9ibboetJfnl9aKibYvfM7zqopBa53FoJMFLWg/132","num":0,"phone":"18401512374","sex":"m","status":0,"uid":1,"unionid":"ospLiwjbEZe50hjJE5RS0TBz4wnw","userBalance":0,"wxId":"oGDHv08Ey3edzVpCB2sAAioxmqFQ","wxName":"垚垚旭"}
         * studentNum : 3
         * account : {"articleMoney":0,"balance":2.84,"frozenAmount":0,"id":1,"shareMoney":0,"todayMoney":0,"uid":1}
         */
        private String todayProfit;
        private String todayStudentPofit;  //今日徒弟收益
        private String allWithDrawMoney;  //累计提现
        private int todayStudentNum;
        private String articleMoney;
        private String studentPofit;     //累计徒弟收益
        private UserBean user;
        private int studentNum;
        private AccountBean account;



        public String getTodayProfit() {
            BigDecimal bg = new BigDecimal(Double.parseDouble(todayProfit)).setScale(2, BigDecimal.ROUND_HALF_UP);
            return bg.doubleValue() +"";
        }

        public void setTodayProfit(String todayProfit) {
            this.todayProfit = todayProfit;
        }

        public String getTodayStudentPofit() {
            BigDecimal bg = new BigDecimal(Double.parseDouble(todayStudentPofit)).setScale(2, BigDecimal.ROUND_HALF_UP);
            return bg.doubleValue() +"";
//            return todayStudentPofit;
        }

        public void setTodayStudentPofit(String todayStudentPofit) {
            this.todayStudentPofit = todayStudentPofit;
        }

        public String getAllWithDrawMoney() {
            BigDecimal bg = new BigDecimal(Double.parseDouble(allWithDrawMoney)).setScale(2, BigDecimal.ROUND_HALF_UP);
            return bg.doubleValue() +"";
//            return allWithDrawMoney;
        }

        public void setAllWithDrawMoney(String allWithDrawMoney) {
            this.allWithDrawMoney = allWithDrawMoney;
        }

        public int getTodayStudentNum() {
            return todayStudentNum;
        }

        public void setTodayStudentNum(int todayStudentNum) {
            this.todayStudentNum = todayStudentNum;
        }

        public String getArticleMoney() {
            return articleMoney;
        }

        public void setArticleMoney(String articleMoney) {
            this.articleMoney = articleMoney;
        }

        public String getStudentPofit() {
            BigDecimal bg = new BigDecimal(Double.parseDouble(studentPofit)).setScale(2, BigDecimal.ROUND_HALF_UP);
            return bg.doubleValue() +"";
//            return studentPofit;
        }

        public void setStudentPofit(String studentPofit) {
            this.studentPofit = studentPofit;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public int getStudentNum() {
            return studentNum;
        }

        public void setStudentNum(int studentNum) {
            this.studentNum = studentNum;
        }

        public AccountBean getAccount() {
            return account;
        }

        public void setAccount(AccountBean account) {
            this.account = account;
        }






        public static class UserBean {
            /**
             * createDate : 1533545880748
             * imgId : http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTLMR8PF1W7ITA88viaxicgjRFTicy5jEAdhpSYIZgd5NI9ibboetJfnl9aKibYvfM7zqopBa53FoJMFLWg/132
             * num : 0
             * phone : 18401512374
             * sex : m
             * status : 0
             * uid : 1
             * unionid : ospLiwjbEZe50hjJE5RS0TBz4wnw
             * userBalance : 0.0
             * wxId : oGDHv08Ey3edzVpCB2sAAioxmqFQ
             * wxName : 垚垚旭
             */

            private long createDate;
            private String imgId;
            private int num;
            private String phone;
            private String sex;
            private int status;
            private int uid;
            private String unionid;
            private double userBalance;
            private String wxId;
            private String wxName;

            public long getCreateDate() {
                return createDate;
            }

            public void setCreateDate(long createDate) {
                this.createDate = createDate;
            }

            public String getImgId() {
                return imgId;
            }

            public void setImgId(String imgId) {
                this.imgId = imgId;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getUnionid() {
                return unionid;
            }

            public void setUnionid(String unionid) {
                this.unionid = unionid;
            }

            public double getUserBalance() {
                return userBalance;
            }

            public void setUserBalance(double userBalance) {
                this.userBalance = userBalance;
            }

            public String getWxId() {
                return wxId;
            }

            public void setWxId(String wxId) {
                this.wxId = wxId;
            }

            public String getWxName() {
                return wxName;
            }

            public void setWxName(String wxName) {
                this.wxName = wxName;
            }
        }

        public static class AccountBean {
            /**
             * articleMoney : 0.0
             * balance : 2.84
             * frozenAmount : 0.0
             * id : 1
             * shareMoney : 0.0
             * todayMoney : 0.0
             * uid : 1
             */

            private double articleMoney;
            private double balance;
            private double frozenAmount;
            private int id;
            private double shareMoney;
            private double todayMoney;
            private int uid;

            public double getArticleMoney() {
                return articleMoney;
            }

            public void setArticleMoney(double articleMoney) {
                this.articleMoney = articleMoney;
            }

            public double getBalance() {
                BigDecimal bg = new BigDecimal(balance).setScale(2, BigDecimal.ROUND_HALF_UP);
                return bg.doubleValue();
            }

            public void setBalance(double balance) {
                this.balance = balance;
            }

            public double getFrozenAmount() {
                return frozenAmount;
            }

            public void setFrozenAmount(double frozenAmount) {
                this.frozenAmount = frozenAmount;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public double getShareMoney() {
                return shareMoney;
            }

            public void setShareMoney(double shareMoney) {
                this.shareMoney = shareMoney;
            }

            public double getTodayMoney() {
                return todayMoney;
            }

            public void setTodayMoney(double todayMoney) {
                this.todayMoney = todayMoney;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }
        }
    }
