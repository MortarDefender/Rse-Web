/*var data = generateDayWiseTimeSeries(new Date("22 Apr 2017").getTime(), 115, {
  min: 30,
  max: 90
});*/

var options1 = {
  chart: {
    id: "chart2",
    type: "area",
    height: 230,
    foreColor: "#ccc",
    toolbar: {
      autoSelected: "pan",
      show: false
    }
  },
  colors: ["#00BAEC"],
  stroke: {
    width: 3
  },
  grid: {
    borderColor: "#555",
    clipMarkers: false,
    yaxis: {
      lines: {
        show: false
      }
    }
  },
  dataLabels: {
    enabled: false
  },
  fill: {
    gradient: {
      enabled: true,
      opacityFrom: 0.55,
      opacityTo: 0
    }
  },
  markers: {
    size: 5,
    colors: ["#000524"],
    strokeColor: "#00BAEC",
    strokeWidth: 3
  },
  series: [
    {
      name: 'rate',
      data: [] // ["150","150","170","170","200"] // ["150","150","170","170","200", "150","150","170","170","200", "150","150","170","170","200",
         // "150","150","170","170","200", "150","150","170","170","200", "150","150","170","170","200"] // data
    }
  ],
  tooltip: {
    theme: "dark"
  },
  xaxis: {
    type: "category",
    categories: [] // ["18:22:35:596","18:22:39:651","18:22:55:789","18:23:08:466","18:23:20:801"] // ["18:22:35:596","18:22:39:651","18:22:55:789","18:23:08:466","18:23:20:801", "19:22:35:596","19:22:39:651","19:22:55:789","19:23:08:466","19:23:20:801", "20:22:35:596","20:22:39:651","20:22:55:789","20:23:08:466","20:23:20:801",
       // "21:22:35:596","21:22:39:651","21:22:55:789","21:23:08:466","21:23:20:801", "22:22:35:596","22:22:39:651","22:22:55:789","22:23:08:466","22:23:20:801", "23:22:35:596","23:22:39:651","23:22:55:789","23:23:08:466","23:23:20:801"]
  },
  yaxis: {
    tickAmount: 4
  },
  noData: {
    text: 'Loading...'
  }
};

var chart1 = new ApexCharts(document.querySelector("#chart-area"), options1);

var options2 = {
  chart: {
    id: "chart1",
    height: 130,
    type: "bar",
    foreColor: "#ccc",
    brush: {
      target: "chart2",
      enabled: true
    },
    selection: {
      enabled: true,
      fill: {
        color: "#fff",
        opacity: 0.4
      },
      xaxis: {
        // min: new Date("27 Jul 2017 10:00:00").getTime() // ,
        // max: new Date("14 Aug 2017 10:00:00").getTime()
      }
    }
  },
  colors: ["#FF0080"],
  series: [{
      data: [] // ["150","150","170","170","200"] //["150","150","170","170","200", "150","150","170","170","200", "150","150","170","170","200", "150","150","170","170","200", "150","150","170","170","200", "150","150","170","170","200"] // [] // data
    }],
  stroke: {
    width: 2
  },
  grid: {
    borderColor: "#444"
  },
  markers: {
    size: 0
  },
  xaxis: {
    type: "category",
    tooltip: {
      enabled: false
    }
  },
  yaxis: {
    tickAmount: 4
  },
  noData: {
    text: 'Loading...'
  }
};

var chart2 = new ApexCharts(document.querySelector("#chart-bar"), options2);

/*function generateDayWiseTimeSeries(baseval, count, yrange) {
  var i = 0;
  var series = [];
  while (i < count) {
    var x = baseval;
    var y =
      Math.floor(Math.random() * (yrange.max - yrange.min + 1)) + yrange.min;

    series.push([x, y]);
    baseval += 86400000;
    i++;
  }
  return series;
}*/

function startAdvChart() {
  chart1.render();
  chart2.render();
}

function createAdvChart(x, y) {
  chart1.updateSeries([{
    name: 'rate',
    data: x
  }]);

  chart1.updateOptions({
    xaxis: {
      categories: y
    }
  });

  chart2.updateSeries([{
    data: x
  }]);
}

// {"yAxis":,"xAxis":}
// ["18:22:35:596","18:22:39:651","18:22:55:789","18:23:08:466","18:23:20:801"]
// ["150","150","170","170","200"]
