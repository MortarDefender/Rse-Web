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
      data: []
    }
  ],
  tooltip: {
    theme: "dark"
  },
  xaxis: {
    type: "category",
    categories: []
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
      }
    }
  },
  colors: ["#FF0080"],
  series: [{
      data: []
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

