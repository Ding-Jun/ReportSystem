import echarts from 'echarts/lib/echarts'
import 'echarts/lib/chart/bar'
import 'echarts/lib/component/tooltip'
import 'echarts/lib/component/toolbox'
import 'echarts/lib/component/title'
import 'echarts/lib/component/markLine'
import 'echarts/lib/component/legend'
import 'echarts/lib/component/dataZoom'
import 'echarts/lib/component/markArea'

const CHART_PASS = 0;
const CHART_FAIL = -1;
//const CHART_ALL = 1;
class ChartUtils {
  static createChart(element, option) {
    var chartInstance = echarts.init(element);
    chartInstance.setOption(option);
    console.log('in createChart:',chartInstance);
    return chartInstance;
  }

  static test() {
    console.log('test')
  }

  static getBarChartOption(chart) {
    var themeColor = CHART_PASS == chart.chartType ? '#4c8fdd' : '#c23531';
    var subtext='';
    if(chart.chartType==CHART_PASS){
      subtext='LSL : {}       N : {}       Sigma : {}\nUSL : {}       Average : {}       Cpk : {}';
      subtext = subtext.replace(/\{\}/,chart.limitMin);
      subtext = subtext.replace(/\{\}/,chart.totalCnt);
      subtext = subtext.replace(/\{\}/,chart.stdev.toFixed(4));
      subtext = subtext.replace(/\{\}/,chart.limitMax);
      subtext = subtext.replace(/\{\}/,chart.realAverage.toFixed(2));
      subtext = subtext.replace(/\{\}/,chart.cpk.toFixed(2));
    }else if(chart.chartType==CHART_FAIL){
      subtext='LSL : {}       N : {}       Sigma : {}\nUSL : {}       Average : {}       ';
      subtext = subtext.replace(/\{\}/,chart.limitMin);
      subtext = subtext.replace(/\{\}/,chart.totalCnt);
      subtext = subtext.replace(/\{\}/,chart.stdev.toFixed(4));
      subtext = subtext.replace(/\{\}/,chart.limitMax);
      subtext = subtext.replace(/\{\}/,chart.realAverage.toFixed(2));
    }
    var option = {
      title: {
        text: chart.title, // chart.title
        subtext: subtext,
        shadowColor: 'rgba(0, 0, 0, 0.5)',
        shadowBlur: 10,
        x: 'center',
        align: 'right',
        textAlign:'left',
        subtextStyle:{
          fontWeight:'bold',
          fontSize:14
        }
      },
      backgroundColor: '#f3f3f3',
      grid: [
        {top: '20%'}

      ],
      tooltip: {
        trigger: 'axis'
        /*
         formatter : function (params) {
         return params.seriesName + ' : [ '
         + params.value[0] + ', '
         + params.value[1] + ' ]';
         }*/
      },

      toolbox: {

        feature: {
          dataZoom: {
            yAxisIndex: 'none'
          },
          restore: {},
          saveAsImage: {}
        }
      },
      xAxis: [
        {
          gridIndex: 0,
          type: 'value',
          scale: true,
          //min:'dataMin' ,
          //max:'dataMax',
          //min:chart.XAxisMin,
          //max:chart.XAxisMax,
          boundaryGap: ['5%', '5%']
          //splitNumber:10
          /*
           axisTick:[{
           show:false,
           length:10
           }]*/
        }
      ],
      yAxis: [
        {
          gridIndex:0,
          name: 'frequency',
          type: 'value',
          nameLocation: 'middle',
          nameGap: 45,
          nameTextStyle: {
            color: themeColor
          },
          max: Math.ceil(chart.quantityMax * 1.1),
          axisLine: {
            show: false,
            lineStyle: {
              color: themeColor
            }
          }
        }

      ],
      dataZoom: [
        {   // 这个dataZoom组件，默认控制x轴。
          type: 'slider', // 这个 dataZoom 组件是 slider 型 dataZoom 组件
          xAxisIndex: [0],
          start: 0,      // 左边在 10% 的位置。
          end: 100         // 右边在 60% 的位置。
        }/*,
         {   // 这个dataZoom组件，默认控制x轴。
         type: 'slider', // 这个 dataZoom 组件是 slider 型 dataZoom 组件
         yAxisIndex: [0],
         start: 0,      // 左边在 10% 的位置。
         //end: 90         // 右边在 60% 的位置。
         }*/
      ],

      series: [
        //for chart
        {
          name: chart.title,
          type: 'bar',
          barWidth: 5,
          itemStyle: {
            normal: {
              color: themeColor
            }
          },
          data: eval(chart.datas),
          markLine: {
            data: [
              /* [{
               name: '平均线',
               // 支持 'average', 'min', 'max'
               type: 'average',
               symbol:'rect',
               value:'gg',
               label:{

               }
               },{
               symbol:'rect'
               }],*/

              [{
                name: 'LSL',
                coord: [chart.limitMin, 0],
                value: 'gg'
                //coord: [150,500]
              }, {
                coord: [chart.limitMin, Math.ceil(chart.quantityMax * 1.1)],
                label: {
                  normal: {
                    position: 'end',
                    formatter: '{b}'
                  }
                }
                //coord: [150, 400]
              }],
              [{
                name: 'USL',
                coord: [chart.limitMax, 0]
              }, {
                coord: [chart.limitMax, Math.ceil(chart.quantityMax * 1.1)],
                label: {
                  normal: {
                    position: 'end',
                    formatter: '{b}'
                  }
                }
              }] /*
               [{
               name: 'typical',
               coord: [chart.typicalValue, 0]
               }, {
               coord: [chart.typicalValue, Math.ceil(chart.quantityMax*1.1)]
               }],*/
            ]
          }
        }
      ]

    };
    /*
     if(chart.chartType==CHART_PASS){
     option.xAxis[0].min=chart.limitMin - Math.abs(chart.limitMin)*0.005;
     option.xAxis[0].max=chart.limitMax + Math.abs(chart.limitMax)*0.005;
     if((chart.limitMin == -Number.MAX_VALUE) ){
     option.xAxis[0].min=chart.realMin - Math.abs(chart.realMin)*0.005;
     }
     if(chart.limitMax == Number.MAX_VALUE){
     option.xAxis[0].max=chart.realMax + Math.abs(chart.realMax)*0.005;
     }
     option.xAxis[0].min=option.xAxis[0].min.toFixed(4);
     option.xAxis[0].max=option.xAxis[0].max.toFixed(4);

     //option.title.text=chart.title+'的良品分布图';
     //option.title.subtext='                  '+ 'Average : '+chart.typicalValue.toFixed(4)+'     '
     //+'N : '+chart.totalCnt+'     '
     //+'Sigma : '+chart.sigma.toFixed(4)+'    '
     //+'Cpk : '+chart.cpk.toFixed(4);
     //option.color='#26A800';

     }
     //failChart
     else if(chart.chartType==CHART_FAIL){
     var margin;
     //if(chart.realMin == chart.realMax){
     margin= Math.abs(chart.rangeMin)*0.001;
     margin=margin>0.001?margin:0.001;
     option.xAxis[0].min=chart.rangeMin -margin;

     margin= Math.abs(chart.rangeMax)*0.001;
     margin=margin>0.001?margin:0.001;
     option.xAxis[0].max=chart.rangeMax + margin;

     option.xAxis[0].min=option.xAxis[0].min.toFixed(4);
     option.xAxis[0].max=option.xAxis[0].max.toFixed(4);
     // }
     //option.title.text=chart.title+'的不良品分布图';
     //option.title.subtext= '                  '+'Average : '+chart.typicalValue.toFixed(4)+'     '
     //+'N : '+chart.totalCnt+'     '
     //+'Sigma : '+chart.sigma.toFixed(4)+'    ';
     //option.series[0].markLine.data='';
     //console.log(option.series[0].markLine);
     }*/
    return option;
  }

}


export default ChartUtils;
