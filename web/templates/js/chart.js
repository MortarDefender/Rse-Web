var options = {
  chart: {
    height: 350,
    foreColor: "#ccc",
    type: 'area',
    toolbar: {
      autoSelected: "pan",
      show: false
    }
  }, stroke: {
    width: 3
  },
  series: [{
    name: 'rate',
    data: []
  }],
  xaxis: {
    categories: []
  },
  noData: {
    text: 'Loading...'
  }
};

var chart = new ApexCharts(document.querySelector("#chart"), options);

function startChart() {
  chart.render();
}

function createChart(x, y) {
  chart.updateSeries([{
    name: 'rate',
    data: x
  }]);

  chart.updateOptions({
    xaxis: {
      categories: y
    }
  });
}