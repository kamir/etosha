<script type="text/javascript">

var doRefreshAgain = true;
var refreshPeriode = 5000;
var t;

function repeatRefresh() {
    doRefreshAgain = true;
    t = setTimeout("refreshPreview()", refreshPeriode);
}

function refreshPreview() {
    document.getElementById("LINKrefresh").innerHTML = "<a href=\"javascript:stopRefreshPreview();\"> stop refresh </a>";
    if ( doRefreshAgain == true ) {
         
         repeatRefresh();
         // alert("refresh view now ... ");
         switchFrame( 1 );
    }
}

function stopRefreshPreview() {
    
    doRefreshAgain = false;
    t = null;
    
    document.getElementById("LINKrefresh").innerHTML = "<a href=\"javascript:refreshPreview();\"> refresh </a>";
}

</script>