(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-00b94523"],{"07e6":function(e,t,r){},"1dde":function(e,t,r){var n=r("d039"),o=r("b622"),a=r("2d00"),i=o("species");e.exports=function(e){return a>=51||!n((function(){var t=[],r=t.constructor={};return r[i]=function(){return{foo:1}},1!==t[e](Boolean).foo}))}},"21a6":function(e,t,r){(function(r){var n,o,a;(function(r,i){o=[],n=i,a="function"===typeof n?n.apply(t,o):n,void 0===a||(e.exports=a)})(0,(function(){"use strict";function t(e,t){return"undefined"==typeof t?t={autoBom:!1}:"object"!=typeof t&&(console.warn("Deprecated: Expected third argument to be a object"),t={autoBom:!t}),t.autoBom&&/^\s*(?:text\/\S*|application\/xml|\S*\/\S*\+xml)\s*;.*charset\s*=\s*utf-8/i.test(e.type)?new Blob(["\ufeff",e],{type:e.type}):e}function n(e,t,r){var n=new XMLHttpRequest;n.open("GET",e),n.responseType="blob",n.onload=function(){s(n.response,t,r)},n.onerror=function(){console.error("could not download file")},n.send()}function o(e){var t=new XMLHttpRequest;t.open("HEAD",e,!1);try{t.send()}catch(e){}return 200<=t.status&&299>=t.status}function a(e){try{e.dispatchEvent(new MouseEvent("click"))}catch(n){var t=document.createEvent("MouseEvents");t.initMouseEvent("click",!0,!0,window,0,0,0,80,20,!1,!1,!1,!1,0,null),e.dispatchEvent(t)}}var i="object"==typeof window&&window.window===window?window:"object"==typeof self&&self.self===self?self:"object"==typeof r&&r.global===r?r:void 0,l=i.navigator&&/Macintosh/.test(navigator.userAgent)&&/AppleWebKit/.test(navigator.userAgent)&&!/Safari/.test(navigator.userAgent),s=i.saveAs||("object"!=typeof window||window!==i?function(){}:"download"in HTMLAnchorElement.prototype&&!l?function(e,t,r){var l=i.URL||i.webkitURL,s=document.createElement("a");t=t||e.name||"download",s.download=t,s.rel="noopener","string"==typeof e?(s.href=e,s.origin===location.origin?a(s):o(s.href)?n(e,t,r):a(s,s.target="_blank")):(s.href=l.createObjectURL(e),setTimeout((function(){l.revokeObjectURL(s.href)}),4e4),setTimeout((function(){a(s)}),0))}:"msSaveOrOpenBlob"in navigator?function(e,r,i){if(r=r||e.name||"download","string"!=typeof e)navigator.msSaveOrOpenBlob(t(e,i),r);else if(o(e))n(e,r,i);else{var l=document.createElement("a");l.href=e,l.target="_blank",setTimeout((function(){a(l)}))}}:function(e,t,r,o){if(o=o||open("","_blank"),o&&(o.document.title=o.document.body.innerText="downloading..."),"string"==typeof e)return n(e,t,r);var a="application/octet-stream"===e.type,s=/constructor/i.test(i.HTMLElement)||i.safari,c=/CriOS\/[\d]+/.test(navigator.userAgent);if((c||a&&s||l)&&"undefined"!=typeof FileReader){var u=new FileReader;u.onloadend=function(){var e=u.result;e=c?e:e.replace(/^data:[^;]*;/,"data:attachment/file;"),o?o.location.href=e:location=e,o=null},u.readAsDataURL(e)}else{var f=i.URL||i.webkitURL,p=f.createObjectURL(e);o?o.location=p:location.href=p,o=null,setTimeout((function(){f.revokeObjectURL(p)}),4e4)}});i.saveAs=s.saveAs=s,e.exports=s}))}).call(this,r("c8ba"))},4127:function(e,t,r){"use strict";var n=r("d233"),o=r("b313"),a={brackets:function(e){return e+"[]"},indices:function(e,t){return e+"["+t+"]"},repeat:function(e){return e}},i=Date.prototype.toISOString,l={delimiter:"&",encode:!0,encoder:n.encode,encodeValuesOnly:!1,serializeDate:function(e){return i.call(e)},skipNulls:!1,strictNullHandling:!1},s=function e(t,r,o,a,i,s,c,u,f,p,d,m){var y=t;if("function"===typeof c)y=c(r,y);else if(y instanceof Date)y=p(y);else if(null===y){if(a)return s&&!m?s(r,l.encoder):r;y=""}if("string"===typeof y||"number"===typeof y||"boolean"===typeof y||n.isBuffer(y)){if(s){var b=m?r:s(r,l.encoder);return[d(b)+"="+d(s(y,l.encoder))]}return[d(r)+"="+d(String(y))]}var v,h=[];if("undefined"===typeof y)return h;if(Array.isArray(c))v=c;else{var g=Object.keys(y);v=u?g.sort(u):g}for(var w=0;w<v.length;++w){var x=v[w];i&&null===y[x]||(h=Array.isArray(y)?h.concat(e(y[x],o(r,x),o,a,i,s,c,u,f,p,d,m)):h.concat(e(y[x],r+(f?"."+x:"["+x+"]"),o,a,i,s,c,u,f,p,d,m)))}return h};e.exports=function(e,t){var r=e,i=t?n.assign({},t):{};if(null!==i.encoder&&void 0!==i.encoder&&"function"!==typeof i.encoder)throw new TypeError("Encoder has to be a function.");var c="undefined"===typeof i.delimiter?l.delimiter:i.delimiter,u="boolean"===typeof i.strictNullHandling?i.strictNullHandling:l.strictNullHandling,f="boolean"===typeof i.skipNulls?i.skipNulls:l.skipNulls,p="boolean"===typeof i.encode?i.encode:l.encode,d="function"===typeof i.encoder?i.encoder:l.encoder,m="function"===typeof i.sort?i.sort:null,y="undefined"!==typeof i.allowDots&&i.allowDots,b="function"===typeof i.serializeDate?i.serializeDate:l.serializeDate,v="boolean"===typeof i.encodeValuesOnly?i.encodeValuesOnly:l.encodeValuesOnly;if("undefined"===typeof i.format)i.format=o["default"];else if(!Object.prototype.hasOwnProperty.call(o.formatters,i.format))throw new TypeError("Unknown format option provided.");var h,g,w=o.formatters[i.format];"function"===typeof i.filter?(g=i.filter,r=g("",r)):Array.isArray(i.filter)&&(g=i.filter,h=g);var x,O=[];if("object"!==typeof r||null===r)return"";x=i.arrayFormat in a?i.arrayFormat:"indices"in i?i.indices?"indices":"repeat":"indices";var j=a[x];h||(h=Object.keys(r)),m&&h.sort(m);for(var A=0;A<h.length;++A){var k=h[A];f&&null===r[k]||(O=O.concat(s(r[k],k,j,u,f,p?d:null,g,m,y,b,w,v)))}var L=O.join(c),D=!0===i.addQueryPrefix?"?":"";return L.length>0?D+L:""}},4328:function(e,t,r){"use strict";var n=r("4127"),o=r("9e6a"),a=r("b313");e.exports={formats:a,parse:o,stringify:n}},5147:function(e,t,r){"use strict";r.r(t);var n=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",[r("el-row",{staticStyle:{"margin-left":"auto","margin-right":"auto","margin-top":"20px","background-color":"rgb(248, 250, 251)"},attrs:{gutter:20}},[r("el-col",{attrs:{span:12}},[r("el-card",{staticStyle:{width:"600px",float:"right","min-height":"616px"},attrs:{"body-style":"padding-top:22px"}},[r("el-form",{ref:"form",attrs:{model:e.form,"label-width":"80px",rules:e.rules,"hide-required-asterisk":!0,"validate-on-rule-change":!1}},[r("el-row",[r("el-col",{attrs:{span:12}},[r("el-form-item",{attrs:{size:"middle",label:"坐标相关",prop:"isPercentage","label-width":"100px"}},[r("el-checkbox",{model:{value:e.form.isPercentage,callback:function(t){e.$set(e.form,"isPercentage",t)},expression:"form.isPercentage"}},[e._v("是否百分比坐标")])],1)],1),r("el-col",{attrs:{span:12}},[r("el-form-item",{attrs:{size:"middle",label:"层级效果",prop:"isLayer","label-width":"100px"}},[r("el-checkbox",{model:{value:e.form.isLayer,callback:function(t){e.$set(e.form,"isLayer",t)},expression:"form.isLayer"}},[e._v("是否用z轴来增加时间")])],1)],1)],1),r("el-row",[r("el-col",{attrs:{span:12}},[r("el-form-item",{attrs:{size:"middle",label:"描边相关",prop:"isOutline","label-width":"100px"}},[r("el-checkbox",{model:{value:e.form.isOutline,callback:function(t){e.$set(e.form,"isOutline",t)},expression:"form.isOutline"}},[e._v("是否全部弹幕描边")])],1)],1),r("el-col",{attrs:{span:12}},[r("el-form-item",{attrs:{size:"middle",label:"线性加速",prop:"isLinearSpeedup","label-width":"100px"}},[r("el-checkbox",{model:{value:e.form.isLinearSpeedup,callback:function(t){e.$set(e.form,"isLinearSpeedup",t)},expression:"form.isLinearSpeedup"}},[e._v("是否全部线性加速")])],1)],1)],1),r("el-row",[r("el-col",{attrs:{span:12}},[r("el-form-item",{attrs:{size:"middle",label:"中间点分句",prop:"isPointCut","label-width":"100px"}},[r("el-checkbox",{model:{value:e.form.isPointCut,callback:function(t){e.$set(e.form,"isPointCut",t)},expression:"form.isPointCut"}},[e._v("是否中间点强制分句")])],1)],1),r("el-col",{attrs:{span:12}},[r("el-form-item",{attrs:{size:"middle",label:"修复闪帧",prop:"isDelayEndTime","label-width":"100px"}},[r("el-checkbox",{model:{value:e.form.isDelayEndTime,callback:function(t){e.$set(e.form,"isDelayEndTime",t)},expression:"form.isDelayEndTime"}},[e._v("是否结束时间延长50ms "),r("el-tooltip",{staticClass:"item",attrs:{effect:"light",content:"",placement:"right-start"}},[r("i",{staticClass:"el-icon-question"}),r("div",{attrs:{slot:"content"},slot:"content"},[e._v("解决PC端时间连续的两段弹幕之间闪烁问题"),r("br"),e._v("但手机端可能会有字重叠观感")])])],1)],1)],1)],1),r("el-row",[r("el-col",{attrs:{span:12}},[r("div",{staticClass:"el-form-item"},[r("el-form-item",{attrs:{size:"middle",label:"上传exo文件","label-width":"100px"}},[r("input",{attrs:{name:"uploadFile",type:"file",id:"uploadFile"},on:{change:function(t){return e.addFile(t,1)}}})])],1)]),r("el-col",{attrs:{span:12}})],1),r("el-row",{staticStyle:{"margin-top":"0px"}},[r("el-col",{attrs:{span:6}},[r("el-button",{on:{click:function(t){return e.submitForm("preview")}}},[e._v("预览XML文件")])],1),r("el-col",{attrs:{span:6}},[r("el-button",{on:{click:function(t){return e.submitForm("save")}}},[e._v("下载XML文件")])],1)],1)],1)],1)],1),r("el-col",{attrs:{span:12}},[r("el-card",{staticStyle:{width:"600px",float:"left","min-height":"616px"}},[r("el-input",{attrs:{type:"textarea",rows:2,placeholder:"XML显示区域",autosize:{minRows:22,maxRows:22},wrap:"off"},model:{value:e.textarea,callback:function(t){e.textarea=t},expression:"textarea"}}),r("el-input",{staticStyle:{"margin-top":"10px"},attrs:{type:"textarea",placeholder:"预览命令显示区域",rows:2,wrap:"off",autosize:{minRows:4,maxRows:4}},model:{value:e.preTextarea,callback:function(t){e.preTextarea=t},expression:"preTextarea"}})],1)],1)],1)],1)},o=[],a=(r("fb6a"),r("b0c0"),r("4328"),r("21a6")),i={name:"Home",components:{},computed:{hexColor:{get:function(){return!this.form.color.indexOf("#")>=0?"#"+this.form.color:this.form.color},set:function(e){null!=e&&(this.form.color=e.slice(1))}}},data:function(){return{value1:"",form:{screenWidth:850,screenHeight:560,isPercentage:!1,isLayer:!0,isOutline:!1,isLinearSpeedup:!1,isDelayEndTime:!0,isPointCut:!1,pool:"0",mode:"4",sendPrefix:null,sendSuffix:null,sendFirstDmTime:null,sendFirstDmOffset:null,sendInterval:"6",sendMode:"1",sendStartDmRow:"1",sendPreview:"1",sendRandomTime:"0",sendRetryInterval:"1"},rules:{},textarea:"",preTextarea:"",txtFile:null,requestId:"",timerStatus:0,timer:null,uploadName:"请上传lrc或ass文件",activeName:"first"}},methods:{submitForm:function(e){var t=this;null!=this.txtFile?this.$refs["form"].validate((function(r){if(r){t.textarea="";var n=new FormData;for(var o in n.append("file",t.txtFile),t.form)null!=t.form[o]&&n.append(o,t.form[o]);t.createCode(),n.append("requestId",t.requestId);var i={headers:{"Content-Type":"application/x-www-form-urlencoded;charset=utf-8"}};t.axios.post("/aul/exo2xml",n,i).then((function(r){if(200==r.data.code&&(t.textarea=r.data.data.xmlStr,t.preTextarea=r.data.data.preStr,"save"==e)){var n=r.data.data.xmlStr,o=new Blob([n],{type:"text/plain;charset=utf-8"}),i=t.txtFile.name.substring(0,t.txtFile.name.lastIndexOf("."))+".xml";Object(a["saveAs"])(o,i)}})).catch((function(e){console.log(e)}))}})):this.$message({message:"请先上传文件!",type:"warning"})},createCode:function(){for(var e=["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"],t=4,r="",n=0;n<t;n++){var o=Math.floor(26*Math.random());r+=e[o]}var a=(new Date).getHours()<10?"0"+(new Date).getHours():(new Date).getHours(),i=(new Date).getMinutes()<10?"0"+(new Date).getMinutes():(new Date).getMinutes();r=""+a+i+r,this.requestId=r},addFile:function(e,t){var r=e.target.files[0];this.txtFile=r;var n=new FormData;n.append("file",r,r.name);var o=r.name.substring(r.name.lastIndexOf(".")+1),a="exo"===o;r.size;a||(this.$message({message:"上传文件只能是exo格式!",type:"warning"}),this.txtFile=null,this.uploadName="上传文件只能是exo格式",e.target.value=""),this.uploadName=r.name}}},l=i,s=(r("c9ca"),r("2877")),c=Object(s["a"])(l,n,o,!1,null,"6e3bf0ee",null);t["default"]=c.exports},8418:function(e,t,r){"use strict";var n=r("a04b"),o=r("9bf2"),a=r("5c6c");e.exports=function(e,t,r){var i=n(t);i in e?o.f(e,i,a(0,r)):e[i]=r}},"9e6a":function(e,t,r){"use strict";var n=r("d233"),o=Object.prototype.hasOwnProperty,a={allowDots:!1,allowPrototypes:!1,arrayLimit:20,decoder:n.decode,delimiter:"&",depth:5,parameterLimit:1e3,plainObjects:!1,strictNullHandling:!1},i=function(e,t){for(var r={},n=t.ignoreQueryPrefix?e.replace(/^\?/,""):e,i=t.parameterLimit===1/0?void 0:t.parameterLimit,l=n.split(t.delimiter,i),s=0;s<l.length;++s){var c,u,f=l[s],p=f.indexOf("]="),d=-1===p?f.indexOf("="):p+1;-1===d?(c=t.decoder(f,a.decoder),u=t.strictNullHandling?null:""):(c=t.decoder(f.slice(0,d),a.decoder),u=t.decoder(f.slice(d+1),a.decoder)),o.call(r,c)?r[c]=[].concat(r[c]).concat(u):r[c]=u}return r},l=function(e,t,r){for(var n=t,o=e.length-1;o>=0;--o){var a,i=e[o];if("[]"===i)a=[],a=a.concat(n);else{a=r.plainObjects?Object.create(null):{};var l="["===i.charAt(0)&&"]"===i.charAt(i.length-1)?i.slice(1,-1):i,s=parseInt(l,10);!isNaN(s)&&i!==l&&String(s)===l&&s>=0&&r.parseArrays&&s<=r.arrayLimit?(a=[],a[s]=n):a[l]=n}n=a}return n},s=function(e,t,r){if(e){var n=r.allowDots?e.replace(/\.([^.[]+)/g,"[$1]"):e,a=/(\[[^[\]]*])/,i=/(\[[^[\]]*])/g,s=a.exec(n),c=s?n.slice(0,s.index):n,u=[];if(c){if(!r.plainObjects&&o.call(Object.prototype,c)&&!r.allowPrototypes)return;u.push(c)}var f=0;while(null!==(s=i.exec(n))&&f<r.depth){if(f+=1,!r.plainObjects&&o.call(Object.prototype,s[1].slice(1,-1))&&!r.allowPrototypes)return;u.push(s[1])}return s&&u.push("["+n.slice(s.index)+"]"),l(u,t,r)}};e.exports=function(e,t){var r=t?n.assign({},t):{};if(null!==r.decoder&&void 0!==r.decoder&&"function"!==typeof r.decoder)throw new TypeError("Decoder has to be a function.");if(r.ignoreQueryPrefix=!0===r.ignoreQueryPrefix,r.delimiter="string"===typeof r.delimiter||n.isRegExp(r.delimiter)?r.delimiter:a.delimiter,r.depth="number"===typeof r.depth?r.depth:a.depth,r.arrayLimit="number"===typeof r.arrayLimit?r.arrayLimit:a.arrayLimit,r.parseArrays=!1!==r.parseArrays,r.decoder="function"===typeof r.decoder?r.decoder:a.decoder,r.allowDots="boolean"===typeof r.allowDots?r.allowDots:a.allowDots,r.plainObjects="boolean"===typeof r.plainObjects?r.plainObjects:a.plainObjects,r.allowPrototypes="boolean"===typeof r.allowPrototypes?r.allowPrototypes:a.allowPrototypes,r.parameterLimit="number"===typeof r.parameterLimit?r.parameterLimit:a.parameterLimit,r.strictNullHandling="boolean"===typeof r.strictNullHandling?r.strictNullHandling:a.strictNullHandling,""===e||null===e||"undefined"===typeof e)return r.plainObjects?Object.create(null):{};for(var o="string"===typeof e?i(e,r):e,l=r.plainObjects?Object.create(null):{},c=Object.keys(o),u=0;u<c.length;++u){var f=c[u],p=s(f,o[f],r);l=n.merge(l,p,r)}return n.compact(l)}},b0c0:function(e,t,r){var n=r("83ab"),o=r("5e77").EXISTS,a=r("9bf2").f,i=Function.prototype,l=i.toString,s=/^\s*function ([^ (]*)/,c="name";n&&!o&&a(i,c,{configurable:!0,get:function(){try{return l.call(this).match(s)[1]}catch(e){return""}}})},b313:function(e,t,r){"use strict";var n=String.prototype.replace,o=/%20/g;e.exports={default:"RFC3986",formatters:{RFC1738:function(e){return n.call(e,o,"+")},RFC3986:function(e){return e}},RFC1738:"RFC1738",RFC3986:"RFC3986"}},c9ca:function(e,t,r){"use strict";r("07e6")},d233:function(e,t,r){"use strict";var n=Object.prototype.hasOwnProperty,o=function(){for(var e=[],t=0;t<256;++t)e.push("%"+((t<16?"0":"")+t.toString(16)).toUpperCase());return e}(),a=function(e){var t;while(e.length){var r=e.pop();if(t=r.obj[r.prop],Array.isArray(t)){for(var n=[],o=0;o<t.length;++o)"undefined"!==typeof t[o]&&n.push(t[o]);r.obj[r.prop]=n}}return t},i=function(e,t){for(var r=t&&t.plainObjects?Object.create(null):{},n=0;n<e.length;++n)"undefined"!==typeof e[n]&&(r[n]=e[n]);return r},l=function e(t,r,o){if(!r)return t;if("object"!==typeof r){if(Array.isArray(t))t.push(r);else{if("object"!==typeof t)return[t,r];(o.plainObjects||o.allowPrototypes||!n.call(Object.prototype,r))&&(t[r]=!0)}return t}if("object"!==typeof t)return[t].concat(r);var a=t;return Array.isArray(t)&&!Array.isArray(r)&&(a=i(t,o)),Array.isArray(t)&&Array.isArray(r)?(r.forEach((function(r,a){n.call(t,a)?t[a]&&"object"===typeof t[a]?t[a]=e(t[a],r,o):t.push(r):t[a]=r})),t):Object.keys(r).reduce((function(t,a){var i=r[a];return n.call(t,a)?t[a]=e(t[a],i,o):t[a]=i,t}),a)},s=function(e,t){return Object.keys(t).reduce((function(e,r){return e[r]=t[r],e}),e)},c=function(e){try{return decodeURIComponent(e.replace(/\+/g," "))}catch(t){return e}},u=function(e){if(0===e.length)return e;for(var t="string"===typeof e?e:String(e),r="",n=0;n<t.length;++n){var a=t.charCodeAt(n);45===a||46===a||95===a||126===a||a>=48&&a<=57||a>=65&&a<=90||a>=97&&a<=122?r+=t.charAt(n):a<128?r+=o[a]:a<2048?r+=o[192|a>>6]+o[128|63&a]:a<55296||a>=57344?r+=o[224|a>>12]+o[128|a>>6&63]+o[128|63&a]:(n+=1,a=65536+((1023&a)<<10|1023&t.charCodeAt(n)),r+=o[240|a>>18]+o[128|a>>12&63]+o[128|a>>6&63]+o[128|63&a])}return r},f=function(e){for(var t=[{obj:{o:e},prop:"o"}],r=[],n=0;n<t.length;++n)for(var o=t[n],i=o.obj[o.prop],l=Object.keys(i),s=0;s<l.length;++s){var c=l[s],u=i[c];"object"===typeof u&&null!==u&&-1===r.indexOf(u)&&(t.push({obj:i,prop:c}),r.push(u))}return a(t)},p=function(e){return"[object RegExp]"===Object.prototype.toString.call(e)},d=function(e){return null!==e&&"undefined"!==typeof e&&!!(e.constructor&&e.constructor.isBuffer&&e.constructor.isBuffer(e))};e.exports={arrayToObject:i,assign:s,compact:f,decode:c,encode:u,isBuffer:d,isRegExp:p,merge:l}},e8b5:function(e,t,r){var n=r("c6b6");e.exports=Array.isArray||function(e){return"Array"==n(e)}},fb6a:function(e,t,r){"use strict";var n=r("23e7"),o=r("e8b5"),a=r("68ee"),i=r("861d"),l=r("23cb"),s=r("07fa"),c=r("fc6a"),u=r("8418"),f=r("b622"),p=r("1dde"),d=p("slice"),m=f("species"),y=[].slice,b=Math.max;n({target:"Array",proto:!0,forced:!d},{slice:function(e,t){var r,n,f,p=c(this),d=s(p),v=l(e,d),h=l(void 0===t?d:t,d);if(o(p)&&(r=p.constructor,a(r)&&(r===Array||o(r.prototype))?r=void 0:i(r)&&(r=r[m],null===r&&(r=void 0)),r===Array||void 0===r))return y.call(p,v,h);for(n=new(void 0===r?Array:r)(b(h-v,0)),f=0;v<h;v++,f++)v in p&&u(n,f,p[v]);return n.length=f,n}})}}]);