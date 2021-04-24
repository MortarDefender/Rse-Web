var options = {
  chart: {
    height: 350,
    type: "line",
    stacked: false,
    toolbar: {
      autoSelected: "pan",
      show: false
    }
  },
  dataLabels: {
    enabled: false
  },
  colors: ["#00BAEC", "#ff0000"],
  series: [
    {
      name: "Series A",
      data: [1.4, 2, 2.5, 1.5, 2.5, 2.8, 3.8, 4.6]
    },
    {
      name: "Series B",
      data: [20, 29, 37, 36, 44, 45, 50, 58]
    }
  ],
  stroke: {
    curve: 'smooth',
    width: [4, 4]
  },
  plotOptions: {
    bar: {
      columnWidth: "20%"
    }
  },
  xaxis: {
    categories: [2009, 2010, 2011, 2012, 2013, 2014, 2015, 2016]
  },
  yaxis: [
    {
      axisTicks: {
        show: true
      },
      axisBorder: {
        show: true,
        color: "#ffffff"
      },
      labels: {
        style: {
          colors: "#ffffff"
        }
      },
      title: {
        text: "Series A",
        style: {
          color: "#ffffff"
        }
      }
    },
    {
      opposite: true,
      axisTicks: {
        show: true
      },
      axisBorder: {
        show: true,
        color: "#ffffff"
      },
      labels: {
        style: {
          colors: "#ffffff"
        }
      },
      title: {
        text: "Series B",
        style: {
          color: "#ffffff"
        }
      }
    }
  ],
  tooltip: {
    shared: false,
    intersect: true,
    x: {
      show: false
    }
  },
  legend: {
    horizontalAlign: "left",
    offsetX: 40
  }
};

var chart = new ApexCharts(document.querySelector("#chart"), options);

chart.render();
