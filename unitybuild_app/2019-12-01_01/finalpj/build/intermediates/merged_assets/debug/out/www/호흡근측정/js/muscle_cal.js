function muscle_cal(data) {
    if (data >= 0) {
        //호기압
        if (data <= 50) {
            //인공호흡장치필요
            return 0;
        } else if (data > 50 && data <= 80) {
            //호흡근력약증
            return 1;
        } else {
            //정상
            return 2;
        }
    } else {
        //흡기압
        if (data >= -20) {
            //인공호흡장치필요
            return 0;
        } else if (data < -20 && data >= -60) {
            //호흡근력약증
            return 1;
        } else {
            //정상
            return 2;
        }
    }
}

function result(data) {
    switch (data) {
        case 0:
            return "인공호흡장치필요";
            break;
        case 1:
            return "호흡근력약증";
            break;
        case 2:
            return "정상";
            break;
    }
}

function result_color(data) {
    switch (data) {
        case 0:
            return "#ED0000";
            break;
        case 1:
            return "#FFE400";
            break;
        case 2:
            return "#6799FF";
            break;
    }
}