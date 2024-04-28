
package com.example.springbootbank.common;

import java.util.Random;
import java.util.regex.Pattern;
//银行卡号码生成与校验
public class CreateBankCards {
    private static final Pattern acctCheckPattern = Pattern.compile("^(?:\\d{16}|\\d{19})$");
    private static final Pattern acctCreatePattern = Pattern.compile("^(?:\\d{15,16}|\\d{18,19})$");
    public CreateBankCards() {}
    /**
     * 校验银行卡号是否合法
     * @param acctId 16位银行卡号或19位银行卡号
     * @return true,银行卡号是否合法;else false
     */
    //检验银行卡号（用校验码检验）
    public static boolean checkAcctId(String acctId) {
        if (acctId == null || !acctCheckPattern.matcher(acctId).matches()) {
            return false;
        }
        return acctId.equals(createAcctId(acctId));
    }

    /**
     * 根据银行卡号前面的数字，生成完整的卡号
     * @param acctId 有效数字为16位银行卡号的前15位，或19位银行卡号的前18位
     * @return 完整的卡号
     */
    //生成校验码
    public static String createAcctId(String acctId) {
        if (acctId == null || !acctCreatePattern.matcher(acctId).matches()) {
            return "";
        }
        int sum = 0;
        int lastIndex = acctId.length() < 17 ? 14 : 17;
        String preAcctId = acctId.substring(0, lastIndex + 1);
        for (int i = 0; i <= lastIndex; i++) {
            int unit = acctId.charAt(lastIndex - i) - '0';
            if ((i & 1) == 0) {
                if (unit < 5) {
                    sum += unit * 2;
                } else {
                    sum += unit * 2 - 9;
                }
            } else {
                sum += unit;
            }
        }
        int remainder = sum % 10;
        int checkCode = remainder == 0 ? 0 : 10 - remainder;
        return preAcctId + checkCode;

    }
    //生成银行卡号
    public String creatBankcards(Integer type){//type为银行卡类型，1为储蓄卡，2为信用卡
        String id="";
        Random random=new Random();
        if(type==1){
            id="622202";
            for(int i=0;i<12;i++){
                int number=random.nextInt(9);
                id=id+number;
            }
        }
        else if(type==3){
            id="427039";
            for(int i=0;i<9;i++){
                int number=random.nextInt(9);
                id=id+number;
            }
        }
        id=createAcctId(id);
        return id;
    }
}
