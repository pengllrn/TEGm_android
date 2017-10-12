package com.pengllrn.tegm.bean;

/**
 * @author Administrator
 * @version $Rev$
 * @des ${UTODO}
 * @updateAuthor ${Author}$
 * @updateDate2017/10/8.
 */

public class UseRate {
    String date;
    String rate;

    public UseRate(String date, String rate) {
        this.date = date;
        this.rate = rate;
    }

    public String getDate() {
        return date;
    }

    public String getRate() {
        return rate;
    }
}
