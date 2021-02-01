package com.deval.event.Util

class CheckItem {
    fun idToValue(id: String): String{
        return if (id.equals("1")){
            "EGM"
        } else if (id.equals("2")){
            "Retail Sales"
        }else if (id.equals("3")){
            "Corporate Sales"
        }else if (id.equals("4")){
            "Supply & Distribution"
        }else if (id.equals("5")){
            "COS"
        }else if (id.equals("6")){
            "RPD"
        }else if (id.equals("7")){
            "HSSE"
        }else if (id.equals("8")){
            "Asset Ops"
        }else if (id.equals("9")){
            "Internal Audit"
        }else if (id.equals("10")){
            "PEC"
        }else if (id.equals("11")){
            "Medical"
        }else if (id.equals("12")){
            "Marine"
        }else if (id.equals("13")){
            "HC"
        }else if (id.equals("14")){
            "Legal Counsel"
        }else if (id.equals("15")){
            "Finance"
        }else if (id.equals("16")){
            "SSC ICT"
        }else if (id.equals("17")){
            "Comm, Rel & CS"
        }else if (id.equals("18")){
            "Lubricants"
        }else{
            "Others"
        }
    }

    fun valueToID(id: String): String{
        return if (id.equals("EGM")){
            "1"
        } else if (id.equals("Retail Sales")){
            "2"
        }else if (id.equals("Corporate Sales")){
            "3"
        }else if (id.equals("Supply & Distribution")){
            "4"
        }else if (id.equals("COS")){
            "5"
        }else if (id.equals("RPD")){
            "6"
        }else if (id.equals("HSSE")){
            "7"
        }else if (id.equals("Asset Ops")){
            "8"
        }else if (id.equals("Internal Audit")){
            "9"
        }else if (id.equals("PEC")){
            "10"
        }else if (id.equals("Medical")){
            "11"
        }else if (id.equals("Marine")){
            "12"
        }else if (id.equals("HC")){
            "13"
        }else if (id.equals("Legal Counsel")){
            "14"
        }else if (id.equals("Finance")){
            "15"
        }else if (id.equals("SSC ICT")){
            "16"
        }else if (id.equals("Comm, Rel & CS")){
            "17"
        }else if (id.equals("Lubricants")){
            "18"
        }else{
            "19"
        }
    }
}