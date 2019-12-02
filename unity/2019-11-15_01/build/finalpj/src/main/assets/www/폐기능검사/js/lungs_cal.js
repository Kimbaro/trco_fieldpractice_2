function lungs_cal(x, y, td_name1, td_name2, td_comment1, td_comment2, name) {


    td_name1.innerText = name;
    td_name2.innerText = name;



    var result_avg_text = document.createElement("span");
    var result_text = document.createElement("span");

    console.log(x + "|" + y);
    if (x >= 0.0 && 80.0 >= x) {
        if (y >= 0.0 && 70.0 >= y) {
            //혼합성폐질환
            //x = 0~80 , y = 0~70
            result_text.innerHTML = "혼합성폐질환";
            result_text.style.color = '#FF0000';
            result_avg_text.innerHTML = y + "%";
        } else if (y >= 70.0) {
            //제한성폐질환
            //x = 0~80 , y = 70 ~
            result_text.innerHTML = "제한성폐질환"
            result_text.style.color = '#FF0000';
            result_avg_text.innerHTML = x + "%";
        }
    } else if (x >= 80.0) {
        if (y >= 0 && y <= 70.0) {
            //폐쇄성폐질환
            //x = 80~ , y = 0~70
            result_text.innerHTML = "폐쇄성폐질환"
            result_text.style.color = '#FF0000';
            result_avg_text.innerHTML = y + "%";
        } else if (y >= 70.0) {
            //정상
            result_text.innerHTML = "정상"
            result_text.style.color = '#1DDB16';
            result_avg_text.innerHTML = y + "%";
        }
    } else {
        result_text.innerHTML = "에러"
    }
        if(result_text.innerText == "정상"){

               td_comment1.appendChild(document.createTextNode("정상인대비"));
               td_comment1.appendChild(result_avg_text).style.fontSize = "20px";
               td_comment1.appendChild(result_avg_text).style.fontWeight = "bold";
               td_comment1.appendChild(document.createTextNode("의 폐기능상태를 가지고 있으며 검진결과,"));

               td_comment2.appendChild(result_text).style.fontSize = "20px";
               td_comment2.appendChild(result_text).style.fontWeight = "bold";
               td_comment2.appendChild(document.createTextNode("입니다."));
           }else{
               td_comment1.appendChild(document.createTextNode("정상인에 비해 "));
               td_comment1.appendChild(result_avg_text).style.fontSize = "20px";
               td_comment1.appendChild(result_avg_text).style.fontWeight = "bold";
               td_comment1.appendChild(document.createTextNode("낮은수치이며,"));

               td_comment2.appendChild(result_text).style.fontSize = "20px";
               td_comment2.appendChild(result_text).style.fontWeight = "bold";
               td_comment2.appendChild(document.createTextNode("이 의심됩니다."));
           }

}
