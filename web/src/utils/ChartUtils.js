import echarts from 'echarts/lib/echarts'
import 'echarts/lib/chart/bar'
import 'echarts/lib/component/tooltip'
import 'echarts/lib/component/title'
import 'echarts/lib/component/markLine'
import 'echarts/lib/component/dataZoom'
class ChartUtils{
  static createChart(element,option){
    var myChart = echarts.init(element);
    myChart.setOption(option);
  }
  static test(){
    console.log("test")
  }
	static getBarChartOption(chart){
    
    var option = {
    title : {
        text:chart.title, // chart.title
        subtext:'funtest',
        shadowColor: 'rgba(0, 0, 0, 0.5)',
        shadowBlur: 10,
        
        subtextStyle:{
          fontSize:16,
          fontWeight:'bold',
          color:'#C23531'
        }
    },
    backgroundColor:'#f3f3f3',
    grid:{
      top:'15%'
    },
    tooltip : {
        trigger: 'axis',
        axisPointer:{
            show: true,
            type : 'cross',
            lineStyle: {
                type : 'dashed',
                width : 5
            }
        }/*
        formatter : function (params) {
            return params.seriesName + ' : [ '
                   + params.value[0] + ', ' 
                   + params.value[1] + ' ]';
        }*/
    },
    legend: {
        data:[{name:chart.title,textStyle:{'fontSize':'20',fontWeight:'bold'}}]
    },
    toolbox: {
        show : true,
        feature : {
            mark : {show: true},
            dataView : {show: true, readOnly: false},
            magicType : {show: true, type: ['line', 'bar']},
            restore : {show: true},
            saveAsImage : {show: true}
        }
    },
    xAxis : [
        {
            type : 'value',
            scale:true,
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
    yAxis : [
        {
            type : 'value',
            max:Math.ceil(chart.quantityMax*1.1),
            axisLine: {
                show:false,
                lineStyle: {
                    color: '#dc143c'
                }
            }
        }
    ],
    
    
    series : [
              {
                  name:chart.title,
                  type:'bar',          
                  barWidth:5,
                  
                  data:eval(chart.datas),
                  markLine : {
                      data :[
                             {
                                 name: '平均线',
                                 // 支持 'average', 'min', 'max'
                                 type: 'average',
                                 value:'gg',
                                 label:{
                                  
                                 }
                             },
                             
                             [{
                                 name: 'LSL',
                                 coord: [chart.limitMin,0],
                                 value:'gg'
                                 //coord: [150,500]
                             }, {
                               coord: [chart.limitMin, Math.ceil(chart.quantityMax*1.1)]
                               //coord: [150, 400]
                             }],
                             [{
                                 name: 'LSH',
                                 coord: [chart.limitMax,0]
                             }, {
                               coord: [chart.limitMax, Math.ceil(chart.quantityMax*1.1)]
                             }],/*
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
      
    };/*
    if(chart.chartType==0){
         option.xAxis[0].min=chart.limitMin - Math.abs(chart.limitMin)*0.005;  
          option.xAxis[0].max=chart.limitMax + Math.abs(chart.limitMax)*0.005;    
          if((chart.limitMin>=-10000 && chart.limitMin<=-9999) ){
              option.xAxis[0].min=chart.realMin - Math.abs(chart.realMin)*0.005; 
          }
          if(chart.limitMax>=9999 && chart.limitMax<=10000){
            option.xAxis[0].max=chart.realMax + Math.abs(chart.realMax)*0.005; 
          }
          option.xAxis[0].min=option.xAxis[0].min.toFixed(4);
          option.xAxis[0].max=option.xAxis[0].max.toFixed(4);
            //option.title.text=chart.title+"的良品分布图";
            option.title.subtext="                  "+ 'Average : '+chart.typicalValue.toFixed(4)+"     "
                +'N : '+chart.totalCnt+"     "
                +'Sigma : '+chart.sigma.toFixed(4)+"    " 
                +'Cpk : '+chart.cpk.toFixed(4);
            //option.color='#26A800';

        }
       //failChart
       else if(chart.chartType==-1){
          var margin;
          //if(chart.realMin == chart.realMax){
              margin= Math.abs(chart.realMin)*0.001;
              margin=margin>0.001?margin:0.001;
              option.xAxis[0].min=chart.realMin -margin; 

              margin= Math.abs(chart.realMax)*0.001;
              margin=margin>0.001?margin:0.001;
              option.xAxis[0].max=chart.realMax + margin; 
              
          option.xAxis[0].min=option.xAxis[0].min.toFixed(4);
          option.xAxis[0].max=option.xAxis[0].max.toFixed(4);
         // }
          //option.title.text=chart.title+"的不良品分布图";
          option.title.subtext= "                  "+'Average : '+chart.typicalValue.toFixed(4)+"     "
                +'N : '+chart.totalCnt+"     "
                +'Sigma : '+chart.sigma.toFixed(4)+"    "; 
          option.series[0].markLine.data='';     
          //console.log(option.series[0].markLine);
       }*/
    return option;
	}

}


export default ChartUtils