function lungs_cal(x, y, result_text, result_avg_text) {
    result_avg_text.innerHTML = y+"%";
    console.log(x + "|" + y);
    if (x >= 0.0 && 80.0 >= x) {
        if (y >= 0.0 && 70.0 >= y) {
            //혼합성폐질환
            //x = 0~80 , y = 0~70
            result_text.innerHTML = "혼합성폐질환";
            result_text.style.color = '#FF0000';
        } else if (y >= 70.0) {
            //제한성폐질환
            //x = 0~80 , y = 70 ~
            result_text.innerHTML = "제한성폐질환"
            result_text.style.color = '#FFBB00';
        }
    } else if (x >= 80.0) {
        if (y >= 0 && y <= 70.0) {
            //폐쇄성폐질환
            //x = 80~ , y = 0~70
            result_text.innerHTML = "폐쇄성폐질환"
            result_text.style.color = '#FFBB00';
        } else if (y >= 70.0) {
            //정상
            result_text.innerHTML = "정상"
            result_text.style.color = '#1DDB16';
        }
    } else {
        result_text.innerHTML = "에러"
    }


}
