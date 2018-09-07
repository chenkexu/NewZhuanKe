package com.dfwr.zhuanke.zhuanke.bean;

import java.io.Serializable;

/**
 * Created by ckx on 2018/7/25.
 */



public class WithDrawHistory implements Serializable{

    private static final long serialVersionUID = 3584532261855766790L;


        /**
         * id : 1
         * uid : 22
         * createDate : 2018-07-20T03:46:58.000+0000
         * money : 1.0
         * balance : 10.0
         * type : 1
         * status : 3
         */

        private int id;
        private int uid;
        private Long createDate;
        private double money;
        private double balance;
        private int type;
        private int status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public Long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(Long createDate) {
            this.createDate = createDate;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
