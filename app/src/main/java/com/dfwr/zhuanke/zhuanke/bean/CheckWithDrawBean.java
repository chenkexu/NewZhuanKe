package com.dfwr.zhuanke.zhuanke.bean;

import java.io.Serializable;

/**
 * Created by ckx on 2018/7/20.
 */

public class CheckWithDrawBean implements Serializable {
    private static final long serialVersionUID = 67271608503751589L;


        /**
         * phoneIsBinding : 1
         * num : 1
         * public : 0
         */
        private int phoneIsBinding;
        private int num;
        private int publicNum;


        public int getPublicNum() {
            return publicNum;
        }

        public void setPublicNum(int publicNum) {
            this.publicNum = publicNum;
        }

    public int getPhoneIsBinding() {
            return phoneIsBinding;
        }

        public void setPhoneIsBinding(int phoneIsBinding) {
            this.phoneIsBinding = phoneIsBinding;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }


    }
