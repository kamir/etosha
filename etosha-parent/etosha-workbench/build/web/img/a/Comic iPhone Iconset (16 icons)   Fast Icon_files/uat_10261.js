var scripts = document.getElementsByTagName('script'); var icount = 1; for (icount = 1; icount < scripts.length; icount++){ var tempScript = scripts[scripts.length - icount]; var uatStart = tempScript.src.indexOf('/uat_'); if (uatStart > 0) { break; }}function parseQuery(query, variableName){var Params = new Object();if (!query) return Params;var Pairs = query.split(/[;&]/);for (var i = 0;i < Pairs.length;i++){var KeyVal = Pairs[i].split('=');if (!KeyVal || KeyVal.length !=2) continue;var key = unescape(KeyVal[0]);var val = unescape(KeyVal[1]);val = val.replace(/\+/g, ' ');Params[key] = val;if (key.toLowerCase() == variableName.toLowerCase())return val;}return false;}function IsIE8Browser() { var rv = -1; var ua = navigator.userAgent; var re = new RegExp("Trident\/([0-9]{1,}[\.0-9]{0,})"); if (re.exec(ua) != null) { rv = parseFloat(RegExp.$1); } return (rv == 4); } var browserversion = "bru"; var ie = false; if (/MSIE (\d+\.\d+);/.test(navigator.userAgent)) { ie = true; var ieversion=new Number(RegExp.$1); if (ieversion>=9) browserversion = "ie9"; else if (ieversion>=8) browserversion = "ie8"; else if (ieversion>=7) { if (IsIE8Browser()) { browserversion = "ie8"; } else { browserversion = "ie7"; } } else if (ieversion>=6) browserversion = "ie6"; else if (ieversion>=5) browserversion = "ie5"; else browserversion = "ieu"; } else if (/Opera[\/\s](\d+\.\d+)/.test(navigator.userAgent)) { var oprversion=new Number(RegExp.$1); if (oprversion>=10) browserversion = "op10"; else if (oprversion>=9) browserversion = "op9"; else if (oprversion>=8) browserversion = "op8"; else if (oprversion>=7) browserversion = "op7"; else browserversion = "opu"; } else if (/Chrome[\/\s](\d+\.\d+)/.test(navigator.userAgent)) { var chromeversion=new Number(RegExp.$1); if (chromeversion>=3) browserversion = "cr3"; else if (chromeversion>=2) browserversion = "cr2"; else if (chromeversion>=1) browserversion = "cr1"; else browserversion = "cru"; } else if (/Safari[\/\s](\d+\.\d+)/.test(navigator.userAgent)) { var oprversion=new Number(RegExp.$1); if (oprversion>=5) browserversion = "sf5"; else if (oprversion>=4) browserversion = "sf4"; else if (oprversion>=3) browserversion = "sf3"; else if (oprversion>=2) browserversion = "sf2"; else browserversion = "sfu"; } else if (/Firefox[\/\s](\d+\.\d+)/.test(navigator.userAgent)) { var ffversion=new Number(RegExp.$1); if (ffversion>=3) browserversion = "ff3"; else if (ffversion>=2) browserversion = "ff2"; else if (ffversion>=1) browserversion = "ff1"; else browserversion = "ffu"; } else browserversion = "bru"; var osname="unk"; var OS = navigator.appVersion; if (!ie) { OS = navigator.userAgent; } if (OS.indexOf("Win")!=-1) { if ((OS.indexOf("Windows NT 5.1")!=-1) || (OS.indexOf("Windows XP")!=-1)) osname="wxp"; else if ((OS.indexOf("Windows NT 7.0")!=-1) || (OS.indexOf("Windows NT 6.1")!=-1)) osname="wn7"; else if ((OS.indexOf("Windows NT 6.0")!=-1)) osname="wvs"; else if (OS.indexOf("Windows ME")!=-1) osname="wme"; else if ((OS.indexOf("Windows NT 4.0")!=-1) || (OS.indexOf("WinNT4.0")!=-1) || (OS.indexOf("WinNT")!=-1)) osname="wnt"; else if ((OS.indexOf("Windows NT 5.2")!=-1)) osname="ws3"; else if ((OS.indexOf("Windows NT 5.0")!=-1) || (OS.indexOf("Windows 2000")!=-1)) osname="w2k"; else if ((OS.indexOf("Windows 98")!=-1) || (OS.indexOf("Win98")!=-1)) osname="w98"; else if ((OS.indexOf("Windows 95")!=-1) || (OS.indexOf("Win95")!=-1) || (OS.indexOf("Windows_95")!=-1)) osname="w95"; else if ((OS.indexOf("Win16")!=-1)) osname="w31"; else osname="wun"; } else if (OS.indexOf("Mac")!=-1) osname="mac"; else if (OS.indexOf("X11")!=-1) osname="unx"; else if (OS.indexOf("Linux")!=-1) osname="lnx"; function WindowGetHeight() {  var y = 0; if (self.innerHeight) {  y = self.innerHeight;  } else if (document.documentElement && document.documentElement.clientHeight) {  y = document.documentElement.clientHeight;  } else if (document.body)  {  y = document.body.clientHeight;  }  return y; } function findPosition( oElement ) {  if( typeof( oElement.offsetParent ) != 'undefined' )  {  for( var posX = 0, posY = 0; oElement; oElement = oElement.offsetParent )  {  posX += oElement.offsetLeft;  posY += oElement.offsetTop;  }  return posY; }  else  {  return oElement.y;  }} function getWidth(parAdSize) {  var pos = parAdSize.search('x'); var width = parAdSize.substring(0, pos); return width; } function getHeight(parAdSize) { var pos = parAdSize.search('x'); var height = parAdSize.substring(pos+1, parAdSize.length); return height; }function getURLParam(strParamName){var strReturn = "323WERKJKLDSJFlk23ididfj42342";var strHref = window.location.href;if ( strHref.indexOf("?") > -1 ){var strQueryString = strHref.substr(strHref.indexOf("?")).toLowerCase();var aQueryString = strQueryString.split("&");for ( var iParam = 0; iParam < aQueryString.length; iParam++ ){if (aQueryString[iParam].indexOf(strParamName.toLowerCase() + "=") > -1 ){var aParam = aQueryString[iParam].split("=");strReturn = aParam[1];break;}}}return unescape(strReturn);} function getUrlVars() { var vars = [], hash; var paramsFound = 0; var keywords = ',ara,ask,as_q,B1,begriff,cadena,Claus,cq,DTqb1,eingabe,general,GoogleSearch,heureka,in,jump,item,k,KERESES,key,keyword,Keywords,kw,look_for,MT,p,PA,palabras,palavra,q,qry,query,query_1,Q1,q1,qkw,qr,qry,qs,qt,qu,query,oldquery,query0,QueryString,queryterm,queryText,question,qw,r,request,requete,s,sb,sc,search,search_for,searchfor,SearchString,searchtext,searchWord,search_string,search_term,soegeord,SP,srch,srchKey,sTerm,str,string,su,suche,suchstr,szukaj,T,term,terml,terms,text,u,w,what,word,words,x,'; var hashes = document.referrer.slice(document.referrer.indexOf('?') + 1).split('&'); for(var i = 0; i < hashes.length; i++) { hash = hashes[i].split('='); var searchKey = ',' + hash[0] + ','; var keySearch = keywords.search(searchKey); if (keySearch > 0) { vars.push(hash[0]); vars[hash[0]] = hash[1]; paramsFound = 1; break; } } if (paramsFound == 0) { hash[0] = -1; hash[1] = -1; vars.push(hash[0]); vars[hash[0]] = hash[1]; } return vars;} function getDomainName() { var getParaIndex = document.referrer.indexOf('?'); if (getParaIndex > -1) { var getRefURL = document.referrer.substring(0, document.referrer.indexOf('?')); } else { getRefURL = document.referrer;} var getProto = getRefURL.search('http://'); if (getProto > -1) { var urlWithProto = getRefURL.substring(7); } else { var getProto = getRefURL.search('https://'); if (getProto > -1) { urlWithProto = getRefURL.substring(8); } else { urlWithProto = getRefURL; } } var pageLoc = urlWithProto.indexOf('/'); if (pageLoc > -1) { var domainName = urlWithProto.substring(0, pageLoc); } else { var domainName = urlWithProto; } return domainName; } function getCookie(parCookieName) { var a_all_cookies = document.cookie.split(';'); var a_temp_cookie = ''; var cookie_name = ''; var cookie_value = ''; var b_cookie_found = false; for(i=0;i<a_all_cookies.length; i++) { a_temp_cookie = a_all_cookies[i].split('='); cookie_name = a_temp_cookie[0].replace(/^\s+|\s+$/g, ''); if (cookie_name.toLowerCase() == parCookieName.toLowerCase()) { b_cookie_found = true; if (a_temp_cookie.length > 1){ cookie_value = unescape( a_temp_cookie[1].replace(/^\s+|\s+$/g, '') );} return cookie_value; break; } a_temp_cookie = null; cookie_name = ''; } if (!b_cookie_found) { return -1; } } function getHostName(parUrl) { try{ varDomain = parUrl.toString().match(/:\/\/(.[^/]+)/)[1]; } catch(e){return parUrl;} varDomainSplit = varDomain.split('.'); varDomainName = ''; if (varDomainSplit.length == 3) varCount = 1; else if (varDomainSplit.length == 2) varCount = 0; else if (varDomainSplit.length > 3) varCount = 2; else { varDomain = ' ' + parUrl.toString(); return varDomain.toLowerCase(); } for (i=varDomainSplit.length-1;i>=varCount;i--) { if (varDomainName = '') varDomainName = varDomainSplit[i]; else varDomainName = varDomainSplit[i] + '.' + varDomainName; } return varDomainName.toLowerCase(); } function getDocReferrer(parDocRef) { window.varNSafe = 'general'; window.varDocRef = 'none'; window.varReferrer = ''; window.varDocRefType = 'none'; window.varDisp = 'iframe'; if (parDocRef != false) { varDocRef = parDocRef; window.varDocRef = parDocRef; window.varDisp = 'iframe'; } else { window.parentAllowed = false; window.current = window; try { for(i = 0;i<=10;i++) { if ((window.current.parent != null) && (window.current.parent != window.current))  { var loc = window.current.parent.location.toString(); var x = loc.length; if (x > 0) { window.current = window.current.parent; window.parentAllowed = true; } else { window.parentAllowed = false; break; } } else { if (i==0) { window.parentAllowed = true; } break; } } } catch (e) { window.parentAllowed = false; } if (window.current.document.referrer.length == 0) { varDocRef = window.current.location; window.varDisp = 'none'; } else { if (window.parentAllowed) { varDocRef = window.current.location; window.varDisp = 'none'; } else { varDocRef = window.current.document.referrer; } } } varDomainName = getHostName(varDocRef); varUrlDomains = 'jeetyetmedia.com,adnxs.com,rubiconproject.com,admeld.com,yieldmanager.com,udmserve.net,lijit.com,liftdna.com,doubleclick.net,mediadakine.com,afy11.net,brandaffinity.net,adtiger.de,technoratimedia.net,adjunky.com,iicdn.com,meviodisplayads.com,pubmatic.com,contextweb.com,alphabirdnetwork.com,openx.com,baronsoffers.com,888media.net,adk2.com,andomedia.com,adserve.com,oadsrv.com,adap.tv,adfrontiers.com,adjug.com,adjuggler.com,adjuggler.net,adshost1.com,adtechus.com,advertising.com,atdmt.com,auditude.com,buysellads.com,casalemedia.com,criteo.com,cubics.com,fastclick.net,fmpub.net,googleadservices.com,harrenmedianetwork.com,havamedia.net,indieclick.com,intellitxt.com,interclick.com,liverail.com,lqw.me,monkeybroker.net,openx.net,openx.org,picadmedia.com,publisheradnetwork.com,realmedia.com,revfusion.net,shinobi.jp,specificclick.net,tradepub.com,video-loader.com,videoegg.com,wahoha.com,xdev.info,xtendmedia.com,yadomedia.com,zedo.com'; varSearchFor = varUrlDomains.indexOf(varDomainName); varUrlKeywords = 'xxx|porn|sex|explicita|gay|horny|bdsm|milf|hentai|nude|shemale|boobs|fuck|anal|ass|xmovie|dick|cock|cunt|pussy|gloryhole|orgy|lesbian|bonner|3xhd|7797.info|adult|asianforumer.com|animetake.com|acidcow.com|7797.info|whore|tit|cum'; varSearchExcKey = varDocRef.toString().search(varUrlKeywords); if (varSearchExcKey > -1) window.varNSafe = 'adult'; if (varSearchFor > -1) window.varDocRefType = 'adnetwork'; window.varReferrer = varDocRef; if (parDocRef != false) varDocReturn = '&nsafe=' + window.varNSafe + '&reftype=' + window.varDocRefType + '&disp=' + window.varDisp; else varDocReturn = 'referrer=' + escape(varDocRef) + '&nsafe=' + window.varNSafe + '&reftype=' + window.varDocRefType + '&disp=' + window.varDisp; return varDocReturn; } var myScript = scripts[scripts.length - icount]; var queryString = myScript.src.replace(/^[^\?]+\??/, ''); varQueryReferrer = parseQuery(queryString, "referrer"); varDocReferrer = getDocReferrer(varQueryReferrer); window.tmiv = 1; queryString = queryString + '&' + varDocReferrer + '&tmiv=' + window.tmiv; var tcb = Math.random(); var uatUrl = 'http://uat-net.technoratimedia.com/00/61/02/adserv_10261.js?' + queryString + '&tcb=' + tcb; document.write('<script type="text/javascript" src='+uatUrl+'></scr' + 'ipt>');