(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-44944396"],{"1dde":function(e,t,l){var a=l("d039"),r=l("b622"),o=l("2d00"),n=r("species");e.exports=function(e){return o>=51||!a((function(){var t=[],l=t.constructor={};return l[n]=function(){return{foo:1}},1!==t[e](Boolean).foo}))}},4127:function(e,t,l){"use strict";var a=l("d233"),r=l("b313"),o={brackets:function(e){return e+"[]"},indices:function(e,t){return e+"["+t+"]"},repeat:function(e){return e}},n=Date.prototype.toISOString,s={delimiter:"&",encode:!0,encoder:a.encode,encodeValuesOnly:!1,serializeDate:function(e){return n.call(e)},skipNulls:!1,strictNullHandling:!1},i=function e(t,l,r,o,n,i,c,p,f,m,u,d){var h=t;if("function"===typeof c)h=c(l,h);else if(h instanceof Date)h=m(h);else if(null===h){if(o)return i&&!d?i(l,s.encoder):l;h=""}if("string"===typeof h||"number"===typeof h||"boolean"===typeof h||a.isBuffer(h)){if(i){var b=d?l:i(l,s.encoder);return[u(b)+"="+u(i(h,s.encoder))]}return[u(l)+"="+u(String(h))]}var v,x=[];if("undefined"===typeof h)return x;if(Array.isArray(c))v=c;else{var g=Object.keys(h);v=p?g.sort(p):g}for(var y=0;y<v.length;++y){var w=v[y];n&&null===h[w]||(x=Array.isArray(h)?x.concat(e(h[w],r(l,w),r,o,n,i,c,p,f,m,u,d)):x.concat(e(h[w],l+(f?"."+w:"["+w+"]"),r,o,n,i,c,p,f,m,u,d)))}return x};e.exports=function(e,t){var l=e,n=t?a.assign({},t):{};if(null!==n.encoder&&void 0!==n.encoder&&"function"!==typeof n.encoder)throw new TypeError("Encoder has to be a function.");var c="undefined"===typeof n.delimiter?s.delimiter:n.delimiter,p="boolean"===typeof n.strictNullHandling?n.strictNullHandling:s.strictNullHandling,f="boolean"===typeof n.skipNulls?n.skipNulls:s.skipNulls,m="boolean"===typeof n.encode?n.encode:s.encode,u="function"===typeof n.encoder?n.encoder:s.encoder,d="function"===typeof n.sort?n.sort:null,h="undefined"!==typeof n.allowDots&&n.allowDots,b="function"===typeof n.serializeDate?n.serializeDate:s.serializeDate,v="boolean"===typeof n.encodeValuesOnly?n.encodeValuesOnly:s.encodeValuesOnly;if("undefined"===typeof n.format)n.format=r["default"];else if(!Object.prototype.hasOwnProperty.call(r.formatters,n.format))throw new TypeError("Unknown format option provided.");var x,g,y=r.formatters[n.format];"function"===typeof n.filter?(g=n.filter,l=g("",l)):Array.isArray(n.filter)&&(g=n.filter,x=g);var w,k=[];if("object"!==typeof l||null===l)return"";w=n.arrayFormat in o?n.arrayFormat:"indices"in n?n.indices?"indices":"repeat":"indices";var D=o[w];x||(x=Object.keys(l)),d&&x.sort(d);for(var S=0;S<x.length;++S){var z=x[S];f&&null===l[z]||(k=k.concat(i(l[z],z,D,p,f,m?u:null,g,d,h,b,y,v)))}var F=k.join(c),O=!0===n.addQueryPrefix?"?":"";return F.length>0?O+F:""}},4328:function(e,t,l){"use strict";var a=l("4127"),r=l("9e6a"),o=l("b313");e.exports={formats:o,parse:r,stringify:a}},8418:function(e,t,l){"use strict";var a=l("a04b"),r=l("9bf2"),o=l("5c6c");e.exports=function(e,t,l){var n=a(t);n in e?r.f(e,n,o(0,l)):e[n]=l}},"9e6a":function(e,t,l){"use strict";var a=l("d233"),r=Object.prototype.hasOwnProperty,o={allowDots:!1,allowPrototypes:!1,arrayLimit:20,decoder:a.decode,delimiter:"&",depth:5,parameterLimit:1e3,plainObjects:!1,strictNullHandling:!1},n=function(e,t){for(var l={},a=t.ignoreQueryPrefix?e.replace(/^\?/,""):e,n=t.parameterLimit===1/0?void 0:t.parameterLimit,s=a.split(t.delimiter,n),i=0;i<s.length;++i){var c,p,f=s[i],m=f.indexOf("]="),u=-1===m?f.indexOf("="):m+1;-1===u?(c=t.decoder(f,o.decoder),p=t.strictNullHandling?null:""):(c=t.decoder(f.slice(0,u),o.decoder),p=t.decoder(f.slice(u+1),o.decoder)),r.call(l,c)?l[c]=[].concat(l[c]).concat(p):l[c]=p}return l},s=function(e,t,l){for(var a=t,r=e.length-1;r>=0;--r){var o,n=e[r];if("[]"===n)o=[],o=o.concat(a);else{o=l.plainObjects?Object.create(null):{};var s="["===n.charAt(0)&&"]"===n.charAt(n.length-1)?n.slice(1,-1):n,i=parseInt(s,10);!isNaN(i)&&n!==s&&String(i)===s&&i>=0&&l.parseArrays&&i<=l.arrayLimit?(o=[],o[i]=a):o[s]=a}a=o}return a},i=function(e,t,l){if(e){var a=l.allowDots?e.replace(/\.([^.[]+)/g,"[$1]"):e,o=/(\[[^[\]]*])/,n=/(\[[^[\]]*])/g,i=o.exec(a),c=i?a.slice(0,i.index):a,p=[];if(c){if(!l.plainObjects&&r.call(Object.prototype,c)&&!l.allowPrototypes)return;p.push(c)}var f=0;while(null!==(i=n.exec(a))&&f<l.depth){if(f+=1,!l.plainObjects&&r.call(Object.prototype,i[1].slice(1,-1))&&!l.allowPrototypes)return;p.push(i[1])}return i&&p.push("["+a.slice(i.index)+"]"),s(p,t,l)}};e.exports=function(e,t){var l=t?a.assign({},t):{};if(null!==l.decoder&&void 0!==l.decoder&&"function"!==typeof l.decoder)throw new TypeError("Decoder has to be a function.");if(l.ignoreQueryPrefix=!0===l.ignoreQueryPrefix,l.delimiter="string"===typeof l.delimiter||a.isRegExp(l.delimiter)?l.delimiter:o.delimiter,l.depth="number"===typeof l.depth?l.depth:o.depth,l.arrayLimit="number"===typeof l.arrayLimit?l.arrayLimit:o.arrayLimit,l.parseArrays=!1!==l.parseArrays,l.decoder="function"===typeof l.decoder?l.decoder:o.decoder,l.allowDots="boolean"===typeof l.allowDots?l.allowDots:o.allowDots,l.plainObjects="boolean"===typeof l.plainObjects?l.plainObjects:o.plainObjects,l.allowPrototypes="boolean"===typeof l.allowPrototypes?l.allowPrototypes:o.allowPrototypes,l.parameterLimit="number"===typeof l.parameterLimit?l.parameterLimit:o.parameterLimit,l.strictNullHandling="boolean"===typeof l.strictNullHandling?l.strictNullHandling:o.strictNullHandling,""===e||null===e||"undefined"===typeof e)return l.plainObjects?Object.create(null):{};for(var r="string"===typeof e?n(e,l):e,s=l.plainObjects?Object.create(null):{},c=Object.keys(r),p=0;p<c.length;++p){var f=c[p],m=i(f,r[f],l);s=a.merge(s,m,l)}return a.compact(s)}},b0c0:function(e,t,l){var a=l("83ab"),r=l("5e77").EXISTS,o=l("9bf2").f,n=Function.prototype,s=n.toString,i=/^\s*function ([^ (]*)/,c="name";a&&!r&&o(n,c,{configurable:!0,get:function(){try{return s.call(this).match(i)[1]}catch(e){return""}}})},b313:function(e,t,l){"use strict";var a=String.prototype.replace,r=/%20/g;e.exports={default:"RFC3986",formatters:{RFC1738:function(e){return a.call(e,r,"+")},RFC3986:function(e){return e}},RFC1738:"RFC1738",RFC3986:"RFC3986"}},d15a:function(e,t,l){"use strict";l.r(t);var a=function(){var e=this,t=e.$createElement,l=e._self._c||t;return l("div",[l("el-row",{staticStyle:{"margin-left":"auto","margin-right":"auto","margin-top":"20px","background-color":"rgb(248, 250, 251)"},attrs:{gutter:20}},[l("el-col",{attrs:{span:12}},[l("el-card",{staticStyle:{width:"600px",float:"right","min-height":"616px"},attrs:{"body-style":"padding-top:5px"}},[l("el-form",{ref:"form",attrs:{model:e.form,"label-width":"80px",rules:e.rules,"hide-required-asterisk":!0,"validate-on-rule-change":!1}},[l("el-tabs",{model:{value:e.activeName,callback:function(t){e.activeName=t},expression:"activeName"}},[l("el-tab-pane",{attrs:{label:"淡入部分",name:"second"}},[l("el-row",[l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"生存时间",prop:"inDuration","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"",maxlength:"10"},model:{value:e.form.inDuration,callback:function(t){e.$set(e.form,"inDuration",t)},expression:"form.inDuration"}},[l("template",{slot:"append"},[e._v("秒")])],2)],1)],1),l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"运动耗时",prop:"inMoveDuration","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"",maxlength:"10"},model:{value:e.form.inMoveDuration,callback:function(t){e.$set(e.form,"inMoveDuration",t)},expression:"form.inMoveDuration"}},[l("template",{slot:"append"},[e._v("毫秒")])],2)],1)],1)],1),l("el-row",[l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"起始x坐标",prop:"inStartX","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"起始x坐标",maxlength:"10"},model:{value:e.form.inStartX,callback:function(t){e.$set(e.form,"inStartX",t)},expression:"form.inStartX"}})],1)],1),l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"起始y坐标",prop:"inStartY","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"起始y坐标",maxlength:"10"},model:{value:e.form.inStartY,callback:function(t){e.$set(e.form,"inStartY",t)},expression:"form.inStartY"}})],1)],1)],1),l("el-row",[l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"结束x坐标",prop:"inEndX","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"起始x坐标",maxlength:"10"},model:{value:e.form.inEndX,callback:function(t){e.$set(e.form,"inEndX",t)},expression:"form.inEndX"}})],1)],1),l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"结束y坐标",prop:"inEndY","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"结束y坐标",maxlength:"10"},model:{value:e.form.inEndY,callback:function(t){e.$set(e.form,"inEndY",t)},expression:"form.inEndY"}})],1)],1)],1),l("el-row",[l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"透明度变化",prop:"inAlpha","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"格式：百分比-百分比"},model:{value:e.form.inAlpha,callback:function(t){e.$set(e.form,"inAlpha",t)},expression:"form.inAlpha"}})],1)],1),l("el-col",{attrs:{span:12}})],1)],1),l("el-tab-pane",{attrs:{label:"基本配置",name:"first"}},[l("el-row",[l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"账号缓存值1",prop:"cookie","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"SESSDATA，相当于发送的弹幕的账号",maxlength:"30"},model:{value:e.form.cookie,callback:function(t){e.$set(e.form,"cookie",t)},expression:"form.cookie"}})],1)],1),l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"视频BV号",prop:"bvid","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"请输入视频BV号",maxlength:"30"},model:{value:e.form.bvid,callback:function(t){e.$set(e.form,"bvid",t)},expression:"form.bvid"}})],1)],1)],1),l("el-row",[l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"账号缓存值2",prop:"csrf","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"bili_jct，用于发送接口账号校验",maxlength:"30"},model:{value:e.form.csrf,callback:function(t){e.$set(e.form,"csrf",t)},expression:"form.csrf"}})],1)],1),l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"分P号",prop:"part","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"默认1，分p时需填要发送弹幕所在的分p号"},model:{value:e.form.part,callback:function(t){e.$set(e.form,"part",t)},expression:"form.part"}})],1)],1)],1),l("el-row",[l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"首句时间",prop:"sendFirstDmTime","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"非必填，首句时间，调整时间轴"},model:{value:e.form.sendFirstDmTime,callback:function(t){e.$set(e.form,"sendFirstDmTime",t)},expression:"form.sendFirstDmTime"}})],1)],1),l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"开始句",prop:"sendStartDmRow","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"配合发送测试弹幕使用，2为发送第二句歌词",maxlength:"30"},model:{value:e.form.sendStartDmRow,callback:function(t){e.$set(e.form,"sendStartDmRow",t)},expression:"form.sendStartDmRow"}})],1)],1)],1),l("el-row",[l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"字体",prop:"fontFamily","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"字体",maxlength:"30"},model:{value:e.form.fontFamily,callback:function(t){e.$set(e.form,"fontFamily",t)},expression:"form.fontFamily"}})],1)],1),l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"文字描边",prop:"bord","label-width":"100px"}},[l("el-select",{model:{value:e.form.bord,callback:function(t){e.$set(e.form,"bord",t)},expression:"form.bord"}},[l("el-option",{attrs:{label:"无描边",value:0}}),l("el-option",{attrs:{label:"有描边",value:1}})],1)],1)],1)],1),l("el-row",[l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"字号",prop:"fontSize","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"请输入字号",maxlength:"3"},model:{value:e.form.fontSize,callback:function(t){e.$set(e.form,"fontSize",t)},expression:"form.fontSize"}})],1)],1),l("el-col",{attrs:{span:10}},[l("el-form-item",{attrs:{size:"small",label:"颜色",prop:"color","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"",maxlength:"30"},model:{value:e.form.color,callback:function(t){e.$set(e.form,"color",t)},expression:"form.color"}})],1)],1),l("el-col",{attrs:{span:2}},[l("el-color-picker",{staticStyle:{float:"right"},model:{value:e.hexColor,callback:function(t){e.hexColor=t},expression:"hexColor"}})],1)],1),l("el-row",[l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"透明度变化",prop:"alpha","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"格式：百分比-百分比",maxlength:"3"},model:{value:e.form.alpha,callback:function(t){e.$set(e.form,"alpha",t)},expression:"form.alpha"}})],1)],1),l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"运动耗时",prop:"moveDuration","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"运动耗时",maxlength:"10"},model:{value:e.form.moveDuration,callback:function(t){e.$set(e.form,"moveDuration",t)},expression:"form.moveDuration"}},[l("template",{slot:"append"},[e._v("毫秒")])],2)],1)],1)],1),l("el-row",[l("el-col",{attrs:{span:12}}),l("el-col",{attrs:{span:12}})],1),l("el-row",[l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"起始x坐标",prop:"startX","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"起始x坐标",maxlength:"10"},model:{value:e.form.startX,callback:function(t){e.$set(e.form,"startX",t)},expression:"form.startX"}})],1)],1),l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"起始y坐标",prop:"startY","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"起始y坐标",maxlength:"10"},model:{value:e.form.startY,callback:function(t){e.$set(e.form,"startY",t)},expression:"form.startY"}})],1)],1)],1),l("el-row",[l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"结束x坐标",prop:"endX","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"起始x坐标",maxlength:"10"},model:{value:e.form.endX,callback:function(t){e.$set(e.form,"endX",t)},expression:"form.endX"}})],1)],1),l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"结束y坐标",prop:"endY","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"结束y坐标",maxlength:"10"},model:{value:e.form.endY,callback:function(t){e.$set(e.form,"endY",t)},expression:"form.endY"}})],1)],1)],1),l("el-row",[l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"发送模式",prop:"sendMode","label-width":"100px"}},[l("el-select",{attrs:{placeholder:"请选择发送模式"},model:{value:e.form.sendMode,callback:function(t){e.$set(e.form,"sendMode",t)},expression:"form.sendMode"}},[l("el-option",{attrs:{label:"测试发送弹幕(发送前1条)",value:"0"}}),l("el-option",{attrs:{label:"正式发送弹幕",value:"1"}})],1)],1)],1),l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"弹幕池",prop:"pool","label-width":"100px"}},[l("el-select",{attrs:{placeholder:"请选择弹幕池"},model:{value:e.form.pool,callback:function(t){e.$set(e.form,"pool",t)},expression:"form.pool"}},[l("el-option",{attrs:{label:"普通池",value:0}}),l("el-option",{attrs:{label:"字幕池",value:1}})],1)],1)],1)],1),l("el-row",[l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"上传lrc或ass","label-width":"100px"}},[l("input",{attrs:{name:"uploadFile",type:"file",id:"uploadFile"},on:{change:function(t){return e.addFile(t,1)}}})])],1),l("el-col",{attrs:{span:12}})],1),l("el-row",{staticStyle:{"margin-top":"0px"}},[l("el-col",{attrs:{span:5}},[l("el-button",{attrs:{size:"medium"},on:{click:function(t){return e.submitPreviewForm()}}},[e._v("预览信息")])],1),l("el-col",{attrs:{span:5}},[l("el-button",{attrs:{size:"medium"},on:{click:function(t){return e.submitForm()}}},[e._v("发送弹幕")])],1),l("el-col",{attrs:{span:5}},[l("el-button",{attrs:{size:"medium"},on:{click:function(t){return e.stopSendDanmaku()}}},[e._v("停止发送")])],1)],1)],1),l("el-tab-pane",{attrs:{label:"淡出部分",name:"third"}},[l("el-row",[l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"生存时间",prop:"outDuration","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"",maxlength:"10"},model:{value:e.form.outDuration,callback:function(t){e.$set(e.form,"outDuration",t)},expression:"form.outDuration"}},[l("template",{slot:"append"},[e._v("秒")])],2)],1)],1),l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"运动耗时",prop:"outMoveDuration","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"",maxlength:"10"},model:{value:e.form.outMoveDuration,callback:function(t){e.$set(e.form,"outMoveDuration",t)},expression:"form.outMoveDuration"}},[l("template",{slot:"append"},[e._v("毫秒")])],2)],1)],1)],1),l("el-row",[l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"起始x坐标",prop:"outStartX","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"起始x坐标",maxlength:"10"},model:{value:e.form.outStartX,callback:function(t){e.$set(e.form,"outStartX",t)},expression:"form.outStartX"}})],1)],1),l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"起始y坐标",prop:"outStartY","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"起始y坐标",maxlength:"10"},model:{value:e.form.outStartY,callback:function(t){e.$set(e.form,"outStartY",t)},expression:"form.outStartY"}})],1)],1)],1),l("el-row",[l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"结束x坐标",prop:"outEndX","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"起始x坐标",maxlength:"10"},model:{value:e.form.outEndX,callback:function(t){e.$set(e.form,"outEndX",t)},expression:"form.outEndX"}})],1)],1),l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"结束y坐标",prop:"outEndY","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"结束y坐标",maxlength:"10"},model:{value:e.form.outEndY,callback:function(t){e.$set(e.form,"outEndY",t)},expression:"form.outEndY"}})],1)],1)],1),l("el-row",[l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"透明度变化",prop:"outAlpha","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"格式：百分比-百分比"},model:{value:e.form.outAlpha,callback:function(t){e.$set(e.form,"outAlpha",t)},expression:"form.outAlpha"}})],1)],1)],1)],1),l("el-tab-pane",{attrs:{label:"更多配置",name:"fourth"}},[l("el-row",[l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"Z轴旋转",prop:"zRotate","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"Z轴旋转",maxlength:"10"},model:{value:e.form.zRotate,callback:function(t){e.$set(e.form,"zRotate",t)},expression:"form.zRotate"}})],1)],1),l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"Y轴旋转",prop:"yRotate","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"Y轴旋转",maxlength:"10"},model:{value:e.form.yRotate,callback:function(t){e.$set(e.form,"yRotate",t)},expression:"form.yRotate"}})],1)],1)],1),l("el-row",[l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"lrc间隙时间",prop:"lrcIntervalTime","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"Lrc歌词用于处理句与句之间间隙时间,250为结尾提前250结束",maxlength:"10"},model:{value:e.form.lrcIntervalTime,callback:function(t){e.$set(e.form,"lrcIntervalTime",t)},expression:"form.lrcIntervalTime"}},[l("template",{slot:"append"},[e._v("毫秒")])],2)],1)],1),l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"重叠时间",prop:"overlapTime","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"淡入、中间、淡出弹幕之间重叠时间",maxlength:"10"},model:{value:e.form.overlapTime,callback:function(t){e.$set(e.form,"overlapTime",t)},expression:"form.overlapTime"}},[l("template",{slot:"append"},[e._v("毫秒")])],2)],1)],1)],1),l("el-row",[l("el-col",{attrs:{span:12}}),l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"small",label:"线性加速",prop:"access","label-width":"100px"}},[l("el-select",{attrs:{placeholder:"请选择弹幕池"},model:{value:e.form.access,callback:function(t){e.$set(e.form,"access",t)},expression:"form.access"}},[l("el-option",{attrs:{label:"无线性加速",value:0}}),l("el-option",{attrs:{label:"有线性加速",value:1}})],1)],1)],1)],1),l("el-row",[l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"middle",label:"发送间隔",prop:"sendInterval","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"发送弹幕间隔时间",maxlength:"30"},model:{value:e.form.sendInterval,callback:function(t){e.$set(e.form,"sendInterval",t)},expression:"form.sendInterval"}},[l("template",{slot:"append"},[e._v("秒")])],2)],1)],1),l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"middle",label:"附加时间",prop:"sendRandomTime","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"附加随机时间"},model:{value:e.form.sendRandomTime,callback:function(t){e.$set(e.form,"sendRandomTime",t)},expression:"form.sendRandomTime"}},[l("template",{slot:"append"},[e._v("秒")])],2)],1)],1)],1),l("el-row",[l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"middle",label:"效果模板",prop:"effectValue","label-width":"100px"}},[l("el-select",{attrs:{filterable:"",placeholder:"请选择"},on:{change:function(t){return e.selectTrigger(e.effectValue)}},model:{value:e.effectValue,callback:function(t){e.effectValue=t},expression:"effectValue"}},e._l(e.effectOptions,(function(e){return l("el-option",{key:e.effectTem,attrs:{label:e.effectTem,value:e}})})),1)],1)],1),l("el-col",{attrs:{span:12}},[l("el-form-item",{attrs:{size:"middle",label:"失败等待",prop:"sendRetryInterval","label-width":"100px"}},[l("el-input",{attrs:{placeholder:"失败等待",maxlength:"30"},model:{value:e.form.sendRetryInterval,callback:function(t){e.$set(e.form,"sendRetryInterval",t)},expression:"form.sendRetryInterval"}},[l("template",{slot:"append"},[e._v("分")])],2)],1)],1)],1),l("el-row",{staticStyle:{"margin-top":"0px"}},[l("el-col",{attrs:{span:5}},[l("el-button",{on:{click:function(t){return e.saveEffectTem()}}},[e._v("保存模板")])],1),l("el-col",{attrs:{span:5}},[l("el-button",{on:{click:function(t){return e.getEffectTem()}}},[e._v("读取模板")])],1)],1)],1)],1)],1)],1)],1),l("el-col",{attrs:{span:12}},[l("el-card",{staticStyle:{width:"600px",float:"left","min-height":"616px"}},[l("span"),l("el-input",{attrs:{type:"textarea",rows:2,placeholder:"发送日志显示区域",autosize:{minRows:26,maxRows:26}},model:{value:e.textarea,callback:function(t){e.textarea=t},expression:"textarea"}})],1)],1)],1)],1)},r=[],o=(l("fb6a"),l("b0c0"),l("4328"),{name:"Home",components:{},computed:{hexColor:{get:function(){return!this.form.color.indexOf("#")>=0?"#"+this.form.color:this.form.color},set:function(e){null!=e&&(this.form.color=e.slice(1))}}},created:function(){this.getEffectTem(),this.getConfig()},data:function(){return{value1:"",form:{cookie:null,csrf:null,bvid:null,part:"1",fontSize:22,color:"FFFFFF",zRotate:0,yRotate:0,fontFamily:"黑体",pool:0,mode:7,sendPrefix:null,sendSuffix:null,sendFirstDmTime:"",sendFirstDmOffset:null,sendInterval:"5",sendMode:"1",sendStartDmRow:"1",sendPreview:"1",bord:1,access:0,alpha:"1-1",startX:20,startY:60,endX:25,endY:60,moveDuration:500,inAlpha:"0-1",inDuration:.3,inMoveDuration:300,inStartX:5,inStartY:60,inEndX:20,inEndY:60,outAlpha:"1-0",outDuration:.3,outMoveDuration:300,outStartX:25,outStartY:40,outEndX:60,outEndY:60,overlapTime:50,outOffset:250,sendRandomTime:0,sendRetryInterval:1,lrcIntervalTime:250,effectTem:null},rules:{},textarea:"",txtFile:null,requestId:"",timerStatus:0,timer:null,uploadName:"请上传lrc或ass文件",activeName:"first",effectOptions:[],effectValue:null}},methods:{submitForm:function(){var e=this;null!=this.txtFile?this.$refs["form"].validate((function(t){if(t){var l=e;e.textarea="";var a=new FormData;for(var r in a.append("file",e.txtFile),e.form)null!=e.form[r]&&a.append(r,e.form[r]);e.createCode(),a.append("requestId",e.requestId);var o={headers:{"Content-Type":"application/x-www-form-urlencoded;charset=utf-8"}};e.axios.post("/danmaku/sendDanmakuM7",a,o).then((function(t){200==t.data.code&&(e.textarea=t.data.data.txtLog,console.log(e.textarea),l.stopSetInterval(),e.timerStatus=0)})).catch((function(e){l.stopSetInterval(),console.log(e)})),console.log("轮询弹幕日志接口"),e.createSetInterval()}})):this.$message({message:"请先上传文件!",type:"warning"})},submitPreviewForm:function(){var e=this;if(null!=this.txtFile){this.textarea="";var t=new FormData;for(var l in t.append("file",this.txtFile),this.form)null!=this.form[l]&&t.append(l,this.form[l]);t.set("sendMode",-1),this.createCode(),t.append("requestId","p"+this.requestId);var a={headers:{"Content-Type":"application/x-www-form-urlencoded;charset=utf-8"}};this.axios.post("/danmaku/sendDanmakuM7",t,a).then((function(t){200==t.data.code&&(e.textarea=t.data.msg)})).catch((function(e){console.log(e)}))}else this.$message({message:"请先上传文件!",type:"warning"})},getFileLog:function(){var e=this,t=new FormData;t.append("fileName",this.txtFile.name),t.append("requestId",this.requestId);var l={headers:{"Content-Type":"application/x-www-form-urlencoded;charset=utf-8"}};this.axios.post("/danmaku/getFileLog",t,l).then((function(t){200==t.data.code&&(e.textarea=t.data.data.txtLog)})).catch((function(e){this.stopSetInterval(),console.log(e)}))},stopSendDanmaku:function(){var e=this;if(""!=this.requestId){var t=new FormData;t.append("fileName",this.txtFile.name),t.append("requestId",this.requestId);var l={headers:{"Content-Type":"application/x-www-form-urlencoded;charset=utf-8"}};this.axios.post("/danmaku/stopSendDanmaku",t,l).then((function(t){200==t.data.code&&(e.textarea=t.data.data.txtLog)})).catch((function(e){this.stopSetInterval(),console.log(e)}))}},createSetInterval:function(){this.stopSetInterval();var e=this;this.timer=setInterval((function(){e.getFileLog()}),5e3)},stopSetInterval:function(){this.timer&&(clearInterval(this.timer),this.timer=null)},createCode:function(){for(var e=["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"],t=4,l="",a=0;a<t;a++){var r=Math.floor(26*Math.random());l+=e[r]}var o=(new Date).getHours()<10?"0"+(new Date).getHours():(new Date).getHours(),n=(new Date).getMinutes()<10?"0"+(new Date).getMinutes():(new Date).getMinutes();l=""+o+n+l,this.requestId=l,console.log(this.requestId)},addFile:function(e,t){var l=e.target.files[0];this.txtFile=l;var a=new FormData;a.append("file",l,l.name);var r=l.name.substring(l.name.lastIndexOf(".")+1),o="lrc"===r,n="ass"===r,s="m1"===r,i=l.size/1024/1024<10;o||n||s?i||(this.$message({message:"上传文件大小不能 10MB!",type:"warning"}),e.target.value="",this.txtFile=null,this.uploadName="请上传lrc或ass文件"):(this.$message({message:"上传文件只能是 lrc、ass格式!",type:"warning"}),this.txtFile=null,this.uploadName="请上传lrc或ass文件",e.target.value=""),this.uploadName=l.name},handleChangeLi:function(e,t){console.log(e),console.log(t),this.fileListAdd=t},handlePictureCardPreview:function(e){console.log(e)},saveEffectTem:function(){var e=this;this.$prompt("请输入保存模板名称","提示",{confirmButtonText:"确定",cancelButtonText:"取消"}).then((function(t){var l=t.value;e.form.effectTem=l,e.axios.post("/danmaku/saveEffectTemM7",e.form).then((function(e){200==e.data.code&&console.log(e.data)})).catch((function(e){console.log(e)})),e.$message({type:"success",message:"保存模板成功"})})).catch((function(){}))},getConfig:function(){var e=this;this.axios.get("/danmaku/getConfig").then((function(t){200==t.data.code&&(e.form.cookie=t.data.data.cookie,e.form.csrf=t.data.data.csrf,console.log(t.data))})).catch((function(e){console.log(e)}))},getEffectTem:function(){var e=this;this.axios.post("/danmaku/getEffectTemM7",this.form).then((function(t){200==t.data.code&&(e.effectOptions=t.data.data,console.log(t.data))})).catch((function(e){console.log(e)}))},selectTrigger:function(e){this.form=Object.assign(this.form,e),console.log(this.form)}},destroyed:function(){this.stopSetInterval()}}),n=o,s=l("2877"),i=Object(s["a"])(n,a,r,!1,null,"cf632312",null);t["default"]=i.exports},d233:function(e,t,l){"use strict";var a=Object.prototype.hasOwnProperty,r=function(){for(var e=[],t=0;t<256;++t)e.push("%"+((t<16?"0":"")+t.toString(16)).toUpperCase());return e}(),o=function(e){var t;while(e.length){var l=e.pop();if(t=l.obj[l.prop],Array.isArray(t)){for(var a=[],r=0;r<t.length;++r)"undefined"!==typeof t[r]&&a.push(t[r]);l.obj[l.prop]=a}}return t},n=function(e,t){for(var l=t&&t.plainObjects?Object.create(null):{},a=0;a<e.length;++a)"undefined"!==typeof e[a]&&(l[a]=e[a]);return l},s=function e(t,l,r){if(!l)return t;if("object"!==typeof l){if(Array.isArray(t))t.push(l);else{if("object"!==typeof t)return[t,l];(r.plainObjects||r.allowPrototypes||!a.call(Object.prototype,l))&&(t[l]=!0)}return t}if("object"!==typeof t)return[t].concat(l);var o=t;return Array.isArray(t)&&!Array.isArray(l)&&(o=n(t,r)),Array.isArray(t)&&Array.isArray(l)?(l.forEach((function(l,o){a.call(t,o)?t[o]&&"object"===typeof t[o]?t[o]=e(t[o],l,r):t.push(l):t[o]=l})),t):Object.keys(l).reduce((function(t,o){var n=l[o];return a.call(t,o)?t[o]=e(t[o],n,r):t[o]=n,t}),o)},i=function(e,t){return Object.keys(t).reduce((function(e,l){return e[l]=t[l],e}),e)},c=function(e){try{return decodeURIComponent(e.replace(/\+/g," "))}catch(t){return e}},p=function(e){if(0===e.length)return e;for(var t="string"===typeof e?e:String(e),l="",a=0;a<t.length;++a){var o=t.charCodeAt(a);45===o||46===o||95===o||126===o||o>=48&&o<=57||o>=65&&o<=90||o>=97&&o<=122?l+=t.charAt(a):o<128?l+=r[o]:o<2048?l+=r[192|o>>6]+r[128|63&o]:o<55296||o>=57344?l+=r[224|o>>12]+r[128|o>>6&63]+r[128|63&o]:(a+=1,o=65536+((1023&o)<<10|1023&t.charCodeAt(a)),l+=r[240|o>>18]+r[128|o>>12&63]+r[128|o>>6&63]+r[128|63&o])}return l},f=function(e){for(var t=[{obj:{o:e},prop:"o"}],l=[],a=0;a<t.length;++a)for(var r=t[a],n=r.obj[r.prop],s=Object.keys(n),i=0;i<s.length;++i){var c=s[i],p=n[c];"object"===typeof p&&null!==p&&-1===l.indexOf(p)&&(t.push({obj:n,prop:c}),l.push(p))}return o(t)},m=function(e){return"[object RegExp]"===Object.prototype.toString.call(e)},u=function(e){return null!==e&&"undefined"!==typeof e&&!!(e.constructor&&e.constructor.isBuffer&&e.constructor.isBuffer(e))};e.exports={arrayToObject:n,assign:i,compact:f,decode:c,encode:p,isBuffer:u,isRegExp:m,merge:s}},e8b5:function(e,t,l){var a=l("c6b6");e.exports=Array.isArray||function(e){return"Array"==a(e)}},fb6a:function(e,t,l){"use strict";var a=l("23e7"),r=l("e8b5"),o=l("68ee"),n=l("861d"),s=l("23cb"),i=l("07fa"),c=l("fc6a"),p=l("8418"),f=l("b622"),m=l("1dde"),u=m("slice"),d=f("species"),h=[].slice,b=Math.max;a({target:"Array",proto:!0,forced:!u},{slice:function(e,t){var l,a,f,m=c(this),u=i(m),v=s(e,u),x=s(void 0===t?u:t,u);if(r(m)&&(l=m.constructor,o(l)&&(l===Array||r(l.prototype))?l=void 0:n(l)&&(l=l[d],null===l&&(l=void 0)),l===Array||void 0===l))return h.call(m,v,x);for(a=new(void 0===l?Array:l)(b(x-v,0)),f=0;v<x;v++,f++)v in m&&p(a,f,m[v]);return a.length=f,a}})}}]);