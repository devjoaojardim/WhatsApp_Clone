package com.jvapp.whatsappclone.helper;

import java.text.SimpleDateFormat;

public class DateUtil {

    public static String dataAtual(){
       long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataString = simpleDateFormat.format(date);
        return dataString;

    }
    public static String dataMesAno(String data){

        String retornoData[] = data.split("/");
        String dia = retornoData[0]; //dia
        String mes =  retornoData[1]; //mes
        String ano = retornoData[2]; //ano

        String mesAno = mes + ano;
        return mesAno;

    }
}
