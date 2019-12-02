function date_to_str(time) {
    var year = time.getFullYear();

    var month = time.getMonth() + 1;
    if (month < 10) month = '0' + month;

    var date = time.getDate();
    if (date < 10) date = '0' + date;

    var hour = time.getHours();
    if (hour < 10) hour = '0' + hour;

    var min = time.getMinutes();
    if (min < 10) min = '0' + min;

    var sec = time.getSeconds();
    if (sec < 10) sec = '0' + sec;

    return year + "년 " + month + "월 " + date + "일 \t" + hour+ "시 " + min + "분 " + sec;
}
