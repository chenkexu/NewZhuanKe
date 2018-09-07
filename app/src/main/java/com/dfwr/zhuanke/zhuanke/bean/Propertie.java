package com.dfwr.zhuanke.zhuanke.bean;

import java.io.Serializable;

/**
 * Created by ckx on 2018/7/25.
 */

public class Propertie implements Serializable {
    private static final long serialVersionUID = -7421694893430429760L;

        /**

         "student_get_balance_to_teacher":"3",    徒弟提现，奖励师傅3元
         "get_balance_money_min":"5",               5元起提现
         "register_reward":"1",                     注册获取1元
             "student_reward_to_teacher":"1",
         "student_reward_n_to_teacher":"2",

         "student_reward":"30%", 徒弟收益
         "share_price":"0.15",  文章分享价格
         "grandson_reward":"10%" //徒孙收益
         *
         *
         *
         */

        private String student_get_balance_to_teacher;
        private String get_balance_money_min;
        private String register_reward;
        private String student_reward_to_teacher;
        private String student_reward_n_to_teacher;
        private String student_reward;
        private String share_price;
        private String grandson_reward;
        private String share_host;

        public String getShare_host() {
            return share_host == null ? "" : share_host;
        }

        public void setShare_host(String share_host) {
            this.share_host = share_host;
        }

        public String getStudent_get_balance_to_teacher() {
            return student_get_balance_to_teacher;
        }

        public void setStudent_get_balance_to_teacher(String student_get_balance_to_teacher) {
            this.student_get_balance_to_teacher = student_get_balance_to_teacher;
        }

        public String getGet_balance_money_min() {
            return get_balance_money_min;
        }

        public void setGet_balance_money_min(String get_balance_money_min) {
            this.get_balance_money_min = get_balance_money_min;
        }

        public String getRegister_reward() {
            return register_reward;
        }

        public void setRegister_reward(String register_reward) {
            this.register_reward = register_reward;
        }

        public String getStudent_reward_to_teacher() {
            return student_reward_to_teacher;
        }

        public void setStudent_reward_to_teacher(String student_reward_to_teacher) {
            this.student_reward_to_teacher = student_reward_to_teacher;
        }

        public String getStudent_reward_n_to_teacher() {
            return student_reward_n_to_teacher;
        }

        public void setStudent_reward_n_to_teacher(String student_reward_n_to_teacher) {
            this.student_reward_n_to_teacher = student_reward_n_to_teacher;
        }

        public String getStudent_reward() {
            return student_reward;
        }

        public void setStudent_reward(String student_reward) {
            this.student_reward = student_reward;
        }

        public String getShare_price() {
            return share_price;
        }

        public void setShare_price(String share_price) {
            this.share_price = share_price;
        }

        public String getGrandson_reward() {
            return grandson_reward;
        }

        public void setGrandson_reward(String grandson_reward) {
            this.grandson_reward = grandson_reward;
        }
    }
