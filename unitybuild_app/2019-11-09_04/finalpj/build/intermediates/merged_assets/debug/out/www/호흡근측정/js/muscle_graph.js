google.charts.load('current', {packages: ['corechart', 'bar']});

function drawMultSeries_mep() {
    var data = google.visualization.arrayToDataTable([
        ['names', {role: 'annotation'}, '위험단계', '호흡근력약증', '진단자'],
        ['진단기준', '', 50, 30, 0],
        ['진단자', result_mep, 0, 0, mep]
    ]);

    var options = {
        animation: {
            duration: 1000,
            easing: 'in',
            startup: true
        },
        annotations: {
            textStyle: {
                fontSize: 14,
                bold: true,
                // The color of the text.
                color: '#000000',
                // The color of the text outline.
                // The transparency of the text.
                opacity: 1,
                auraColor: '#FFFFFF'
            }
        },
        title: '불었을때 ( 호기압 )',
        titleTextStyle: {
            fontSize: 14,
            color: '#000000',
            bold: true,
            italic: false
        },
        width: screen.availWidth,
        height: 150,
        legend: {position: 'bottom', maxLines: 3},
        colors: ['#ED0000', '#FFE400', result_mep_color],
        bar: {groupWidth: '75%'},
        isStacked: true
    };

    /* 6799FF */  /* google.visualization.ColumnChart */
    var chart = new google.visualization.BarChart(
        document.getElementById('chart_div_mep'));
    chart.draw(data, options);
}

function drawMultSeries_mip() {
    var data = google.visualization.arrayToDataTable([
        ['names', {role: 'annotation'}, '위험단계', '호흡근력약증', '진단자'],
        ['진단기준', '', -20, -60, 0],
        ['진단자', result_mip, 0, 0, mip]
    ]);

    var options = {
        animation: {
            duration: 1000,
            easing: 'in',
            startup: true
        },
        annotations: {
            textStyle: {
                fontSize: 14,
                bold: true,
                // The color of the text.
                color: '#000000',
                // The color of the text outline.
                // The transparency of the text.
                opacity: 1,
                auraColor: '#FFFFFF'
            }
        },
        title: '들이마셨을때 ( 흡기압 )',
        titleTextStyle: {
            fontSize: 14,
            color: '#000000',
            bold: true,
            italic: false
        },
        width: screen.availWidth,
        height: 150,
        legend: {position: 'bottom', maxLines: 3},
        colors: ['#ED0000', '#FFE400', result_mip_color],
        bar: {groupWidth: '75%'},
        isStacked: true
    };
    /* 6799FF */  /* google.visualization.ColumnChart */
    var chart = new google.visualization.BarChart(
        document.getElementById('chart_div_mip'));
    chart.draw(data, options);
}
