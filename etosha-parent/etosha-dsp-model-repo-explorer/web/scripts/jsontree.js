/*! json-tree - v0.2.1 - 2015-07-29 */
var JSONTree=function(){var a={"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#x27;","/":"&#x2F;"},b=0,c=0;this.create=function(a,b){return c+=1,p(f(a,0,!1),{"class":"jstValue"})};var d=function(b){return b.replace(/[&<>'"]/g,function(b){return a[b]})},e=function(){return c+"_"+b++},f=function(a,b,c){if(null===a)return l(c?b:0);var d=typeof a;switch(d){case"boolean":return k(a,c?b:0);case"number":return j(a,c?b:0);case"string":return i(a,c?b:0);default:return a instanceof Array?h(a,b,c):g(a,b,c)}},g=function(a,b,c){var d=e(),f=Object.keys(a).map(function(c){return m(c,a[c],b+1,!0)}).join(o()),g=[r("{",c?b:0,d),p(f,{id:d}),s("}",b)].join("\n");return p(g,{})},h=function(a,b,c){var d=e(),g=a.map(function(a){return f(a,b+1,!0)}).join(o()),h=[r("[",c?b:0,d),p(g,{id:d}),s("]",b)].join("\n");return h},i=function(a,b){return p(t(n(d(a)),b),{"class":"jstStr"})},j=function(a,b){return p(t(a,b),{"class":"jstNum"})},k=function(a,b){return p(t(a,b),{"class":"jstBool"})},l=function(a){return p(t("null",a),{"class":"jstNull"})},m=function(a,b,c){var e=t(n(d(a))+": ",c),g=p(f(b,c,!1),{});return p(e+g,{"class":"jstProperty"})},n=function(a){return'"'+a+'"'},o=function(){return p(",\n",{"class":"jstComma"})},p=function(a,b){return q("span",b,a)},q=function(a,b,c){return"<"+a+Object.keys(b).map(function(a){return" "+a+'="'+b[a]+'"'}).join("")+">"+c+"</"+a+">"},r=function(a,b,c){return p(t(a,b),{"class":"jstBracket"})+p("",{"class":"jstFold",onclick:"JSONTree.toggle('"+c+"')"})};this.toggle=function(a){var b=document.getElementById(a),c=b.parentNode,d=b.previousElementSibling;""===b.className?(b.className="jstHiddenBlock",c.className="jstFolded",d.className="jstExpand"):(b.className="",c.className="",d.className="jstFold")};var s=function(a,b){return p(t(a,b),{})},t=function(a,b){return Array(2*b+1).join(" ")+a};return this}();  