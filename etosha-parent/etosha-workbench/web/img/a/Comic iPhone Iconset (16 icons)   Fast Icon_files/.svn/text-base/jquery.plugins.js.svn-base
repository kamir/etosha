/* ----------------------------------------------------------------------
* -- jQuery Plugins
* ----------------------------------------------------------------------
* - hoverIntent (delayed hovers)
* - ajaxLoader (modified)
* - JSON
*/

/**
* hoverIntent r5 // 2007.03.27 // jQuery 1.1.2+
* <http://cherne.net/brian/resources/jquery.hoverIntent.html>
* 
* @param  f  onMouseOver function || An object with configuration options
* @param  g  onMouseOut function  || Nothing (use configuration options object)
* @author    Brian Cherne <brian@cherne.net>
*/
(function($){$.fn.hoverIntent=function(f,g){var cfg={sensitivity:7,interval:100,timeout:0};cfg=$.extend(cfg,g?{over:f,out:g}:f);var cX,cY,pX,pY;var track=function(ev){cX=ev.pageX;cY=ev.pageY;};var compare=function(ev,ob){ob.hoverIntent_t=clearTimeout(ob.hoverIntent_t);if((Math.abs(pX-cX)+Math.abs(pY-cY))<cfg.sensitivity){$(ob).unbind("mousemove",track);ob.hoverIntent_s=1;return cfg.over.apply(ob,[ev]);}else{pX=cX;pY=cY;ob.hoverIntent_t=setTimeout(function(){compare(ev,ob);},cfg.interval);}};var delay=function(ev,ob){ob.hoverIntent_t=clearTimeout(ob.hoverIntent_t);ob.hoverIntent_s=0;return cfg.out.apply(ob,[ev]);};var handleHover=function(e){var p=(e.type=="mouseover"?e.fromElement:e.toElement)||e.relatedTarget;while(p&&p!=this){try{p=p.parentNode;}catch(e){p=this;}}if(p==this){return false;}var ev=jQuery.extend({},e);var ob=this;if(ob.hoverIntent_t){ob.hoverIntent_t=clearTimeout(ob.hoverIntent_t);}if(e.type=="mouseover"){pX=ev.pageX;pY=ev.pageY;$(ob).bind("mousemove",track);if(ob.hoverIntent_s!=1){ob.hoverIntent_t=setTimeout(function(){compare(ev,ob);},cfg.interval);}}else{$(ob).unbind("mousemove",track);if(ob.hoverIntent_s==1){ob.hoverIntent_t=setTimeout(function(){delay(ev,ob);},cfg.timeout);}}};return this.mouseover(handleHover).mouseout(handleHover);};})(jQuery);


// ----------------------------------------------------------------------
// -- jQuery Plugin: ajaxLoader (modified)
// ----------------------------------------------------------------------
// -- jQuery Plugin 'ajaxLoader' (by LD Jan. 2010)
// modified from Original Plugin 'ajaxLoader' / Andreas Lagerkvist
jQuery.fn.ajaxLoader = function (conf) {
	var config = jQuery.extend({
		className:	'jquery-ajax-loader'
	}, conf);

	return this.each(function () {
		var t = jQuery(this);

		if (!this.ajaxLoader) {
			var offset = t.offset();
			var dim = {
				left:	offset.left, 
				top:	offset.top, 
				width:	t.outerWidth(), 
				height:	t.outerHeight()
			};

			if (dim.width<100)
				dim.width=100;
			if (dim.height<100)
				dim.height=100;

			this.ajaxLoader = jQuery('<div class="' + config.className + '"></div>').css({
				position:	'absolute', 
				'z-index':	'2000',
				left:		'0px', 
				top:		'0px',
				width:		dim.width + 'px',
				height:		dim.height + 'px'
			}).prependTo(t).hide();
		}

		this.ajaxLoader.show();
	});
};

jQuery.fn.ajaxLoaderRemove = function () {
	return this.each(function () {
		if (this.ajaxLoader) {
			this.ajaxLoader.fadeOut(500);
		}
	});
};


// ------------------------
// -- JQuery Plugin: JSON
// ------------------------
jQuery.JSON={useHasOwn:({}.hasOwnProperty?true:false),pad:function(n){return n<10?"0"+n:n;},m:{"\b":'\\b',"\t":'\\t',"\n":'\\n',"\f":'\\f',"\r":'\\r','"':'\\"',"\\":'\\\\'},encodeString:function(s){if(/["\\\x00-\x1f]/.test(s)){return'"'+s.replace(/([\x00-\x1f\\"])/g,function(a,b){var c=m[b];if(c){return c;}
c=b.charCodeAt();return"\\u00"+
Math.floor(c/16).toString(16)+
(c%16).toString(16);})+'"';}
return'"'+s+'"';},encodeArray:function(o){var a=["["],b,i,l=o.length,v;for(i=0;i<l;i+=1){v=o[i];switch(typeof v){case"undefined":case"function":case"unknown":break;default:if(b){a.push(',');}
a.push(v===null?"null":this.encode(v));b=true;}}
a.push("]");return a.join("");},encodeDate:function(o){return'"'+o.getFullYear()+"-"+
pad(o.getMonth()+1)+"-"+
pad(o.getDate())+"T"+
pad(o.getHours())+":"+
pad(o.getMinutes())+":"+
pad(o.getSeconds())+'"';},encode:function(o){if(typeof o=="undefined"||o===null){return"null";}else if(o instanceof Array){return this.encodeArray(o);}else if(o instanceof Date){return this.encodeDate(o);}else if(typeof o=="string"){return this.encodeString(o);}else if(typeof o=="number"){return isFinite(o)?String(o):"null";}else if(typeof o=="boolean"){return String(o);}else{var self=this;var a=["{"],b,i,v;for(i in o){if(!this.useHasOwn||o.hasOwnProperty(i)){v=o[i];switch(typeof v){case"undefined":case"function":case"unknown":break;default:if(b){a.push(',');}
a.push(self.encode(i),":",v===null?"null":self.encode(v));b=true;}}}
a.push("}");return a.join("");}},decode:function(json){return eval("("+json+')');}};

// ------------------------
// -- JQuery Plugin: Boxy
// ------------------------
jQuery.fn.boxy=function(options){options=options||{};return this.each(function(){var node=this.nodeName.toLowerCase(),self=this;if(node=='a'){jQuery(this).click(function(){var active=Boxy.linkedTo(this),href=this.getAttribute('href'),localOptions=jQuery.extend({actuator:this,title:this.title},options);if(active){active.show();}else if(href.indexOf('#')>=0){var content=jQuery(href.substr(href.indexOf('#'))),newContent=content.clone(true);content.remove();localOptions.unloadOnHide=false;new Boxy(newContent,localOptions);}else{if(!localOptions.cache)localOptions.unloadOnHide=true;Boxy.load(this.href,localOptions);}
return false;});}else if(node=='form'){jQuery(this).bind('submit.boxy',function(){Boxy.confirm(options.message||'Please confirm:',function(){jQuery(self).unbind('submit.boxy').submit();});return false;});}});};function Boxy(element,options){this.boxy=jQuery(Boxy.WRAPPER);jQuery.data(this.boxy[0],'boxy',this);this.visible=false;this.options=jQuery.extend({},Boxy.DEFAULTS,options||{});if(this.options.modal){this.options=jQuery.extend(this.options,{center:true,draggable:false});}
if(this.options.actuator){jQuery.data(this.options.actuator,'active.boxy',this);}
this.setContent(element||"<div></div>");this._setupTitleBar();this.boxy.css('display','none').appendTo(document.body);this.toTop();if(this.options.fixed){if(jQuery.browser.msie&&jQuery.browser.version<7){this.options.fixed=false;}else{this.boxy.addClass('fixed');}}
if(this.options.center&&Boxy._u(this.options.x,this.options.y)){this.center();}else{this.moveTo(Boxy._u(this.options.x)?this.options.x:Boxy.DEFAULT_X,Boxy._u(this.options.y)?this.options.y:Boxy.DEFAULT_Y);}
if(this.options.show)this.show();};Boxy.EF=function(){};jQuery.extend(Boxy,{WRAPPER:"<table cellspacing='0' cellpadding='0' border='0' class='boxy-wrapper'>"+"<tr><td class='top-left'></td><td class='top'></td><td class='top-right'></td></tr>"+"<tr><td class='left'></td><td class='boxy-inner'></td><td class='right'></td></tr>"+"<tr><td class='bottom-left'></td><td class='bottom'></td><td class='bottom-right'></td></tr>"+"</table>",DEFAULTS:{title:null,closeable:true,draggable:true,clone:false,actuator:null,center:true,show:true,modal:false,fixed:true,closeText:'[close]',unloadOnHide:false,clickToFront:false,behaviours:Boxy.EF,afterDrop:Boxy.EF,afterShow:Boxy.EF,afterHide:Boxy.EF,beforeUnload:Boxy.EF},DEFAULT_X:50,DEFAULT_Y:50,zIndex:1337,dragConfigured:false,resizeConfigured:false,dragging:null,load:function(url,options){options=options||{};var ajax={url:url,type:'GET',dataType:'html',cache:false,success:function(html){html=jQuery(html);if(options.filter)html=jQuery(options.filter,html);new Boxy(html,options);}};jQuery.each(['type','cache'],function(){if(this in options){ajax[this]=options[this];delete options[this];}});jQuery.ajax(ajax);},get:function(ele){var p=jQuery(ele).parents('.boxy-wrapper');return p.length?jQuery.data(p[0],'boxy'):null;},linkedTo:function(ele){return jQuery.data(ele,'active.boxy');},alert:function(message,callback,options){return Boxy.ask(message,['OK'],callback,options);},confirm:function(message,after,options){return Boxy.ask(message,['OK','Cancel'],function(response){if(response=='OK')after();},options);},ask:function(question,answers,callback,options){options=jQuery.extend({modal:true,closeable:false},options||{},{show:true,unloadOnHide:true});var body=jQuery('<div></div>').append(jQuery('<div class="question"></div>').html(question));var map={},answerStrings=[];if(answers instanceof Array){for(var i=0;i<answers.length;i++){map[answers[i]]=answers[i];answerStrings.push(answers[i]);}}else{for(var k in answers){map[answers[k]]=k;answerStrings.push(answers[k]);}}
var buttons=jQuery('<form class="answers"></form>');buttons.html(jQuery.map(answerStrings,function(v){return"<input type='button' value='"+v+"' />";}).join(' '));jQuery('input[type=button]',buttons).click(function(){var clicked=this;Boxy.get(this).hide(function(){if(callback)callback(map[clicked.value]);});});body.append(buttons);new Boxy(body,options);},isModalVisible:function(){return jQuery('.boxy-modal-blackout').length>0;},_u:function(){for(var i=0;i<arguments.length;i++)
if(typeof arguments[i]!='undefined')return false;return true;},_handleResize:function(evt){var d=jQuery(document);jQuery('.boxy-modal-blackout').css('display','none').css({width:d.width(),height:d.height()}).css('display','block');},_handleDrag:function(evt){var d;if(d=Boxy.dragging){d[0].boxy.css({left:evt.pageX-d[1],top:evt.pageY-d[2]});}},_nextZ:function(){return Boxy.zIndex++;},_viewport:function(){var d=document.documentElement,b=document.body,w=window;return jQuery.extend(jQuery.browser.msie?{left:b.scrollLeft||d.scrollLeft,top:b.scrollTop||d.scrollTop}:{left:w.pageXOffset,top:w.pageYOffset},!Boxy._u(w.innerWidth)?{width:w.innerWidth,height:w.innerHeight}:(!Boxy._u(d)&&!Boxy._u(d.clientWidth)&&d.clientWidth!=0?{width:d.clientWidth,height:d.clientHeight}:{width:b.clientWidth,height:b.clientHeight}));}});Boxy.prototype={estimateSize:function(){this.boxy.css({visibility:'hidden',display:'block'});var dims=this.getSize();this.boxy.css('display','none').css('visibility','visible');return dims;},getSize:function(){return[this.boxy.width(),this.boxy.height()];},getContentSize:function(){var c=this.getContent();return[c.width(),c.height()];},getPosition:function(){var b=this.boxy[0];return[b.offsetLeft,b.offsetTop];},getCenter:function(){var p=this.getPosition();var s=this.getSize();return[Math.floor(p[0]+s[0]/2),Math.floor(p[1]+s[1]/2)];},getInner:function(){return jQuery('.boxy-inner',this.boxy);},getContent:function(){return jQuery('.boxy-content',this.boxy);},setContent:function(newContent){newContent=jQuery(newContent).css({display:'block'}).addClass('boxy-content');if(this.options.clone)newContent=newContent.clone(true);this.getContent().remove();this.getInner().append(newContent);this._setupDefaultBehaviours(newContent);this.options.behaviours.call(this,newContent);return this;},moveTo:function(x,y){this.moveToX(x).moveToY(y);return this;},moveToX:function(x){if(typeof x=='number')this.boxy.css({left:x});else this.centerX();return this;},moveToY:function(y){if(typeof y=='number')this.boxy.css({top:y});else this.centerY();return this;},centerAt:function(x,y){var s=this[this.visible?'getSize':'estimateSize']();if(typeof x=='number')this.moveToX(x-s[0]/2);if(typeof y=='number')this.moveToY(y-s[1]/2);return this;},centerAtX:function(x){return this.centerAt(x,null);},centerAtY:function(y){return this.centerAt(null,y);},center:function(axis){var v=Boxy._viewport();var o=this.options.fixed?[0,0]:[v.left,v.top];if(!axis||axis=='x')this.centerAt(o[0]+v.width/2,null);if(!axis||axis=='y')this.centerAt(null,o[1]+v.height/2);return this;},centerX:function(){return this.center('x');},centerY:function(){return this.center('y');},resize:function(width,height,after){if(!this.visible)return;var bounds=this._getBoundsForResize(width,height);this.boxy.css({left:bounds[0],top:bounds[1]});this.getContent().css({width:bounds[2],height:bounds[3]});if(after)after(this);return this;},tween:function(width,height,after){if(!this.visible)return;var bounds=this._getBoundsForResize(width,height);var self=this;this.boxy.stop().animate({left:bounds[0],top:bounds[1]});this.getContent().stop().animate({width:bounds[2],height:bounds[3]},function(){if(after)after(self);});return this;},isVisible:function(){return this.visible;},show:function(){if(this.visible)return;if(this.options.modal){var self=this;if(!Boxy.resizeConfigured){Boxy.resizeConfigured=true;jQuery(window).resize(function(){Boxy._handleResize();});}
this.modalBlackout=jQuery('<div class="boxy-modal-blackout"></div>').css({zIndex:Boxy._nextZ(),opacity:0.7,width:jQuery(document).width(),height:jQuery(document).height()}).appendTo(document.body);this.toTop();if(this.options.closeable){jQuery(document.body).bind('keypress.boxy',function(evt){var key=evt.which||evt.keyCode;if(key==27){self.hide();jQuery(document.body).unbind('keypress.boxy');}});}}
this.boxy.stop().css({opacity:1}).show();this.visible=true;this._fire('afterShow');return this;},hide:function(after){if(!this.visible)return;var self=this;if(this.options.modal){jQuery(document.body).unbind('keypress.boxy');this.modalBlackout.animate({opacity:0},function(){jQuery(this).remove();});}
this.boxy.stop().animate({opacity:0},300,function(){self.boxy.css({display:'none'});self.visible=false;self._fire('afterHide');if(after)after(self);if(self.options.unloadOnHide)self.unload();});return this;},toggle:function(){this[this.visible?'hide':'show']();return this;},hideAndUnload:function(after){this.options.unloadOnHide=true;this.hide(after);return this;},unload:function(){this._fire('beforeUnload');this.boxy.remove();if(this.options.actuator){jQuery.data(this.options.actuator,'active.boxy',false);}},toTop:function(){this.boxy.css({zIndex:Boxy._nextZ()});return this;},getTitle:function(){return jQuery('> .title-bar h2',this.getInner()).html();},setTitle:function(t){jQuery('> .title-bar h2',this.getInner()).html(t);return this;},_getBoundsForResize:function(width,height){var csize=this.getContentSize();var delta=[width-csize[0],height-csize[1]];var p=this.getPosition();return[Math.max(p[0]-delta[0]/2,0),Math.max(p[1]-delta[1]/2,0),width,height];},_setupTitleBar:function(){if(this.options.title){var self=this;var tb=jQuery("<div class='title-bar'></div>").html("<h2>"+this.options.title+"</h2>");if(this.options.closeable){tb.append(jQuery("<a href='#' class='close'></a>").html(this.options.closeText));}
if(this.options.draggable){tb[0].onselectstart=function(){return false;}
tb[0].unselectable='on';tb[0].style.MozUserSelect='none';if(!Boxy.dragConfigured){jQuery(document).mousemove(Boxy._handleDrag);Boxy.dragConfigured=true;}
tb.mousedown(function(evt){self.toTop();Boxy.dragging=[self,evt.pageX-self.boxy[0].offsetLeft,evt.pageY-self.boxy[0].offsetTop];jQuery(this).addClass('dragging');}).mouseup(function(){jQuery(this).removeClass('dragging');Boxy.dragging=null;self._fire('afterDrop');});}
this.getInner().prepend(tb);this._setupDefaultBehaviours(tb);}},_setupDefaultBehaviours:function(root){var self=this;if(this.options.clickToFront){root.click(function(){self.toTop();});}
jQuery('.close',root).click(function(){self.hide();return false;}).mousedown(function(evt){evt.stopPropagation();});},_fire:function(event){this.options[event].call(this);}};

// -----------------------------
// -- JQuery Plugin: Simpletip (http://craigsworks.com/projects/simpletip)
// -----------------------------
(function($){function Simpletip(elem,conf){var self=this;elem=jQuery(elem);var tooltip=jQuery(document.createElement('div')).addClass(conf.baseClass).addClass((conf.fixed)?conf.fixedClass:'').addClass((conf.persistent)?conf.persistentClass:'').html(conf.content).appendTo(elem);if(!conf.hidden)tooltip.show();else tooltip.hide();if(!conf.persistent){elem.hover(function(event){self.show(event)},function(){self.hide()});if(!conf.fixed){elem.mousemove(function(event){if(tooltip.css('display')!=='none')self.updatePos(event);});};}else
{elem.click(function(event){if(event.target===elem.get(0)){if(tooltip.css('display')!=='none')self.hide();else
self.show();};});jQuery(window).mousedown(function(event){if(tooltip.css('display')!=='none'){var check=(conf.focus)?jQuery(event.target).parents('.tooltip').andSelf().filter(function(){return this===tooltip.get(0)}).length:0;if(check===0)self.hide();};});};jQuery.extend(self,{getVersion:function(){return[1,2,0];},getParent:function(){return elem;},getTooltip:function(){return tooltip;},getPos:function(){return tooltip.offset();},setPos:function(posX,posY){var elemPos=elem.offset();if(typeof posX=='string')posX=parseInt(posX)+elemPos.left;if(typeof posY=='string')posY=parseInt(posY)+elemPos.top;tooltip.css({left:posX,top:posY});return self;},show:function(event){conf.onBeforeShow.call(self);self.updatePos((conf.fixed)?null:event);switch(conf.showEffect){case'fade':tooltip.fadeIn(conf.showTime);break;case'slide':tooltip.slideDown(conf.showTime,self.updatePos);break;case'custom':conf.showCustom.call(tooltip,conf.showTime);break;default:case'none':tooltip.show();break;};tooltip.addClass(conf.activeClass);conf.onShow.call(self);return self;},hide:function(){conf.onBeforeHide.call(self);switch(conf.hideEffect){case'fade':tooltip.fadeOut(conf.hideTime);break;case'slide':tooltip.slideUp(conf.hideTime);break;case'custom':conf.hideCustom.call(tooltip,conf.hideTime);break;default:case'none':tooltip.hide();break;};tooltip.removeClass(conf.activeClass);conf.onHide.call(self);return self;},update:function(content){tooltip.html(content);conf.content=content;return self;},load:function(uri,data){conf.beforeContentLoad.call(self);tooltip.load(uri,data,function(){conf.onContentLoad.call(self);});return self;},boundryCheck:function(posX,posY){var newX=posX+tooltip.outerWidth();var newY=posY+tooltip.outerHeight();var windowWidth=jQuery(window).width()+jQuery(window).scrollLeft();var windowHeight=jQuery(window).height()+jQuery(window).scrollTop();return[(newX>=windowWidth),(newY>=windowHeight)];},updatePos:function(event){var tooltipWidth=tooltip.outerWidth();var tooltipHeight=tooltip.outerHeight();if(!event&&conf.fixed){if(conf.position.constructor==Array){posX=parseInt(conf.position[0]);posY=parseInt(conf.position[1]);}else if(jQuery(conf.position).attr('nodeType')===1){var offset=jQuery(conf.position).offset();posX=offset.left;posY=offset.top;}else
{var elemPos=elem.offset();var elemWidth=elem.outerWidth();var elemHeight=elem.outerHeight();switch(conf.position){case'top':var posX=elemPos.left-(tooltipWidth/2)+(elemWidth/2);var posY=elemPos.top-tooltipHeight;break;case'bottom':var posX=elemPos.left-(tooltipWidth/2)+(elemWidth/2);var posY=elemPos.top+elemHeight;break;case'left':var posX=elemPos.left-tooltipWidth;var posY=elemPos.top-(tooltipHeight/2)+(elemHeight/2);break;case'right':var posX=elemPos.left+elemWidth;var posY=elemPos.top-(tooltipHeight/2)+(elemHeight/2);break;default:case'default':var posX=(elemWidth/2)+elemPos.left+20;var posY=elemPos.top;break;};};}else
{var posX=event.pageX;var posY=event.pageY;};if(typeof conf.position!='object'){posX=posX+conf.offset[0];posY=posY+conf.offset[1];if(conf.boundryCheck){var overflow=self.boundryCheck(posX,posY);if(overflow[0])posX=posX-(tooltipWidth/2)-(2*conf.offset[0]);if(overflow[1])posY=posY-(tooltipHeight/2)-(2*conf.offset[1]);}}else
{if(typeof conf.position[0]=="string")posX=String(posX);if(typeof conf.position[1]=="string")posY=String(posY);};self.setPos(posX,posY);return self;}});};jQuery.fn.simpletip=function(conf){var api=jQuery(this).eq(typeof conf=='number'?conf:0).data("simpletip");if(api)return api;var defaultConf={content:'A simple tooltip',persistent:false,focus:false,hidden:true,position:'default',offset:[0,0],boundryCheck:true,fixed:true,showEffect:'fade',showTime:150,showCustom:null,hideEffect:'fade',hideTime:150,hideCustom:null,baseClass:'tooltip',activeClass:'active',fixedClass:'fixed',persistentClass:'persistent',focusClass:'focus',onBeforeShow:function(){},onShow:function(){},onBeforeHide:function(){},onHide:function(){},beforeContentLoad:function(){},onContentLoad:function(){}};jQuery.extend(defaultConf,conf);this.each(function(){var el=new Simpletip(jQuery(this),defaultConf);jQuery(this).data("simpletip",el);});return this;};})();

// -----------------------------
// -- JQuery Plugin: CLEditor WYSIWYG HTML Editor v1.3.0 (http://premiumsoftware.net/cleditor)
// -----------------------------
(function(e){function aa(a){var b=this,c=a.target,d=e.data(c,x),h=s[d],f=h.popupName,i=p[f];if(!(b.disabled||e(c).attr(n)==n)){var g={editor:b,button:c,buttonName:d,popup:i,popupName:f,command:h.command,useCSS:b.options.useCSS};if(h.buttonClick&&h.buttonClick(a,g)===false)return false;if(d=="source"){if(t(b)){delete b.range;b.$area.hide();b.$frame.show();c.title=h.title}else{b.$frame.hide();b.$area.show();c.title="Show Rich Text"}setTimeout(function(){u(b)},100)}else if(!t(b))if(f){var j=e(i);if(f==
"url"){if(d=="link"&&M(b)===""){z(b,"A selection is required when inserting a link.",c);return false}j.children(":button").unbind(q).bind(q,function(){var k=j.find(":text"),o=e.trim(k.val());o!==""&&v(b,g.command,o,null,g.button);k.val("http://");r();w(b)})}else f=="pastetext"&&j.children(":button").unbind(q).bind(q,function(){var k=j.find("textarea"),o=k.val().replace(/\n/g,"<br />");o!==""&&v(b,g.command,o,null,g.button);k.val("");r();w(b)});if(c!==e.data(i,A)){N(b,i,c);return false}return}else if(d==
"print")b.$frame[0].contentWindow.print();else if(!v(b,g.command,g.value,g.useCSS,c))return false;w(b)}}function O(a){a=e(a.target).closest("div");a.css(H,a.data(x)?"#FFF":"#FFC")}function P(a){e(a.target).closest("div").css(H,"transparent")}function ba(a){var b=a.data.popup,c=a.target;if(!(b===p.msg||e(b).hasClass(B))){var d=e.data(b,A),h=e.data(d,x),f=s[h],i=f.command,g,j=this.options.useCSS;if(h=="font")g=c.style.fontFamily.replace(/"/g,"");else if(h=="size"){if(c.tagName=="DIV")c=c.children[0];
g=c.innerHTML}else if(h=="style")g="<"+c.tagName+">";else if(h=="color")g=Q(c.style.backgroundColor);else if(h=="highlight"){g=Q(c.style.backgroundColor);if(l)i="backcolor";else j=true}b={editor:this,button:d,buttonName:h,popup:b,popupName:f.popupName,command:i,value:g,useCSS:j};if(!(f.popupClick&&f.popupClick(a,b)===false)){if(b.command&&!v(this,b.command,b.value,b.useCSS,d))return false;r();w(this)}}}function C(a){for(var b=1,c=0,d=0;d<a.length;++d){b=(b+a.charCodeAt(d))%65521;c=(c+b)%65521}return c<<
16|b}function R(a,b,c,d,h){if(p[a])return p[a];var f=e(m).hide().addClass(ca).appendTo("body");if(d)f.html(d);else if(a=="color"){b=b.colors.split(" ");b.length<10&&f.width("auto");e.each(b,function(i,g){e(m).appendTo(f).css(H,"#"+g)});c=da}else if(a=="font")e.each(b.fonts.split(","),function(i,g){e(m).appendTo(f).css("fontFamily",g).html(g)});else if(a=="size")e.each(b.sizes.split(","),function(i,g){e(m).appendTo(f).html("<font size="+g+">"+g+"</font>")});else if(a=="style")e.each(b.styles,function(i,
g){e(m).appendTo(f).html(g[1]+g[0]+g[1].replace("<","</"))});else if(a=="url"){f.html('Enter URL:<br><input type=text value="http://" size=35><br><input type=button value="Submit">');c=B}else if(a=="pastetext"){f.html("Paste your content here and click submit.<br /><textarea cols=40 rows=3></textarea><br /><input type=button value=Submit>");c=B}if(!c&&!d)c=S;f.addClass(c);l&&f.attr(I,"on").find("div,font,p,h1,h2,h3,h4,h5,h6").attr(I,"on");if(f.hasClass(S)||h===true)f.children().hover(O,P);p[a]=f[0];
return f[0]}function T(a,b){if(b){a.$area.attr(n,n);a.disabled=true}else{a.$area.removeAttr(n);delete a.disabled}try{if(l)a.doc.body.contentEditable=!b;else a.doc.designMode=!b?"on":"off"}catch(c){}u(a)}function v(a,b,c,d,h){D(a);if(!l){if(d===undefined||d===null)d=a.options.useCSS;a.doc.execCommand("styleWithCSS",0,d.toString())}d=true;var f;if(l&&b.toLowerCase()=="inserthtml")y(a).pasteHTML(c);else{try{d=a.doc.execCommand(b,0,c||null)}catch(i){f=i.description;d=false}d||("cutcopypaste".indexOf(b)>
-1?z(a,"For security reasons, your browser does not support the "+b+" command. Try using the keyboard shortcut or context menu instead.",h):z(a,f?f:"Error executing the "+b+" command.",h))}u(a);return d}function w(a){setTimeout(function(){t(a)?a.$area.focus():a.$frame[0].contentWindow.focus();u(a)},0)}function y(a){if(l)return J(a).createRange();return J(a).getRangeAt(0)}function J(a){if(l)return a.doc.selection;return a.$frame[0].contentWindow.getSelection()}function Q(a){var b=/rgba?\((\d+), (\d+), (\d+)/.exec(a),
c=a.split("");if(b)for(a=(b[1]<<16|b[2]<<8|b[3]).toString(16);a.length<6;)a="0"+a;return"#"+(a.length==6?a:c[1]+c[1]+c[2]+c[2]+c[3]+c[3])}function r(){e.each(p,function(a,b){e(b).hide().unbind(q).removeData(A)})}function U(){var a=e("link[href$='jquery.cleditor.css']").attr("href");return a.substr(0,a.length-19)+"images/"}function K(a){var b=a.$main,c=a.options;a.$frame&&a.$frame.remove();var d=a.$frame=e('<iframe frameborder="0" src="javascript:true;">').hide().appendTo(b),h=d[0].contentWindow,f=
a.doc=h.document,i=e(f);f.open();f.write(c.docType+"<html>"+(c.docCSSFile===""?"":'<head><link rel="stylesheet" type="text/css" href="'+c.docCSSFile+'" /></head>')+'<body style="'+c.bodyStyle+'"></body></html>');f.close();l&&i.click(function(){w(a)});E(a);if(l){i.bind("beforedeactivate beforeactivate selectionchange keypress",function(g){if(g.type=="beforedeactivate")a.inactive=true;else if(g.type=="beforeactivate"){!a.inactive&&a.range&&a.range.length>1&&a.range.shift();delete a.inactive}else if(!a.inactive){if(!a.range)a.range=
[];for(a.range.unshift(y(a));a.range.length>2;)a.range.pop()}});d.focus(function(){D(a)})}(e.browser.mozilla?i:e(h)).blur(function(){V(a,true)});i.click(r).bind("keyup mouseup",function(){u(a)});L?a.$area.show():d.show();e(function(){var g=a.$toolbar,j=g.children("div:last"),k=b.width();j=j.offset().top+j.outerHeight()-g.offset().top+1;g.height(j);j=(/%/.test(""+c.height)?b.height():parseInt(c.height))-j;d.width(k).height(j);a.$area.width(k).height(ea?j-2:j);T(a,a.disabled);u(a)})}function u(a){if(!L&&
e.browser.webkit&&!a.focused){a.$frame[0].contentWindow.focus();window.focus();a.focused=true}var b=a.doc;if(l)b=y(a);var c=t(a);e.each(a.$toolbar.find("."+W),function(d,h){var f=e(h),i=e.cleditor.buttons[e.data(h,x)],g=i.command,j=true;if(a.disabled)j=false;else if(i.getEnabled){j=i.getEnabled({editor:a,button:h,buttonName:i.name,popup:p[i.popupName],popupName:i.popupName,command:i.command,useCSS:a.options.useCSS});if(j===undefined)j=true}else if((c||L)&&i.name!="source"||l&&(g=="undo"||g=="redo"))j=
false;else if(g&&g!="print"){if(l&&g=="hilitecolor")g="backcolor";if(!l||g!="inserthtml")try{j=b.queryCommandEnabled(g)}catch(k){j=false}}if(j){f.removeClass(X);f.removeAttr(n)}else{f.addClass(X);f.attr(n,n)}})}function D(a){l&&a.range&&a.range[0].select()}function M(a){D(a);if(l)return y(a).text;return J(a).toString()}function z(a,b,c){var d=R("msg",a.options,fa);d.innerHTML=b;N(a,d,c)}function N(a,b,c){var d,h,f=e(b);if(c){var i=e(c);d=i.offset();h=--d.left;d=d.top+i.height()}else{i=a.$toolbar;
d=i.offset();h=Math.floor((i.width()-f.width())/2)+d.left;d=d.top+i.height()-2}r();f.css({left:h,top:d}).show();if(c){e.data(b,A,c);f.bind(q,{popup:b},e.proxy(ba,a))}setTimeout(function(){f.find(":text,textarea").eq(0).focus().select()},100)}function t(a){return a.$area.is(":visible")}function E(a,b){var c=a.$area.val(),d=a.options,h=d.updateFrame,f=e(a.doc.body);if(h){var i=C(c);if(b&&a.areaChecksum==i)return;a.areaChecksum=i}c=h?h(c):c;c=c.replace(/<(?=\/?script)/ig,"&lt;");if(d.updateTextArea)a.frameChecksum=
C(c);if(c!=f.html()){f.html(c);e(a).triggerHandler(F)}}function V(a,b){var c=e(a.doc.body).html(),d=a.options,h=d.updateTextArea,f=a.$area;if(h){var i=C(c);if(b&&a.frameChecksum==i)return;a.frameChecksum=i}c=h?h(c):c;if(d.updateFrame)a.areaChecksum=C(c);if(c!=f.val()){f.val(c);e(a).triggerHandler(F)}}e.cleditor={defaultOptions:{width:500,height:250,controls:"bold italic underline strikethrough subscript superscript | font size style | color highlight removeformat | bullets numbering | outdent indent | alignleft center alignright justify | undo redo | rule image link unlink | cut copy paste pastetext | print source",
colors:"FFF FCC FC9 FF9 FFC 9F9 9FF CFF CCF FCF CCC F66 F96 FF6 FF3 6F9 3FF 6FF 99F F9F BBB F00 F90 FC6 FF0 3F3 6CC 3CF 66C C6C 999 C00 F60 FC3 FC0 3C0 0CC 36F 63F C3C 666 900 C60 C93 990 090 399 33F 60C 939 333 600 930 963 660 060 366 009 339 636 000 300 630 633 330 030 033 006 309 303",fonts:"Arial,Arial Black,Comic Sans MS,Courier New,Narrow,Garamond,Georgia,Impact,Sans Serif,Serif,Tahoma,Trebuchet MS,Verdana",sizes:"1,2,3,4,5,6,7",styles:[["Paragraph","<p>"],["Header 1","<h1>"],["Header 2","<h2>"],
["Header 3","<h3>"],["Header 4","<h4>"],["Header 5","<h5>"],["Header 6","<h6>"]],useCSS:false,docType:'<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">',docCSSFile:"",bodyStyle:"margin:4px; font:10pt Arial,Verdana; cursor:text"},buttons:{init:"bold,,|italic,,|underline,,|strikethrough,,|subscript,,|superscript,,|font,,fontname,|size,Font Size,fontsize,|style,,formatblock,|color,Font Color,forecolor,|highlight,Text Highlight Color,hilitecolor,color|removeformat,Remove Formatting,|bullets,,insertunorderedlist|numbering,,insertorderedlist|outdent,,|indent,,|alignleft,Align Text Left,justifyleft|center,,justifycenter|alignright,Align Text Right,justifyright|justify,,justifyfull|undo,,|redo,,|rule,Insert Horizontal Rule,inserthorizontalrule|image,Insert Image,insertimage,url|link,Insert Hyperlink,createlink,url|unlink,Remove Hyperlink,|cut,,|copy,,|paste,,|pastetext,Paste as Text,inserthtml,|print,,|source,Show Source"},
imagesPath:function(){return U()}};e.fn.cleditor=function(a){var b=e([]);this.each(function(c,d){if(d.tagName=="TEXTAREA"){var h=e.data(d,Y);h||(h=new cleditor(d,a));b=b.add(h)}});return b};var H="backgroundColor",A="button",x="buttonName",F="change",Y="cleditor",q="click",n="disabled",m="<div>",I="unselectable",W="cleditorButton",X="cleditorDisabled",ca="cleditorPopup",S="cleditorList",da="cleditorColor",B="cleditorPrompt",fa="cleditorMsg",l=e.browser.msie,ea=/msie\s6/i.test(navigator.userAgent),
L=/iphone|ipad|ipod/i.test(navigator.userAgent),p={},Z,s=e.cleditor.buttons;e.each(s.init.split("|"),function(a,b){var c=b.split(","),d=c[0];s[d]={stripIndex:a,name:d,title:c[1]===""?d.charAt(0).toUpperCase()+d.substr(1):c[1],command:c[2]===""?d:c[2],popupName:c[3]===""?d:c[3]}});delete s.init;cleditor=function(a,b){var c=this;c.options=b=e.extend({},e.cleditor.defaultOptions,b);var d=c.$area=e(a).hide().data(Y,c).blur(function(){E(c,true)}),h=c.$main=e(m).addClass("cleditorMain").width(b.width).height(b.height),
f=c.$toolbar=e(m).addClass("cleditorToolbar").appendTo(h),i=e(m).addClass("cleditorGroup").appendTo(f);e.each(b.controls.split(" "),function(g,j){if(j==="")return true;if(j=="|"){e(m).addClass("cleditorDivider").appendTo(i);i=e(m).addClass("cleditorGroup").appendTo(f)}else{var k=s[j],o=e(m).data(x,k.name).addClass(W).attr("title",k.title).bind(q,e.proxy(aa,c)).appendTo(i).hover(O,P),G={};if(k.css)G=k.css;else if(k.image)G.backgroundImage="url("+U()+k.image+")";if(k.stripIndex)G.backgroundPosition=
k.stripIndex*-24;o.css(G);l&&o.attr(I,"on");k.popupName&&R(k.popupName,b,k.popupClass,k.popupContent,k.popupHover)}});h.insertBefore(d).append(d);if(!Z){e(document).click(function(g){g=e(g.target);g.add(g.parents()).is("."+B)||r()});Z=true}/auto|%/.test(""+b.width+b.height)&&e(window).resize(function(){K(c)});K(c)};var $=cleditor.prototype;e.each([["clear",function(a){a.$area.val("");E(a)}],["disable",T],["execCommand",v],["focus",w],["hidePopups",r],["sourceMode",t,true],["refresh",K],["select",
function(a){setTimeout(function(){t(a)?a.$area.select():v(a,"selectall")},0)}],["selectedHTML",function(a){D(a);a=y(a);if(l)return a.htmlText;var b=e("<layer>")[0];b.appendChild(a.cloneContents());return b.innerHTML},true],["selectedText",M,true],["showMessage",z],["updateFrame",E],["updateTextArea",V]],function(a,b){$[b[0]]=function(){for(var c=[this],d=0;d<arguments.length;d++)c.push(arguments[d]);c=b[1].apply(this,c);if(b[2])return c;return this}});$.change=function(a){var b=e(this);return a?b.bind(F,
a):b.trigger(F)}})(jQuery);

// -----------------------------
// -- JQuery Plugin: Horizontal Accordion script (http://www.dynamicdrive.com)
// -----------------------------
/* Horizontal Accordion script
* Created: Oct 27th, 2009. This notice must stay intact for usage 
* Author: Dynamic Drive at http://www.dynamicdrive.com/
* Visit http://www.dynamicdrive.com/ for full source code
*/

var haccordion={
	//customize loading message if accordion markup is fetched via Ajax:
	ajaxloadingmsg: '<div style="margin: 1em; font-weight: bold"><img src="/1archive/iconarchive.com/images/jquery/ajaxloadr.gif" style="vertical-align: middle" /></div>',

	accordioninfo: {}, //class that holds config information of each haccordion instance

	expandli:function(accordionid, targetli){
		var config=haccordion.accordioninfo[accordionid]
		var $targetli=(typeof targetli=="number")? config.$targetlis.eq(targetli) : (typeof targetli=="string")? jQuery('#'+targetli) : jQuery(targetli)
		if (typeof config.$lastexpanded!="undefined") //targetli may be an index, ID string, or DOM reference to LI
			config.$lastexpanded.stop().animate({width:config.paneldimensions.peekw}, config.speed) //contract last opened content
		$targetli.stop().animate({width:$targetli.data('hpaneloffsetw')}, config.speed) //expand current content
		config.$lastexpanded=$targetli
	},


	urlparamselect:function(accordionid){
		var result=window.location.search.match(new RegExp(accordionid+"=(\\d+)", "i")) //check for "?accordionid=index" in URL
		if (result!=null)
			result=parseInt(RegExp.$1)+"" //return value as string so 0 doesn't test for false
		return result //returns null or index, where index is the desired selected hcontent index
	},

	getCookie:function(Name){ 
		var re=new RegExp(Name+"=[^;]+", "i") //construct RE to search for target name/value pair
		if (document.cookie.match(re)) //if cookie found
			return document.cookie.match(re)[0].split("=")[1] //return its value
		return null
	},

	setCookie:function(name, value){
		document.cookie = name + "=" + value + "; path=/"
	},


	loadexternal:function($, config){ //function to fetch external page containing the entire accordion content markup
		var $hcontainer=$('#'+config.ajaxsource.container).html(this.ajaxloadingmsg)
		$.ajax({
			url: config.ajaxsource.path, //path to external content
			async: true,
			error:function(ajaxrequest){
				$hcontainer.html('Error fetching content.<br />Server Response: '+ajaxrequest.responseText)
			},
			success:function(content){
				$hcontainer.html(content)
				haccordion.init($, config)
			}
		})
	},


	init:function($, config){
			haccordion.accordioninfo[config.accordionid]=config //cache config info for this accordion
			var $targetlis=$('#'+config.accordionid).find('ul:eq(0) > li') //find top level LIs
			config.$targetlis=$targetlis
			config.selectedli=config.selectedli || [] //set default selectedli option
			config.speed=config.speed || "normal" //set default speed
			$targetlis.each(function(i){
				var $target=$(this).data('pos', i) //give each li an index #
				$target.data('hpaneloffsetw', $target.find('.hpanel:eq(0)').outerWidth()) //get offset width of each .hpanel DIV (config.dimensions.fullw + any DIV padding)
				$target.mouseenter(function(){
						haccordion.expandli(config.accordionid, this)
					config.$lastexpanded=$(this)
				})
				if (config.collapsecurrent){ //if previous content should be contracted when expanding current
					$target.mouseleave(function(){
						$(this).stop().animate({width:config.paneldimensions.peekw}, config.speed) //contract previous content
					})
				}				
			}) //end $targetlis.each
			var selectedli=haccordion.urlparamselect(config.accordionid) || ((config.selectedli[1] && haccordion.getCookie(config.accordionid))? parseInt(haccordion.getCookie(config.accordionid)) : config.selectedli[0])
			selectedli=parseInt(selectedli)
			if (selectedli>=0 && selectedli<config.$targetlis.length){ //if selectedli index is within range
				config.$lastexpanded=$targetlis.eq(selectedli)
				config.$lastexpanded.css('width', config.$lastexpanded.data('hpaneloffsetw')) //expand selected li
			}
			$(window).bind('unload', function(){ //clean up and persist on page unload
				haccordion.uninit($, config)
			}) //end window.onunload
	},

	uninit:function($, config){
		var $targetlis=config.$targetlis
		var expandedliindex=-1 //index of expanded content to remember (-1 indicates non)
		$targetlis.each(function(){
			var $target=$(this)
			$target.unbind()
			if ($target.width()==$target.data('hpaneloffsetw'))
				expandedliindex=$target.data('pos')
		})
		if (config.selectedli[1]==true) //enable persistence?
			haccordion.setCookie(config.accordionid, expandedliindex)
	},

	setup:function(config){
		//Use JS to write out CSS that sets up initial dimensions of each LI, for JS enabled browsers only
		document.write('<style type="text/css">\n')
		document.write('#'+config.accordionid+' li{width: '+config.paneldimensions.peekw+';\nheight: '+config.paneldimensions.h+';\n}\n')
		document.write('#'+config.accordionid+' li .hpanel{width: '+config.paneldimensions.fullw+';\nheight: '+config.paneldimensions.h+';\n}\n')
		document.write('<\/style>')
		jQuery(document).ready(function($){ //on Dom load
			if (config.ajaxsource) //if config.ajaxsource option defined
				haccordion.loadexternal($, config)
			else
				haccordion.init($, config)
		}) //end DOM load
	}

}

// -----------------------------
// -- No JQuery Plugin: jscolor, JavaScript Color Picker @author  Jan Odvarko, http://odvarko.cz (http://jscolor.com)
// -----------------------------
var jscolor={dir:baseurl+'static/js/jscolor/',bindClass:'color',binding:true,preloading:true,install:function(){jscolor.addEvent(window,'load',jscolor.init);},init:function(){if(jscolor.binding){jscolor.bind();}
if(jscolor.preloading){jscolor.preload();}},getDir:function(){if(!jscolor.dir){var detected=jscolor.detectDir();jscolor.dir=detected!==false?detected:'jscolor/';}
return jscolor.dir;},detectDir:function(){var base=location.href;var e=document.getElementsByTagName('base');for(var i=0;i<e.length;i+=1){if(e[i].href){base=e[i].href;}}
var e=document.getElementsByTagName('script');for(var i=0;i<e.length;i+=1){if(e[i].src&&/(^|\/)jscolor\.js([?#].*)?$/i.test(e[i].src)){var src=new jscolor.URI(e[i].src);var srcAbs=src.toAbsolute(base);srcAbs.path=srcAbs.path.replace(/[^\/]+$/,'');srcAbs.query=null;srcAbs.fragment=null;return srcAbs.toString();}}
return false;},bind:function(){var matchClass=new RegExp('(^|\\s)('+jscolor.bindClass+')\\s*(\\{[^}]*\\})?','i');var e=document.getElementsByTagName('input');for(var i=0;i<e.length;i+=1){var m;if(!e[i].color&&e[i].className&&(m=e[i].className.match(matchClass))){var prop={};if(m[3]){try{eval('prop='+m[3]);}catch(eInvalidProp){}}
e[i].color=new jscolor.color(e[i],prop);}}},preload:function(){for(var fn in jscolor.imgRequire){if(jscolor.imgRequire.hasOwnProperty(fn)){jscolor.loadImage(fn);}}},images:{pad:[181,101],sld:[16,101],cross:[15,15],arrow:[7,11]},imgRequire:{},imgLoaded:{},requireImage:function(filename){jscolor.imgRequire[filename]=true;},loadImage:function(filename){if(!jscolor.imgLoaded[filename]){jscolor.imgLoaded[filename]=new Image();jscolor.imgLoaded[filename].src=jscolor.getDir()+filename;}},fetchElement:function(mixed){return typeof mixed==='string'?document.getElementById(mixed):mixed;},addEvent:function(el,evnt,func){if(el.addEventListener){el.addEventListener(evnt,func,false);}else if(el.attachEvent){el.attachEvent('on'+evnt,func);}},fireEvent:function(el,evnt){if(!el){return;}
if(document.createEventObject){var ev=document.createEventObject();el.fireEvent('on'+evnt,ev);}else if(document.createEvent){var ev=document.createEvent('HTMLEvents');ev.initEvent(evnt,true,true);el.dispatchEvent(ev);}else if(el['on'+evnt]){el['on'+evnt]();}},getElementPos:function(e){var e1=e,e2=e;var x=0,y=0;if(e1.offsetParent){do{x+=e1.offsetLeft;y+=e1.offsetTop;}while(e1=e1.offsetParent);}
while((e2=e2.parentNode)&&e2.nodeName.toUpperCase()!=='BODY'){x-=e2.scrollLeft;y-=e2.scrollTop;}
return[x,y];},getElementSize:function(e){return[e.offsetWidth,e.offsetHeight];},getMousePos:function(e){if(!e){e=window.event;}
if(typeof e.pageX==='number'){return[e.pageX,e.pageY];}else if(typeof e.clientX==='number'){return[e.clientX+document.body.scrollLeft+document.documentElement.scrollLeft,e.clientY+document.body.scrollTop+document.documentElement.scrollTop];}},getViewPos:function(){if(typeof window.pageYOffset==='number'){return[window.pageXOffset,window.pageYOffset];}else if(document.body&&(document.body.scrollLeft||document.body.scrollTop)){return[document.body.scrollLeft,document.body.scrollTop];}else if(document.documentElement&&(document.documentElement.scrollLeft||document.documentElement.scrollTop)){return[document.documentElement.scrollLeft,document.documentElement.scrollTop];}else{return[0,0];}},getViewSize:function(){if(typeof window.innerWidth==='number'){return[window.innerWidth,window.innerHeight];}else if(document.body&&(document.body.clientWidth||document.body.clientHeight)){return[document.body.clientWidth,document.body.clientHeight];}else if(document.documentElement&&(document.documentElement.clientWidth||document.documentElement.clientHeight)){return[document.documentElement.clientWidth,document.documentElement.clientHeight];}else{return[0,0];}},URI:function(uri){this.scheme=null;this.authority=null;this.path='';this.query=null;this.fragment=null;this.parse=function(uri){var m=uri.match(/^(([A-Za-z][0-9A-Za-z+.-]*)(:))?((\/\/)([^\/?#]*))?([^?#]*)((\?)([^#]*))?((#)(.*))?/);this.scheme=m[3]?m[2]:null;this.authority=m[5]?m[6]:null;this.path=m[7];this.query=m[9]?m[10]:null;this.fragment=m[12]?m[13]:null;return this;};this.toString=function(){var result='';if(this.scheme!==null){result=result+this.scheme+':';}
if(this.authority!==null){result=result+'//'+this.authority;}
if(this.path!==null){result=result+this.path;}
if(this.query!==null){result=result+'?'+this.query;}
if(this.fragment!==null){result=result+'#'+this.fragment;}
return result;};this.toAbsolute=function(base){var base=new jscolor.URI(base);var r=this;var t=new jscolor.URI;if(base.scheme===null){return false;}
if(r.scheme!==null&&r.scheme.toLowerCase()===base.scheme.toLowerCase()){r.scheme=null;}
if(r.scheme!==null){t.scheme=r.scheme;t.authority=r.authority;t.path=removeDotSegments(r.path);t.query=r.query;}else{if(r.authority!==null){t.authority=r.authority;t.path=removeDotSegments(r.path);t.query=r.query;}else{if(r.path===''){t.path=base.path;if(r.query!==null){t.query=r.query;}else{t.query=base.query;}}else{if(r.path.substr(0,1)==='/'){t.path=removeDotSegments(r.path);}else{if(base.authority!==null&&base.path===''){t.path='/'+r.path;}else{t.path=base.path.replace(/[^\/]+$/,'')+r.path;}
t.path=removeDotSegments(t.path);}
t.query=r.query;}
t.authority=base.authority;}
t.scheme=base.scheme;}
t.fragment=r.fragment;return t;};function removeDotSegments(path){var out='';while(path){if(path.substr(0,3)==='../'||path.substr(0,2)==='./'){path=path.replace(/^\.+/,'').substr(1);}else if(path.substr(0,3)==='/./'||path==='/.'){path='/'+path.substr(3);}else if(path.substr(0,4)==='/../'||path==='/..'){path='/'+path.substr(4);out=out.replace(/\/?[^\/]*$/,'');}else if(path==='.'||path==='..'){path='';}else{var rm=path.match(/^\/?[^\/]*/)[0];path=path.substr(rm.length);out=out+rm;}}
return out;}
if(uri){this.parse(uri);}},color:function(target,prop){this.required=true;this.adjust=true;this.hash=false;this.caps=true;this.valueElement=target;this.styleElement=target;this.hsv=[0,0,1];this.rgb=[1,1,1];this.pickerOnfocus=true;this.pickerMode='HSV';this.pickerPosition='bottom';this.pickerFace=10;this.pickerFaceColor='ThreeDFace';this.pickerBorder=1;this.pickerBorderColor='ThreeDHighlight ThreeDShadow ThreeDShadow ThreeDHighlight';this.pickerInset=1;this.pickerInsetColor='ThreeDShadow ThreeDHighlight ThreeDHighlight ThreeDShadow';this.pickerZIndex=10000;for(var p in prop){if(prop.hasOwnProperty(p)){this[p]=prop[p];}}
this.hidePicker=function(){if(isPickerOwner()){removePicker();}};this.showPicker=function(){if(!isPickerOwner()){var tp=jscolor.getElementPos(target);var ts=jscolor.getElementSize(target);var vp=jscolor.getViewPos();var vs=jscolor.getViewSize();var ps=[2*this.pickerBorder+4*this.pickerInset+2*this.pickerFace+jscolor.images.pad[0]+2*jscolor.images.arrow[0]+jscolor.images.sld[0],2*this.pickerBorder+2*this.pickerInset+2*this.pickerFace+jscolor.images.pad[1]];var a,b,c;switch(this.pickerPosition.toLowerCase()){case'left':a=1;b=0;c=-1;break;case'right':a=1;b=0;c=1;break;case'top':a=0;b=1;c=-1;break;default:a=0;b=1;c=1;break;}
var l=(ts[b]+ps[b])/2;var pp=[-vp[a]+tp[a]+ps[a]>vs[a]?(-vp[a]+tp[a]+ts[a]/2>vs[a]/2&&tp[a]+ts[a]-ps[a]>=0?tp[a]+ts[a]-ps[a]:tp[a]):tp[a],-vp[b]+tp[b]+ts[b]+ps[b]-l+l*c>vs[b]?(-vp[b]+tp[b]+ts[b]/2>vs[b]/2&&tp[b]+ts[b]-l-l*c>=0?tp[b]+ts[b]-l-l*c:tp[b]+ts[b]-l+l*c):(tp[b]+ts[b]-l+l*c>=0?tp[b]+ts[b]-l+l*c:tp[b]+ts[b]-l-l*c)];drawPicker(pp[a],pp[b]);}};this.importColor=function(){if(!valueElement){this.exportColor();}else{if(!this.adjust){if(!this.fromString(valueElement.value,leaveValue)){styleElement.style.backgroundColor=styleElement.jscStyle.backgroundColor;styleElement.style.color=styleElement.jscStyle.color;this.exportColor(leaveValue|leaveStyle);}}else if(!this.required&&/^\s*$/.test(valueElement.value)){valueElement.value='';styleElement.style.backgroundColor=styleElement.jscStyle.backgroundColor;styleElement.style.color=styleElement.jscStyle.color;this.exportColor(leaveValue|leaveStyle);}else if(this.fromString(valueElement.value)){}else{this.exportColor();}}};this.exportColor=function(flags){if(!(flags&leaveValue)&&valueElement){var value=this.toString();if(this.caps){value=value.toUpperCase();}
if(this.hash){value='#'+value;}
valueElement.value=value;}
if(!(flags&leaveStyle)&&styleElement){styleElement.style.backgroundColor='#'+this.toString();styleElement.style.color=0.213*this.rgb[0]+
0.715*this.rgb[1]+
0.072*this.rgb[2]<0.5?'#FFF':'#000';}
if(!(flags&leavePad)&&isPickerOwner()){redrawPad();}
if(!(flags&leaveSld)&&isPickerOwner()){redrawSld();}};this.fromHSV=function(h,s,v,flags){h<0&&(h=0)||h>6&&(h=6);s<0&&(s=0)||s>1&&(s=1);v<0&&(v=0)||v>1&&(v=1);this.rgb=HSV_RGB(h===null?this.hsv[0]:(this.hsv[0]=h),s===null?this.hsv[1]:(this.hsv[1]=s),v===null?this.hsv[2]:(this.hsv[2]=v));this.exportColor(flags);};this.fromRGB=function(r,g,b,flags){r<0&&(r=0)||r>1&&(r=1);g<0&&(g=0)||g>1&&(g=1);b<0&&(b=0)||b>1&&(b=1);var hsv=RGB_HSV(r===null?this.rgb[0]:(this.rgb[0]=r),g===null?this.rgb[1]:(this.rgb[1]=g),b===null?this.rgb[2]:(this.rgb[2]=b));if(hsv[0]!==null){this.hsv[0]=hsv[0];}
if(hsv[2]!==0){this.hsv[1]=hsv[1];}
this.hsv[2]=hsv[2];this.exportColor(flags);};this.fromString=function(hex,flags){var m=hex.match(/^\W*([0-9A-F]{3}([0-9A-F]{3})?)\W*$/i);if(!m){return false;}else{if(m[1].length===6){this.fromRGB(parseInt(m[1].substr(0,2),16)/255,parseInt(m[1].substr(2,2),16)/255,parseInt(m[1].substr(4,2),16)/255,flags);}else{this.fromRGB(parseInt(m[1].charAt(0)+m[1].charAt(0),16)/255,parseInt(m[1].charAt(1)+m[1].charAt(1),16)/255,parseInt(m[1].charAt(2)+m[1].charAt(2),16)/255,flags);}
return true;}};this.toString=function(){return((0x100|Math.round(255*this.rgb[0])).toString(16).substr(1)+
(0x100|Math.round(255*this.rgb[1])).toString(16).substr(1)+
(0x100|Math.round(255*this.rgb[2])).toString(16).substr(1));};function RGB_HSV(r,g,b){var n=Math.min(Math.min(r,g),b);var v=Math.max(Math.max(r,g),b);var m=v-n;if(m===0){return[null,0,v];}
var h=r===n?3+(b-g)/m:(g===n?5+(r-b)/m:1+(g-r)/m);return[h===6?0:h,m/v,v];}
function HSV_RGB(h,s,v){if(h===null){return[v,v,v];}
var i=Math.floor(h);var f=i%2?h-i:1-(h-i);var m=v*(1-s);var n=v*(1-s*f);switch(i){case 6:case 0:return[v,n,m];case 1:return[n,v,m];case 2:return[m,v,n];case 3:return[m,n,v];case 4:return[n,m,v];case 5:return[v,m,n];}}
function removePicker(){delete jscolor.picker.owner;document.getElementsByTagName('body')[0].removeChild(jscolor.picker.boxB);}
function drawPicker(x,y){if(!jscolor.picker){jscolor.picker={box:document.createElement('div'),boxB:document.createElement('div'),pad:document.createElement('div'),padB:document.createElement('div'),padM:document.createElement('div'),sld:document.createElement('div'),sldB:document.createElement('div'),sldM:document.createElement('div')};for(var i=0,segSize=4;i<jscolor.images.sld[1];i+=segSize){var seg=document.createElement('div');seg.style.height=segSize+'px';seg.style.fontSize='1px';seg.style.lineHeight='0';jscolor.picker.sld.appendChild(seg);}
jscolor.picker.sldB.appendChild(jscolor.picker.sld);jscolor.picker.box.appendChild(jscolor.picker.sldB);jscolor.picker.box.appendChild(jscolor.picker.sldM);jscolor.picker.padB.appendChild(jscolor.picker.pad);jscolor.picker.box.appendChild(jscolor.picker.padB);jscolor.picker.box.appendChild(jscolor.picker.padM);jscolor.picker.boxB.appendChild(jscolor.picker.box);}
var p=jscolor.picker;posPad=[x+THIS.pickerBorder+THIS.pickerFace+THIS.pickerInset,y+THIS.pickerBorder+THIS.pickerFace+THIS.pickerInset];posSld=[null,y+THIS.pickerBorder+THIS.pickerFace+THIS.pickerInset];p.box.onmouseup=p.box.onmouseout=function(){target.focus();};p.box.onmousedown=function(){abortBlur=true;};p.box.onmousemove=function(e){holdPad&&setPad(e);holdSld&&setSld(e);};p.padM.onmouseup=p.padM.onmouseout=function(){if(holdPad){holdPad=false;jscolor.fireEvent(valueElement,'change');}};p.padM.onmousedown=function(e){holdPad=true;setPad(e);};p.sldM.onmouseup=p.sldM.onmouseout=function(){if(holdSld){holdSld=false;jscolor.fireEvent(valueElement,'change');}};p.sldM.onmousedown=function(e){holdSld=true;setSld(e);};p.box.style.width=4*THIS.pickerInset+2*THIS.pickerFace+jscolor.images.pad[0]+2*jscolor.images.arrow[0]+jscolor.images.sld[0]+'px';p.box.style.height=2*THIS.pickerInset+2*THIS.pickerFace+jscolor.images.pad[1]+'px';p.boxB.style.position='absolute';p.boxB.style.clear='both';p.boxB.style.left=x+'px';p.boxB.style.top=y+'px';p.boxB.style.zIndex=THIS.pickerZIndex;p.boxB.style.border=THIS.pickerBorder+'px solid';p.boxB.style.borderColor=THIS.pickerBorderColor;p.boxB.style.background=THIS.pickerFaceColor;p.pad.style.width=jscolor.images.pad[0]+'px';p.pad.style.height=jscolor.images.pad[1]+'px';p.padB.style.position='absolute';p.padB.style.left=THIS.pickerFace+'px';p.padB.style.top=THIS.pickerFace+'px';p.padB.style.border=THIS.pickerInset+'px solid';p.padB.style.borderColor=THIS.pickerInsetColor;p.padM.style.position='absolute';p.padM.style.left='0';p.padM.style.top='0';p.padM.style.width=THIS.pickerFace+2*THIS.pickerInset+jscolor.images.pad[0]+jscolor.images.arrow[0]+'px';p.padM.style.height=p.box.style.height;p.padM.style.cursor='crosshair';p.sld.style.overflow='hidden';p.sld.style.width=jscolor.images.sld[0]+'px';p.sld.style.height=jscolor.images.sld[1]+'px';p.sldB.style.position='absolute';p.sldB.style.right=THIS.pickerFace+'px';p.sldB.style.top=THIS.pickerFace+'px';p.sldB.style.border=THIS.pickerInset+'px solid';p.sldB.style.borderColor=THIS.pickerInsetColor;p.sldM.style.position='absolute';p.sldM.style.right='0';p.sldM.style.top='0';p.sldM.style.width=jscolor.images.sld[0]+jscolor.images.arrow[0]+THIS.pickerFace+2*THIS.pickerInset+'px';p.sldM.style.height=p.box.style.height;try{p.sldM.style.cursor='pointer';}catch(eOldIE){p.sldM.style.cursor='hand';}
switch(modeID){case 0:var padImg='hs.png';break;case 1:var padImg='hv.png';break;}
p.padM.style.background="url('"+jscolor.getDir()+"cross.gif') no-repeat";p.sldM.style.background="url('"+jscolor.getDir()+"arrow.gif') no-repeat";p.pad.style.background="url('"+jscolor.getDir()+padImg+"') 0 0 no-repeat";redrawPad();redrawSld();jscolor.picker.owner=THIS;document.getElementsByTagName('body')[0].appendChild(p.boxB);}
function redrawPad(){switch(modeID){case 0:var yComponent=1;break;case 1:var yComponent=2;break;}
var x=Math.round((THIS.hsv[0]/6)*(jscolor.images.pad[0]-1));var y=Math.round((1-THIS.hsv[yComponent])*(jscolor.images.pad[1]-1));jscolor.picker.padM.style.backgroundPosition=(THIS.pickerFace+THIS.pickerInset+x-Math.floor(jscolor.images.cross[0]/2))+'px '+
(THIS.pickerFace+THIS.pickerInset+y-Math.floor(jscolor.images.cross[1]/2))+'px';var seg=jscolor.picker.sld.childNodes;switch(modeID){case 0:var rgb=HSV_RGB(THIS.hsv[0],THIS.hsv[1],1);for(var i=0;i<seg.length;i+=1){seg[i].style.backgroundColor='rgb('+
(rgb[0]*(1-i/seg.length)*100)+'%,'+
(rgb[1]*(1-i/seg.length)*100)+'%,'+
(rgb[2]*(1-i/seg.length)*100)+'%)';}
break;case 1:var rgb,s,c=[THIS.hsv[2],0,0];var i=Math.floor(THIS.hsv[0]);var f=i%2?THIS.hsv[0]-i:1-(THIS.hsv[0]-i);switch(i){case 6:case 0:rgb=[0,1,2];break;case 1:rgb=[1,0,2];break;case 2:rgb=[2,0,1];break;case 3:rgb=[2,1,0];break;case 4:rgb=[1,2,0];break;case 5:rgb=[0,2,1];break;}
for(var i=0;i<seg.length;i+=1){s=1-1/(seg.length-1)*i;c[1]=c[0]*(1-s*f);c[2]=c[0]*(1-s);seg[i].style.backgroundColor='rgb('+
(c[rgb[0]]*100)+'%,'+
(c[rgb[1]]*100)+'%,'+
(c[rgb[2]]*100)+'%)';}
break;}}
function redrawSld(){switch(modeID){case 0:var yComponent=2;break;case 1:var yComponent=1;break;}
var y=Math.round((1-THIS.hsv[yComponent])*(jscolor.images.sld[1]-1));jscolor.picker.sldM.style.backgroundPosition='0 '+(THIS.pickerFace+THIS.pickerInset+y-Math.floor(jscolor.images.arrow[1]/2))+'px';}
function isPickerOwner(){return jscolor.picker&&jscolor.picker.owner===THIS;}
function blurTarget(){if(valueElement===target){THIS.importColor();}
if(THIS.pickerOnfocus){THIS.hidePicker();}}
function blurValue(){if(valueElement!==target){THIS.importColor();}}
function setPad(e){var posM=jscolor.getMousePos(e);var x=posM[0]-posPad[0];var y=posM[1]-posPad[1];switch(modeID){case 0:THIS.fromHSV(x*(6/(jscolor.images.pad[0]-1)),1-y/(jscolor.images.pad[1]-1),null,leaveSld);break;case 1:THIS.fromHSV(x*(6/(jscolor.images.pad[0]-1)),null,1-y/(jscolor.images.pad[1]-1),leaveSld);break;}}
function setSld(e){var posM=jscolor.getMousePos(e);var y=posM[1]-posPad[1];switch(modeID){case 0:THIS.fromHSV(null,null,1-y/(jscolor.images.sld[1]-1),leavePad);break;case 1:THIS.fromHSV(null,1-y/(jscolor.images.sld[1]-1),null,leavePad);break;}}
var THIS=this;var modeID=this.pickerMode.toLowerCase()==='hvs'?1:0;var abortBlur=false;var
valueElement=jscolor.fetchElement(this.valueElement),styleElement=jscolor.fetchElement(this.styleElement);var
holdPad=false,holdSld=false;var
posPad,posSld;var
leaveValue=1<<0,leaveStyle=1<<1,leavePad=1<<2,leaveSld=1<<3;jscolor.addEvent(target,'focus',function(){if(THIS.pickerOnfocus){THIS.showPicker();}});jscolor.addEvent(target,'blur',function(){if(!abortBlur){window.setTimeout(function(){abortBlur||blurTarget();abortBlur=false;},0);}else{abortBlur=false;}});if(valueElement){var updateField=function(){THIS.fromString(valueElement.value,leaveValue);};jscolor.addEvent(valueElement,'keyup',updateField);jscolor.addEvent(valueElement,'input',updateField);jscolor.addEvent(valueElement,'blur',blurValue);valueElement.setAttribute('autocomplete','off');}
if(styleElement){styleElement.jscStyle={backgroundColor:styleElement.style.backgroundColor,color:styleElement.style.color};}
switch(modeID){case 0:jscolor.requireImage('hs.png');break;case 1:jscolor.requireImage('hv.png');break;}
jscolor.requireImage('cross.gif');jscolor.requireImage('arrow.gif');this.importColor();}};jscolor.install();