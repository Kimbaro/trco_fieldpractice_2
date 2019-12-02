function getLocation(url, area, lat, lon, dist) {
    if (navigator.geolocation) { // GPS를 지원하면
        navigator.geolocation.getCurrentPosition(function (position) {
            lat = position.coords.latitude;
            lon = position.coords.longitude;
            location.replace(url + "?area=" + area + "&latitude=" + lat + "&longitude=" + lon + "&distance=" + dist);
        }, function (error) {
            console.error(error);
            alert("정확한 위치를 파악할 수 없습니다");
            location.replace(url + "?area=" + area + "&latitude=" + lat + "&longitude=" + lon + "&distance=" + dist);
        }, {
            enableHighAccuracy: false,
            maximumAge: 0,
            timeout: Infinity
        });
    } else {
        alert('GPS를 지원하지 않습니다');
    }
}

