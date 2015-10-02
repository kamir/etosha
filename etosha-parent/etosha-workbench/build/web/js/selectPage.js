<script type="text/javascript">
    function switchFrame( i ) {
    
        var urls = new Array( 
            "module/mainIntro.jsp",
            "module/showPreview.jsp",
            "scan.jpg"
        );
        document.getElementById("DOCiFRAME").src = urls[i];
    }
    
    
    function doPreview() {
        switchFrame( 1 )
    }
    
    function resetPreview() {
        switchFrame( 0 )
    }
    
</script>