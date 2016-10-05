import _ from 'lodash'
var fetchData=(url,option,callBack)=>{
	if(!option){
    option={};
  }
	var defaultOption={
    method:option.method || 'POST',
    headers: option.headers || {
      "Content-type":"application/x-www-form-urlencoded;charset=UTF-8"
    }
    
    
  }
  //if(optionoption.data)
  if(defaultOption.method=='GET'){
    url+='?'+toUrlEncoded(option.data);
  }else{
    defaultOption.body=toUrlEncoded(option.data);//"reportName="+value
  }
  console.log("fetchData:"+url);
  console.log("defaultOption:",defaultOption);
	var result = fetch(url,defaultOption);
      result.then(function(response) {
        //console.log('response', response)
        //console.log('header', response.headers.get('Content-Type'))
        return response.json()
      }).then(function(data) {
        console.log('fetchData:', data)
        callBack(data);
      }).catch(function(ex) {
        console.log('failed', ex)
      })



   
}
function toUrlEncoded(obj){
  console.log("urlEncode:",obj);
      var paramStr="";
      for(var key in obj){


       if(param instanceof String||param instanceof Number||param instanceof Boolean){
            paramStr+="&"+key+"="+encodeURIComponent(param);
       }else{        _.each(param,function(i){
              var k=key==null?i:key+(param instanceof Array?"["+i+"]":"."+i);
              paramStr+='&'+toUrlEncoded(this, k);
           });
      }
    }
      return paramStr.substr(1);
  };

exports['default'] = {
	fetchData:fetchData,
  toUrlEncoded:toUrlEncoded
}
module.exports = exports['default'];
